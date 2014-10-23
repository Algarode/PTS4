package com.fontys.edwin.fotoopproductdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by Edwin on 14-10-2014.
 */
public class MyImageView extends View {

    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.f;
    private static final int INVALID_POINTER_ID = -1;
    private float fPosX;
    private float fPosY;
    private float fLastTouchX;
    private float fLastTouchY;
    private int activePointerId = INVALID_POINTER_ID;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        scaleGestureDetector.onTouchEvent(event);

        final int action = event.getAction();

        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN: {

                final float x = event.getX();
                final float y = event.getY();

                fLastTouchX = x;
                fLastTouchY = y;

                activePointerId = event.getPointerId(0);

                break;

            }

            case MotionEvent.ACTION_MOVE: {

                final int pointerIndex = event.findPointerIndex(activePointerId);
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                if (!scaleGestureDetector.isInProgress()) {
                    final float dx = x - fLastTouchX;
                    final float dy = y - fLastTouchY;

                    fPosX += dx;
                    fPosY += dy;
                }

                fLastTouchX = x;
                fLastTouchY = y;

                break;

            }

            case MotionEvent.ACTION_UP: {

                activePointerId = INVALID_POINTER_ID;
                break;

            }

            case MotionEvent.ACTION_CANCEL: {

                activePointerId = INVALID_POINTER_ID;
                break;

            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);

                if (pointerId == activePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    fLastTouchX = MotionEventCompat.getX(event, newPointerIndex);
                    fLastTouchY = MotionEventCompat.getY(event, newPointerIndex);
                    activePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                }

                break;

            }

        }

        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.save();
        canvas.translate(fPosX, fPosY);
        canvas.scale(mScaleFactor, mScaleFactor);
        MainActivity.drawables[1].draw(canvas);
        canvas.restore();

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            mScaleFactor *= detector.getScaleFactor();

            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            return true;

        }

    }

}
