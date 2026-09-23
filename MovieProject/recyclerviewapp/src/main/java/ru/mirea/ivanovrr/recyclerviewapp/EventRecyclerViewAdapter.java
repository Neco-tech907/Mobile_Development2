package ru.mirea.ivanovrr.recyclerviewapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Адаптер связывает список событий с ViewHolder'ами.
 * Переопределены три обязательных метода: onCreateViewHolder, onBindViewHolder, getItemCount.
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private final List<HistoricalEvent> events;
    private Context context;

    public EventRecyclerViewAdapter(List<HistoricalEvent> events) {
        this.events = events;
    }

    /** Вызывается, когда RecyclerView нужен новый ViewHolder: надуваем разметку карточки. */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.event_item_view, parent, false);
        return new EventViewHolder(itemView);
    }

    /** Вызывается для каждого элемента, который становится видимым: кладём данные во view. */
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        HistoricalEvent event = events.get(position);

        // ищем drawable по имени, как флаг в методичке; 0 — если ресурса нет
        int resId = context.getResources().getIdentifier(
                event.getImageName(), "drawable", context.getPackageName());
        holder.getImageView().setImageResource(resId);

        holder.getYearView().setText(String.valueOf(event.getYear()));
        holder.getTitleView().setText(event.getTitle());
        holder.getDescriptionView().setText(event.getDescription());

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, event.toString(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
