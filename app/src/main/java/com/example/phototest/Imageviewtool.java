package com.example.phototest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Imageviewtool {
  
  /** 
   * 我们先看下质量压缩方法 
   * 
   * @param image 
   * @return 
   */
  public static Bitmap compressimage(Bitmap image) {

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
    image.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
    int Options = 100;
    while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
      baos.reset();// 重置baos即清空baos 
      image.compress(android.graphics.Bitmap.CompressFormat.JPEG, Options, baos);// 这里压缩Options%，把压缩后的数据存放到baos中
      Options -= 10;// 每次都减少10
    } 
    ByteArrayInputStream isbm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
      Bitmap Bitmap = BitmapFactory.decodeStream(isbm, null, null);// 把ByteArrayInputStream数据生成图片
    return Bitmap;
  } 
  
  /** 
   * 图片按比例大小压缩方法（根据路径获取图片并压缩） 
   * 
   * @param srcpath 
   * @return 
   */
  public static Bitmap getimage(String srcpath) {
    BitmapFactory.Options newopts = new BitmapFactory.Options();
    // 开始读入图片，此时把Options.inJustDecodeBounds 设回true了
    newopts.inJustDecodeBounds = true;
      Bitmap bitmap = BitmapFactory.decodeFile(srcpath, newopts);// 此时返回bm为空
  
    newopts.inJustDecodeBounds = false;
    int w = newopts.outWidth;
    int h = newopts.outHeight;
    // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为 
    float hh = 800f;// 这里设置高度为800f 
    float ww = 480f;// 这里设置宽度为480f 
    // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可 
    int be = 1;// be=1表示不缩放 
    if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放 
      be = (int) (newopts.outWidth / ww);
    } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放 
      be = (int) (newopts.outHeight / hh);
    } 
    if (be <= 0) 
      be = 1; 
    newopts.inSampleSize = be;// 设置缩放比例
    // 重新读入图片，注意此时已经把Options.inJustDecodeBounds 设回false了
    bitmap = BitmapFactory.decodeFile(srcpath, newopts);
    return compressimage(bitmap);// 压缩好比例大小后再进行质量压缩
  } 
  
  /** 
   * 图片按比例大小压缩方法（根据Bitmap图片压缩）
   * 
   * @param image 
   * @return 
   */
  public static Bitmap comp(Bitmap image) {
  
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    image.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, baos);
    if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1m,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
      baos.reset();// 重置baos即清空baos 
      image.compress(android.graphics.Bitmap.CompressFormat.JPEG, 30, baos);// 这里压缩50%，把压缩后的数据存放到baos中
    } 
    ByteArrayInputStream isbm = new ByteArrayInputStream(baos.toByteArray());
    BitmapFactory.Options newopts = new BitmapFactory.Options();
    // 开始读入图片，此时把Options.inJustDecodeBounds 设回true了
    newopts.inJustDecodeBounds = true;
    Bitmap Bitmap = BitmapFactory.decodeStream(isbm, null, newopts);
    newopts.inJustDecodeBounds = false;
    int w = newopts.outWidth;
    int h = newopts.outHeight;
    // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为 
    float hh = 150f;// 这里设置高度为800f 
    float ww = 150f;// 这里设置宽度为480f 
    // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可 
    int be = 1;// be=1表示不缩放 
    if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放 
      be = (int) (newopts.outWidth / ww);
    } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放 
      be = (int) (newopts.outHeight / hh);
    } 
    if (be <= 0) 
      be = 1; 
    newopts.inSampleSize = be;// 设置缩放比例
    // 重新读入图片，注意此时已经把Options.inJustDecodeBounds 设回false了
    isbm = new ByteArrayInputStream(baos.toByteArray());
    Bitmap = BitmapFactory.decodeStream(isbm, null, newopts);
    return compressimage(Bitmap);// 压缩好比例大小后再进行质量压缩
  } 
  
  public static byte[] Bitmap2bytes(Bitmap bm) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
    return baos.toByteArray();
  } 
  
  /** 
   * 按原比例压缩图片到指定尺寸 
   * 
   * @param context
   * @param inputuri 
   * @param outputuri 
   * @param maxlenth 
   *      最长边长 
   */
  public static void reducepicture(Context context, Uri inputuri,
                                   Uri outputuri, int maxlenth, int compress) {
    BitmapFactory.Options Options = new BitmapFactory.Options();
    Options.inJustDecodeBounds = true;
    InputStream is = null;
    try { 
      is = context.getContentResolver().openInputStream(inputuri);
      BitmapFactory.decodeStream(is, null, Options);
      is.close(); 
      int samplesize = 1; 
      int longestside = 0; 
      int longestsidelenth = 0; 
      if (Options.outWidth > Options.outHeight) {
        longestsidelenth = Options.outWidth;
        longestside = 0; 
      } else { 
        longestsidelenth = Options.outHeight;
        longestside = 1; 
      } 
      if (longestsidelenth > maxlenth) { 
        samplesize = longestsidelenth / maxlenth; 
      }
      Options.inJustDecodeBounds = false;
      Options.inSampleSize = samplesize;
  
      is = context.getContentResolver().openInputStream(inputuri);
      Bitmap Bitmap = BitmapFactory.decodeStream(is, null, Options);
      is.close(); 
  
      if (Bitmap == null) {
        Toast.makeText(context, "图片获取失败，请确认您的存储卡是否正常",
            Toast.LENGTH_SHORT).show();
        return; 
      } 
  
      Bitmap srcBitmap = Bitmap;
      float scale = 0; 
      if (longestside == 0) { 
        scale = (float) maxlenth / (float) (srcBitmap.getWidth());
      } else { 
        scale = (float) maxlenth / (float) (srcBitmap.getHeight());
      } 
      Matrix matrix = new Matrix();
      matrix.postScale(scale, scale);
      Bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
          srcBitmap.getHeight(), matrix, true);
      // 如果尺寸不变会返回本身，所以需要判断是否是统一引用来确定是否需要回收 
      if (srcBitmap != Bitmap) {
        srcBitmap.recycle();
        srcBitmap = null;
      } 
  
      saveBitmaptouri(Bitmap, outputuri, compress);
      Bitmap.recycle();
      Bitmap = null;
    } catch (FileNotFoundException e) {
      // todo auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // todo auto-generated catch block 
      e.printStackTrace();
    }
  } 
  
  private static boolean saveBitmaptouri(Bitmap Bitmap, Uri uri, int compress)
      throws IOException {
    File file = new File(uri.getPath());
    if (file.exists()) { 
      if (file.delete()) { 
        if (!file.createNewFile()) {
          return false; 
        } 
      } 
    }
    BufferedOutputStream outstream = new BufferedOutputStream(
        new FileOutputStream(file));
    Bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, compress, outstream);
    outstream.flush(); 
    outstream.close();
    return true; 
  }
}