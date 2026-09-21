package ru.mirea.ivanovrr.lesson9.presentation;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.ivanovrr.lesson9.R;
import ru.mirea.ivanovrr.lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.ivanovrr.lesson9.domain.models.Movie;
import ru.mirea.ivanovrr.lesson9.domain.repository.MovieRepository;
import ru.mirea.ivanovrr.lesson9.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.ivanovrr.lesson9.domain.usecases.SaveMovieToFavoriteUseCase;

/**
 * Слой presentation. Экран собирает зависимости (подставляет реализацию
 * репозитория в use case) и умеет только показывать результат.
 */
public class MainActivity extends AppCompatActivity {

    private GetFavoriteFilmUseCase getFavoriteFilmUseCase;
    private SaveMovieToFavoriteUseCase saveMovieToFavoriteUseCase;

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

        // Context уходит только в data-слой, domain о нём не знает
        MovieRepository movieRepository = new MovieRepositoryImpl(this);
        getFavoriteFilmUseCase = new GetFavoriteFilmUseCase(movieRepository);
        saveMovieToFavoriteUseCase = new SaveMovieToFavoriteUseCase(movieRepository);

        EditText editTextMovie = findViewById(R.id.editTextMovie);
        TextView textViewMovie = findViewById(R.id.textViewMovie);

        findViewById(R.id.buttonSaveMovie).setOnClickListener(view -> {
            Movie movie = new Movie(2, editTextMovie.getText().toString());
            boolean result = saveMovieToFavoriteUseCase.execute(movie);
            textViewMovie.setText(String.format("Save result %s", result));
        });

        findViewById(R.id.buttonGetMovie).setOnClickListener(view -> {
            Movie movie = getFavoriteFilmUseCase.execute();
            if (movie == null) {
                textViewMovie.setText("Нет данных!");
            } else {
                textViewMovie.setText(String.format("Любимый фильм: %s", movie.getName()));
            }
        });
    }
}
