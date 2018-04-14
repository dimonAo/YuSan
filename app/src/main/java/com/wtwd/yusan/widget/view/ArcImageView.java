package com.wtwd.yusan.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.MimeTypeFilter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.wtwd.yusan.R;

/**
 * time:2018/4/14
 * Created by w77996
 */

public class ArcImageView extends View {
    private int mArcHeight;
    private int arcImageRes;
    private static final String TAG = "ArcImageView";

    private Bitmap mBitmap;

    public ArcImageView(Context context) {
        this(context, null);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcImageView);
        mArcHeight = typedArray.getDimensionPixelSize(R.styleable.ArcImageView_arcHeight, 0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, getHeight() - mArcHeight);
        path.quadTo(getWidth() / 2, getHeight(), getWidth(), getHeight() - mArcHeight);
        path.lineTo(getWidth(), 0);
        path.close();
        canvas.clipPath(path);


        RectF rectF = new RectF();
        rectF.top = 0;
        rectF.left = 0;
        rectF.right = getWidth();
        rectF.bottom = getHeight();


        Matrix matrix = new Matrix();
        float leng = (float) Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight());
//        float mHeightScale = getHeight()*1f / mBitmap.getHeight();
//        float mWidthScale = getWidth()*1f / mBitmap.getWidth();

        float a = leng * 1f / mBitmap.getHeight();
        matrix.postScale(a, a,mBitmap.getWidth()/2,mBitmap.getHeight()/2);

        Rect rect = new Rect();
        rect.left = (int) ((a - getWidth())/2);
        rect.right = (int) ((a + getWidth())/2);
        rect.top = (int) ((a - getHeight())/2);
        rect.bottom = (int) ((a + getHeight())/2);

        Log.e("TAG",rect.toString());
        Log.e("TAG",rectF.toString());

        int l = Math.abs(rect.left);
        int t = Math.abs(rect.top);

        Bitmap newbm = Bitmap.createBitmap(mBitmap,l,t, (int) (a-l), (int) (a-t), matrix, false);
        canvas.drawBitmap(newbm, rect, rectF, mPaint);

    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
        invalidate();
    }


}
