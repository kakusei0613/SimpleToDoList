package com.kakusei.simpletodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kakusei.simpletodolist.entity.Event;

import java.util.Date;

public class DetialActivity extends AppCompatActivity {
    private Switch dateSwitch;
    private Switch timeSwitch;
    private TextView date;
    private TextView time;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial);
        dateSwitch = findViewById(R.id.detial_deadLine_switch);
        timeSwitch = findViewById(R.id.detial_time_switch);
        date = findViewById(R.id.detial_deadLine_showDate_textView);
        time = findViewById(R.id.detial_time_showTime_textView);

        RelativeLayout relativeLayout = findViewById(R.id.detial_deadLine_relativeLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dateSwitch.isChecked()) {
                    datePickerDialog.show();
                } else {
                    dateSwitch.setChecked(true);
                }
            }
        });
        dateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (datePickerDialog == null) {
                    datePickerDialog = new DatePickerDialog(compoundButton.getContext());
                    datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            date.setText(i + "-" + i1 + "-" + i2);
//                            Toast.makeText(datePicker.getContext(),i + " " + i1 + " " + i2, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (b) {
                    datePickerDialog.show();
                } else {
                    date.setText("");
                }
            }
        });
        relativeLayout = findViewById(R.id.detial_time_relativeLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeSwitch.isChecked()) {
                    timePickerDialog.show();
                } else {
                    timeSwitch.setChecked(true);
                }
            }
        });
        timeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (timePickerDialog == null) {
                    timePickerDialog = new TimePickerDialog(compoundButton.getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            time.setText(i + ":" + i1);
                            Toast.makeText(timePickerDialog.getContext(),i + ":" + i1, Toast.LENGTH_SHORT).show();
                        }
                    }, 8, 30, true);
                }
                if (b) {
                    timePickerDialog.show();
                } else {
                    time.setText("");
                }
            }
        });
//        TextView init
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateSwitch.isChecked()) {
                    datePickerDialog.show();
                } else {
                    return;
                }
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeSwitch.isChecked()) {
                    timePickerDialog.show();
                } else {
                    return;
                }
            }
        });
    }
}