package com.example.refrigeratorgo;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class JoinActivity extends AppCompatActivity {
    private EditText et_phone, et_pass, et_name, et_email, et_rename;
    private Button join_btn;
    private ImageButton gallery;
    private ImageView imageView;
    private static int PICK_IMAGE_REQUEST = 1;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    final int PICK_FROM_ALBUM = 999;
    static final String TAG = "JoinActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {//액티비티 시작시 처음으로 실행되는 생명주기!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        //프로필 이미지

        imageView = (ImageView) findViewById(R.id.profile_image);
        gallery = (ImageButton) findViewById(R.id.gallery);
        //id값 찾아주기.
        et_phone=findViewById(R.id.et_phone);
        et_pass=findViewById(R.id.et_pass);
        et_name=findViewById(R.id.et_name);
        et_email=findViewById(R.id.et_email);
        et_rename=findViewById(R.id.et_rename);

        //회원가입 버튼 클릭 시 수행
        join_btn=findViewById(R.id.join_btn);
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edittext에 입력되어있는 값을 get해옴
                String userName = et_name.getText().toString();
                String userID = et_phone.getText().toString();
                String userEmail = et_email.getText().toString();
                String userReName = et_rename.getText().toString();
                String userPass = et_pass.getText().toString();

                Response.Listener<String> responseListener =  new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//회원등록에 성공한 경우
                                Toast.makeText(getApplicationContext(), "회원등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);

                                String user_name = et_name.getText().toString();
                                String user_email = et_email.getText().toString();
                                String user_phone = et_phone.getText().toString();
                                String user_rename = et_rename.getText().toString();
                                intent.putExtra("EXTRA_MESSAGE", user_name);
                                intent.putExtra("EXTRA_MESSAGE1", user_email);
                                intent.putExtra("EXTRA_MESSAGE2", user_phone);
                                intent.putExtra("EXTRA_MESSAGE3", user_rename);

                                startActivity(intent);
                            }
                            else {//회원등록에 실패한 경우
                                Toast.makeText(getApplicationContext(), "회원등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                //서버로 volley이용해 요청.
                RegisterRequest registerRequest = new RegisterRequest(userName, userID,  userEmail, userReName, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
                queue.add(registerRequest);


            }
        });

        gallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //check runtime permission
                ActivityCompat.requestPermissions(
                        JoinActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PICK_FROM_ALBUM
                );
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PICK_FROM_ALBUM){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
            else{
                Toast.makeText(getApplicationContext(), "You dont have permission to access file locationl", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void loadImagefromGallery(View view) {
        //Intent 생성
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //ACTION_PIC과 차이점?
        intent.setType("image/*"); //이미지만 보이게
        //Intent 시작 - 갤러리앱을 열어서 원하는 이미지를 선택할 수 있다.
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //갤러리에서 이미지 데려오기
        if (requestCode == PICK_FROM_ALBUM) {
            if (resultCode == RESULT_OK && null != data){
                Uri uri = data.getData(); // data에서 절대경로 이미지 가져옴

                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 150, 150, true); //150로 이미지 크기 고정
                    imageView.setImageBitmap(scaled);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    //이미지 선택작업을 후의 결과 처리
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            //set imag to image view
            imageView.setImageURI(data.getData());
        }
        try {
            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                //data에서 절대경로로 이미지를 가져옴
                Uri uri = data.getData();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                imageView.setImageBitmap(scaled);

            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }*/
}
