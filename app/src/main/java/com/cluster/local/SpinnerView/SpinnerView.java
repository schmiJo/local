package com.cluster.local.SpinnerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.cluster.local.Utils;


public class SpinnerView extends View {

    public static final byte EXTEND_CLOSED = 0;
    public static final byte EXTEND_CLOSING = 1;
    public static final byte EXTEND_OPEN = 2;
    public static final byte EXTEND_OPENING = 3;
    public static final byte SCROLL_IDLE = 0;
    public static final byte SCROLL_DRAGING = 1;
    public static final byte SCROLL_SETTELING = 2;
    private OnOpenListener onOpenListener;
    private byte extendState = EXTEND_CLOSED;
    private byte scrollState = SCROLL_SETTELING;

    private int[] icons;
    private short wheelX = 0;
    private short wheelY = 0;
    private short wheelRadius = 0;
    private long downStamp;
    private Paint wheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint wheelStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int openFactor;
    private int wheelRadiusAdd = 0;
    private float degrees2;
    private float degrees;
    private float degreesShown = 0;
    private float centerX;
    private float height;
    private float downDegrees;
    private float dp16;
    private float centerY;
    private float velocity = 0;
    private boolean closeIt = false;
    private boolean clickOutside = false;
    private VelocityTracker velocityTracker;

    public SpinnerView(Context context) {
        super(context);
        init();
    }

    public SpinnerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpinnerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void init() {
        onOpenListener = (OnOpenListener) getContext();
        wheelPaint.setColor(Color.parseColor("#C1CCCC"));
        wheelPaint.setStyle(Paint.Style.FILL);
        wheelStrokePaint.setColor(Color.parseColor("#4d526c"));
        wheelStrokePaint.setStrokeWidth(Utils.dpToPixel(3, getContext()));
        wheelStrokePaint.setStyle(Paint.Style.STROKE);
        dp16 = (Utils.dpToPixel(16, getContext()));
        icons = new int[]{1, 2, 3, 4, 6, 7};
        velocityTracker = VelocityTracker.obtain();
    }

    public byte getExtendState() {
        return extendState;
    }


    private void open() {
        if (extendState != EXTEND_CLOSED) return;
        extendState = EXTEND_OPENING;
        onOpenListener.open();
        //  if (scrollState == SCROLL_IDLE)
        //      degreesShown = openSweepDegrees;
        //  else
        //      degreesShown = 0;
        invalidate();

    }


    public void close() {
        if (extendState != EXTEND_OPEN) return;
        extendState = EXTEND_CLOSING;
        onOpenListener.close();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (wheelY == 0) {
            wheelY = (short) (canvas.getHeight() - Utils.dpToPixel(32, getContext()));
            wheelX = (short) (((float) canvas.getWidth()) / 2);
            wheelRadius = (short) Utils.dpToPixel(20, getContext());
            openFactor = (int) ((float) canvas.getWidth() * 0.33f);
            centerX = canvas.getWidth() / 2;
            height = canvas.getHeight();
            centerY = canvas.getHeight() - Utils.dpToPixel(33, getContext());
        }

        canvas.drawCircle(wheelX, wheelY, wheelRadius + wheelRadiusAdd, wheelPaint);
        canvas.drawCircle(wheelX, wheelY, wheelRadius + wheelRadiusAdd, wheelStrokePaint);


        switch (scrollState) {

            case SCROLL_SETTELING:
                velocity = velocity * 0.98f;
                if (Math.abs(velocity) < 40) scrollState = SCROLL_IDLE;
                degrees = degrees + velocity / 600;
                invalidate();

                break;
            case SCROLL_DRAGING:


                break;

        }
        switch (extendState) {
            case EXTEND_CLOSED:

                wheelRadiusAdd = 0;
                break;
            case EXTEND_CLOSING: {
                double x = (((System.currentTimeMillis() - downStamp)) * 0.009);
                wheelRadiusAdd = (int) ((float) openFactor * closeAnimFunction(x));
                if (x > 3.51) {
                    wheelRadiusAdd = 0;
                    extendState = EXTEND_CLOSED;
                    break;
                }


                for (int i = 0; i < icons.length; i++) {
                    drawItem(i, canvas);
                }
                invalidate();
                break;
            }
            case EXTEND_OPEN:
                wheelRadiusAdd = openFactor;

                for (int i = 0; i < icons.length; i++) {
                    drawItem(i, canvas);
                }
                break;
            case EXTEND_OPENING:
                double x = (((System.currentTimeMillis() - downStamp)) * 0.01);

                wheelRadiusAdd = (int) ((float) openFactor * openAnimFunction(x));
                //   if(degreesShown != 0+)
                //   degreesShown = (float) (degrees - (0.04 * Math.pow((x - 0.2), 3) - 0.17 * Math.pow((x - 0.2), 2) - 0.14 * (x - 0.2) + 0.89) * openSweepDegrees);

                if (x > 3.63) {
                    wheelRadiusAdd = openFactor;
                    extendState = EXTEND_OPEN;
                    degreesShown = 0;

                }

                for (int i = 0; i < icons.length; i++) {
                    drawItem(i, canvas);
                }
                invalidate();
                break;
        }


        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                velocityTracker.clear();
                if (extendState == EXTEND_CLOSED) {
                    setDownStamp();
                } else if (extendState == EXTEND_OPEN) {
                    setDownStamp();
                    degrees2 = degrees;
                    downDegrees = getAngleAtCoordinates(event.getX(), event.getY());
                    if (Utils.inCircle(event.getX(), event.getY(), wheelX, wheelY, wheelRadius))
                        closeIt = true;
                }


                clickOutside = !(Utils.inCircle(event.getX(), event.getY(), wheelX, wheelY, wheelRadius + wheelRadiusAdd));

                if (!clickOutside) {
                    velocityTracker.addMovement(event);
                }
                return true;

            case MotionEvent.ACTION_UP:

                if (clickOutside) {
                    close();
                    return super.onTouchEvent(event);
                }
                if (scrollState != SCROLL_SETTELING) {
                    scrollState = SCROLL_IDLE;
                }

                velocityTracker.addMovement(event);
                switch (extendState) {
                    case EXTEND_CLOSED:
                        open();

                        break;
                    case EXTEND_OPEN:
                        if (closeIt) close();

                        break;

                }
                closeIt = false;


                break;
            case MotionEvent.ACTION_MOVE:
                int pointerId = event.getPointerId(event.getActionIndex());
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);

                // velocity of pixels per second
                float xVelocity = VelocityTrackerCompat.getXVelocity(velocityTracker, pointerId);
                float yVelocity = VelocityTrackerCompat.getYVelocity(velocityTracker, pointerId);

                if (Math.abs(xVelocity) + Math.abs(yVelocity) > 8000) {
                    scrollState = SCROLL_SETTELING;
                    if (event.getX() < centerX) {

                        velocity = (xVelocity - yVelocity) / 2;
                    } else {

                        velocity = (xVelocity + yVelocity) / 2;
                    }
                    invalidate();
                } else {
                    scrollState = SCROLL_DRAGING;
                    degrees = degrees2 + (downDegrees - getAngleAtCoordinates(event.getX(), event.getY()));
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setDownStamp() {
        downStamp = System.currentTimeMillis();
    }

    private double openAnimFunction(double x) {
        return (-0.07 * Math.pow(x, 3) + 0.33 * Math.pow(x, 2));
    }

    private double closeAnimFunction(double x) {
        return 0.13 * Math.pow(x, 2) - 0.74 * x + 1;
    }

    private void drawItem(int position, Canvas canvas) {
        double angle = ((2 * Math.PI) / (icons.length)) * position;
        angle = angle - Math.PI / 2;
        angle = angle + Math.toRadians(degrees - degreesShown);
        float factor = (wheelRadiusAdd - dp16 * 3);
        if (factor > dp16) {
            int y = (int) (centerY - (Math.sin(angle) * -factor));
            int x = (int) (centerX + (Math.cos(angle) * factor));
            if (y < height + 3 * dp16) canvas.drawCircle(x, y, 30, wheelStrokePaint);
        }
    }

    private float getAngleAtCoordinates(float x, float y) {
        float a = centerY - y;
        float b = x - centerX;
        return (float) Math.toDegrees(Math.atan(a / b));
    }

    public interface OnOpenListener {

        void close();

        void open();
    }


}
