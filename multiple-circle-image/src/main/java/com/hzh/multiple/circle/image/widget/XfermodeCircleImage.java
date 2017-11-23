package com.hzh.multiple.circle.image.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class XfermodeCircleImage extends ImageView {
    private int mRadius;
    private Paint mPaint;
    private Xfermode xfermode;
    private Bitmap mCircleBitmap;

    public XfermodeCircleImage(Context context) {
        super(context);
        init();
    }

    public XfermodeCircleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
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

        setMeasuredDimension(mRadius * 2, mRadius * 2);
    }

    //生成一个实心圆形Bitmap,这个Bitmap宽高要与当前的View的宽高相同
    private Bitmap getCircleBitmap() {
        if (mCircleBitmap == null) {
            mCircleBitmap = Bitmap.createBitmap(2 * mRadius, 2 * mRadius,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(mCircleBitmap);

            mPaint.reset();
            mPaint.setStyle(Style.FILL);
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        }
        return mCircleBitmap;
    }

    //将两张图片以XferMode（DST_IN）的方式组合到一张照片中
    private Bitmap combineBitmap(Drawable drawable, Bitmap maskBitmap) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        // 将drawable转bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //将图片自动放缩到View的宽高，即2倍的半径
        drawable.setBounds(0, 0, mRadius * 2, mRadius * 2);
        drawable.draw(canvas);

        // 先将XferMode设置好，然后将盖在上面的bitmap绘制出来
        mPaint.reset();
        mPaint.setXfermode(xfermode);
        canvas.drawBitmap(maskBitmap, 0, 0, mPaint);
        mPaint.setXfermode(null);

        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取设置的src图片
        Drawable drawable = getDrawable();
        //获取盖在src上面的实心圆形Bitmap
        Bitmap circleBitmap = getCircleBitmap();

        //两张图片以XferMode（DST_IN）的方式组合
        Bitmap bitmap = combineBitmap(drawable, circleBitmap);

        //将最终的bitmap画到画板上面
        canvas.drawBitmap(bitmap, 0, 0, mPaint);
    }
}