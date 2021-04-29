package com.sammbo.imdemo.ui.tab.session;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.geely.imsdk.client.bean.message.SIMSecType;
import com.geely.imsdk.client.bean.message.SIMSessionType;
import com.geely.imsdk.client.bean.session.SIMDisturbParam;
import com.geely.imsdk.client.bean.session.SIMSessionRequest;
import com.geely.imsdk.client.bean.session.SIMTopParam;
import com.geely.imsdk.client.listener.SIMCallBack;
import com.geely.imsdk.client.manager.session.send.SIMSessionManager;
import com.geely.imsdk.client.manager.system.SIMManager;
import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.data.http.SBaseResponse;
import com.sammbo.imdemo.data.repository.SessionRepository;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.entity.SessionEntity;
import com.sammbo.imdemo.entity.busevent.EventConnectState;
import com.sammbo.imdemo.entity.busevent.EventMessage;
import com.sammbo.imdemo.sdk.EnvService;
import com.sammbo.imdemo.sdk.SDKManager;
import com.sammbo.imdemo.ui.login.LoginActivity;
import com.sammbo.imdemo.ui.login.bean.UploadAddress;
import com.sammbo.imdemo.ui.tab.address.AddressActivity;
import com.sammbo.imdemo.ui.widget.ConversationMenu;
import com.sammbo.imdemo.utils.SRxUtils;
import com.sammbo.imdemo.utils.UploadUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

import static com.sammbo.imdemo.entity.SessionEntity.NOTDISTURB_Y;
import static com.sammbo.imdemo.entity.busevent.EventCode.CODE_LOGIN_OUT;
import static com.sammbo.imdemo.entity.busevent.EventCode.CODE_SESSION_UPDATE;

public class SessioinViewModel extends BaseViewModel<SessionRepository> {

    private Disposable connectStateDisposable;
    private Disposable receiveMsgDisposable;
    private Disposable eventEessageDisposable;
    public BindingCommand exit = new BindingCommand(() -> exitLogin());
    public BindingCommand chooseMembers = new BindingCommand(() -> chooseMembers());

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
        SIMTopParam param = new SIMTopParam();
        param.setSecurityType(SIMSecType.NORMAL);
        param.setSessionId(entity.getSessionId());
        param.setTop(!isTop);
        param.setSessionType(SIMSessionType.getType(entity.getSessionType()));
        SIMSessionManager.getInstance().setIsTop(param, new SIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
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
        });
    }

    private void mute(SessionItemViewModel item) {
        SessionEntity entity = item.getEntity().get();
        boolean distub = entity.getNotDisturb() == SessionEntity.NOTDISTURB_N;
        SIMDisturbParam param = new SIMDisturbParam();
        param.setSecurityType(SIMSecType.NORMAL);
        param.setSessionId(entity.getSessionId());
        param.setSessionType(SIMSessionType.getType(entity.getSessionType()));
        param.setDisturb(!distub);
        SIMSessionManager.getInstance().setNotDisturb(param, new SIMCallBack() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess() {
                if (distub) {
                    entity.setNotDisturb(SessionEntity.NOTDISTURB_Y);
                } else {
                    entity.setNotDisturb(SessionEntity.NOTDISTURB_N);
                }
                item.setEntity(entity);
            }
        });
    }

    private void delete(SessionItemViewModel itemViewModel) {
        SessionEntity entity = itemViewModel.getEntity().get();
        SIMSessionRequest request = new SIMSessionRequest();
        request.setSessionId(entity.getSessionId());
        request.setSessionType(entity.getSessionType());
        request.setSecurityType(SIMSecType.NORMAL.value());
        SIMManager.getInstance().getSessionManager().hideSession(request, new SIMCallBack() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess() {
                observableList.remove(itemViewModel);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updateSessions();
        getUploadAddress();
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
        eventEessageDisposable = RxBus.getDefault().toObservable(EventMessage.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    int code = message.getCode();
                    if (code == CODE_LOGIN_OUT) {
                        exitLogin();
                    } else if (code == CODE_SESSION_UPDATE) {
                        updateSessions();
                    }
                }, throwable -> {

                });
        RxSubscriptions.add(connectStateDisposable);
        RxSubscriptions.add(receiveMsgDisposable);
        RxSubscriptions.add(eventEessageDisposable);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(connectStateDisposable);
        RxSubscriptions.remove(receiveMsgDisposable);
        RxSubscriptions.remove(eventEessageDisposable);
    }

    public void showMenu(View view, SessionItemViewModel itemViewModel) {
        ConversationMenu menu = new ConversationMenu(view);
        SessionEntity entity = itemViewModel.getEntity().get();
        menu.show(entity, v -> {
            top(itemViewModel);
        }, v -> {
            delete(itemViewModel);
        }, v -> {
            mute(itemViewModel);
        });
    }

    private void exitLogin() {
        model.removeAccount();
        startActivity(LoginActivity.class);
        finish();
    }

    private void getUploadAddress() {
        if(SDKManager.envService != EnvService.PRD){
            addSubscribe(model.getUploadAddress().compose(SRxUtils.schedulersTransformer())
                    .subscribe(uploadAddressSBaseResponse -> {
                        UploadAddress address = uploadAddressSBaseResponse.getData();
                        if (address != null) {
                            UploadUtils.getInstance().setToken(address.getSecurityToken());
                            UploadUtils.getInstance().setHost(address.getSecurityDomain());
                        }
                    }, throwable -> {

                    }));
        }else {
            UploadUtils.getInstance().setToken("FsUSX95uOR0Wq0f50j1T2BnxcJhgwHyWr21IglCq:5PGKCjX8Y8rc8E9i-aAwMLCttR0=:eyJzY29wZSI6InNpbS1zZGsiLCJkZWFkbGluZSI6MTk3ODkyMjMyN30=");
            UploadUtils.getInstance().setHost("http://qiniu-test.sammbo.com/");
        }
    }

    public void addNewSession(SessionEntity entity) {
        boolean hasContain = false;
        for (SessionItemViewModel sessionItemViewModel : observableList) {
            SessionEntity oldEntity = sessionItemViewModel.getEntity().get();
            if (oldEntity == entity) {
                hasContain = true;
            }
        }
        if (!hasContain) {
            SessionItemViewModel model = new SessionItemViewModel(this, entity);
            observableList.add(model);
        }
    }

    public void chooseMembers() {
        startActivity(AddressActivity.class);
    }
}
