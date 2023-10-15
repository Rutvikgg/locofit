package com.rutvik.locofit.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            InputStream input = context.getContentResolver().openInputStream(uri);

            // Calculate inSampleSize to decode smaller version of the image
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, options);
            assert input != null;
            input.close();

            // Set inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 512, 512);

            // Decode bitmap with inSampleSize set
            input = context.getContentResolver().openInputStream(uri);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);

            // Rotate the bitmap if needed
            int rotation = ImageUtil.getOrientation(context, uri);
            if (rotation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);
                assert bitmap != null;
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            assert input != null;
            input.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static int getOrientation(Context context, Uri photoUri) {
        String[] projection = {android.provider.MediaStore.Images.ImageColumns.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(photoUri, projection, null, null, null);
        if (cursor == null) {
            return 0;
        }
        int orientation = -1;
        if (cursor.moveToFirst()) {
            orientation = cursor.getInt(0);
        }
        cursor.close();
        return orientation;
    }
}
