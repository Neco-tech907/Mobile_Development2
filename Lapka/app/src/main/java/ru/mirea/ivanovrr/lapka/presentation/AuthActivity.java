package ru.mirea.ivanovrr.lapka.presentation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.ivanovrr.lapka.R;
import ru.mirea.ivanovrr.lapka.di.ServiceLocator;
import ru.mirea.ivanovrr.lapka.domain.models.AuthResult;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetProfileUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.LoginUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.RegisterUseCase;

/**
 * Стартовый экран по макетам 02 «Вход» и 03 «Регистрация».
 * Один экран, два режима: вход и регистрация переключаются без смены Activity.
 * Работает только с use case'ами — о Firebase знает лишь модуль data.
 */
public class AuthActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private LoginUseCase loginUseCase;
    private RegisterUseCase registerUseCase;

    private boolean registerMode;

    private TextView textTitle;
    private TextView textSubtitle;
    private View layoutName;
    private View layoutPasswordRepeat;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordRepeat;
    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonCreateAccount;
    private Button buttonGuest;
    private View buttonBack;
    private View imageLogo;
    private TextView textToLogin;
    private ProgressBar progressBar;
    private TextView textViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ServiceLocator serviceLocator = ServiceLocator.getInstance(this);
        GetProfileUseCase getProfileUseCase = serviceLocator.provideGetProfileUseCase();
        // Firebase помнит сессию — если уже входили, сразу открываем главный экран
        if (getProfileUseCase.execute() != null) {
            openMain();
            return;
        }
        loginUseCase = serviceLocator.provideLoginUseCase();
        registerUseCase = serviceLocator.provideRegisterUseCase();

        // шапка тёмная — значки статус-бара делаем светлыми
        EdgeToEdge.enable(this, SystemBarStyle.dark(Color.TRANSPARENT));
        setContentView(R.layout.activity_auth);
        InkHeader.applyInsets(findViewById(R.id.main), findViewById(R.id.header));

        textTitle = findViewById(R.id.textTitle);
        textSubtitle = findViewById(R.id.textSubtitle);
        layoutName = findViewById(R.id.layoutName);
        layoutPasswordRepeat = findViewById(R.id.layoutPasswordRepeat);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordRepeat = findViewById(R.id.editTextPasswordRepeat);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        buttonGuest = findViewById(R.id.buttonGuest);
        buttonBack = findViewById(R.id.buttonBack);
        imageLogo = findViewById(R.id.imageLogo);
        textToLogin = findViewById(R.id.textToLogin);
        progressBar = findViewById(R.id.progressBar);
        textViewStatus = findViewById(R.id.textViewStatus);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            setLoading(true);
            // вход ходит в сеть — только в фоновом потоке
            executor.execute(() -> {
                AuthResult result = loginUseCase.execute(email, password);
                runOnUiThread(() -> handleResult(result));
            });
        });

        buttonRegister.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            String passwordRepeat = editTextPasswordRepeat.getText().toString();
            if (!password.equals(passwordRepeat)) {
                showError(getString(R.string.auth_passwords_differ));
                return;
            }
            setLoading(true);
            executor.execute(() -> {
                AuthResult result = registerUseCase.execute(email, password, name);
                runOnUiThread(() -> handleResult(result));
            });
        });

        buttonCreateAccount.setOnClickListener(v -> setRegisterMode(true));
        textToLogin.setOnClickListener(v -> setRegisterMode(false));
        buttonBack.setOnClickListener(v -> setRegisterMode(false));
        buttonGuest.setOnClickListener(v -> openMain());

        // системная «назад» в режиме регистрации возвращает к входу
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (registerMode) {
                    setRegisterMode(false);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        setRegisterMode(false);
    }

    /** Переключает экран между макетом «Вход» и макетом «Регистрация». */
    private void setRegisterMode(boolean enabled) {
        registerMode = enabled;
        int registerOnly = enabled ? View.VISIBLE : View.GONE;
        int loginOnly = enabled ? View.GONE : View.VISIBLE;

        textTitle.setText(enabled ? R.string.auth_register_title : R.string.auth_login_title);
        textSubtitle.setText(enabled ? R.string.auth_register_subtitle
                : R.string.auth_login_subtitle);

        layoutName.setVisibility(registerOnly);
        layoutPasswordRepeat.setVisibility(registerOnly);
        buttonRegister.setVisibility(registerOnly);
        textToLogin.setVisibility(registerOnly);
        buttonBack.setVisibility(registerOnly);

        buttonLogin.setVisibility(loginOnly);
        buttonCreateAccount.setVisibility(loginOnly);
        buttonGuest.setVisibility(loginOnly);
        imageLogo.setVisibility(loginOnly);

        textViewStatus.setVisibility(View.GONE);
    }

    private void handleResult(AuthResult result) {
        setLoading(false);
        if (result.isSuccess()) {
            Toast.makeText(this, getString(R.string.auth_welcome, result.getUser().getName()),
                    Toast.LENGTH_SHORT).show();
            openMain();
        } else {
            showError(result.getErrorMessage());
        }
    }

    private void showError(String message) {
        textViewStatus.setText(message);
        textViewStatus.setVisibility(View.VISIBLE);
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        buttonLogin.setEnabled(!loading);
        buttonRegister.setEnabled(!loading);
        buttonCreateAccount.setEnabled(!loading);
        buttonGuest.setEnabled(!loading);
        if (loading) {
            textViewStatus.setVisibility(View.GONE);
        }
    }

    private void openMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
