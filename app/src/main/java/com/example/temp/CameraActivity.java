package com.example.temp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;


public class CameraActivity extends Activity {
    private EditText title_text;
    private ImageView photo;
    private Bitmap newbm;
    private Uri outputFileUri;
    private String titleText;
    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        processViews();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File tmpFile = new File(Environment.getExternalStorageDirectory(),"image.jpg");
        outputFileUri = Uri.fromFile(tmpFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUri);

        //SQLiteActivity = new SQLiteActivity(getApplicationContext());

        if (savedInstanceState != null){
            String inputString = savedInstanceState.getString("str");
            String path_string = savedInstanceState.getString("path");
        }

        // start the image capture Intent
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onSaveInstanceState (Bundle state){
        super.onSaveInstanceState(state);

        state.putString("str", titleText);
        state.putString("path", path);
    }

    public void onSubmit(View view){
        if (view.getId() == R.id.button_add){
            titleText = title_text.getText().toString();
            path = outputFileUri.getPath();

            Intent result = getIntent();
            result.putExtra("titleText",titleText);
            result.putExtra("path",path);


            //result.putExtra("image", newbm);

            setResult(Activity.RESULT_OK, result);
        }
        finish();
    }

    private void processViews(){
        title_text = (EditText) findViewById(R.id.title_text);
        photo = (ImageView) findViewById(R.id.image);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK){
            //Bundle extras = data.getExtras();
            //Bitmap bmp = (Bitmap) extras.get("data");
            Bitmap bmp = BitmapFactory.decodeFile(outputFileUri.getPath());

            int width = bmp.getWidth();
            int height = bmp.getHeight();

            int newWidth = 960;
            int newHeight = 1280;

            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);

            newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix,true);


            photo.setImageBitmap(newbm);
        }
        else{
            return;
        }

        //super.onActivityResult(requestCode,resultCode,data);
    }

}
