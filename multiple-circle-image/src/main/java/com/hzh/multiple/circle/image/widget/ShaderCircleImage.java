package com.hzh.multiple.circle.image.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ShaderCircleImage extends ImageView {

    private int mRadius;
    private Paint mPaint;

    public ShaderCircleImage(Context context) {
        super(context);
        init();
    }

    public ShaderCircleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height) {
            mRadius = height / 2;
        } else {
            mRadius = width / 2;
        }

        setMeasuredDimension(mRadius * 2, 2 * mRadius);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, mRadius * 2, mRadius * 2);
            drawable.draw(canvas);
            return bitmap;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 将Drawable转为Bitmap
        Bitmap bmp = drawableToBitmap(getDrawable());
        // 通过Bitmap和指定x,y方向的平铺方式构造出BitmapShader对象
        BitmapShader mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
        // 将BitmapShader设置到当前的Paint对象中
        mPaint.setShader(mBitmapShader);
        // 绘制出一个圆
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
    }
}