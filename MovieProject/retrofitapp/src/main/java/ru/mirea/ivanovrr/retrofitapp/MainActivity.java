package ru.mirea.ivanovrr.retrofitapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit: GET /todos → RecyclerView, PUT /todos/{id} при смене чекбокса.
 * Ошибки сети — блок с текстом и кнопкой «Повторить». Картинки — Picasso.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View layoutError;
    private TextView textViewError;
    private Spinner spinnerMode;

    private TodoAdapter todoAdapter;
    private ApiService apiService;

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

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        layoutError = findViewById(R.id.layoutError);
        textViewError = findViewById(R.id.textViewError);
        spinnerMode = findViewById(R.id.spinnerMode);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrofit: базовый адрес + конвертер Gson (JSON <-> Todo)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // выбор режима отображения картинок
        spinnerMode.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.image_modes, android.R.layout.simple_spinner_dropdown_item));
        spinnerMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (todoAdapter != null) {
                    todoAdapter.setImageMode(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        findViewById(R.id.buttonRetry).setOnClickListener(v -> loadTodos());
        loadTodos();
    }

    private void loadTodos() {
        showLoading();
        apiService.getTodos().enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Todo>> call,
                                   @NonNull Response<List<Todo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Todo> todos = response.body();
                    todoAdapter = new TodoAdapter(MainActivity.this, todos,
                            MainActivity.this::updateTodo);
                    todoAdapter.setImageMode(spinnerMode.getSelectedItemPosition());
                    recyclerView.setAdapter(todoAdapter);
                    showList();
                } else {
                    // сервер ответил, но не 2xx — например 404 или 500
                    Log.e(TAG, "onResponse: " + response.code());
                    showError(getString(R.string.error_http, response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Todo>> call, @NonNull Throwable t) {
                // нет сети, неверный адрес, не разобрался JSON
                Log.e(TAG, "onFailure: " + t.getMessage());
                showError(getString(R.string.error_network, t.getMessage()));
            }
        });
    }

    /** Задание методички: при смене чекбокса отправить PUT с новым состоянием. */
    private void updateTodo(Todo todo, boolean completed) {
        todo.setCompleted(completed);
        apiService.updateTodo(todo.getId(), todo).enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(@NonNull Call<Todo> call, @NonNull Response<Todo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String state = getString(response.body().getCompleted()
                            ? R.string.done : R.string.not_done);
                    Toast.makeText(MainActivity.this,
                            getString(R.string.todo_updated, todo.getId(), state),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "updateTodo: " + response.code());
                    Toast.makeText(MainActivity.this,
                            getString(R.string.todo_update_failed, todo.getId()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Todo> call, @NonNull Throwable t) {
                Log.e(TAG, "updateTodo failure: " + t.getMessage());
                // откатываем чекбокс, раз сервер не подтвердил
                todo.setCompleted(!completed);
                todoAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,
                        getString(R.string.todo_update_failed, todo.getId()),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showList() {
        progressBar.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        textViewError.setText(message);
        layoutError.setVisibility(View.VISIBLE);
    }
}
