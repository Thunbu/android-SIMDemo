package com.sammbo.imdemo.ui.widget;

import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sammbo.imdemo.utils.TimeUtil;

import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class ViewAdapter {

    /**
     * view的焦点发生变化的事件绑定
     */
    @BindingAdapter({"onFocusChangeCommand"})
    public static void onFocusChangeCommand(View view, final BindingCommand<Boolean> onFocusChangeCommand) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusChangeCommand != null) {
                    onFocusChangeCommand.execute(hasFocus);
                }
            }
        });
    }

    @BindingAdapter("duration")
    public static void setTime(TextView textView, long timeMillis) {
        textView.setText(TimeUtil.getTime(timeMillis));
    }

    @BindingAdapter("sessionTime")
    public static void setSessionTime(TextView textView, long timeMillis) {
        textView.setText(TimeUtil.formatConversationTime(textView.getContext(), timeMillis));
    }

    @BindingAdapter("refereshFinished")
    public static void onRefresh(RecyclerView recyclerView, final BindingCommand command){
        if (command != null) {

        }
    }
}
