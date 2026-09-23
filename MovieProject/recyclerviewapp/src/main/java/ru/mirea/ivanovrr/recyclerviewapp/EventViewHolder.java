package ru.mirea.ivanovrr.recyclerviewapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ViewHolder хранит ссылки на view одного элемента, чтобы не вызывать
 * findViewById при каждой прокрутке.
 */
public class EventViewHolder extends RecyclerView.ViewHolder {

    private final ImageView imageView;
    private final TextView yearView;
    private final TextView titleView;
    private final TextView descriptionView;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageViewEvent);
        yearView = itemView.findViewById(R.id.textViewYear);
        titleView = itemView.findViewById(R.id.textViewTitle);
        descriptionView = itemView.findViewById(R.id.textViewDescription);
    }

    public ImageView getImageView() { return imageView; }
    public TextView getYearView() { return yearView; }
    public TextView getTitleView() { return titleView; }
    public TextView getDescriptionView() { return descriptionView; }
}
