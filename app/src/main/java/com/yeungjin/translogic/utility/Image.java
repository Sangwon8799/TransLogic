package com.yeungjin.translogic.utility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.TypedValue;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Image {
    private static final String JPEG = "/9j/4AAQSkZJRgABAQAAAQABAAD/[A-Za-z0-9]";

    private Image() { }

    public static String toBase64(@NonNull Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
        byte[] bytes = outputStream.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @NonNull
    public static Bitmap toBitmap(@NonNull String base64) {
        byte[] bytes = null;

        try {
            bytes = Base64.decode(base64, Base64.DEFAULT);
        } catch (Exception error) {
            error.printStackTrace();
        }

        return BitmapFactory.decodeByteArray(bytes, 0, Objects.requireNonNull(bytes).length);
    }

    public static boolean isImage(@NonNull String content) {
        Pattern pattern = Pattern.compile(JPEG, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);

        return matcher.find();
    }

    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
