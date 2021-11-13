package com.kakusei.simpletodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kakusei.simpletodolist.entity.Event;
import com.kakusei.simpletodolist.repository.IEventRepository;
import com.kakusei.simpletodolist.repository.impl.EventRepositoryImpl;
import com.kakusei.simpletodolist.util.RecyclerViewAdapter;


public class MainActivity extends AppCompatActivity {
    private IEventRepository eventRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventRepository = new EventRepositoryImpl(this);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolBar));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerViewAdapter(eventRepository.queryForList(null,null,null,null,null,null,null)));
        Event event = new Event();
        event.setId(Long.parseLong("123"));
        event.setTitle("test5555");
        event.setStatus(1);
//        event.setBody("ABCSDSD");
//        event.setDeadLine("12314512");
//        event.setTime("12343254");

//        eventRepository.insert(event);
//        eventRepository.update(event);
//        eventRepository.deleteById(Long.parseLong("123"));
//        System.out.println(eventRepository.queryForList(null,null,null,null,null,null,null));
        findViewById(R.id.main_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detial = new Intent(view.getContext(),DetialActivity.class);
                startActivity(detial);
            }
        });
    }
}