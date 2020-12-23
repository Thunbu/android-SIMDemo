package com.sammbo.imdemo.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.sammbo.imdemo.R;

public class CountView extends AppCompatTextView {
    private boolean small = false;

    public void setSmall(boolean small){
       this.small = small;
    }

    public CountView(Context context) {
        super(context);
    }

    public CountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCount(int count) {
        if (count == 0) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            if (count < 10) {
                if(small){
                    setBackgroundResource(R.drawable.small_shape_one_num);
                }else {
                    setBackgroundResource(R.drawable.shape_one_num);
                }
            } else if (count > 99) {
                if(small){
                    setBackgroundResource(R.drawable.small_shape_three_num);
                }else {
                    setBackgroundResource(R.drawable.shape_three_num);
                }
            } else {
                if(small){
                    setBackgroundResource(R.drawable.small_shape_two_num);
                }else {
                    setBackgroundResource(R.drawable.shape_two_num);
                }
            }
            setText(getCountText(count));
        }
        setStyle();
    }


    public void setStyle(){
        this.setGravity(Gravity.CENTER);
        this.setTextColor(Color.WHITE);
        if(small){
            this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,10);
        }else {
            this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        }
    }

    public static String getCountText(int count){
        if(count > 99){
            return String.valueOf("99+");
        }else {
            return String.valueOf(count);
        }
    }
}
