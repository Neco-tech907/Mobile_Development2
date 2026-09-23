package ru.mirea.ivanovrr.lapka.presentation;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.ivanovrr.lapka.domain.models.AuthResult;
import ru.mirea.ivanovrr.lapka.domain.models.User;
import ru.mirea.ivanovrr.lapka.domain.usecases.GetProfileUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.LoginUseCase;
import ru.mirea.ivanovrr.lapka.domain.usecases.RegisterUseCase;

/**
 * ViewModel экрана входа. Вызывает use case'ы авторизации в фоновом потоке
 * и публикует состояние экрана через LiveData. Про Activity и Context не знает.
 * Режим (вход / регистрация), загрузка и текст ошибки переживают поворот экрана.
 */
public class AuthViewModel extends ViewModel {

    private static final String TAG = AuthViewModel.class.getSimpleName();

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final GetProfileUseCase getProfileUseCase;

    // Firebase ходит в сеть — отдельный поток, который живёт столько же, сколько ViewModel
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<Boolean> registerMode = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    // Появляется, когда вход или регистрация прошли успешно (или сессия уже была)
    private final MutableLiveData<User> signedInUser = new MutableLiveData<>();

    public AuthViewModel(LoginUseCase loginUseCase, RegisterUseCase registerUseCase,
                         GetProfileUseCase getProfileUseCase) {
        Log.d(TAG, "AuthViewModel created");
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
        this.getProfileUseCase = getProfileUseCase;
    }

    public LiveData<Boolean> getRegisterMode() { return registerMode; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }
    public LiveData<User> getSignedInUser() { return signedInUser; }

    /** Firebase помнит сессию: если пользователь уже входил, сразу отдаём его. */
    public void checkSession() {
        User user = getProfileUseCase.execute();
        if (user != null) {
            signedInUser.setValue(user);
        }
    }

    public void setRegisterMode(boolean enabled) {
        registerMode.setValue(enabled);
        error.setValue(null);
    }

    public boolean isRegisterMode() {
        return Boolean.TRUE.equals(registerMode.getValue());
    }

    public void login(String email, String password) {
        startLoading();
        executor.execute(() -> handleResult(loginUseCase.execute(email, password)));
    }

    public void register(String name, String email, String password, String passwordRepeat) {
        if (password == null || !password.equals(passwordRepeat)) {
            error.setValue("Пароли не совпадают");
            return;
        }
        startLoading();
        executor.execute(() -> handleResult(registerUseCase.execute(email, password, name)));
    }

    private void startLoading() {
        error.setValue(null);
        loading.setValue(true);
    }

    /** Вызывается из фонового потока, поэтому postValue, а не setValue. */
    private void handleResult(AuthResult result) {
        loading.postValue(false);
        if (result.isSuccess()) {
            signedInUser.postValue(result.getUser());
        } else {
            error.postValue(result.getErrorMessage());
        }
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "AuthViewModel cleared");
        executor.shutdownNow();
        super.onCleared();
    }
}
