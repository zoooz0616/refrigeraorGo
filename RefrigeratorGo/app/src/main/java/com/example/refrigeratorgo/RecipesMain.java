package com.example.refrigeratorgo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class RecipesMain extends AppCompatActivity {

    TextView btn1, btn2, btn3, btn4;
    ImageView home_btn, camera_btn, user_btn, alarm_btn;
    private EditText editText;
    private ImageView searchbtn;

    public String editValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        btn1 = (TextView) findViewById(R.id.btn_1);
        btn2 = (TextView) findViewById(R.id.btn_2);
        btn3 = (TextView) findViewById(R.id.btn_3);
        btn4 = (TextView) findViewById(R.id.btn_4);

        //FragmentTransaction의 API를 사용하여 Fragment의 추가, 제거, 변경 등의 작업을 수행
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setSelected(true);
                btn2.setSelected(false);
                btn3.setSelected(false);
                btn4.setSelected(false);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                RecipesFragment1 fragment1 = new RecipesFragment1();
                //버튼을 눌렀을 때 RE-Fr자바를 탈 수 있도록 함
                transaction.replace(R.id.frame, fragment1); //프레임 레이아웃에서 프레그먼트 1로 변경(replace)해라
                transaction.commit(); //저장해라 commit
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setSelected(false);
                btn2.setSelected(true);
                btn3.setSelected(false);
                btn4.setSelected(false);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                RecipesFragment2 fragment2 = new RecipesFragment2();
                transaction.replace(R.id.frame, fragment2);
                transaction.commit();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setSelected(false);
                btn2.setSelected(false);
                btn3.setSelected(true);
                btn4.setSelected(false);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                RecipesFragment3 fragment3 = new RecipesFragment3();
                transaction.replace(R.id.frame, fragment3);
                transaction.commit();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn1.setSelected(false);
                btn2.setSelected(false);
                btn3.setSelected(false);
                btn4.setSelected(true);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                RecipesFragment4 fragment4 = new RecipesFragment4();
                transaction.replace(R.id.frame, fragment4);
                transaction.commit();
            }
        });

        editText = (EditText) findViewById(R.id.search_bar);
        searchbtn = (ImageView) findViewById(R.id.search_button);

        try{
            searchbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editValue = editText.getText().toString();
                    Intent intent = new Intent(RecipesMain.this, RecipesWeb.class);
                    intent.putExtra("editText", editValue); //editText 값 보냄
                    startActivity(intent);
                }
            });
        }catch (NullPointerException ex){
            Log.e("에러발생","button;");
        }

        //밑아이콘 클릭시 이동
        home_btn = (ImageView) findViewById(R.id.page_home);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipesMain.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });

        camera_btn =  (ImageView) findViewById(R.id.plus_camera);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipesMain.this, AddfoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });

        //알림
        alarm_btn = (ImageView) findViewById(R.id.alarm);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipesMain.this, NotificationChannelCreate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        user_btn = (ImageView) findViewById(R.id.users);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipesMain.this, UserSetMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags) 홈버튼 클릭해서 홈으로 가면 그 전에 있던 레시피는 스택에서 사라져.
                startActivity(intent);
            }
        });
    }
}