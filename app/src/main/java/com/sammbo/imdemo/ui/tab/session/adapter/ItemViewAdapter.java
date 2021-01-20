package com.sammbo.imdemo.ui.tab.session.adapter;

import android.view.View;

import androidx.databinding.BindingAdapter;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

/**
 * author : wangqiang
 * e-mail : qiang.wang12@geely.com
 * time   : 2021/01/20
 * desc   :
 * version:
 */
public class ItemViewAdapter {
    /**
     * view的onLongClick事件绑定
     */
    @BindingAdapter(value = {"onLongClickCommand"}, requireAll = false)
    public static void onLongClickCommand(View view, final BindingCommand clickCommand) {
        RxView.longClicks(view)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        if (clickCommand != null) {
                            clickCommand.execute(view);
                        }
                    }
                });
    }
}
