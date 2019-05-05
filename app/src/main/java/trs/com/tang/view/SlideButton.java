package trs.com.tang.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SlideButton extends View {
    //布局宽度
    private int width;
    //布局高度
    private int height;
    //移动的位置
    private int position;
    //按钮的宽度
    private int spacing;

    //背景的画笔
    private Paint bgPaint;
    //滑动按钮的画笔
    private Paint slidePaint;
    //文字的画笔
    private Paint txtPaint;

    private int downX;
    private int moveX;
    private int offsetX;
    //是否可以滑动
    private boolean isSlide;

    //滑动前的文字
    private String startTxt ;
    //滑动后的文字
    private String endTxt ;

    //最底层的颜色
    private int backgroudColor;
    //滑动后的颜色
    private int slideColor;
    //滑动按钮的颜色
    private int paseColor;
    //文字颜色
    private int txtColor;

    public SlideButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //初始化所有配置
    private void init() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        slidePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        position = 0;
        isSlide = true;
        backgroudColor = Color.GRAY;
        slideColor = Color.GREEN;
        paseColor = Color.YELLOW;
        txtColor = Color.WHITE;
        startTxt = "滑动刷新";
        endTxt = "刷新完成";
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        spacing = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bgPaint.setColor(backgroudColor);
        canvas.drawRect(position, 0, width, height, bgPaint);

        slidePaint.setColor(paseColor);
        canvas.drawRect(position, 0, position + spacing, height, slidePaint);

        bgPaint.setColor(slideColor);
        canvas.drawRect(0, 0, position, height, bgPaint);

        txtPaint.setColor(txtColor);
        txtPaint.setTextSize(height/3f);

        if (position == 0){
            float startTextWidth = txtPaint.measureText(startTxt);
            canvas.drawText(startTxt,(width-startTextWidth)/2,height/2f + height/7f,txtPaint);
        }
        if (position == width-spacing){
            float endTextWidth = txtPaint.measureText(endTxt);
            canvas.drawText(endTxt,(width-endTextWidth)/2,height/2f + height/7f,txtPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                if (downX > spacing){
                    return false;
                }
                if (!isSlide){
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX();
                offsetX = (moveX-downX);
                if (offsetX>0 && offsetX < width-spacing){
                    position = offsetX;
                    invalidate();
                }
                if (offsetX >= width-spacing){
                    position = width-spacing;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (offsetX < width-spacing){
                    position = 0;
                    invalidate();
                }
                if (offsetX >= width-spacing){
                    isSlide = false;
                    onSlideListener.onSlideCompete();
                }
                break;
        }
        return true;
    }

    public void setSpacing(int spacing){
        this.spacing = spacing;
        invalidate();
    }

    public void setStartTxt(String startTxt) {
        this.startTxt = startTxt;
        invalidate();
    }

    public void setEndTxt(String endTxt) {
        this.endTxt = endTxt;
        invalidate();
    }

    public void setBackgroudColor(int backgroudColor) {
        this.backgroudColor = backgroudColor;
        invalidate();
    }

    public void setSlideColor(int slideColor) {
        this.slideColor = slideColor;
        invalidate();
    }

    public void setPaseColor(int paseColor) {
        this.paseColor = paseColor;
        invalidate();
    }

    //设置滑动完成监听事件
    public void setOnSlideListener(OnSlideListener onSlideListener){
        this.onSlideListener = onSlideListener;
    }

    private OnSlideListener onSlideListener;

    public interface OnSlideListener{
        void onSlideCompete();
    }
}
