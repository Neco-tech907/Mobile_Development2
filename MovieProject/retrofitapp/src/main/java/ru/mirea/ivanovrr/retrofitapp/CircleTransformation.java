package ru.mirea.ivanovrr.retrofitapp;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

/** Трансформация Picasso: обрезает картинку в круг. */
public class CircleTransformation implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        if (squared != source) {
            source.recycle();
        }

        Bitmap result = Bitmap.createBitmap(size, size, source.getConfig());
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP,
                BitmapShader.TileMode.CLAMP));
        float radius = size / 2f;
        canvas.drawCircle(radius, radius, radius, paint);
        squared.recycle();
        return result;
    }

    @Override
    public String key() {
        return "circle";
    }
}
