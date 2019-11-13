package com.example.phototest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Imagefactory {
  
  /** 
   * 从指定的图像路径获取位图 
   * 
   * @param imgpath 
   * @return 
   */
  public Bitmap getBitmap(String imgpath) {
    // get Bitmap through image path
    BitmapFactory.Options newopts = new BitmapFactory.Options();
    newopts.inJustDecodeBounds = false;
    newopts.inPurgeable = true;
    newopts.inInputShareable = true;
    // do not compress 
    newopts.inSampleSize = 1;
    newopts.inPreferredConfig = Bitmap.Config.RGB_565;
    return BitmapFactory.decodeFile(imgpath, newopts);
  } 
  
  /** 
   * 压缩图片（质量压缩） 
   * 
   * @param Bitmap
   */
  public static File compressimage(Bitmap Bitmap) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
    int Options = 100;
    while (baos.toByteArray().length / 1024 > 500) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
      baos.reset();// 重置baos即清空baos 
      Options -= 10;// 每次都减少10
      Bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, Options, baos);// 这里压缩Options%，把压缩后的数据存放到baos中
      long length = baos.toByteArray().length;
    } 
    SimpleDateFormat format = new SimpleDateFormat("yyyymmddhhmmss");
    Date date = new Date(System.currentTimeMillis());
    String filename = format.format(date);
    File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
    try {
      FileOutputStream fos = new FileOutputStream(file);
      try { 
        fos.write(baos.toByteArray());
        fos.flush(); 
        fos.close(); 
      } catch (IOException e) {
  
        e.printStackTrace();
      } 
    } catch (FileNotFoundException e) {
  
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    recycleBitmap(Bitmap);
    return file; 
  } 
  
  public static void recycleBitmap(Bitmap... Bitmaps) {
    if (Bitmaps == null) {
      return; 
    } 
    for (Bitmap bm : Bitmaps) {
      if (null != bm && !bm.isRecycled()) {
        bm.recycle(); 
      } 
    } 
  } 
  
  /** 
   * 将位图存储到指定的图像路径中 
   * 
   * @param Bitmap
   * @param outpath 
   * @throws FileNotFoundException
   */
  public void storeimage(Bitmap Bitmap, String outpath) throws FileNotFoundException {
    FileOutputStream os = new FileOutputStream(outpath);
    Bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, os);
  } 
  
  /** 
   * 通过像素压缩图像，这将改变图像的宽度/高度。用于获取缩略图 
   * 
   * 
   * @param imgpath 
   *      image path 
   * @param pixelw 
   *      目标宽度像素 
   * @param pixelh 
   *      高度目标像素 
   * @return 
   */
  public Bitmap ratio(String imgpath, float pixelw, float pixelh) {
    BitmapFactory.Options newopts = new BitmapFactory.Options();
    // 开始读入图片，此时把Options.inJustDecodeBounds 设回true，即只读边不读内容
    newopts.inJustDecodeBounds = true;
    newopts.inPreferredConfig = Bitmap.Config.RGB_565;
    // get Bitmap info, but notice that Bitmap is null now
    Bitmap Bitmap = BitmapFactory.decodeFile(imgpath, newopts);
  
    newopts.inJustDecodeBounds = false;
    int w = newopts.outWidth;
    int h = newopts.outHeight;
    // 想要缩放的目标尺寸 
    float hh = pixelh;// 设置高度为240f时，可以明显看到图片缩小了 
    float ww = pixelw;// 设置宽度为120f，可以明显看到图片缩小了 
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
    // 开始压缩图片，注意此时已经把Options.inJustDecodeBounds 设回false了
    Bitmap = BitmapFactory.decodeFile(imgpath, newopts);
    // 压缩好比例大小后再进行质量压缩 
    // return compress(Bitmap, maxsize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
    return Bitmap;
  } 
  
  /** 
   * 压缩图像的大小，这将修改图像宽度/高度。用于获取缩略图 
   * 
   * 
   * @param image 
   * @param pixelw 
   *      target pixel of width 
   * @param pixelh 
   *      target pixel of height 
   * @return 
   */
  public Bitmap ratio(Bitmap image, float pixelw, float pixelh) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    image.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, os);
    if (os.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1m,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
      os.reset();// 重置baos即清空baos 
      image.compress(android.graphics.Bitmap.CompressFormat.JPEG, 50, os);// 这里压缩50%，把压缩后的数据存放到baos中
    } 
    ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    BitmapFactory.Options newopts = new BitmapFactory.Options();
    // 开始读入图片，此时把Options.inJustDecodeBounds 设回true了
    newopts.inJustDecodeBounds = true;
    newopts.inPreferredConfig = Bitmap.Config.RGB_565;
    Bitmap Bitmap = BitmapFactory.decodeStream(is, null, newopts);
    newopts.inJustDecodeBounds = false;
    int w = newopts.outWidth;
    int h = newopts.outHeight;
    float hh = pixelh;// 设置高度为240f时，可以明显看到图片缩小了 
    float ww = pixelw;// 设置宽度为120f，可以明显看到图片缩小了 
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
    is = new ByteArrayInputStream(os.toByteArray());
    Bitmap = BitmapFactory.decodeStream(is, null, newopts);
    // 压缩好比例大小后再进行质量压缩 
    // return compress(Bitmap, maxsize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
    return Bitmap;
  } 
  
  /** 
   * 按质量压缩，并将图像生成指定的路径 
   * 
   * @param image 
   * @param outpath 
   * @param maxsize 
   *      目标将被压缩到小于这个大小（kb）。 
   * @throws IOException
   */
  public void compressandgenimage(Bitmap image, String outpath, int maxsize) throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    // scale 
    int Options = 100;
    // store the Bitmap into output stream(no compress)
    image.compress(android.graphics.Bitmap.CompressFormat.JPEG, Options, os);
    // compress by loop 
    while (os.toByteArray().length / 1024 > maxsize) {
      // clean up os 
      os.reset(); 
      // interval 10 
      Options -= 10;
      image.compress(android.graphics.Bitmap.CompressFormat.JPEG, Options, os);
    } 
  
    // generate compressed image file 
    FileOutputStream fos = new FileOutputStream(outpath);
    fos.write(os.toByteArray());
    fos.flush(); 
    fos.close(); 
  } 
  
  /** 
   * 按质量压缩，并将图像生成指定的路径 
   * 
   * @param imgpath 
   * @param outpath 
   * @param maxsize 
   *      目标将被压缩到小于这个大小（kb）。 
   * @param needsdelete 
   *      是否压缩后删除原始文件 
   * @throws IOException
   */
  public void compressandgenimage(String imgpath, String outpath, int maxsize, boolean needsdelete)
      throws IOException {
    compressandgenimage(getBitmap(imgpath), outpath, maxsize);
  
    // delete original file 
    if (needsdelete) { 
      File file = new File(imgpath);
      if (file.exists()) { 
        file.delete(); 
      } 
    } 
  } 
  
  /** 
   * 比例和生成拇指的路径指定 
   * 
   * @param image 
   * @param outpath 
   * @param pixelw 
   *      目标宽度像素 
   * @param pixelh 
   *      高度目标像素 
   * @throws FileNotFoundException
   */
  public void ratioandgenthumb(Bitmap image, String outpath, float pixelw, float pixelh)
      throws FileNotFoundException {
    Bitmap Bitmap = ratio(image, pixelw, pixelh);
    storeimage(Bitmap, outpath);
  } 
  
  /** 
   * 比例和生成拇指的路径指定 
   * 
   * @param imgpath
   * @param outpath 
   * @param pixelw 
   *      目标宽度像素 
   * @param pixelh 
   *      高度目标像素 
   * @param needsdelete 
   *      是否压缩后删除原始文件 
   * @throws FileNotFoundException
   */
  public void ratioandgenthumb(String imgpath, String outpath, float pixelw, float pixelh, boolean needsdelete)
      throws FileNotFoundException {
    Bitmap Bitmap = ratio(imgpath, pixelw, pixelh);
    storeimage(Bitmap, outpath);
  
    // delete original file 
    if (needsdelete) { 
      File file = new File(imgpath);
      if (file.exists()) { 
        file.delete(); 
      } 
    } 
  } 
  
}