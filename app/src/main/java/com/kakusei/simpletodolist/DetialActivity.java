package com.kakusei.simpletodolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakusei.simpletodolist.entity.Event;
import com.kakusei.simpletodolist.repository.IEventRepository;
import com.kakusei.simpletodolist.repository.impl.EventRepositoryImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetialActivity extends AppCompatActivity {
    private EditText title;
    private EditText body;
    private DatePickerDialog deadLineDatePickerDialog;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private FloatingActionButton doneFloatingActionButton;
    private MenuItem alter;
    private MenuItem deadLine;
    private MenuItem delete;
    private Event event;
    private SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private IEventRepository eventRepository = new EventRepositoryImpl(this);
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detial_toolbar,menu);
        alter = menu.findItem(R.id.toolbar_alter);
        deadLine = menu.findItem(R.id.toolbar_deadLine);
        delete = menu.findItem(R.id.toolbar_delete);
        if (event.getId() == null) {
            delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        } else {
            delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        if (event.getTime() == null) {
            alter.setIcon(R.drawable.ic_toolbar_notification);
        } else {
            alter.setIcon(R.drawable.ic_toolbar_notification_fill);
        }
        if (event.getDeadLine() == null) {
            deadLine.setIcon(R.drawable.ic_toolbar_deadline);
        } else {
            deadLine.setIcon(R.drawable.ic_toolbar_deadline_fill);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_alter: {
                Calendar calendar;
                if (event.getTime() == null) {
                    calendar = Calendar.getInstance();
                } else {
                    calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(dateAndTimeFormat.parse(event.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (datePickerDialog == null) {
                    datePickerDialog = new DatePickerDialog(DetialActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            timePickerDialog = new TimePickerDialog(DetialActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//                                    Log.d("kakusei", "onTimeSet:"+ year + "-" + month + "-" + day + " " + hour + ":" + minute);
                                    event.setTime(year + "-" + (month + 1) + "-" + day + " " + hour + ":" + minute);
                                }
                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                            timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    Toast.makeText(DetialActivity.this,"You cancel setting time.",Toast.LENGTH_SHORT).show();
                                }
                            });
                            timePickerDialog.show();
                        }
                    },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
                    datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Toast.makeText(DetialActivity.this,"You cancel setting dead line.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                datePickerDialog.show();
                break;
            }
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.toolbar_deadLine: {
                Calendar calendar;
                try {
                    if (event.getDeadLine() == null)
                        calendar = Calendar.getInstance();
                    else {
                        calendar = Calendar.getInstance();
                        calendar.setTime(dateFormat.parse(event.getDeadLine()));
                    }
                    if (deadLineDatePickerDialog == null) {
                        deadLineDatePickerDialog = new DatePickerDialog(DetialActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                event.setDeadLine(i + "-" + (i1 + 1) + "-" + i2);
//                                Log.d("kakusei","DeadLinePickerDialog: DealLine = " + i + "-" + i1 + "-" + i2);
//                                Log.d("kakusei", "event.getDeadLine: " + event.getDeadLine());
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                deadLineDatePickerDialog.show();
                break;
            }
            case R.id.toolbar_delete: {
                if (event == null || event.getId() == null) {
                    return false;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(DetialActivity.this);
                builder.setTitle("Warning!");
                builder.setMessage("Do you want to delete this event?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eventRepository.deleteById(event.getId());
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        setResult(Activity.RESULT_CANCELED);
//                        finish();
                        return;
                    }
                });
                builder.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial);
//        setActionBar((Toolbar) findViewById(R.id.detial_toolBar));
        setSupportActionBar(findViewById(R.id.detial_toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        title = findViewById(R.id.detial_title_editText);
        body = findViewById(R.id.detial_body_editText);
        doneFloatingActionButton = findViewById(R.id.detial_done_floatActionButton);
        event = (Event) this.getIntent().getParcelableExtra("event");
        if (event == null) {
            event = new Event();
            event.setCreationTime(dateAndTimeFormat.format(new Date(System.currentTimeMillis())));
        }
        doneFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event.getTitle() == null) {
                    Toast.makeText(DetialActivity.this,"Title can not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                event.setBody(body.getText().toString());
                event.setStatus(0);
                event.setTitle(title.getText().toString());
                if (event.getId() == null) {
                    eventRepository.insert(event);
                } else {
                    eventRepository.update(event);
                }
                setResult(Activity.RESULT_OK);
//                setResult();
                finish();
            }
        });
        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == false) {
                    if (title.getText().toString().length() == 0) {
                        Toast.makeText(DetialActivity.this,"Title can not be empty!", Toast.LENGTH_SHORT).show();
                    }
                    event.setTitle(title.getText().toString());
                }
            }
        });
        title.setText(event.getTitle());
        body.setText(event.getBody());
        event.setStatus(0);
    }
}