package com.sammbo.imdemo.ui.widget;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sammbo.imdemo.R;
import com.sammbo.imdemo.entity.SessionEntity;

public class ConversationMenu {
    private PopupWindow mPopupWindow;
    private View vAnchor;
    private TextView vTopText;
    private TextView vDisturbText;
    private View vDel;
    private View menu;

    public ConversationMenu(View anchor) {
        vAnchor = anchor;
        Context context = anchor.getContext();
        menu = LayoutInflater.from(context).inflate(R.layout.item_conversation_menu, null);
        vDel = menu.findViewById(R.id.conversation_del);
        vTopText = menu.findViewById(R.id.top_text);
        vDisturbText = menu.findViewById(R.id.conversation_disturb);
        mPopupWindow = new PopupWindow(menu, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
    }

    public void show(SessionEntity entity, View.OnClickListener top, View.OnClickListener del, View.OnClickListener disturbclick) {
        // 一定要先调用一下这个方法，不然下面获取的宽高不准确
        mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        boolean isTop = entity.getTop() == SessionEntity.TOP_Y;
        if (isTop) {
            vTopText.setText("取消置顶");
        } else {
            vTopText.setText("置顶");
        }
        boolean disturb = entity.getNotDisturb() == 1;
        if (disturb) {
            vDisturbText.setText("取消免打扰");
        } else {
            vDisturbText.setText("免打扰");
        }
        vTopText.setOnClickListener(v -> {
            top.onClick(mPopupWindow.getContentView());
            mPopupWindow.dismiss();
        });
        vDel.setOnClickListener(v -> {
            del.onClick(mPopupWindow.getContentView());
            mPopupWindow.dismiss();
        });
        vDisturbText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disturbclick.onClick(mPopupWindow.getContentView());
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAsDropDown(vAnchor, vAnchor.getMeasuredWidth() / 2, -vAnchor.getHeight() / 3);
    }

}
