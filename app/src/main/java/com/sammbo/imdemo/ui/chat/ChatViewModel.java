package com.sammbo.imdemo.ui.chat;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;

import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.data.Global;
import com.sammbo.imdemo.data.repository.ChatRepository;
import com.sammbo.imdemo.entity.FileInfo;
import com.sammbo.imdemo.entity.ImageInfo;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.entity.VideoInfo;
import com.sammbo.imdemo.entity.busevent.EventConnectState;
import com.sammbo.imdemo.sdk.SDKManager;
import com.sammbo.imdemo.ui.chat.msg.BaseMsgViewModel;
import com.sammbo.imdemo.ui.chat.msg.ChatImgInViewModel;
import com.sammbo.imdemo.ui.chat.msg.ChatImgOutViewModel;
import com.sammbo.imdemo.ui.chat.msg.ChatTxtInViewModel;
import com.sammbo.imdemo.ui.chat.msg.ChatTxtOutViewModel;
import com.sammbo.imdemo.ui.chat.msg.ChatVideoInViewModel;
import com.sammbo.imdemo.ui.chat.msg.ChatVideoOutViewModel;
import com.sammbo.imdemo.ui.chat.msg.FileInfoViewModel;
import com.sammbo.imdemo.utils.SRxUtils;
import com.sammbo.imdemo.utils.event.Event;

import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

public class ChatViewModel extends BaseViewModel<ChatRepository> {
    private static final int TYPE_TXT_IN = 0;
    private static final int TYPE_TXT_OUT = 1;
    private static final int TYPE_IMG_IN = 2;
    private static final int TYPE_IMG_OUT = 3;
    private static final int TYPE_VIDEO_IN = 4;
    private static final int TYPE_VIDEO_OUT = 5;
    private static final int TYPE_FILE_IN = 6;
    private static final int TYPE_FILE_OUT = 7;
    private Disposable receiveMsgDisposable;

    public ObservableField<String> sessionId = new ObservableField<>("");
    public ObservableField<String> sessionName = new ObservableField<>("");
    public ObservableField<Boolean> refereshing = new ObservableField<>();

    public MutableLiveData<Event<Object>> scrollToEnd = new MutableLiveData<>();

//    //封装一个界面发生改变的观察者
//    public UIChangeObservable uc = new UIChangeObservable();
//    public class UIChangeObservable {
//        //下拉刷新完成
//        public SingleLiveEvent finishRefreshing = new SingleLiveEvent<>();
//    }

    public ChatViewModel(@NonNull Application application) {
        super(application);
    }

    public ChatViewModel(@NonNull Application application, ChatRepository model) {
        super(application, model);
    }

    public ObservableList<BaseMsgViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<BaseMsgViewModel> itemBinding = ItemBinding.of(new OnItemBind<BaseMsgViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, BaseMsgViewModel item) {
            int itemType = (int) item.getItemType();
            if (itemType == TYPE_TXT_IN) {
                itemBinding.set(BR.viewModel, R.layout.rc_item_msg_txt_in);
            } else if (itemType == TYPE_TXT_OUT) {
                itemBinding.set(BR.viewModel, R.layout.rc_item_msg_txt_out);
            } else if (itemType == TYPE_IMG_IN) {
                itemBinding.set(BR.viewModel, R.layout.rc_item_msg_img_in);
            } else if (itemType == TYPE_IMG_OUT) {
                itemBinding.set(BR.viewModel, R.layout.rc_item_msg_img_out);
            } else if (itemType == TYPE_VIDEO_IN) {
                itemBinding.set(BR.viewModel, R.layout.rc_item_msg_video_in);
            } else if (itemType == TYPE_VIDEO_OUT) {
                itemBinding.set(BR.viewModel, R.layout.rc_item_msg_video_out);
            } else if (itemType == TYPE_FILE_IN) {
                itemBinding.set(BR.viewModel, R.layout.rc_item_msg_file_in);
            } else if (itemType == TYPE_FILE_OUT) {
                itemBinding.set(BR.viewModel, R.layout.rc_item_msg_file_out);
            }
        }
    });

    public BindingCommand backClick = new BindingCommand(() -> onBackPressed());

    private boolean firstRefresh = true;
    public BindingCommand refreshListener = new BindingCommand(() -> {
        refereshing.set(true);
        fetchOfflineMsgs();
    });

    @Override
    public void onCreate() {
        super.onCreate();
        refereshing.set(true);
        fetchOfflineMsgs();
    }

    private void fetchOfflineMsgs() {
        String boundaryMsgId = observableList.isEmpty() ? "" : observableList.get(0).entity.get().getMessageId();
        addSubscribe(model.fetchHistoryMsg(sessionId.get(), boundaryMsgId)
                .compose(SRxUtils.singleSchedulersTransformer())
                .subscribe(list -> {
                    refereshing.set(false);
                    if (firstRefresh) {
                        observableList.clear();
                    }
                    if (list != null && !list.isEmpty()) {
                        Collections.reverse(list);
                        for (MessageEntity entity : list) {
                            if (entity.getMessageId().equals(boundaryMsgId)) {
                                continue;
                            }
                            addMessageToList(true, entity);
                        }
                    }
                    if (firstRefresh) {
                        scrollToEnd.postValue(new Event<>(new Object()));
                    }
                    firstRefresh = false;
                }, throwable -> {
                    Log.e("Tag","fetchOfflineMsgs error :"+throwable.getMessage());
                    refereshing.set(false);
                }));
    }

    private void addMessageToList(boolean header, MessageEntity entity) {
        BaseMsgViewModel itemViewModel;
        switch (entity.getMsgType()) {
            case MessageEntity.TYPE_IMG:
                if (entity.getBoxType() == MessageEntity.BOX_IN) {
                    itemViewModel = new ChatImgInViewModel(this, entity);
                    itemViewModel.multiItemType(TYPE_IMG_IN);
                } else {
                    itemViewModel = new ChatImgOutViewModel(this, entity);
                    itemViewModel.multiItemType(TYPE_IMG_OUT);
                }
                break;
            case MessageEntity.TYPE_VIDEO:
                if (entity.getBoxType() == MessageEntity.BOX_IN) {
                    itemViewModel = new ChatVideoInViewModel(this, entity);
                    itemViewModel.multiItemType(TYPE_VIDEO_IN);
                } else {
                    itemViewModel = new ChatVideoOutViewModel(this, entity);
                    itemViewModel.multiItemType(TYPE_VIDEO_OUT);
                }
                break;
            case MessageEntity.TYPE_FILE:
                if (entity.getBoxType() == MessageEntity.BOX_IN) {
                    itemViewModel = new FileInfoViewModel(this, entity);
                    itemViewModel.multiItemType(TYPE_FILE_IN);
                } else {
                    itemViewModel = new FileInfoViewModel(this, entity);
                    itemViewModel.multiItemType(TYPE_FILE_OUT);
                }
                break;
            case MessageEntity.TYPE_TXT:
            default:
                if (entity.getBoxType() == MessageEntity.BOX_IN) {
                    itemViewModel = new ChatTxtInViewModel(this, entity);
                    itemViewModel.multiItemType(TYPE_TXT_IN);
                } else {
                    itemViewModel = new ChatTxtOutViewModel(this, entity);
                    itemViewModel.multiItemType(TYPE_TXT_OUT);
                }
                break;
        }
        if (header) {
            observableList.add(0, itemViewModel);
        } else {
            observableList.add(itemViewModel);
            scrollToEnd.postValue(new Event<>(new Object()));
        }
    }

    public void sendText(String body) {
        MessageEntity entity = create();
        entity.setMsgType(MessageEntity.TYPE_TXT);
        entity.setData(body);
        addMessageToList(false, SDKManager.getInstance().sendMessage(entity));
    }

    public void sendImage(ImageInfo imageInfo) {
        MessageEntity entity = create();
        entity.setMsgType(MessageEntity.TYPE_IMG);
        entity.setData(imageInfo);
        addMessageToList(false, SDKManager.getInstance().sendMessage(entity));
    }

    public void sendVideo(VideoInfo videoInfo) {
        MessageEntity entity = create();
        entity.setMsgType(MessageEntity.TYPE_VIDEO);
        entity.setData(videoInfo);
        addMessageToList(false, SDKManager.getInstance().sendMessage(entity));
    }

    public void sendFile(FileInfo fileInfo) {
        MessageEntity<FileInfo> entity = create();
        entity.setMsgType(MessageEntity.TYPE_FILE);
        entity.setData(fileInfo);
        addMessageToList(false, SDKManager.getInstance().sendMessage(entity));
    }

    private MessageEntity create() {
        return MessageEntity.builder()
                .sessionId(sessionId.get())
                .to(sessionId.get())
                .createTime(System.currentTimeMillis())
                .from(Global.getAccount())
                .boxType(MessageEntity.BOX_OUT)
                //todo send state
                .build();
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        receiveMsgDisposable = RxBus.getDefault().toObservable(MessageEntity.class)
                .compose(SRxUtils.schedulersTransformer())
                .subscribe(messageEntity -> {
                    addMessageToList(false, messageEntity);
                    scrollToEnd.postValue(new Event<>(new Object()));
                });
        RxSubscriptions.add(receiveMsgDisposable);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(receiveMsgDisposable);
    }
}
