package com.kakusei.simpletodolist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kakusei.simpletodolist.entity.Event;
import com.kakusei.simpletodolist.repository.IEventRepository;
import com.kakusei.simpletodolist.repository.impl.EventRepositoryImpl;
import com.kakusei.simpletodolist.util.RecyclerViewAdapter;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private IEventRepository eventRepository;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                recreate();
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventRepository = new EventRepositoryImpl(this);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolBar));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(eventRepository.queryForList(null,null,null,null,null,null,null),activityResultLauncher);
//        recyclerViewAdapter.setData(eventRepository.queryForList(null,null,null,null,null,null,null));
        recyclerView.setAdapter(recyclerViewAdapter);

        findViewById(R.id.main_add_floatActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detial = new Intent(MainActivity.this, DetialActivity.class);
                activityResultLauncher.launch(detial);
            }
        });
    }
}