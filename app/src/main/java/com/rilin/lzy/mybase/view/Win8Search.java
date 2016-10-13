package com.rilin.lzy.mybase.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lzy on 16/9/20.
 * 模仿win8加载的进度条
 * 好像只能在android版本4.4以上才有效果,以下要关闭硬件加速(具体我也不是很了解)
 * http://blog.csdn.net/zhangml0522/article/details/52556418
 */
public class Win8Search extends View {

    private Paint mPaint;
    private Path mPath;//用于画圆
    private PathMeasure mPathMeasure;//用PathMeasure根据ValueAnimator返回的值截取path上的一个点
    private int mWidth,mHeight;
    private ValueAnimator valueAnimator;
    //用这个来接收ValueAnimator的返回值,代表整个动画的进度
    private float t;
    private Path mDst;

    public Win8Search(Context context) {
        super(context);

        init();
        valueAnimator.start();
    }

    public Win8Search(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
        valueAnimator.start();
    }

    /**
     * 结束动画
     */
    public void stop(){
        valueAnimator.cancel();
    }

    /**
     * 开始动画
     */
    public void start(){
        valueAnimator.start();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(10);
        //设置画笔为圆笔
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);//抗锯齿

        mPath = new Path();

        RectF rect = new RectF(-66,-66,66,66);
        mPath.addArc(rect,-90,359.9f);//不能写360,手机性能优化有关

        mPathMeasure = new PathMeasure(mPath,false);

        valueAnimator = ValueAnimator.ofFloat(0f,1f).setDuration(3000);

        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                t = (float) animation.getAnimatedValue();
                //刷新视图
                invalidate();
            }
        });


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);

        mDst = new Path();

        if(t >= 0.95){//在快到起始点的时候,提前画好一个圆,避免闪顿情况
            canvas.drawPoint(0,-66,mPaint);
        }

        int num = (int)(t/0.05);
        float s,y,x;
        //我们设置让t每间隔0.05就画一个点，总共画4个点，注意这里getSegment()的最后一个要设置为true来保证画出来的是多个点而不是一条线
        /*
        * 时间--路程关系函数

            函数为y = -x*x + 2*x，当x=1时，y=mPathMeasure.getLength();
            设s = mPathMeasure.getLength();
            最终我们套用函数:y = -s*x*x+2*s*x;
            这里的Y轴代表的是path的长度，X轴对应时间
            所以把流程二中的y = s*x改成y = -s*x*x+2*s*x即可

        * */
        switch (num) {
            default:
            case 3:
                x = t - 0.15f * (1 - t);
                s = mPathMeasure.getLength();
                y = -s*x*x + 2*s*x;
                mPathMeasure.getSegment(y,y+1, mDst,true);
            case 2:
                x = t - 0.10f * (1 - t);
                s = mPathMeasure.getLength();
                y = -s*x*x + 2*s*x;
                mPathMeasure.getSegment(y,y+1, mDst,true);
            case 1:
                x = t - 0.05f * (1 - t);
                s = mPathMeasure.getLength();
                y = -s*x*x + 2*s*x;
                mPathMeasure.getSegment(y,y+1, mDst,true);
            case 0:
                x = t ;
                s = mPathMeasure.getLength();
                y = -s*x*x + 2*s*x;
                mPathMeasure.getSegment(y,y+1, mDst,true);
                break;
        }

        canvas.drawPath(mDst,mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }
}
