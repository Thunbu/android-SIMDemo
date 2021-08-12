package com.sammbo.imdemo.ui.chat.menu;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.geely.imsdk.client.bean.message.Message;
import com.geely.imsdk.client.bean.message.elem.file.SIMFileFormat;
import com.sammbo.imdemo.R;
import com.sammbo.imdemo.entity.MessageEntity;
import com.sammbo.imdemo.utils.Consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatItemMenu {
    public static final String TAG = "ChatItemMenu";

//    public static final int MENU_RESEND = 0;
//    public static final int MENU_COPY = 1;
//    public static final int MENU_FORWARD = 2;
//    public static final int MENU_REPLY = 3;
    public static final int MENU_DELETE = 0;
//    public static final int MENU_REVOKE = 5;
//    public static final int MENU_VOICE_HANDSET = 6;
//    public static final int MENU_VOICE_SPEAKER = 7;
//    public static final int MENU_MULTI = 8;
//    public static final int MENU_SAVE = 9;
//    public static final int MENU_TRANSLATE = 10;
//    public static final int MENU_PREVIEW = 11;
//    public static final int MENU_COLLECT = 12;
//    public static final int MENU_TOPIC = 13;
    public static final int MENU_SIGN = 1;
    public static final int MENU_UNSIGN = 2;
//    public static final int MENU_TODO = 16;
//    public static final int MENU_ADD_EXPRESSION = 17;
//    public static final int MENU_ASR = 18;
//    public static final int MENU_ASR_CANCEL = 19;
//    public static final int MENU_ASR_COPY = 20;

    private static int[] ICON_SRC = {
            R.drawable.chat_menu_delete,
            R.drawable.chat_menu_sign,
//            R.drawable.chat_menu_resend,
//            R.drawable.chat_menu_copy,
//            R.drawable.chat_menu_forward,
//            R.drawable.chat_menu_reply,
//            R.drawable.chat_menu_revoke,
//            R.drawable.chat_menu_handset,
//            R.drawable.chat_menu_speaker,
//            R.drawable.chat_menu_multiple,
//            R.drawable.chat_menu_save,
//            R.drawable.chat_menu_translate,
//            R.drawable.chat_menu_copy, // todo
//            R.drawable.chat_menu_copy, // todo
//            R.drawable.chat_menu_copy, // todo
            R.drawable.chat_menu_unsign,
//            R.drawable.chat_menu_todo,
//            R.drawable.chat_menu_add_expression,
//            R.drawable.chat_menu_asr,
//            R.drawable.chat_menu_asr_cancel,
//            R.drawable.chat_menu_copy,
//            R.drawable.chat_menu_save_cloud
    };

    private static int[] NAME_SRC = {
            R.string.im_delete_msg,
            R.string.im_sign_msg,
            R.string.im_unsign_msg,
//            R.string.im_resend_msg,
//            R.string.im_copy_msg,
//            R.string.im_forward_msg,
//            R.string.im_reply_msg,
//            R.string.im_revoke_msg,
//            R.string.im_turn_off_speaker,
//            R.string.im_turn_on_speaker,
//            R.string.im_multi_choice,
//            R.string.im_save_msg,
//            R.string.im_translate_msg,
//            R.string.chat_file_preview,
//            R.string.im_collect_msg,
//            R.string.im_topic_msg,
//            R.string.im_todo_msg,
//            R.string.im_add_expression,
//            R.string.im_asr,
//            R.string.im_asr_cancel,
//            R.string.im_asr_copy_asr_text,
//            R.string.save_to_cloud
    };

    private static boolean isOneMenuShow; // 避免多点长按时出现几个menu
    private Config config;
    private PopupWindow menuPopup;
    private BaseQuickAdapter<Integer, BaseViewHolder> menuAdatper;

    public static ChatItemMenu createDefault(View area, View anchor, MessageEntity message, boolean hide, boolean isEventGroup, boolean isLeaveOffice, Consumer<Integer> click) {
        ChatItemMenu menu = new Builder()
                .displayArea(area)
                .anchor(anchor)
                .message(message)
                .hide(hide)
                .eventGroup(isEventGroup)
                .leaveOffice(isLeaveOffice)
                .click(click)
                .build();
        anchor.setOnLongClickListener(v -> {
            menu.show();
            return true;
        });
        return menu;
    }

    public ChatItemMenu(Config config) {
        this.config = config;
        if (!config.checkValid()) {
//            XLog.e(TAG, "config checkValid err");
            return;
        }
        Context context = config.anchorView.getContext();
        View menuRoot = LayoutInflater.from(context).inflate(R.layout.chatting_msg_menu, null);
        RecyclerView rvMenu = menuRoot.findViewById(R.id.rv_msg_menu);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
        rvMenu.setLayoutManager(layoutManager);
        menuAdatper = new BaseQuickAdapter<Integer, BaseViewHolder>(R.layout.item_msg_menu) {
            @Override
            protected void convert(BaseViewHolder helper, Integer item) {
                helper.setImageResource(R.id.menu_icon, ICON_SRC[item])
                        .setText(R.id.menu_name, NAME_SRC[item])
                        .addOnClickListener(R.id.menu_content);
            }
        };
        menuPopup = new PopupWindow(menuRoot, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        menuPopup.setFocusable(true);
        menuPopup.setTouchable(true);
        menuPopup.setOnDismissListener(() -> isOneMenuShow = false);
        menuAdatper.setOnItemChildClickListener((adapter, v, position) -> {
            config.click.accept(menuAdatper.getData().get(position));
            dismiss();
        });
        rvMenu.setAdapter(menuAdatper);

        config.anchorView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                config.clickPoint.set((int) event.getRawX(), (int) event.getRawY());
            }
            return false;
        });

    }

    public void dismiss() {
        if (menuPopup.isShowing()) {
            menuPopup.dismiss();
        }
    }

    public void show() {
        if (isOneMenuShow) return;
        List<Integer> menuList = getMenuList(config.message);
        menuAdatper.setNewData(menuList);
        if (config.hide || menuList.isEmpty()) {
            return;
        }
        int[] location = new int[2];
        config.anchorView.getLocationOnScreen(location);
        menuPopup.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); // 一定要先调用一下这个方法，不然下面获取的宽高不准确
        // 计算显示位置
        int x = (getScreenWidth(config.anchorView.getContext()) / 2) - location[0] - menuPopup.getContentView().getMeasuredWidth() / 2;
        int y = config.clickPoint.y - (location[1] + config.anchorView.getHeight()) - menuPopup.getContentView().getMeasuredHeight();
        int[] locationChatArea = new int[2];
        config.displayArea.getLocationOnScreen(locationChatArea);
        if (location[1] + config.anchorView.getHeight() + y < locationChatArea[1]) {
            y = config.clickPoint.y - (location[1] + config.anchorView.getHeight());
        }
        menuPopup.showAsDropDown(config.anchorView, x, y);
        isOneMenuShow = true;
    }

    public void show(Point point) {
        config.clickPoint = point;
        show();
    }

    private List<Integer> getMenuList(MessageEntity msg) {
        Integer[] receiveMenu;
        if(!msg.isSign()){

            receiveMenu = new Integer[]{MENU_DELETE,MENU_SIGN};
        }else {
            receiveMenu = new Integer[]{MENU_DELETE,MENU_UNSIGN};
        }
        List<Integer> menuList = new ArrayList<>(Arrays.asList(receiveMenu));
        if (config.excludeMenu != null && !config.excludeMenu.isEmpty()) {
            menuList.removeAll(config.excludeMenu);
        }
        return menuList;
    }


    private static class Config {
        private View displayArea;
        private View anchorView;
        private View shadeView;
        private Consumer<Integer> click;
        private boolean hide;
        private MessageEntity message;
        private boolean isEventGroup;
        // 离职
        private boolean isLeaveOffice;
        private Point clickPoint = new Point();
        private boolean isExpressionAdded; // 表情是否已经添加
        private List<Integer> excludeMenu;

        boolean checkValid() {
            if (anchorView == null) {
//                XLog.e(TAG, "anchorView is null");
                return false;
            }

            if (message == null) {
//                XLog.e(TAG, "message is null");
                return false;
            }

            return true;
        }
    }

    public static class Builder {
        private Config config = new Config();

        public Builder displayArea(View displayArea) {
            config.displayArea = displayArea;
            return this;
        }

        public Builder anchor(View anchorView) {
            config.anchorView = anchorView;
            return this;
        }

        public Builder shade(View shadeView) {
            config.shadeView = shadeView;
            return this;
        }

        public Builder click(Consumer<Integer> click) {
            config.click = click;
            return this;
        }

        public Builder message(MessageEntity message) {
            config.message = message;
            return this;
        }

        public Builder hide(boolean hide) {
            config.hide = hide;
            return this;
        }

        public Builder eventGroup(boolean isEventGroup) {
            config.isEventGroup = isEventGroup;
            return this;
        }

        public Builder leaveOffice(boolean isleaveOffice) {
            config.isLeaveOffice = isleaveOffice;
            return this;
        }

        public Builder expressionAdded(boolean isExpressionAdded) {
            config.isExpressionAdded = isExpressionAdded;
            return this;
        }

        public Builder excludeMenu(List<Integer> excludeMenu) {
            config.excludeMenu = excludeMenu;
            return this;
        }

        public ChatItemMenu build() {
            return new ChatItemMenu(config);
        }

    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        if (null != context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metric);
        }
        int width = metric.widthPixels;
        return width;
    }
}