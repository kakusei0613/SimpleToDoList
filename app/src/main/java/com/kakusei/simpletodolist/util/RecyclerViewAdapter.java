package com.kakusei.simpletodolist.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kakusei.simpletodolist.DetialActivity;
import com.kakusei.simpletodolist.MainActivity;
import com.kakusei.simpletodolist.R;
import com.kakusei.simpletodolist.entity.Event;
import com.kakusei.simpletodolist.repository.IEventRepository;
import com.kakusei.simpletodolist.repository.impl.EventRepositoryImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Event> data;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private IEventRepository eventRepository;
    private Context context;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Date today;


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView creationTime;
        ImageView imageView;
        LinearLayout item;
        Integer colorOnErr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.event_item_title);
            creationTime = (TextView) itemView.findViewById(R.id.event_item_creationTime);
            item = (LinearLayout) itemView.findViewById(R.id.main_event_ListLayout);
            imageView = (ImageView) itemView.findViewById(R.id.event_item_image);
            colorOnErr = itemView.getResources().getColor(R.color.red);
        }

        public Integer getColorOnErr() {
            return colorOnErr;
        }
    }

    public RecyclerViewAdapter(Context context, List<Event> data, ActivityResultLauncher<Intent> activityResultLauncher) {
        this.data = data;
        this.activityResultLauncher = activityResultLauncher;
        this.today = new Date(System.currentTimeMillis());
        this.context = context;
        this.eventRepository = new EventRepositoryImpl(context);
    }

    public void setData(List<Event> data) {
        this.data = data;
    }

    public List<Event> getData() {
        return data;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_event_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.item.setLongClickable(true);
        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getLayoutPosition();
                Intent event = new Intent(view.getContext(), DetialActivity.class);
                event.putExtra("event",data.get(position));
                activityResultLauncher.launch(event);
                return true;
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getLayoutPosition();
                Resources.Theme theme = view.getContext().getTheme();
                if (data.get(position).getStatus() == 0) {
                    holder.imageView.setImageResource(R.drawable.ic_item_selected);
                    holder.title.setTextColor(Color.GRAY);
                    holder.imageView.setColorFilter(Color.GRAY);
                } else {
                    int color;
                    try {
                        if (data.get(position).getDeadLine() != null && today.after(simpleDateFormat.parse(data.get(position).getDeadLine()))) {
                            color = view.getResources().getColor(R.color.red,theme);
                        } else {
                            color = view.getResources().getColor(R.color.design_default_color_on_secondary,theme);
                        }
                        holder.imageView.setImageResource(R.drawable.ic_item_unselected);
                        holder.title.setTextColor(color);
                        holder.imageView.setColorFilter(color);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (data.get(position).getStatus() == 1) {
                    data.get(position).setStatus(0);
                } else {
                    data.get(position).setStatus(1);
                }
                eventRepository.update(data.get(position));
                data.remove(position);
                notifyItemRemoved(position);
                Snackbar.make(view,"Succeeded!",Snackbar.LENGTH_SHORT).show();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Event event = data.get(position);
        holder.title.setText(event.getTitle());
        holder.creationTime.setText(event.getCreationTime());
        try {
            if (event.getDeadLine() != null && today.after(simpleDateFormat.parse(event.getDeadLine()))) {
                holder.imageView.setColorFilter(holder.getColorOnErr());
                holder.title.setTextColor(holder.getColorOnErr());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (event.getStatus() == 1) {
            holder.imageView.setImageResource(R.drawable.ic_item_selected);
            holder.imageView.setColorFilter(Color.GRAY);
            holder.title.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
