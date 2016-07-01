package com.ustclin.petchicken;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

public class MyGestureListener extends SimpleOnGestureListener {
	
	private static final String TAG = "MyGestureListener";
	private Context mContext;

	public MyGestureListener() {
		
	}
	public MyGestureListener(Context mContext ) {
		this.mContext= mContext;
	}
	/**
	 * 双击的第二下Touch down时触发 
	 * @param e
	 * @return
	 */
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		Log.i(TAG, "onDoubleTap : " + e.getAction());
		return super.onDoubleTap(e);
	}

	/**
	 * 双击的第二下 down和up都会触发，可用e.getAction()区分。
	 * @param e
	 * @return
	 */
	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		Log.i(TAG, "onDoubleTapEvent : " + e.getAction());
		return super.onDoubleTapEvent(e);
	}

	/**
	 * down时触发 
	 * @param e
	 * @return
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		Log.i(TAG, "onDown : " + e.getAction());
		return super.onDown(e);
	}

	/**
	 * Touch了滑动一点距离后，up时触发。
	 * @param e1
	 * @param e2
	 * @param velocityX
	 * @param velocityY
	 * @return
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.i(TAG, "onFling e1 : " + e1.getAction() + ", e2 : " + e2.getAction() + ", distanceX : " + velocityX + ", distanceY : " + velocityY);
		// 手势向下 down
        if ((e2.getRawY() - e1.getRawY()) > 200) {
        	System.out.println("手势向下 down");
        	
            return true;
        }
        // 手势向上 up
        if ((e1.getRawY() - e2.getRawY()) > 200) {
        	System.out.println("手势向shang up");
        	// 关闭软键盘
    		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    		// 得到InputMethodManager的实例
    		if (imm.isActive())
    		{
    			// 如果开启
    			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
    					InputMethodManager.HIDE_NOT_ALWAYS);
    			// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
    		}
            return true;
        }
		return super.onFling(e1, e2, velocityX, velocityY);
	}

	/**
	 * Touch了不移动一直 down时触发 
	 * @param e
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		Log.i(TAG, "onLongPress : " + e.getAction());
		super.onLongPress(e);
	}

	/**
	 * Touch了滑动时触发。 
	 * @param e1
	 * @param e2
	 * @param distanceX
	 * @param distanceY
	 * @return
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.i(TAG, "onScroll e1 : " + e1.getAction() + ", e2 : " + e2.getAction() + ", distanceX : " + distanceX + ", distanceY : " + distanceY);
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	/**
	 * Touch了还没有滑动时触发 
	 * @param e
	 */
	@Override
	public void onShowPress(MotionEvent e) {
		Log.i(TAG, "onShowPress : " + e.getAction());
		super.onShowPress(e);
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		Log.i(TAG, "onSingleTapConfirmed : " + e.getAction());
		return super.onSingleTapConfirmed(e);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.i(TAG, "onSingleTapUp : " + e.getAction());
		return super.onSingleTapUp(e);
	}
}
