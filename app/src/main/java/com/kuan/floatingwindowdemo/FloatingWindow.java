package com.kuan.floatingwindowdemo;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;

/**
 * Created by Kuan on 2017/4/9.
 */

public class FloatingWindow {


    private WindowManager.LayoutParams mLayoutParams;

    private WindowManager mWindowManager;

    private  View mWindowsView;

    private  Context mContext;
    public FloatingWindow(Context context){

        mContext = context;
    }

    public void show(){

        if (mWindowManager == null) {
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

            mLayoutParams = new WindowManager.LayoutParams();

            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
            mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            mLayoutParams.x = 100;
            mLayoutParams.y = 200;


            mWindowsView = new FloatingView(mContext);

            mWindowManager.addView(mWindowsView, mLayoutParams);
        }
    }

    public void remove(){
        mWindowManager.removeView(mWindowsView);
        mWindowManager = null;
    }


    class FloatingView extends RelativeLayout{


        private ImageView mCloesd;

        /**
         * 相对于 View的坐标 x
         */
        private float mInnerX;

        /**
         * 相对于 View的坐标 y
         */
        private float mInnerY;

        /**
         * 屏幕坐标系 x
         */
        private float mOutX;
        /**
         * 相对屏幕的坐标系 y
         */
        private float mOutY;

        /**
         * 状态栏的高度
         */
        private int mStatusBarHeight;


        public FloatingView(Context context) {
            super(context);
            init();
        }

        private void init(){

            LayoutInflater.from(getContext()).inflate(R.layout.floating_view,this,true);
            mCloesd = (ImageView)findViewById(R.id.iv_cloesd);
            mCloesd.setOnClickListener(mOnClickListener);

        }

        private OnClickListener mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        };

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mOutX = event.getRawX();
                    mOutY = event.getRawY();
                    mInnerX = event.getX();
                    mInnerY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mOutX = event.getRawX();
                    mOutY  = event.getRawY();
                    updateViewPosition();
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }

            return true;
        }

        private void updateViewPosition(){
            float positionX = mOutX - mInnerX;
            float positionY = mOutY - mInnerY - getmStatusBarHeight();
            mLayoutParams.x = (int) positionX;
            mLayoutParams.y = (int)positionY;
            mWindowManager.updateViewLayout(mWindowsView,mLayoutParams);

        }

        private int getmStatusBarHeight() {
            if (mStatusBarHeight == 0) {
                try {
                    Class<?> c = Class.forName("com.android.internal.R$dimen");
                    Object o = c.newInstance();
                    Field field = c.getField("status_bar_height");
                    int x = (Integer) field.get(o);
                    mStatusBarHeight = getResources().getDimensionPixelSize(x);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return mStatusBarHeight;
        }
    }


}
