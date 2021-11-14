package com.kakusei.simpletodolist.util;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kakusei.simpletodolist.DetialActivity;
import com.kakusei.simpletodolist.MainActivity;
import com.kakusei.simpletodolist.R;
import com.kakusei.simpletodolist.entity.Event;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Event> data;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.event_item_title);
            item = (LinearLayout) itemView.findViewById(R.id.main_event_ListLayout);
        }
    }

    public RecyclerViewAdapter(List<Event> data) {
        this.data = data;
    }

    public void setData(List<Event> data) {
        this.data = data;
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
                view.getContext().startActivity(event);
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Event event = data.get(position);
        holder.title.setText(event.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
