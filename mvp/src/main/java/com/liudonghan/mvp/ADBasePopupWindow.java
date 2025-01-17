package com.liudonghan.mvp;

import android.content.Context;
import android.os.Build;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:4/20/23
 */
public class ADBasePopupWindow<T> {

    private T t;
    private Context context;
    private ADBasePopupWindowLayout adBasePopupWindowLayout;
    private PopupWindow popupWindow;
    private View contentView;
    private int animationStyle;
    private boolean isTouchModal;
    private View anchorView;
    private int width, height;

    public ADBasePopupWindow(Context context, View anchorView) {
        this.context = context;
        this.popupWindow = new PopupWindow(context);
        this.anchorView = anchorView;
    }

    public ADBasePopupWindow<T> setContentView(int layoutResId) {
        contentView = View.inflate(context, layoutResId, null);
        return this;
    }

    /**
     * 设置layout布局
     *
     * @param adBasePopupWindowLayout view接口
     * @return ADBasePopupWindow
     */
    public ADBasePopupWindow<T> setContentView(ADBasePopupWindowLayout adBasePopupWindowLayout) {
        this.adBasePopupWindowLayout = adBasePopupWindowLayout;
        return this;
    }

    /**
     * 设置layout组件
     *
     * @param adBasePopupWindowLayoutId view接口
     * @return ADBasePopupWindow
     */
    public ADBasePopupWindow<T> findViewById(ADBasePopupWindowLayoutId<T> adBasePopupWindowLayoutId) {
        adBasePopupWindowLayoutId.findViewById(new BaseViewHolder(null != contentView ? contentView : adBasePopupWindowLayout.onCreateLayoutId()), t);
        return this;
    }

    /**
     * 设置弹窗动画
     *
     * @param animationStyle 动画style
     * @return ADBasePopupWindow<T>
     */
    public ADBasePopupWindow<T> setAnimationStyle(int animationStyle) {
        this.animationStyle = animationStyle;
        return this;
    }

    /**
     * 设置点击外部是否关闭
     *
     * @param isTouchModal 是否禁用
     * @return ADBasePopupWindow
     */
    public ADBasePopupWindow<T> setTouchModal(boolean isTouchModal) {
        this.isTouchModal = isTouchModal;
        return this;
    }

    /**
     * 设置数据源
     *
     * @param t t
     * @return ADBasePopupWindow
     */
    public ADBasePopupWindow<T> setData(T t) {
        this.t = t;
        return this;
    }

    /**
     * 设置宽高
     *
     * @param width  宽度
     * @param height 高度
     * @return ADBasePopupWindow
     */
    public ADBasePopupWindow<T> setWidthHeight(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 配置builder
     *
     * @return ADBasePopupWindow<T>
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public ADBasePopupWindow<T> builder() {
        popupWindow.setContentView(null != contentView ? contentView : adBasePopupWindowLayout.onCreateLayoutId());
        popupWindow.setAnimationStyle(animationStyle);
        popupWindow.setTouchModal(isTouchModal);
        return this;
    }

    public void show() {
        popupWindow.showAsDropDown(anchorView, 0, 0);
    }

    public void showAtLocation() {

        popupWindow.showAtLocation(anchorView, Gravity.CENTER, (int) anchorView.getX(), (int) anchorView.getY());
    }

    public interface ADBasePopupWindowLayout {
        /**
         * 创建View
         *
         * @return View
         */
        View onCreateLayoutId();
    }

    public interface ADBasePopupWindowLayoutId<T> {
        /**
         * 查找视图ID
         *
         * @param helper view引用
         */
        void findViewById(BaseViewHolder helper, T t);
    }

    public static class BaseViewHolder {

        private View view;
        /**
         * Views indexed with their IDs
         */
        private SparseArray<View> views;

        public BaseViewHolder(View view) {
            this.view = view;
            this.views = new SparseArray<>();
        }

        public BaseViewHolder setText(@IdRes int viewId, CharSequence value) {
            TextView view = getView(viewId);
            view.setText(value);
            return this;
        }

        public BaseViewHolder setText(@IdRes int viewId, @StringRes int strId) {
            TextView view = getView(viewId);
            view.setText(strId);
            return this;
        }

        public <T extends View> T getView(@IdRes int viewId) {
            View v = views.get(viewId);
            if (v == null) {
                v = view.findViewById(viewId);
                views.put(viewId, view);
            }
            return (T) v;
        }
    }
}
