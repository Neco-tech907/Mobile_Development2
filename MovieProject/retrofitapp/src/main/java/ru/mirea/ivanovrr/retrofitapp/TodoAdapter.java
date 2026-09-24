package ru.mirea.ivanovrr.retrofitapp;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
 *
 * Задание методички §2: resize / fit / centerCrop / centerInside / transform.
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    public static final int MODE_DEFAULT = 0;
    public static final int MODE_CENTER_CROP = 1;
    public static final int MODE_CIRCLE = 2;
    public static final int MODE_SMALL = 3;

    private static final int SIZE_NORMAL_DP = 72;
    private static final int SIZE_SMALL_DP = 48;
    /** Фон только для centerInside — чтобы были видны поля вокруг горизонтального фото. */
    private static final int LETTERBOX_COLOR = Color.parseColor("#E0E6E2");

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

        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(todo.getCompleted());
        holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                listener.onCompletedChanged(todo, isChecked);
            }
        });

        loadImage(holder, todo);
    }

    private void loadImage(TodoViewHolder holder, Todo todo) {
        ImageView imageView = holder.imageView;
        Picasso.get().cancelRequest(imageView);

        int sizeDp = (imageMode == MODE_SMALL) ? SIZE_SMALL_DP : SIZE_NORMAL_DP;
        int sizePx = dpToPx(imageView, sizeDp);
        setImageViewSize(imageView, sizePx);

        // фон только у «Обычные» (centerInside + letterbox); иначе квадрат просвечивает под кругом
        if (imageMode == MODE_DEFAULT) {
            imageView.setBackgroundColor(LETTERBOX_COLOR);
        } else {
            imageView.setBackgroundColor(Color.TRANSPARENT);
        }

        RequestCreator request = Picasso.get()
                .load(todo.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(android.R.drawable.ic_dialog_alert);

        switch (imageMode) {
            case MODE_CENTER_CROP:
                // как в методичке: заполняет квадрат, края обрезаются
                request.resize(sizePx, sizePx).centerCrop();
                break;
            case MODE_CIRCLE:
                // transform — «круглый стиль» из методички; без фона углы прозрачные
                request.resize(sizePx, sizePx).centerCrop().transform(new CircleTransformation());
                break;
            case MODE_SMALL:
                // resize + centerCrop — как пример resize(100,100).centerCrop() в методичке
                request.resize(sizePx, sizePx).centerCrop();
                break;
            default:
                // centerInside: вся горизонтальная картинка целиком, поля фона видны
                request.resize(sizePx, sizePx).centerInside();
                break;
        }
        request.into(imageView);
    }

    private static int dpToPx(View view, int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, view.getResources().getDisplayMetrics()));
    }

    private static void setImageViewSize(ImageView imageView, int sizePx) {
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        if (lp.width != sizePx || lp.height != sizePx) {
            lp.width = sizePx;
            lp.height = sizePx;
            imageView.setLayoutParams(lp);
        }
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }
}
