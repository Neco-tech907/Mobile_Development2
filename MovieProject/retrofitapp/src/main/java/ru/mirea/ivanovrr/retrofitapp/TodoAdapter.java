package ru.mirea.ivanovrr.retrofitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;
import java.util.Locale;

/**
 * Адаптер списка дел. Картинки грузит Picasso; режим отображения (обычный, centerCrop,
 * круглые, маленькие) переключается из Activity через setImageMode.
 * Смена чекбокса уходит наружу через OnCompletedChangeListener — там отправляется PUT.
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    public static final int MODE_DEFAULT = 0;
    public static final int MODE_CENTER_CROP = 1;
    public static final int MODE_CIRCLE = 2;
    public static final int MODE_SMALL = 3;

    public interface OnCompletedChangeListener {
        void onCompletedChanged(Todo todo, boolean completed);
    }

    private final LayoutInflater layoutInflater;
    private final List<Todo> todos;
    private final OnCompletedChangeListener listener;
    private int imageMode = MODE_DEFAULT;

    public TodoAdapter(Context context, List<Todo> todoList, OnCompletedChangeListener listener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.todos = todoList;
        this.listener = listener;
    }

    public void setImageMode(int mode) {
        this.imageMode = mode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.textViewId.setText(String.format(Locale.getDefault(), "#%d · пользователь %d",
                todo.getId(), todo.getUserId()));
        holder.textViewTitle.setText(todo.getTitle());

        // снимаем старый слушатель, иначе setChecked при переиспользовании вызовет PUT
        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(todo.getCompleted());
        holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                listener.onCompletedChanged(todo, isChecked);
            }
        });

        loadImage(holder, todo);
    }

    /** Picasso: placeholder пока грузится, error если не загрузилось, плюс выбранный режим. */
    private void loadImage(TodoViewHolder holder, Todo todo) {
        RequestCreator request = Picasso.get()
                .load(todo.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(android.R.drawable.ic_dialog_alert);

        switch (imageMode) {
            case MODE_CENTER_CROP:
                request.fit().centerCrop();
                break;
            case MODE_CIRCLE:
                request.fit().centerCrop().transform(new CircleTransformation());
                break;
            case MODE_SMALL:
                request.resize(48, 48).centerInside();
                break;
            default:
                request.fit().centerInside();
        }
        request.into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }
}
