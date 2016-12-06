package cn.edu.pku.hanqin.view;

/**
 * Created by 76568 on 2016/11/28 0028.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * ListView��߰���A-Z�����SideBar�ؼ�
 * @author Test
 *
 */
public class SideBar extends View {

    //SideBar����ʾ����ĸ��#��
    private static final String[] CHARACTERS = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    //SideBar�ĸ߶�
    private int width;
    //SideBar�Ŀ��
    private int height;
    //SideBar��ÿ����ĸ����ʾ����ĸ߶�
    private float cellHeight;
    //����ĸ�Ļ���
    private Paint characterPaint;
    //SideBar����ĸ���Ƶľ�������
    private Rect textRect;
    //��ָ������SideBar�ϵĺ�������
    private float touchY;
    private float touchX;

    private OnSelectListener listener;

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SideBar(Context context) {
        super(context);
        init(context);
    }

    //��ʼ������
    private void init(Context context){
        textRect = new Rect();
        characterPaint = new Paint();
        characterPaint.setColor(Color.parseColor("#6699ff"));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed){ //���������SideBar�ĸ߶ȺͿ��
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            //SideBar�ĸ߶ȳ�����Ҫ��ʾ����ĸ�ĸ���������ÿ����ĸ��ʾ����ĸ߶�
            cellHeight = height * 1.0f / CHARACTERS.length;
            //����SideBar�Ŀ�Ⱥ�ÿ����ĸ��ʾ�ĸ߶ȣ�ȷ��������ĸ�����ִ�С����������ĺô��ǣ����ڲ�ͬ�ֱ��ʵ���Ļ�����ִ�С�ǿɱ��
            int textSize = (int) ((width > cellHeight ? cellHeight : width) * (3.0f / 4));
            characterPaint.setTextSize(textSize);
        }
    }

    //����SideBar�ϵ���ĸ
    private void drawCharacters(Canvas canvas){
        for(int i = 0; i < CHARACTERS.length; i++){
            String s = CHARACTERS[i];
            //��ȡ����ĸ�ľ�������
            characterPaint.getTextBounds(s, 0, s.length(), textRect);
            //������һ����õľ������򣬻�����ĸ
            canvas.drawText(s,
                    (width - textRect.width()) / 2f,
                    cellHeight * i + (cellHeight + textRect.height()) / 2f,
                    characterPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCharacters(canvas);
    }

    //������ָ���������꣬��ȡ��ǰѡ�����ĸ
    private String getHint(){
        int index = (int) (touchY / cellHeight);
        if(index >= 0 && index < CHARACTERS.length){
            return CHARACTERS[index];
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //��ȡ��ָ����������
                touchX = event.getX();
                touchY = event.getY();
                if(listener != null && touchX > 0){
                    listener.onSelect(getHint());
                }
                if(listener != null && touchX < 0){
                    listener.onMoveUp(getHint());
                }
                return true;
            case MotionEvent.ACTION_UP:
                touchY = event.getY();
                if(listener != null){
                    listener.onMoveUp(getHint());
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    //��������������ָ��SideBar�ϰ��º�̧��Ķ���
    public interface OnSelectListener{
        void onSelect(String s);
        void onMoveUp(String s);
    }

    //���ü�����
    public void setOnSelectListener(OnSelectListener listener){
        this.listener = listener;
    }

}
