package com.example.refrigeratorgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    Button joinButton;//회원가입 버튼
    Button log_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_id=findViewById(R.id.et_id);
        et_pass=findViewById(R.id.et_pass);
        joinButton = (Button)findViewById(R.id.joinbutton);
        log_btn= (Button)findViewById(R.id.log_btn);

        /*final Button button = new Button(this);
        button.setMinWidth(0);
        button.setMinHeight(0);
        button.setMinimumWidth(0);
        button.setMinimumHeight(0);
        button.setPadding(0, 0, 0, 0);*/

        //회원가입 버튼을 클릭했을 때 회원가입 액티비티로 전환
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼을 클릭했을 때 로그인화면 액티비티로 전환
        log_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //edittext에 입력되어있는 값을 get해옴
                final String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){//로그인에 성공한 경우
                                String userID = jsonObject.getString("userID");
                                String userPass = jsonObject.getString("userPassword");

                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();

                                Intent intent2 = getIntent();
                                String user_name = intent2.getStringExtra("EXTRA_MESSAGE");
                                String user_email = intent2.getStringExtra("EXTRA_MESSAGE1");
                                String user_phone = intent2.getStringExtra("EXTRA_MESSAGE2");
                                String user_rename = intent2.getStringExtra("EXTRA_MESSAGE3");

                                Intent intent3 = new Intent(LoginActivity.this, MainActivity.class);
                                intent3.putExtra("EXTRA_MESSAGE", user_name);
                                intent3.putExtra("EXTRA_MESSAGE1", user_email);
                                intent3.putExtra("EXTRA_MESSAGE2", user_phone);
                                intent3.putExtra("EXTRA_MESSAGE3", user_rename);
                                startActivity(intent3);
                            }
                            else {//로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}
