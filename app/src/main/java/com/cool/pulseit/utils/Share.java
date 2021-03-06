package com.cool.pulseit.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.core.content.FileProvider;

import com.cool.pulseit.ui.main.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Share {
    private static Context _context = MainActivity.getContext();

    public static void shareView(View view) {

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);

        File cachePath = new File(_context.getCacheDir(), "images");
        cachePath.mkdirs();
        try {
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
        } catch (IOException e) {
            Log.e("Share", e.getMessage());
        }

        File image = new File(cachePath, "image.jpg");
        Uri contentUri = FileProvider.getUriForFile(_context, "com.cool.pulseit.fileprovider", image);

        if (contentUri != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setDataAndType(contentUri, _context.getContentResolver().getType(contentUri));
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            _context.startActivity(Intent.createChooser(shareIntent, "Share via..."));
        }
    }
}
