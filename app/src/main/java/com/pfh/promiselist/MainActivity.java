package com.pfh.promiselist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AVUser.logInInBackground("admin", "admin", new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, avUser.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("TAG",avUser.toString());
                } else {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        AVObject product = new AVObject("Product");
        product.put("title", "xxx");
        product.put("description", "desc");
        product.put("price", 2);
        product.put("owner", AVUser.getCurrentUser());

        Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] info = baos.toByteArray();
        product.put("image", new AVFile("productPic", info));

        product.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "img upload success ", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "img upload fail "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Date date = new Date();
        date.toString();
    }
}
