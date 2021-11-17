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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kakusei.simpletodolist.entity.Event;
import com.kakusei.simpletodolist.repository.IEventRepository;
import com.kakusei.simpletodolist.repository.impl.EventRepositoryImpl;
import com.kakusei.simpletodolist.util.RecyclerViewAdapter;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addFloatingActionButton;
    private IEventRepository eventRepository;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private MenuItem showAndHide;
    private Boolean show = false;
    private Boolean sort = false;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                recyclerViewAdapter.setData(eventRepository.queryForList(null,null,null,null,null,null,null));
                recyclerViewAdapter.notifyDataSetChanged();
                Snackbar.make(addFloatingActionButton,"Succeeded!", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(addFloatingActionButton, "Cancelled!",Snackbar.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_showAndHide: {
                if (!show) {
                    show = true;
                    showAndHide.setIcon(R.drawable.ic_toolbar_hide);
                } else {
                    show = false;
                    showAndHide.setIcon(R.drawable.ic_toolbar_show);
                }
//                recyclerViewAdapter.setData(eventRepository.queryForList(null,"status = ?", new String[] {show ? "1" : "0"},null,null,"creationTime "+ (sort ? "ASC" : "DESC"),null));
                recyclerViewAdapter = null;
                recyclerViewAdapter = new RecyclerViewAdapter(this, eventRepository.queryForList(null,"status = ?", new String[] {show ? "1" : "0"},null,null,"creationTime "+ (sort ? "ASC" : "DESC"),null),activityResultLauncher);
                break;
            }
            case R.id.toolbar_sort: {
                String status = show ? "1":"0";
                if (sort) {
                    sort = false;
                } else {
                    sort = true;
                }
//                recyclerViewAdapter.setData(eventRepository.queryForList(null,"status = ?", new String[] {status},null,null,"creationTime " + (sort ? "ASC" : "DESC"),null));
                recyclerViewAdapter = null;
                recyclerViewAdapter = new RecyclerViewAdapter(this, eventRepository.queryForList(null,"status = ?", new String[] {status},null,null,"creationTime " + (sort ? "ASC" : "DESC"),null),activityResultLauncher);
                break;
            }
        }
//        recyclerViewAdapter = new RecyclerViewAdapter(this, eventRepository.queryForList(null,"status = ?", new String[] {show ? "1" : "0"},null,null,"creationTime "+ (sort ? "ASC" : "DESC"),null),activityResultLauncher);
//        recyclerViewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerViewAdapter);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar,menu);
        showAndHide = menu.findItem(R.id.toolbar_showAndHide);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventRepository = new EventRepositoryImpl(this);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolBar));
        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
        addFloatingActionButton = findViewById(R.id.main_add_floatActionButton);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerViewAdapter = new RecyclerViewAdapter(this, eventRepository.queryForList(null,"status = ?",new String[]{"0"},null,null,"creationTime DESC",null),activityResultLauncher);
        recyclerViewAdapter = new RecyclerViewAdapter(this, eventRepository.queryForList(null,"status = ?",new String[]{"0"},null,null,"creationTime DESC",null),activityResultLauncher);

        recyclerView.setAdapter(recyclerViewAdapter);
//        分隔线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        findViewById(R.id.main_add_floatActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detial = new Intent(MainActivity.this, DetialActivity.class);
                activityResultLauncher.launch(detial);
            }
        });
    }
}