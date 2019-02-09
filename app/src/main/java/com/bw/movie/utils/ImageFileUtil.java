package com.bw.movie.util;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;

/**
  * @作者 GXY
  * @创建日期 2019/1/26 14:48
  * @描述 质量压缩和转换成file文件
  *
  */
public class ImageFileUtil {
    public static void setBitmap(Bitmap bitmap, String path, int quality){
        String sub = path.substring(0, path.lastIndexOf("/"));
        File subFile = new File(sub);
        if(!subFile.exists() || !subFile.isDirectory()){
            try {
                if(!subFile.mkdirs()){
                    return;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(path);
            if(bitmap.compress(Bitmap.CompressFormat.PNG,quality,outputStream)){
                outputStream.flush();
                outputStream.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
