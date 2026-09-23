package ru.mirea.ivanovrr.lapka.presentation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mirea.ivanovrr.lapka.R;

/**
 * Временный экран-проверка слоёв. Каждая кнопка вызывает метод MainViewModel,
 * а результат приходит обратно через LiveData. Activity не знает ни про use case'ы, ни про потоки.
 * После поворота экрана текст результата, пользователь и карточка приюта остаются на месте.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity created");

        EdgeToEdge.enable(this, SystemBarStyle.dark(Color.TRANSPARENT));
        setContentView(R.layout.activity_main);
        InkHeader.applyInsets(findViewById(R.id.main), findViewById(R.id.header));

        vm = new ViewModelProvider(this, new ViewModelFactory(this)).get(MainViewModel.class);

        TextView log = findViewById(R.id.textViewLog);
        TextView textViewUser = findViewById(R.id.textViewUser);
        TextView textViewShelter = findViewById(R.id.textViewShelter);
        Button buttonAccount = findViewById(R.id.buttonAccount);

        // RecyclerView приютов: адаптер получает данные из LiveData
        ShelterAdapter shelterAdapter = new ShelterAdapter();
        RecyclerView recyclerShelters = findViewById(R.id.recyclerShelters);
        recyclerShelters.setLayoutManager(new LinearLayoutManager(this));
        recyclerShelters.setAdapter(shelterAdapter);

        // --- подписки на LiveData ---
        vm.getLog().observe(this, text -> {
            if (text != null) {
                log.setText(text);
            }
        });
        vm.getShelterDetails().observe(this, textViewShelter::setText);
        vm.getShelters().observe(this, shelterAdapter::setItems);
        vm.getCurrentUser().observe(this, user -> {
            if (user == null) {
                textViewUser.setText(R.string.main_guest);
                buttonAccount.setText(R.string.main_login);
            } else {
                textViewUser.setText(getString(R.string.main_user, user.getName(), user.getEmail()));
                buttonAccount.setText(R.string.main_logout);
            }
        });
        vm.getLoggedOut().observe(this, loggedOut -> {
            if (Boolean.TRUE.equals(loggedOut)) {
                startActivity(new Intent(this, AuthActivity.class));
                finish();
            }
        });

        // --- нажатия уходят во ViewModel ---
        findViewById(R.id.buttonRecognize).setOnClickListener(v -> vm.recognize());
        findViewById(R.id.buttonShelters).setOnClickListener(v -> vm.loadShelters());
        findViewById(R.id.buttonPets).setOnClickListener(v -> vm.loadPets());
        findViewById(R.id.buttonShelterDetails).setOnClickListener(v -> vm.loadShelterDetails());
        findViewById(R.id.buttonSaveEncounter).setOnClickListener(v -> vm.saveEncounter());
        findViewById(R.id.buttonAlbum).setOnClickListener(v -> vm.loadAlbum());
        findViewById(R.id.buttonAddReview).setOnClickListener(v -> vm.addReview());
        findViewById(R.id.buttonReviews).setOnClickListener(v -> vm.loadReviews());
        findViewById(R.id.buttonProfile).setOnClickListener(v -> vm.loadProfile());
        buttonAccount.setOnClickListener(v -> vm.logout());
    }
}
