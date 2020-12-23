package com.sammbo.imdemo.app;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sammbo.imdemo.data.repository.AppRepository;
import com.sammbo.imdemo.data.repository.ChatRepository;
import com.sammbo.imdemo.data.repository.SessionRepository;
import com.sammbo.imdemo.ui.chat.ChatViewModel;
import com.sammbo.imdemo.ui.login.LoginViewModel;
import com.sammbo.imdemo.ui.tab.session.SessioinViewModel;

public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private final Application mApplication;

    public static AppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    private AppViewModelFactory(Application application) {
        this.mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(mApplication, AppRepository.getInstance());
        } else if (modelClass.isAssignableFrom(SessioinViewModel.class)) {
            return (T) new SessioinViewModel(mApplication, SessionRepository.getInstance());
        } else if (modelClass.isAssignableFrom(ChatViewModel.class)) {
            return (T) new ChatViewModel(mApplication, ChatRepository.getInstance());
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
