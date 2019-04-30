package trs.com.tang.view;

import android.content.Context;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import trs.com.tang.R;

public class DragItem extends LinearLayout {

    private Context context;

    private int contentWidth;
    private int contentHeight;
    private float downX;
    private float moveX;
    private float offsetX;
    private boolean isOpen;
    private int maxDragWith;
    private int paddingLeft;

    public DragItem(Context context,AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        isOpen = false;
    }

    private void initView() {
        View view = inflate(context, R.layout.drag_delete,null);
        addView(view,contentWidth/5,contentHeight);
        maxDragWith = contentWidth/5;
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDragDelect.delete();
            }
        });
    }


    public void setOnDragDelect(OnDragDelect onDragDelect){
        this.onDragDelect = onDragDelect;
    }
    private OnDragDelect onDragDelect;
    public interface OnDragDelect{
        void delete();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = getMeasuredWidth();
        contentHeight = getMeasuredHeight();
        setMeasuredDimension(contentWidth+maxDragWith,contentHeight);
        initView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                offsetX = moveX - downX;
                if (offsetX >= -maxDragWith && offsetX <= maxDragWith){
                    if (isOpen && offsetX > 0){
                        paddingLeft = (int) (offsetX - maxDragWith);
                        setPadding(paddingLeft,0,0,0);
                        requestDisallowInterceptTouchEvent(true);
                    }
                    if (!isOpen && offsetX < 0){
                        paddingLeft = (int) offsetX;
                        setPadding(paddingLeft,0,0,0);
                        requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (paddingLeft < -maxDragWith/2){
                    isOpen = true;
                    setPadding(-maxDragWith,0,0,0);
                }else {
                    isOpen = false;
                    setPadding(0,0,0,0);
                }
                break;
        }
        return true;
    }
}
