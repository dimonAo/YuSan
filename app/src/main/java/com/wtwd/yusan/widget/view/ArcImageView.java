package com.wtwd.yusan.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.MimeTypeFilter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.wtwd.yusan.R;

import java.security.cert.CertificateNotYetValidException;

/**
 * time:2018/4/14
 * Created by w77996
 */

public class ArcImageView extends View {
    private int mArcHeight;
    private int arcImageRes;
    private static final String TAG = "ArcImageView";

    private Bitmap mBitmap;
    private Context mContext;
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
        this.mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, getHeight() - mArcHeight);
        path.quadTo(getWidth() / 2, getHeight(), getWidth(), getHeight() - mArcHeight);
        path.lineTo(getWidth(), 0);
        path.close();
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
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

        float scanB = leng * 1f / mBitmap.getHeight();
        float scanS = mBitmap.getHeight() * 1f / leng;
        matrix.postScale(scanB, scanB, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);

        Rect rect = new Rect();
//        rect.left = (int) ((a - getWidth())/2);
//        rect.right = (int) ((a + getWidth())/2);
//        rect.top = (int) ((a - getHeight())/2);
//        rect.bottom = (int) ((a + getHeight())/2);

        int cutX = (int) ((mBitmap.getWidth() - (getWidth() * scanS)) / 2);
        int cutY = (int) ((mBitmap.getHeight() - (getHeight() * scanS)) / 2);


        int l = rect.left;
        int t = rect.top;

//        Bitmap newbm = Bitmap.createBitmap(mBitmap, cutX, cutY, (int) (Math.floor(getWidth() * scanS)+10), (int) (Math.floor(getHeight() * scanS)), matrix, false);
        Bitmap newbm = Bitmap.createBitmap(mBitmap, cutX, cutY, (int) (Math.floor(getWidth() * scanS)), (int) (Math.floor(getHeight() * scanS)), matrix, false);
        Bitmap bitmap = blurBitmap(newbm,25);
      //  canvas.drawColor(Color.parseColor("#000000"));
        canvas.drawBitmap(bitmap, 0, 0, mPaint);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(getContext(),R.color.alpha_black_30));
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);

//        c

    }
    /**
     * 获取模糊的图片
     *
     * @param bitmap  传入的bitmap图片
     * @param radius  模糊度（Radius最大只能设置25.f）
     * @return
     */
    public  Bitmap blurBitmap(Bitmap bitmap, int radius) {
        //用需要创建高斯模糊bitmap创建一个空的bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
        RenderScript rs = RenderScript.create(mContext);
        // 创建高斯模糊对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方 法，并制定一个后备类型存储给定类型
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        //设定模糊度(注：Radius最大只能设置25.f)
        blurScript.setRadius(radius);
        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        // recycle the original bitmap
        // bitmap.recycle();
        // After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }



    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
        invalidate();
    }


}
