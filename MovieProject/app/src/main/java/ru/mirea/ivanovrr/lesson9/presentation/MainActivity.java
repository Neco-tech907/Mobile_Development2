package ru.mirea.ivanovrr.lesson9.presentation;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.ivanovrr.lesson9.R;
import ru.mirea.ivanovrr.lesson9.domain.models.Movie;

/**
 * Слой presentation, часть View. Activity только показывает данные и передаёт нажатия во ViewModel.
 * Use case'ов и репозиториев здесь больше нет.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    /*
     * Шаг методички «что отобразится в logcat при повороте экрана»:
     * false — ViewModel создаётся обычным new и пересоздаётся при каждом повороте;
     * true  — ViewModel берётся из ViewModelProvider и переживает поворот.
     */
    private static final boolean CREATE_WITH_PROVIDER = true;

    private MainViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d(TAG, "MainActivity created");

        ViewModelFactory factory = new ViewModelFactory(this);
        if (CREATE_WITH_PROVIDER) {
            // правильно: при повороте вернётся тот же объект
            vm = new ViewModelProvider(this, factory).get(MainViewModel.class);
        } else {
            // неправильно: то же самое, что new MainViewModel(...), — новый объект на каждый onCreate
            vm = factory.create(MainViewModel.class);
        }

        EditText editTextMovie = findViewById(R.id.editTextMovie);
        TextView textViewMovie = findViewById(R.id.textViewMovie);

        // подписка на LiveData: после поворота придёт последнее значение
        vm.getFavoriteMovie().observe(this, text -> textViewMovie.setText(text));

        findViewById(R.id.buttonSaveMovie).setOnClickListener(view ->
                vm.setText(new Movie(2, editTextMovie.getText().toString())));

        findViewById(R.id.buttonGetMovie).setOnClickListener(view -> vm.getText());
    }
}
