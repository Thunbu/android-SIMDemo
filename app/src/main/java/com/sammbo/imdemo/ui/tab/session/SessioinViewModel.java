package com.sammbo.imdemo.ui.tab.session;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.data.repository.SessionRepository;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.entity.SessionEntity;
import com.sammbo.imdemo.entity.busevent.EventConnectState;
import com.sammbo.imdemo.ui.widget.ConversationMenu;
import com.sammbo.imdemo.utils.SRxUtils;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class SessioinViewModel extends BaseViewModel<SessionRepository> {

    private Disposable connectStateDisposable;
    private Disposable receiveMsgDisposable;

    public SessioinViewModel(@NonNull Application application) {
        super(application);
    }

    public SessioinViewModel(@NonNull Application application, SessionRepository model) {
        super(application, model);
    }

    public ObservableList<SessionItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<SessionItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.rc_item_sessioin);


    private void top(SessionItemViewModel itemViewModel) {
        SessionEntity entity = itemViewModel.getEntity().get();
        boolean isTop = entity.getTop() == SessionEntity.TOP_Y;
        if (isTop) {
            entity.setTop(SessionEntity.TOP_N);
            observableList.remove(itemViewModel);
            observableList.add(itemViewModel);
        } else {
            entity.setTop(SessionEntity.TOP_Y);
            observableList.remove(itemViewModel);
            observableList.add(0, itemViewModel);
        }
    }

    private void mute(SessionItemViewModel item) {

    }

    private void delete(SessionItemViewModel itemViewModel){
        observableList.remove(itemViewModel);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        updateSessions();
    }

    private void onConnectStateChanged(EventConnectState event) {
        switch (event.getState()) {
            case CONNECT_SUCCESS:
                // 连接成功
                updateSessions();
                break;
            case CONNECTING:
                // 连接中
                break;
            case CONNECT_FAILED:
                // 连接失败
            default:
                break;
        }
    }

    private void updateSessions() {
        addSubscribe(model.updateSessions()
                .compose(SRxUtils.schedulersTransformer())
                .subscribe(sessionEntities -> {
                    if (sessionEntities != null && !sessionEntities.isEmpty()) {
                        observableList.clear();
                        for (SessionEntity entity : sessionEntities) {
                            SessionItemViewModel itemViewModel = new SessionItemViewModel(this, entity);
                            observableList.add(itemViewModel);
                        }
                    }
                }, Throwable::printStackTrace));
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        connectStateDisposable = RxBus.getDefault().toObservable(EventConnectState.class)
                .subscribe(eventConnectState -> {
                    onConnectStateChanged(eventConnectState);
                });
        receiveMsgDisposable = RxBus.getDefault().toObservable(MessageEntity.class)
                .compose(SRxUtils.schedulersTransformer())
                .subscribe(messageEntity -> {
                    updateSessions();
                });
        RxSubscriptions.add(connectStateDisposable);
        RxSubscriptions.add(receiveMsgDisposable);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(connectStateDisposable);
        RxSubscriptions.remove(receiveMsgDisposable);
    }

    public void showMenu(View view, SessionItemViewModel itemViewModel) {
        ConversationMenu menu = new ConversationMenu(view);
        SessionEntity entity = itemViewModel.getEntity().get();
        menu.show(entity, v -> {
            top(itemViewModel);
        }, v -> {
            delete(itemViewModel);
        });
    }
}
