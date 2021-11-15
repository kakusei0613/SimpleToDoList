package com.kakusei.simpletodolist.repository.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kakusei.simpletodolist.entity.Event;
import com.kakusei.simpletodolist.repository.IEventRepository;
import com.kakusei.simpletodolist.util.TodoListHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventRepositoryImpl implements IEventRepository {
    private TodoListHelper todoListHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;

    public EventRepositoryImpl(Context context) {
        this.todoListHelper = new TodoListHelper(context,"event.db",null,1);
    }
    @Deprecated
    public ContentValues packageValues(Event event) {
        ContentValues values = new ContentValues();
        if (event.getTitle() != null) {
            values.put("title", event.getTitle());
        }
        if (event.getBody() != null) {
            values.put("body", event.getBody());
        }
        if (event.getDeadLine() != null) {
            values.put("deadline", event.getDeadLine());
        }
        if (event.getTime() != null) {
            values.put("time", event.getTime());
        }
        if (event.getStatus() != null) {
            values.put("status", event.getStatus());
        }
        if (event.getId() != null) {
            values.put("id", event.getId());
        }
        return values;
    }

    /***
     * This function can detect the field is null or not and dynamic generation the SQL statement.
     * @param event Event entity.
     */
    @Override
    public void insert(Event event) {
//        Get writable database;
        sqLiteDatabase = todoListHelper.getWritableDatabase();
//        ContentValues values = packageValues(event);
        Class eventClass = event.getClass();
        Field[] fields = eventClass.getDeclaredFields();
//        Insert SQL statement
        StringBuilder insertStatement = new StringBuilder("INSERT INTO event");
        StringBuilder fieldList = new StringBuilder("(");
        StringBuilder valueList = new StringBuilder("VALUES(");
        StringBuilder methodName;
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            if (f.getName().equals("CREATOR"))
                continue;
//            In order to get the getter methods.
            methodName = new StringBuilder();
            methodName.append("get");
            methodName.append(f.getName().substring(0,1).toUpperCase());
            methodName.append(f.getName().substring(1));
            try {
                Method method = eventClass.getMethod(methodName.toString());
//                Use reflect get the field's value.
                Object value = method.invoke(event);
//                System.out.println("Field:" + f.getName() + " FieldType:" + f.getType().getName() + " Method: " + method.getName() + "Result:" + method.invoke(event));
//                generation the SQL statement.
                if(value != null) {
                    if (fieldList.substring(fieldList.length() - 1).equals("(")) {
                        fieldList.append(f.getName());
                    } else {
                        fieldList.append("," + f.getName());
                    }
                    if (valueList.substring(valueList.length() - 1).equals("(")) {
                        if(f.getType() == java.lang.String.class) {
                            valueList.append("'" + method.invoke(event) + "'");
                        } else {
                            valueList.append(method.invoke(event));
                        }
                    } else {
                        if(f.getType() == java.lang.String.class) {
                            valueList.append(",'" + method.invoke(event) + "'");
                        } else {
                            valueList.append("," + method.invoke(event));
                        }
                    }
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
//        If the entity field was all not null.
//        The sql statement is : INSERT INTO event(id, title, body, deadline, time, status) VALUES(?,?,?,?,?,?)
        fieldList.append(") ");
        valueList.append(") ");
        insertStatement.append(fieldList.toString());
        insertStatement.append(valueList.toString());
//        System.out.println(insertStatement);
        sqLiteDatabase.execSQL(insertStatement.toString());
        sqLiteDatabase.close();
    }

    /***
     *  This function just package the Google's SQLiteDatabase API.
     *  Let the return type is List instead of Cursor.
     * @param columns
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return A event list.
     */
    @Override
    public List<Event> queryForList(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        sqLiteDatabase = todoListHelper.getReadableDatabase();
        Field[] fields = Event.class.getDeclaredFields();
        cursor = sqLiteDatabase.query("event", columns, selection, selectionArgs, groupBy, having, orderBy, limit);
//        Execute the query sql statement.cursor = sqLiteDatabase.query("event", columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        List<Event> result = new ArrayList<Event>();
        StringBuilder methodName;
        Method method;
        Method cursorMethod;
        while (cursor.moveToNext()) {
            Event event = new Event();
            for (Field f : fields) {
                if (f.getName().equals("CREATOR"))
                    continue;
//                Get setter methods.
                methodName = new StringBuilder();
                methodName.append("set");
                methodName.append(f.getName().substring(0,1).toUpperCase());
                methodName.append(f.getName().substring(1));
                try {
                    method = Event.class.getMethod(methodName.toString(),new Class[] {f.getType()});
                    cursorMethod = f.getType().getSimpleName().equals("Integer") ?
                            Cursor.class.getMethod("get" + f.getType().getSimpleName().substring(0,3), int.class) :
                            Cursor.class.getMethod("get" + f.getType().getSimpleName(), int.class);
//                    System.out.println("cursor:" + cursor + " cursorMethod:" + cursorMethod.getName() + " result:" + cursorMethod.invoke(cursor, cursor.getColumnIndex(f.getName())));
                    method.invoke(event,new Object[] {cursorMethod.invoke(cursor, cursor.getColumnIndex(f.getName()))});
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            result.add(event);
        }
        sqLiteDatabase.close();
        return result;
    }

    @Override
    public void deleteById(Long id) {
        sqLiteDatabase = todoListHelper.getWritableDatabase();
        sqLiteDatabase.delete("event","id = ?", new String[] {id.toString()});
        sqLiteDatabase.close();
    }

    @Override
    public void update(Event event) {
        sqLiteDatabase = todoListHelper.getWritableDatabase();
        StringBuilder updateStatement = new StringBuilder("UPDATE event SET ");
        StringBuilder updateColumnAndValue = new StringBuilder();
        StringBuilder whereArgs = new StringBuilder(" WHERE id = " + event.getId());
        StringBuilder getterMethodName;
        Method getterMethod;
        Field[] fields = event.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals("id")) {
                continue;
            }
            if (fields[i].getName().equals("CREATOR")) {
                continue;
            }
            getterMethodName = new StringBuilder("get");
            getterMethodName.append(fields[i].getName().substring(0,1).toUpperCase());
            getterMethodName.append(fields[i].getName().substring(1));

            try {
                getterMethod = event.getClass().getMethod(getterMethodName.toString());
                Object value = getterMethod.invoke(event);
                if(value == null) {
                    if (updateColumnAndValue.length() == 0) {
                        updateColumnAndValue.append(fields[i].getName() + " = " + "null");
                    } else {
                        updateColumnAndValue.append(", " + fields[i].getName() + " = " + "null");
                    }
                    continue;
                }
                if (updateColumnAndValue.length() == 0) {
                    updateColumnAndValue.append(
                            fields[i].getType().getSimpleName().equals("String") ?
                                    (fields[i].getName() + " = '" + getterMethod.invoke(event) + "'") : (fields[i].getName() + " = " + getterMethod.invoke(event))
                );
                } else {
                    updateColumnAndValue.append(
                            fields[i].getType().getSimpleName().equals("String") ?
                                    (", " + fields[i].getName() + " = '" + getterMethod.invoke(event) + "'") : (", " + fields[i].getName() + " = " + getterMethod.invoke(event))
                    );
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (i == fields.length - 1)
                updateColumnAndValue.append(" ");
        }
        updateStatement.append(updateColumnAndValue.toString());
        updateStatement.append(whereArgs.toString());
//        System.out.println("Update Statement:" + updateStatement.toString());
        sqLiteDatabase.execSQL(updateStatement.toString());
        sqLiteDatabase.close();
    }

}
