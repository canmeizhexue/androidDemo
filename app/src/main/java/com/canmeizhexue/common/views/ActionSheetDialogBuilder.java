package com.canmeizhexue.common.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canmeizhexue.common.R;


/**
 * <p>仿ios弹出按钮选择器控件 1、提供像DialogBuilder一样新建控件的方法；</p>
 *
 * @author canmeizhexue
 * @version 1.0 (2015/10/19)
 */
public class ActionSheetDialogBuilder implements View.OnClickListener {
    private Dialog dialog;
    private Context context;
    /**
     * 默认三个按钮
     */
    private TextView textViewButton1, textViewButton2, textViewButton3;
    /**
     * 头部的提示内容
     */
    private TextView textViewTitle;
    /**
     * 常规3个按钮的对话框监听器
     */
    private DialogInterface.OnClickListener listener;
    private LinearLayout llBtnParent;

    /**
     * 第一个按钮对应的标识符
     */
    public static final int BUTTON1 = 0;
    /**
     * 第二个按钮对应的标识符
     */
    public static final int BUTTON2 = 1;
    /**
     * 第三个按钮对应的标识符
     */
    public static final int BUTTON3 = 2;

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public ActionSheetDialogBuilder(Context context) {
        this.context = context;
        initDialog();
        initViews();
    }

    /**
     * 初始化对话框
     */
    private void initDialog() {
        dialog = new Dialog(context, R.style.ActionSheet);
        Window w = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = w.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = -1000;
        layoutParams.gravity = Gravity.BOTTOM;
        dialog.onWindowAttributesChanged(layoutParams);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_actionsheet);
    }

    /**
     * 根据id获取对应的view
     *
     * @param resId 控件id
     * @param <V>   泛型
     * @return 对应的view
     */
    @SuppressWarnings("unchecked")
    public <V extends View> V getView(int resId) {
        return (V) dialog.findViewById(resId);
    }

    /**
     * 加载视图
     */
    private void initViews() {
        llBtnParent = getView(R.id.ll_btn_parents);
        textViewButton1 = getView(R.id.textView_button1);
        textViewButton2 = getView(R.id.textView_button2);
        textViewButton3 = getView(R.id.textView_button3);
        textViewTitle = getView(R.id.textView_title);
    }

    /**
     * 设置标题
     *
     * @param titleMessage 标题信息
     * @return ActionSheetDialogBuilder
     */
    public ActionSheetDialogBuilder setTitleMessage(String titleMessage) {
        textViewTitle.setText(titleMessage);
        return this;
    }

    /**
     * 设置标题
     *
     * @param titleMessage 标题信息
     * @return ActionSheetDialogBuilder
     */
    public ActionSheetDialogBuilder setTitleMessage(int titleMessage) {
        return setTitleMessage(getString(titleMessage));
    }

    /**
     * 是否隐藏标题消息栏
     *
     * @param visible 是否可见
     */
    public void setTitleVisibility(boolean visible) {
        textViewTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (visible) {
            textViewButton1.setBackgroundResource(R.drawable.actionsheet_middle_selector);
        } else {
            textViewButton1.setBackgroundResource(R.drawable.actionsheet_middle_selector);
        }

    }

    /**
     * 设置按钮
     *
     * @param buttonStr1 第一个button的Text
     * @param buttonStr2 第二个button的Text
     * @param buttonStr3 第三个button的Text
     * @param listener   点击事件的回调
     * @return ActionSheetDialogBuilder
     */
    public ActionSheetDialogBuilder setButtons(String buttonStr1, String buttonStr2, String buttonStr3,
                                               DialogInterface.OnClickListener listener) {
        this.listener = listener;

        if (buttonStr1 != null) {
            textViewButton1.setText(buttonStr1);
            textViewButton1.setOnClickListener(this);
        } else {
            textViewButton1.setVisibility(View.GONE);
        }

        if (buttonStr2 != null) {
            textViewButton2.setText(buttonStr2);
            textViewButton2.setOnClickListener(this);
        } else {
            textViewButton2.setVisibility(View.GONE);
        }

        if (buttonStr3 != null) {
            textViewButton3.setText(buttonStr3);
            textViewButton3.setOnClickListener(this);
        } else {
            textViewButton3.setVisibility(View.GONE);
        }

        return this;
    }

    /**
     * 设置按钮
     *
     * @param buttonResId1 第一个button的id
     * @param buttonResId2 第二个button的id
     * @param buttonResId3 第三个button的id
     * @param listener     点击事件的回调
     * @return ActionSheetDialogBuilder
     */
    public ActionSheetDialogBuilder setButtons(int buttonResId1,
                                               int buttonResId2,
                                               int buttonResId3,
                                               DialogInterface.OnClickListener listener) {

        return setButtons(getString(buttonResId1),
                getString(buttonResId2),
                getString(buttonResId3),
                listener);
    }

    /**
     * 监听对话的取消操作
     *
     * @param listener 取消操作监听
     * @return ActionSheetDialogBuilder
     */
    public ActionSheetDialogBuilder setOnDismissListener(DialogInterface.OnCancelListener listener) {
        if (dialog == null || listener == null) {
            return this;
        }
        dialog.setOnCancelListener(listener);
        return this;
    }

    @Override
    public void onClick(View view) {

        if (listener == null) {
            return;
        }

        int resId = view.getId();
        if (resId == R.id.textView_button1) {
            listener.onClick(dialog, BUTTON1);

        } else if (resId == R.id.textView_button2) {
            listener.onClick(dialog, BUTTON2);

        } else if (resId == R.id.textView_button3) {
            listener.onClick(dialog, BUTTON3);
        }

        dialog.dismiss();
    }

    /**
     * 创建窗口
     *
     * @return
     */
    public Dialog create() {
        return dialog;
    }

    /**
     * 获取资源字符串
     *
     * @param resId 资源id
     * @return 资源对应的字符
     */
    private String getString(int resId) {
        return context.getResources().getString(resId);
    }


    /************************************** 以下是多个按钮的封装逻辑 ***************************************************/


    /**
     * 获取指定的button (必须调用在setButtons 方法之后)
     *
     * @param position 按钮的位置
     * @return V
     */
    @SuppressWarnings("unchecked")
    private <V extends View> V getButtonTextView(int position) {
        LinearLayout linearLayout = (LinearLayout) llBtnParent.getChildAt(position);
        return (V) linearLayout.getChildAt(0);
    }

    /**
     * 定义drawable 位置的枚举类
     */
    public enum DrawablePosition {
        /**
         * 左，右，上。下
         */
        LEFT, RIGHT, TOP, BOTTOM,
        /**
         * 左边和右边
         */
        LEFT_AND_RIGHT,
        /**
         * 左边和顶部
         */
        LEFT_AND_TOP,
        /**
         * 左边和下面
         */
        LEFT_AND_BOTTOM,
        /**
         * 右边和顶部
         */
        RIGHT_AND_TOP,
        /**
         * 右边和下面
         */
        RIGHT_AND_BOTTOM,
    }

    /**
     * 设置按钮的drawable
     *
     * @param position         按钮的位置
     * @param resId            drawable id
     * @param drawablePadding  drawable间距
     * @param drawablePosition drawable的位置
     * @see DrawablePosition
     */
    private void setButtonDrawable(int position, int resId, int drawablePadding, DrawablePosition drawablePosition) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        TextView textView = getButtonTextView(position);
        textView.setCompoundDrawablePadding(drawablePadding);
        switch (drawablePosition) {
            case LEFT:
                textView.setCompoundDrawables(drawable, null, null, null);
                break;

            case RIGHT:
                textView.setCompoundDrawables(null, null, drawable, null);
                break;

            case TOP:
                textView.setCompoundDrawables(null, drawable, null, null);
                break;

            case BOTTOM:
                textView.setCompoundDrawables(null, null, null, drawable);
                break;
            default:
                textView.setCompoundDrawables(null, null, null, null);
                break;
        }

    }

    /**
     * 设置按钮的左边图标
     *
     * @param position        按钮的位置
     * @param resId           drawable id
     * @param drawablePadding drawable间距
     */
    public void setButtonDrawableLeft(int position, int resId, int drawablePadding) {
        setButtonDrawable(position, resId, drawablePadding, DrawablePosition.LEFT);
    }

    /**
     * 设置按钮的右边图标
     *
     * @param position        按钮的位置
     * @param resId           drawable id
     * @param drawablePadding drawable间距
     */
    public void setButtonDrawableRight(int position, int resId, int drawablePadding) {
        setButtonDrawable(position, resId, drawablePadding, DrawablePosition.RIGHT);
    }

    /**
     * 设置指定按钮的背景
     *
     * @param position 按钮的位置
     * @param resId    背景 id
     */
    public void setButtonBackground(int position, int resId) {
        LinearLayout linearLayout = (LinearLayout) llBtnParent.getChildAt(position);
        if (linearLayout == null) {
            return;
        }

        linearLayout.setBackgroundResource(resId);
    }


    /**
     * 当按钮超过3个时，可用该方法来创建dialog
     *
     * @param btnTexts 按钮文本资源id 数组
     * @param listener 按钮点击的监听器
     * @return ActionSheetDialogBuilder
     */
    public ActionSheetDialogBuilder setButtons(int[] btnTexts, ActionSheetDialogOnClickListener listener) {
        if (btnTexts == null) {
            return this;
        }

        int l = btnTexts.length;
        String[] btnStrs = new String[l];
        for (int i = 0; i < l; i++) {
            String s = getString(btnTexts[i]);
            btnStrs[i] = s;
        }
        return setButtons(btnStrs, listener);
    }

    /**
     * 当按钮超过3个时，可用该方法来创建dialog
     *
     * @param btnTexts               按钮文本数组
     * @param zeroIndexIndicateTitle 表明btnTexts第一个元素是否作为标题用
     * @param listener               按钮点击的监听器
     * @return ActionSheetDialogBuilder
     */
    public ActionSheetDialogBuilder setButtons(String[] btnTexts, boolean zeroIndexIndicateTitle, final
    ActionSheetDialogOnClickListener listener) {
        int btnSize = btnTexts.length;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if (llBtnParent == null) {
            return this;
        }

        llBtnParent.removeAllViews();

        for (int i = 0; i < btnSize; i++) {
            View view;
            TextView textView;
            if (i == 0 && zeroIndexIndicateTitle) {
                // 标题
                view = layoutInflater.inflate(R.layout.item_dialog_actionsheet_title, llBtnParent, false);
                textView = (TextView) view.findViewById(R.id.textView_title);
                llBtnParent.addView(view);

            } else if (i == btnSize - 1) {
                // 取消按钮
                view = layoutInflater.inflate(R.layout.item_dialog_actionsheet_cancel_button, llBtnParent, false);
                textView = (TextView) view.findViewById(R.id.textView_button3);
                llBtnParent.addView(view);

            } else if (i == btnSize - 2) {
                // 底部按钮
                view = layoutInflater.inflate(R.layout.item_dialog_actionsheet_bottom_button, llBtnParent, false);
                textView = (TextView) view.findViewById(R.id.textView_button2);
                llBtnParent.addView(view);
            } else {
                // 中间按钮
                view = layoutInflater.inflate(R.layout.item_dialog_actionsheet_middle_button, llBtnParent, false);
                textView = (TextView) view.findViewById(R.id.textView_button1);
                llBtnParent.addView(view);
            }

            view.setTag(i);
            if (textView == null) {
                continue;
            }

            textView.setText(btnTexts[i]);

            if (listener == null) {
                continue;
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(dialog, (Integer) v.getTag());
                    dialog.dismiss();
                }
            });

        }

        return this;
    }

    /**
     * 当按钮超过3个时，可用该方法来创建dialog
     *
     * @param btnTexts 按钮文本数组
     * @param listener 按钮点击的监听器
     * @return ActionSheetDialogBuilder
     */
    public ActionSheetDialogBuilder setButtons(String[] btnTexts, final ActionSheetDialogOnClickListener listener) {
        return setButtons(btnTexts, true, listener);

    }

    /**
     * 按钮点击回调
     */
    public interface ActionSheetDialogOnClickListener {
        /**
         * 监听对话框的点击
         *
         * @param dialog      当前dialog
         * @param btnPosition button 的位置
         */
        void onClick(Dialog dialog, int btnPosition);
    }
}
