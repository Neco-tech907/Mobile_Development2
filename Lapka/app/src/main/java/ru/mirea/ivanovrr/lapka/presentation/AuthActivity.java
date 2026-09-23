package ru.mirea.ivanovrr.lapka.presentation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.ivanovrr.lapka.R;

/**
 * Стартовый экран по макетам 02 «Вход» и 03 «Регистрация».
 * Activity только рисует состояние из AuthViewModel и передаёт ей нажатия.
 * Use case'ов, потоков и Firebase здесь нет.
 */
public class AuthActivity extends AppCompatActivity {

    private static final String TAG = AuthActivity.class.getSimpleName();

    private AuthViewModel vm;

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
        Log.d(TAG, "AuthActivity created");

        vm = new ViewModelProvider(this, new ViewModelFactory(this)).get(AuthViewModel.class);

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

        // --- подписки на LiveData: экран просто отражает состояние ViewModel ---
        vm.getRegisterMode().observe(this, this::showRegisterMode);
        vm.getLoading().observe(this, this::showLoading);
        vm.getError().observe(this, message -> {
            textViewStatus.setText(message);
            textViewStatus.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
        });
        vm.getSignedInUser().observe(this, user -> {
            if (user == null) {
                return;
            }
            Toast.makeText(this, getString(R.string.auth_welcome, user.getName()),
                    Toast.LENGTH_SHORT).show();
            openMain();
        });

        // --- нажатия уходят во ViewModel ---
        buttonLogin.setOnClickListener(v -> vm.login(
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString()));

        buttonRegister.setOnClickListener(v -> vm.register(
                editTextName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString(),
                editTextPasswordRepeat.getText().toString()));

        buttonCreateAccount.setOnClickListener(v -> vm.setRegisterMode(true));
        textToLogin.setOnClickListener(v -> vm.setRegisterMode(false));
        buttonBack.setOnClickListener(v -> vm.setRegisterMode(false));
        buttonGuest.setOnClickListener(v -> openMain());

        // системная «назад» в режиме регистрации возвращает к входу
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (vm.isRegisterMode()) {
                    vm.setRegisterMode(false);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        // если Firebase помнит сессию, сработает наблюдатель signedInUser
        if (savedInstanceState == null) {
            vm.checkSession();
        }
    }

    /** Переключает экран между макетом «Вход» и макетом «Регистрация». */
    private void showRegisterMode(Boolean enabled) {
        boolean register = Boolean.TRUE.equals(enabled);
        int registerOnly = register ? View.VISIBLE : View.GONE;
        int loginOnly = register ? View.GONE : View.VISIBLE;

        textTitle.setText(register ? R.string.auth_register_title : R.string.auth_login_title);
        textSubtitle.setText(register ? R.string.auth_register_subtitle
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
    }

    private void showLoading(Boolean isLoading) {
        boolean loading = Boolean.TRUE.equals(isLoading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        buttonLogin.setEnabled(!loading);
        buttonRegister.setEnabled(!loading);
        buttonCreateAccount.setEnabled(!loading);
        buttonGuest.setEnabled(!loading);
    }

    private void openMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
