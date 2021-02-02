package com.sammbo.imdemo.ui.tab.address;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.sammbo.imdemo.ui.chat.ChatActivity;
import com.sammbo.imdemo.ui.tab.address.bean.AddressEntity;

import me.goldze.mvvmhabit.base.ItemViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * author : wangqiang
 * e-mail : qiang.wang12@geely.com
 * time   : 2021/01/26
 * desc   :
 * version:
 */
public class AddressItemViewModel extends ItemViewModel<AddressViewModel> {
    public ObservableField<AddressEntity> addressEntityObservableField = new ObservableField<>();
    public ObservableField<Boolean> showCheckBoxField = new ObservableField<>();
    public ObservableField<Boolean> isCheckedFiled = new ObservableField<>(false);


    public AddressItemViewModel(@NonNull AddressViewModel viewModel, AddressEntity entity, Boolean showCheckBox) {
        super(viewModel);
        addressEntityObservableField.set(entity);
        showCheckBoxField.set(showCheckBox);
    }

    public BindingCommand startChatClick = new BindingCommand(() ->
            viewModel.startActivity(ChatActivity.class, ChatActivity.startParams(addressEntityObservableField.get().getAccount(), addressEntityObservableField.get().getUserNickname()))
    );

}
