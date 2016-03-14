package com.benli.common.view;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benli.common.R;


/**
 * Created by comeyi on 2015/7/17.
 */
public class TitleBuilder {

    /**
     * title栏根布局
     */
    private View titleView;
    private ImageView ivLeft;
    private TextView tvLeft;
    private TextView tvMiddle;
    private ImageView tvRight;
    private Activity _activity;
    private StatusBarLayout llStatusBar;
    private RelativeLayout rlLayout;

    /**
     * 构造函数
     * @param activity
     */
    public TitleBuilder(Activity activity) {
        this._activity = activity;
        titleView = _activity.findViewById(R.id.title_bar);
        instanceObjects(titleView);
    }

    /**
     * 构造函数
     * @param
     */
    public TitleBuilder(View view) {
        View titleView = view.findViewById(R.id.title_bar);
        instanceObjects(titleView);
    }

    /**
     * 实例化对象
     */
    private void instanceObjects(View titleView){
        llStatusBar = (StatusBarLayout) titleView.findViewById(R.id.title_statusBar);
        rlLayout = (RelativeLayout) titleView.findViewById(R.id.title_rlContainer);
        ivLeft = (ImageView) titleView.findViewById(R.id.title_left);
        tvLeft = (TextView) titleView.findViewById(R.id.title_left_textview);
        tvMiddle = (TextView) titleView.findViewById(R.id.title_middle_textview);
        tvRight = (ImageView) titleView.findViewById(R.id.title_right);
    }

    /**
     * 设置文本标题
     * @param text
     * @return
     */
    public TitleBuilder setMiddleTitleText(String text) {
        tvMiddle.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        tvMiddle.setText(text);
        return this;
    }

    /**
     * 设置左侧图标
     * @param resId
     * @return
     */
    public TitleBuilder setLeftIconFont(int resId) {
        ivLeft.setVisibility((resId <= 0) ? View.GONE : View.VISIBLE);
        ivLeft.setImageResource(resId);
        return this;
    }

    /**
     * 设置右侧图标
     * @param resId
     * @return
     */
    public TitleBuilder setRightIconFont(int resId) {
        tvRight.setVisibility((resId <= 0) ? View.GONE : View.VISIBLE);
        tvRight.setImageResource(resId);
        return this;
    }

    /**
     * 设置左侧图标点击处理函数
     */
    public TitleBuilder setLeftIconListener(View.OnClickListener listener) {
        if(ivLeft.getVisibility() == View.VISIBLE){
            ivLeft.setOnClickListener(listener);
        }

        if (tvLeft.getVisibility() == View.VISIBLE){
            tvLeft.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置右侧图标点击处理函数
     */
    public TitleBuilder setRightIconListener(View.OnClickListener listener) {
        if (listener != null && tvRight.getVisibility() == View.VISIBLE) {
            tvRight.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置右侧图标是否显示
     */
    public TitleBuilder setRightIconVisibility(int visibility){
        tvRight.setVisibility(visibility);
        return this;
    }

    /**
     * 设置标题栏（可定制标题栏事件处理类）
     */
    public TitleBuilder setTitlebar(int leftIcon, View.OnClickListener leftCilckListener, String titleName,
                             int rightIcon, View.OnClickListener rightClickListener) {
        if(leftCilckListener == null){
            leftCilckListener = new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    _activity.finish();
                }
            };
        }

        return this.setLeftIconFont(leftIcon)
                .setLeftIconListener(leftCilckListener)
                .setMiddleTitleText(titleName)
                .setRightIconFont(rightIcon)
                .setRightIconListener(rightClickListener);
    }

    /**
     * 设置标题栏的外观
     * @param width
     * @param height
     * @param backgroundColor
     * @return
     */
    public TitleBuilder setTitlebar(int width, int height, int backgroundColor){
        rlLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        llStatusBar.setBackgroundResource(backgroundColor);
        rlLayout.setBackgroundResource(backgroundColor);
        return this;
    }

    /**
     * 设置标题栏
     */
    public TitleBuilder setTitlebar(String titleName) {
        return setTitlebar(R.drawable.titlebar_back, null, titleName, 0, null);
    }

    /**
     * 设置标题栏透明
     * @return
     */
    public TitleBuilder isBackgroundTransparent(){
        return setBackgroundColor(android.R.color.transparent);

    }

    /**
     * 设置标题栏的背景
     * @param color
     * @return
     */
    public TitleBuilder setBackgroundColor(int color){
        llStatusBar.setBackgroundColor(color);
        rlLayout.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置标题栏背景
     * @param resid
     * @return
     */
    public TitleBuilder setBackgroundResource(int resid){
        llStatusBar.setBackgroundResource(resid);
        rlLayout.setBackgroundResource(resid);
        return this;
    }

    /**
     * 设置左侧文字
     * @param text
     * @return
     */
    public TitleBuilder setLeftText(String text){
        if (TextUtils.isEmpty(text))
            tvLeft.setVisibility(View.INVISIBLE);
        else {
            tvLeft.setVisibility(View.VISIBLE);
            tvLeft.setText(text);
        }
        return this;
    }
}
