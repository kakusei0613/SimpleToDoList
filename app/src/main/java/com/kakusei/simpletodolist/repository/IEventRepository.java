package com.kakusei.simpletodolist.repository;

import com.kakusei.simpletodolist.entity.Event;

import java.util.List;

public interface IEventRepository {
    public void insert(Event event);
    public List<Event> queryForList(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);
    public void deleteById(Long id);
    public void update(Event event);
}
