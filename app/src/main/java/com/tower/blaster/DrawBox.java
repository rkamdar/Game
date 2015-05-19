package com.tower.blaster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by Rushabh on 11/2/2014.
 */
public class DrawBox {
    static int[] broken = new int[]{R.drawable.broken1,R.drawable.broken2,R.drawable.broken3,R.drawable.broken4,R.drawable.broken5,R.drawable.broken6,R.drawable.broken7,R.drawable.broken8,R.drawable.broken9,R.drawable.broken10};
    static int place = 0;

    public DrawBox() {
    }

    public static Bitmap drawScore(Context context, int width, int height, int score){
        Bitmap box = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(box);

        Paint paint = new Paint();

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.scoreboard);
        bmp = Bitmap.createScaledBitmap(bmp, width, height, true);
        canvas.drawBitmap(bmp, 0, 0, paint);

        paint.setTextSize(height*2/3);

        paint.setARGB(255, 0, 0, 0);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        canvas.drawText("Score: "+Integer.toString(score),width/2,height*4/6,paint);

        paint.setARGB(255, 255, 255, 255);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawText("Score: "+Integer.toString(score), width/2, height*4/6, paint);

        return box;
    }

    public static Bitmap drawBit(Context context, int width, int height, int value, int selected){
        Bitmap box = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(box);

        Paint paint = new Paint();

        if(value == 0){
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.question);
            bmp = Bitmap.createScaledBitmap(bmp, width, height, true);
            canvas.drawBitmap(bmp,0,0,paint);
        }
        else{
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            //canvas.drawRect(0, 0, width, height, paint);

            //bitmapcode
            if(selected == 2 || selected == 1) {
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.select_brick);
                bmp = Bitmap.createScaledBitmap(bmp, width, height, true);
                canvas.drawBitmap(bmp, 0, 0, paint);
            }else if(selected == 3){
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.random_brick);
                bmp = Bitmap.createScaledBitmap(bmp, width, height, true);
                canvas.drawBitmap(bmp, 0, 0, paint);
            }
            //bitmapcode

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(10);

            //canvas.drawRect(0, 0, width, height, paint);

            if(selected == 1){
                canvas.drawRect(0, 0, width, height, paint);
            }

            paint.setTextSize(height*2/3);

            paint.setARGB(255, 0, 0, 0);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);

            canvas.drawText(Integer.toString(value),width/2,height*5/6,paint);

            paint.setARGB(255, 255, 255, 255);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawText(Integer.toString(value), width/2, height*5/6, paint);
        }

        return box;
    }

    public static Bitmap draw(Context context, int width, int height, int value, int selected){
        float realWidth = value * (width - height) / 50;
        realWidth = realWidth + height;

        //for broken
        if(selected == 5){
            realWidth = width;
        }

        Bitmap box = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(box);

        Paint paint = new Paint();

        if(value<11)    paint.setColor(Color.CYAN);
        else if(value<21) paint.setColor(Color.YELLOW);
        else if(value<31) paint.setColor(Color.BLUE);
        else if(value<41) paint.setColor(Color.GREEN);
        else paint.setColor(Color.RED);

        paint.setColor(Color.RED);

        paint.setStyle(Paint.Style.FILL);

        //canvas.drawRect((width - realWidth)/2, 0, ((width - realWidth)/2)+realWidth, height, paint);

        //bitmapcode
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bricks);
        if(selected == 5){
            bmp = BitmapFactory.decodeResource(context.getResources(), broken[place]);
            place++;
            if(place == 10) place=0;
        }
        bmp = Bitmap.createScaledBitmap(bmp, (int)realWidth, height, true);
        canvas.drawBitmap(bmp,(width - realWidth)/2,0,paint);
        //bitmapcode

        if(selected == 1){
            Bitmap bmp2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow);
            bmp2 = Bitmap.createScaledBitmap(bmp2, (int)(width/8), height/2, true);
            canvas.drawBitmap(bmp2,0,height/4,paint);
        }else if(selected == 2){
            Bitmap bmp2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_backward);
            bmp2 = Bitmap.createScaledBitmap(bmp2, (int)(width/8), height/2, true);
            canvas.drawBitmap(bmp2,width*7/8,height/4,paint);
        }else if(selected == 3){
            Bitmap bmp2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_backward_selected);
            bmp2 = Bitmap.createScaledBitmap(bmp2, (int)(width/8), height/2, true);
            canvas.drawBitmap(bmp2,width*7/8,height/4,paint);
        }else if(selected == 4){
            Bitmap bmp2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.blast);
            bmp2 = Bitmap.createScaledBitmap(bmp2, height*3/2, height*3/2, true);
            canvas.drawBitmap(bmp2,(width/2)-(height*3/4),-height/4,paint);
        }

        //---seems no use
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        //---seems no use

        //canvas.drawRect((width - realWidth)/2, 0, ((width - realWidth)/2)+realWidth, height, paint);

        paint.setTextSize(height*2/3);

        paint.setARGB(255, 0, 0, 0);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        if(selected != 5)
        canvas.drawText(Integer.toString(value),width/2,height*5/6,paint);

        paint.setARGB(255, 255, 255, 255);
        paint.setStyle(Paint.Style.FILL);

        if(selected != 5)
        canvas.drawText(Integer.toString(value), width/2, height*5/6, paint);

        return box;
    }
}
