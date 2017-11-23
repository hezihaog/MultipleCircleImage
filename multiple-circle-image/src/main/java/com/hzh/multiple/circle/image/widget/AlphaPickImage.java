package com.hzh.multiple.circle.image.widget;

import android.widget.ImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Package: com.hzh.circle.image.util.widget
 * FileName: AlphaPickImage
 * Date: on 2017/11/23  下午6:18
 * Auther: zihe
 * Descirbe:
 * Email: hezihao@linghit.com
 */

public class AlphaPickImage extends ImageView {
    private int mRadius;
    private Paint mPaint;

    public AlphaPickImage(Context context) {
        super(context);
        init();
    }

    public AlphaPickImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlphaPickImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    //获取圆形Bitmap
    private Bitmap getCircleMask() {
        Bitmap bitmap = Bitmap.createBitmap(mRadius * 2, mRadius * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        return bitmap;
    }

    //将rgbBitmap的RGB值与alphaBitmap的alpha值组成新的Bitmap
    private Bitmap getBitmap(Bitmap rgbBitmap, Bitmap alphaBitmap) {
        Bitmap newBmp = Bitmap.createBitmap(mRadius * 2, mRadius * 2,
                Bitmap.Config.ARGB_8888);

        int alphaMask = 0xFF << 24;
        int rgbMask = ~alphaMask;
        for (int x = 0; x < 2 * mRadius; x++) {
            for (int y = 0; y < 2 * mRadius; y++) {
                int color = (rgbMask & rgbBitmap.getPixel(x, y))
                        | (alphaMask & alphaBitmap.getPixel(x, y));
                newBmp.setPixel(x, y, color);
            }

        }
        return newBmp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 将Drawable转为Bitmap
        Bitmap rgbBitmap = drawableToBitmap(getDrawable());
        //提取alpha值通道
        Bitmap alphaBitmap = getCircleMask().extractAlpha();
        //将最终图片绘制出来
        canvas.drawBitmap(getBitmap(rgbBitmap, alphaBitmap), 0, 0, mPaint);
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
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
            return bitmap;
        }
    }
}
