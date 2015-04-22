package com.ultimatumedia.autosmsresponder.ResponseTree;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ultimatumedia.autosmsresponder.Database.SMSMessageDatasource;

import java.util.ArrayList;

public class ResponseTreeCanvas extends View {
    public ArrayList<Node> nodes;
    private int initXTouch = 0, initYTouch = 0;
    public ResponseTree rt;
    private SMSMessageDatasource dataSource;

    public ResponseTreeCanvas(Context context) {
        super(context);
        init(context);
    }

    public ResponseTreeCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        nodes = new ArrayList<Node>();
        /*dataSource = new SMSMessageDatasource(context);
        dataSource.open();
        if(dataSource.getNodes().size() < 1) {
            Node rootNode = new Node(context, 0, "<New Node>\n<Message>", 100, 100);
            dataSource.create(rootNode);
            nodes.add(rootNode);
        }else{
            nodes = dataSource.getNodes(context);
        }

        dataSource.close();*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas) {
        for(Node currentNode : nodes) {
            if(currentNode.selected) {
                canvas.drawRect(currentNode.selBounds, currentNode.selPaint);
            }
            canvas.drawRect(currentNode.rectBounds, currentNode.rectPaint);
            canvas.drawText(currentNode.text, currentNode.rectBounds.left, currentNode.rectBounds.bottom, currentNode.textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        boolean handled = false;
        int xTouch, yTouch;
        int actionIndex;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                initXTouch = (int) event.getX(0);
                initYTouch = (int) event.getY(0);

                for(Node currentNode : nodes) {
                    if(currentNode.rectBounds.contains(initXTouch, initYTouch)) {
                        currentNode.selected = !currentNode.selected;
                    }
                }

                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();

                int diffx = 0;
                int diffy = 0;
                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    diffx = initXTouch - xTouch;
                    diffy = initYTouch - yTouch;
                    for(Node currentNode : nodes) {
                        currentNode.update(currentNode.xStart - diffx, currentNode.yStart - diffy);
                    }
                }
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                for(Node currentNode : nodes) {
                    currentNode.xStart = currentNode.rectBounds.left;
                    currentNode.yStart = currentNode.rectBounds.top;
                }
                rt.updateView();
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                handled = true;
                break;

            default:
                break;
        }

        return super.onTouchEvent(event) || handled;
    }
}
