package com.yeungjin.translogic.utility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.TypedValue;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BitmapTranslator {
    private BitmapTranslator() { }

    public static String toBase64(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

        byte[] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @NonNull
    public static Bitmap toBitmap(@NonNull String base64) {
        byte[] bytes = new byte[0];

        try {
            bytes = Base64.decode(base64, Base64.DEFAULT);
        } catch (Exception error) {
            error.printStackTrace();
        }

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static boolean isImage(String content) {
        Pattern pattern = Pattern.compile("/9j/4AAQSkZJRgABAQAAAQABAAD/[A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);

        return matcher.find();
    }

    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
