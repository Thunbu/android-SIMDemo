package com.sammbo.imdemo.ui.tab.address;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.MutableLiveData;

import com.geely.imsdk.client.listener.SIMValueCallBack;
import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.data.repository.AddressRepository;
import com.sammbo.imdemo.entity.busevent.EventCode;
import com.sammbo.imdemo.entity.busevent.EventMessage;
import com.sammbo.imdemo.sdk.SDKManager;
import com.sammbo.imdemo.ui.tab.address.bean.AddressEntity;
import com.sammbo.imdemo.utils.SRxUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * author : wangqiang
 * e-mail : qiang.wang12@geely.com
 * time   : 2021/01/26
 * desc   :
 * version:
 */
public class AddressViewModel extends BaseViewModel<AddressRepository> {
    public ObservableField<Boolean> hasCheckBox = new ObservableField<>();

    public AddressViewModel(@NonNull Application application, AddressRepository model) {
        super(application, model);
    }

    public ObservableList<AddressItemViewModel> observableList = new ObservableArrayList<>();
    public ItemBinding<AddressItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.rc_item_address);
    public MutableLiveData<Integer> page = new MutableLiveData<>();

    public BindingCommand createGroupCommand = new BindingCommand(() -> createGroup());

    private void createGroup() {
        StringBuilder groupName=new StringBuilder();
        List<String> groupList = new ArrayList<>();
        for (AddressItemViewModel itemViewModel : observableList) {
            if (itemViewModel.isCheckedFiled.get()) {
                groupName.append(itemViewModel.addressEntityObservableField.get().getUserNickname()+"、");
                groupList.add(itemViewModel.addressEntityObservableField.get().getAccount());
            }
        }
        if (groupList.size() > 0) {
            model.createGroup(groupName.toString(),groupList, new SIMValueCallBack<String>() {
                @Override
                public void onError(int i, String s) {
                    Log.d("Tag", "create group fail :" + s);
                }

                @Override
                public void onSuccess(String s) {
                    Log.d("Tag","create group success");
                    finish();
                    EventMessage message=new EventMessage();
                    message.setCode(EventCode.CODE_SESSION_UPDATE);
                    RxBus.getDefault().post(message);
                }
            });
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void getAddressBookList(SmartRefreshLayout smartRefreshLayout) {
        int pageNum = page.getValue();
        page.setValue(pageNum + 1);
        addSubscribe(model.getAddressBookList(pageNum + 1, 10)
                .compose(SRxUtils.schedulersTransformer())
                .doOnSubscribe(disposable -> {
                    showDialog();
                })
                .subscribe(addressEntitySBaseResponse -> {
                    dismissDialog();
                    if (pageNum == 0) {
                        smartRefreshLayout.finishRefresh();
                    } else {
                        smartRefreshLayout.finishLoadMore();
                    }
                    if (addressEntitySBaseResponse.isOk()) {
                        if (pageNum == 0) {
                            observableList.clear();
                        }
                        List<AddressEntity> addressEntityList = addressEntitySBaseResponse.getData().getList();
                        for (AddressEntity addressEntity : addressEntityList) {
                            AddressItemViewModel itemViewModel = new AddressItemViewModel(this, addressEntity, hasCheckBox.get());
                            observableList.add(itemViewModel);
                        }
                    } else {

                    }
                }, throwable -> {
                    Log.e("Tag", "getAddressBookList error :" + throwable.getMessage());
                }));
    }

    public void initRefreshListener(SmartRefreshLayout smartRefreshLayout) {
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page.setValue(page.getValue() + 1);
            getAddressBookList(smartRefreshLayout);
        });
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            page.setValue(0);
            getAddressBookList(smartRefreshLayout);
        });
    }
}
