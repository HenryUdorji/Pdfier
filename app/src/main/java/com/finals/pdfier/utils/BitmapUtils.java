package com.finals.pdfier.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BitmapUtils {
    private static final String TAG = "BitmapUtils";
    private BitmapUtils() {}

    /**
     * Converts bitmap to byte array in PNG format
     */
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                }catch (IOException e) {
                    Log.e(TAG, "convertBitmapToByteArray: ByteArrayOutStream was not closed");
                }
            }
        }
    }

    public static Bitmap convertByteArrayToBitmap(byte[] bitmapByteArray) {
        return BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.length);
    }
}
