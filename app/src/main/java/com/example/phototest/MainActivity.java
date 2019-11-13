package com.example.phototest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private ImageView mImg;
    /**
     * 压缩
     */
    private Button mBtn;
    /**
     * 111
     */
    private TextView mTvYuan;
    /**
     * 222
     */
    private TextView mTvNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mImg = (ImageView) findViewById(R.id.img);
        mBtn = (Button) findViewById(R.id.btn);
        mBtn.setOnClickListener(this);
        mTvYuan = (TextView) findViewById(R.id.tv_yuan);
        mTvNew = (TextView) findViewById(R.id.tv_new);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn:
                initPermission();
                break;
        }
    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.GET_PERMISSIONS) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//授权
                Toast.makeText(this, "同意授权", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            } else {
                Toast.makeText(this, "不同意授权", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            Bitmap datas = data.getParcelableExtra("data");
            int width = datas.getWidth();
            int height = datas.getHeight();
            mTvYuan.setText("初始宽："+width+"---初始高:"+height+"----大小:"+datas.getByteCount());

            PhotoCompressUtil util = new PhotoCompressUtil();
            Bitmap img = util.bitmapCompress(datas);
            File file = util.getFile(img);

            //Imageviewtool imageviewtool = new Imageviewtool();
            //Bitmap img = imageviewtool.comp(datas);
            mTvNew.setText("压缩宽："+img.getWidth()+"---压缩高:"+img+"----大小:"+img.getByteCount());
            mImg.setImageBitmap(img);
        }
    }
    @SuppressLint("NewApi")
    private void changeSizeOfImg(String pathName) {
        //width: 263,ImageView实际比较小
//        int width = mIv.getWidth();
        int width = 200; //图片要压缩到的宽度
//        int height = mIv.getHeight();
        int height = 300;//图片要压缩到的高度
        //图片的原始大小:
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        Log.d(TAG, "bitmap图片的原始大小: " + bitmap.getAllocationByteCount());
        BitmapFactory.Options options = new BitmapFactory.Options();//取出图片的属性，包括 宽高
        //设置为true,加载图片时不会获取到bitmap对象,但是可以拿到图片的宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        //计算采样率,对图片进行相应的缩放
        int outWidth = options.outWidth; //得到原始图片的宽
        int outHeight = options.outHeight;//得到原始图片的高

        Log.d(TAG, "图片的宽高-->outWidth: " + outWidth + ",outHeight:" + outHeight);
        float widthRatio = outWidth * 1.0f / width;  //算出安宽的比例压缩的倍数
        float heightRatio = outHeight * 1.0f / height;//算出安高的比例压缩的倍数
        Log.d(TAG, "widthRatio: " + widthRatio + ",heightRatio:" + heightRatio);
        float max = Math.max(widthRatio, heightRatio);//比较宽高的压缩比例，取最大的那个
        //向上舍入
        int inSampleSize = (int) Math.ceil(max);  //得到最终的压缩比例  3.1--》4
        Log.d(TAG, "inSampleSize: " + inSampleSize);
        //改为false,因为要获取采样后的图片了
        options.inJustDecodeBounds = false;
        //8
        options.inSampleSize = inSampleSize;//设置压缩倍数
        Bitmap bitmap1 = BitmapFactory.decodeFile(pathName, options);//用新的压缩倍数（压缩的比例）加载图片，进行压缩
        //采样后图片大小:144000,是采样前图片的inSampleSize*inSampleSize分之一(1/64)
        Log.d(TAG, "二次采样bitmap大小: " + bitmap1.getAllocationByteCount());
        mImg.setImageBitmap(bitmap1);

    }
}
