package com.cluster.local.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cluster.local.R;
import com.cluster.local.Utils;


/**
 * TODO: document your custom view class.
 */
public class Compass extends View {

    private Paint mTextPaint;
    private float halfHeight;
    private float halfWidth;

    private Bitmap triangle;
    private Matrix rotationMatrix;

    private Rect textContainer;
    private float radius;

    private float angle = 0;
    private OnCompassClickListener listener;



    public Compass(Context context) {
        super(context);
        init();
    }

    public Compass(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Compass(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        triangle = Utils.drawableToBitmap(getContext(), R.drawable.ic_arrow_drop_up_black_24dp);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(halfWidth, halfHeight);

        if (mTextPaint == null) {
            mTextPaint = new TextPaint();
            mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTextSize(canvas.getHeight() * 0.7f);
            mTextPaint.setColor(Color.parseColor("#4d526c"));
            textContainer = new Rect();
            mTextPaint.getTextBounds("N", 0, 1, textContainer);
            halfHeight = canvas.getHeight() / 2;
            halfWidth = canvas.getWidth() / 2;
            rotationMatrix = new Matrix();
            radius = (float) Math.sqrt(Math.pow(textContainer.width() / 2, 2) + Math.pow(textContainer.height() / 2, 2));
        }



        canvas.drawText("N", 0, textContainer.height() / 2, mTextPaint);
        rotationMatrix.reset();
        rotationMatrix.preTranslate(-triangle.getWidth() / 2, -radius - triangle.getHeight() * 0.55f);
        rotationMatrix.postRotate(-angle, 0, 0);


        canvas.drawBitmap(triangle, rotationMatrix, null);
        //Debug

        //  canvas.drawCircle(0,0,radius,testPaintStroke);

        //   canvas.drawRect(-halfWidth, -halfHeight, halfWidth, halfHeight, testPaintStroke);


        invalidate();
    }

    private float[] rotateMatrix(float[] vector2F, float[] matrix) {
        vector2F[0] = matrix[0] * vector2F[0] + matrix[1] * vector2F[1];
        vector2F[1] = matrix[2] * vector2F[0] + matrix[3] * vector2F[1];
        return vector2F;
    }

    private float[] multiplyMM2x2(float[] matrix1, float[] matrix2) {
        float[] result = new float[4];
        result[0] = matrix1[0] * matrix2[0] + matrix1[1] * matrix2[2];
        result[1] = matrix1[0] * matrix2[1] + matrix1[1] * matrix2[3];
        result[2] = matrix1[2] * matrix2[0] + matrix1[3] * matrix2[2];
        result[3] = matrix1[2] * matrix2[1] + matrix1[3] * matrix2[3];
        return result;
    }

    public void update(float angleDeg) {
        this.angle = angleDeg;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (listener != null) {
                listener.onClick();
            return true;
            }

        }
        return super.onTouchEvent(event);
    }

    public void setOnClickListener(OnCompassClickListener listener) {
        this.listener = listener;
    }

    public interface OnCompassClickListener {

        void onClick();
    }
}
