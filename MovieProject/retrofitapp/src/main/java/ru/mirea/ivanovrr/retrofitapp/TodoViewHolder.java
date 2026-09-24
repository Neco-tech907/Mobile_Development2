package ru.mirea.ivanovrr.retrofitapp;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoViewHolder extends RecyclerView.ViewHolder {

    final ImageView imageView;
    final TextView textViewId;
    final TextView textViewTitle;
    final CheckBox checkBoxCompleted;

    public TodoViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageViewTodo);
        textViewId = itemView.findViewById(R.id.textViewId);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
    }
}
