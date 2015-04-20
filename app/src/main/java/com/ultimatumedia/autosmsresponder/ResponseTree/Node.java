package com.ultimatumedia.autosmsresponder.ResponseTree;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Size;

import com.ultimatumedia.autosmsresponder.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by erik on 4/19/15.
 */
public class Node {
    public int level;
    public Paint rectPaint;
    public Rect rectBounds;

    public Paint textPaint;
    public String text;

    public int rectHeight, rectWidth;
    public int xStart, yStart;

    public boolean selected;
    public Paint selPaint;
    public Rect selBounds;
    int boundPadding;
    int selPadding;

    String Condition = "";
    String message = "";

    public Node(Context context, int treeLevel, String text, int x, int y) {
        level = treeLevel;
        selected = false;
        xStart = x;
        yStart = y;
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(Color.CYAN);

        selPaint = new Paint();
        selPaint.setAntiAlias(true);
        selPaint.setColor(Color.YELLOW);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.node_text_size));

        this.text = text;

        Rect bounds = new Rect();
        textPaint.getTextBounds(text,0,text.length(),bounds);
        rectHeight = bounds.height();
        rectWidth = bounds.width();

        //TODO:There is a problem with this when screen is clicked the node moves up and to the left.
        //boundPadding = context.getResources().getDimensionPixelSize(R.dimen.node_padding);
        boundPadding = 0;
        rectBounds = new Rect(xStart - boundPadding, yStart - boundPadding, xStart + rectWidth + boundPadding, yStart + rectHeight + boundPadding);

        selPadding = context.getResources().getDimensionPixelSize(R.dimen.node_selected_padding);
        selBounds = new Rect(rectBounds.left - selPadding, rectBounds.top - selPadding, rectBounds.right + selPadding, rectBounds.bottom + selPadding);
    }

    public void update(int x, int y) {
        rectBounds.left = x - boundPadding;
        rectBounds.right = x + rectWidth + boundPadding;
        rectBounds.top = y - boundPadding;
        rectBounds.bottom = y + rectHeight + boundPadding;
        //rectBounds.left = x;
        //rectBounds.right = x + rectWidth;
        //rectBounds.top = y;
        //rectBounds.bottom = y + rectHeight;

        selBounds.left = rectBounds.left - selPadding;
        selBounds.right = rectBounds.right + selPadding;
        selBounds.top = rectBounds.top - selPadding;
        selBounds.bottom = rectBounds.bottom + selPadding;
    }
}
