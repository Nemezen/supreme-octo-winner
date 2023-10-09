package com.example.examen;

import static com.example.examen.MainActivity.paint_brush;
import static com.example.examen.MainActivity.path;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

import android.graphics.Path;
import android.view.ViewGroup;

import java.util.ArrayList;

public class vista extends View {
    public static ArrayList <Path>pathList=new ArrayList<>();
    public static ArrayList<Integer>coloresList=new ArrayList<>();
    public ViewGroup.LayoutParams params;
    public static int current_brush= Color.BLACK;
    public vista(Context context) {
        super(context);
        init(context);
    }

    public vista(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public vista(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init(Context context){
        paint_brush.setAntiAlias(true);
        paint_brush.setColor(Color.BLACK);
        paint_brush.setStyle(Paint.Style.STROKE);
        paint_brush.setStrokeCap(Paint.Cap.ROUND);
        paint_brush.setStrokeJoin(Paint.Join.ROUND);
        paint_brush.setStrokeWidth(10f);
        params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x=event.getX();
        float y=event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x,y);
                pathList.add(path);
                coloresList.add(current_brush);
                invalidate();
                return true;
            default:
                return false;
        }
    }
    @Override
    protected void onDraw (Canvas canvas){
        for (int i=0;i<pathList.size();i++){
            paint_brush.setColor(coloresList.get(i));
            canvas.drawPath(pathList.get(i), paint_brush);
            invalidate();
        }
    }
}

