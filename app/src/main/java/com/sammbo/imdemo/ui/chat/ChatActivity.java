package com.sammbo.imdemo.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.sammbo.imdemo.BR;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.app.AppViewModelFactory;
import com.sammbo.imdemo.databinding.ActivityChatBinding;
import com.sammbo.imdemo.ui.SBaseActivity;

public class ChatActivity extends SBaseActivity<ActivityChatBinding, ChatViewModel> {
    private static final String SESSION_ID = "sessionId";
    private static final String SESSION_NAME = "sessionName";

    public static Bundle startParams(String sessionId, String sessionName) {
        Bundle bundle = new Bundle();
        bundle.putString(SESSION_ID, sessionId);
        bundle.putString(SESSION_NAME, sessionName);
        return bundle;
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_chat;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ChatViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(ChatViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        viewModel.sessionId.set(bundle.getString(SESSION_ID));
        viewModel.sessionName.set(bundle.getString(SESSION_NAME));
        binding.setAdapter(new MsgBindingRecyclerViewAdapter());
        binding.inputPanel.init(this, viewModel, binding.rootLayout);
        binding.inputPanel.setupChat(bundle.getString(SESSION_ID));
        binding.msgRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                binding.inputPanel.closeChatInputPanel();
                return false;
            }
        });
    }

    @Override
    public void initViewObservable() {
//        viewModel.uc.finishRefreshing.observe(this, o -> {});
        viewModel.scrollToEnd.observe(this, exchangeEvent -> {
            if (exchangeEvent.getContentIfNotHandled() != null) {
                binding.msgRecyclerView.scrollToPosition(viewModel.observableList.size() - 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        binding.inputPanel.onActivityResult(requestCode,resultCode,data);
    }
}
