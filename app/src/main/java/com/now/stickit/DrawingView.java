package com.now.stickit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by User on 6/26/2015.
 */
public class DrawingView extends View {
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = Color.BLACK;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    private float brushSize, lastBrushSize;
    private boolean drawMode=false;
    private boolean eraseMode=false;
    private ArrayList<Path> paths=new ArrayList<Path>();
    private ArrayList<PaintState> paintStates=new ArrayList<PaintState>();
    private ArrayList<Integer> paintColors=new ArrayList<Integer>();
    private PaintState paintState;
    private enum PaintState{draw,erase}


    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;

        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paintState=(eraseMode)?PaintState.erase:PaintState.draw;
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if(drawMode==false){

        }else{
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.reset();
                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    drawCanvas.drawPath(drawPath, drawPaint);
                    paths.add(drawPath);
                    paintStates.add(paintState);
                    paintColors.add(paintColor);
                    drawPath=new Path();
                    break;
                default:
                    return false;
            }
        }
        invalidate();
        return true;
    }


    public void drawMode(boolean drawMode){
        this.drawMode=drawMode;
    }

    public void drawMode(boolean drawMode,boolean eraseMode){
        this.drawMode=drawMode;
        this.eraseMode=eraseMode;
        if(eraseMode==true){
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }else{
            drawPaint.setXfermode(null);
        }
    }

    public void undo(){
        if(paths.size()>0){
            paths.remove(paths.size()-1);
            paintStates.remove(paintStates.size()-1);
            drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            for(int i=0; i<paths.size(); i++) {
                if(paintStates.get(i)==PaintState.draw){
                    drawPaint.setXfermode(null);
                    paintColor=paintColors.get(i);
                    drawCanvas.drawPath(paths.get(i), drawPaint);
                }else {
                    drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    drawCanvas.drawPath(paths.get(i), drawPaint);
                }
            }
            invalidate();
        }else{
            Toast.makeText(getContext(), "Nothing more to undo", Toast.LENGTH_SHORT).show();
        }
    }

    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setBrushSize(float newSize){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }
}
