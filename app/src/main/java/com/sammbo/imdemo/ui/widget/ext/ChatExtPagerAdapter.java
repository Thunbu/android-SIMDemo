/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.sammbo.imdemo.ui.widget.ext;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ChatExtPagerAdapter extends PagerAdapter {
    private SparseArray<ChatExtPageView> pagers = new SparseArray<>();
    private List<ChatExt> exts;
    private ChatExtPageView.OnExtViewClickListener listener;

    public ChatExtPagerAdapter(List<ChatExt> exts, ChatExtPageView.OnExtViewClickListener listener) {
        this.exts = exts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ChatExtPageView view = pagers.get(position);
        if (view == null) {
            view = new ChatExtPageView(container.getContext());
            view.setPageIndex(position);
            view.setOnExtViewClickListener(listener);
            int startIndex = ChatExtPageView.EXT_PER_PAGE * position;
            int end = startIndex + ChatExtPageView.EXT_PER_PAGE > exts.size() ? exts.size() : startIndex + ChatExtPageView.EXT_PER_PAGE;
            view.updateExtViews(exts.subList(startIndex, end));

            container.addView(view);
            pagers.put(position, view);
        }
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return exts == null ? 0 : (exts.size() + 7) / 8;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
