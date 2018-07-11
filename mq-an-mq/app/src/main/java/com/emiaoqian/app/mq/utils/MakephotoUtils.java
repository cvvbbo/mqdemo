package com.emiaoqian.app.mq.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



/**
 * Created by xiong on 2018/4/17.
 */

public class MakephotoUtils {

    private static Bitmap afterbitmap;



    //保存图片，这个是保存bitmap的图片从相册
    public static File saveImageofalbum(Context m, Bitmap bmp, String photoname) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = photoname;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //100%品质就是不会压缩
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /***下面这个注释掉，这个api的意思是在系统的图库中再拷贝一份自己的刚才保存的图片 4.17**/
//        // 其次把文件插入到系统图库(网上的大部分方案还需要这个 3.27)
//        try {
//            MediaStore.Images.Media.insertImage(m.getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        //因为这个方法是为了临时保存的，所以看看不刷新会怎么样
        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));

        return file;

    }


    //压缩图片的方法
    public static Bitmap compressImage(Activity m, String filepath) {
        int height = m.getWindowManager().getDefaultDisplay().getHeight();
        int width = m.getWindowManager().getDefaultDisplay().getWidth();
        Point p = new Point();
        m.getWindowManager().getDefaultDisplay().getSize(p);
        width = p.x;
        height = p.y;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //下面这个是获取不到大小的，因为加载进内存的大小为0
        Bitmap bitmap = BitmapFactory.decodeFile(filepath);
        BitmapFactory.decodeFile(filepath, options);
        //Log.e("--压缩之前", bitmap.getByteCount() + " ");
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int index = 1;
        if (outHeight > height || outWidth > width) {
            float heightRate = outHeight / height;
            float widthrate = outHeight / width;

            index = (int) Math.max(heightRate, widthrate);
        }
        options.inSampleSize = index;
        options.inJustDecodeBounds = false;
        Bitmap afterbitmap = BitmapFactory.decodeFile(filepath, options);
        //Log.e("--压缩之后", afterbitmap.getByteCount() + " ");
        return afterbitmap;
    }


    //保存图片，这个是保存bitmap的图片。
    public static  void saveImage(Context m, Bitmap bmp, String photoname) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "emiaoqian");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = photoname;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //100%品质就是不会压缩，这里只是把图片保存到另一个地方
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            MediaStore.Images.Media.insertImage(m.getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        m.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
    }



}
