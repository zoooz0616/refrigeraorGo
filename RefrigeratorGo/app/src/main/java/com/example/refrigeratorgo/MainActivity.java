package com.example.refrigeratorgo;

import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static SQLiteHelper sqLiteHelper, sqLiteHelper2;
    private Button checklist, milk, egg, seafood, dressing, vegetable, snack, sidedish,
                    drink, others, garbage;
    //   //밑아이콘
    private ImageView home_btn, recipe_btn, camera_btn, group_btn, user_btn;
    private ImageView alarm;
    private String name, date;
    private byte[] image;

    //   oncreate는 이 어플이 시작될 때 이 밑에 있는 내용이 모두 실행되라~~
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        // 받은 냉장고 이름으로 설정
        Intent intent4 = getIntent();
        String user_rename = intent4.getStringExtra("EXTRA_MESSAGE3");

        TextView textView =findViewById(R.id.title);
        if(user_rename!= null){
            textView.setText(user_rename);
        }
        else {
            textView.setText("나의 냉장고");
        }

        sqLiteHelper = new SQLiteHelper(this, "Refrigerator.sqlite", null, 1); //데이터베이스이름
        sqLiteHelper2 = new SQLiteHelper(this, "FoodDB.sqlite", null, 1); // DB 이름

        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsideChecklist.class);
                startActivity(intent); //액티비티 이동 구문
            }
        });
        milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsideMilk.class);
                startActivity(intent); //액티비티 이동 구문
            }
        });

        egg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsideEgg.class);
                startActivity(intent); //액티비티 이동 구문
            }
        });

        seafood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsideSeafood.class);
                startActivity(intent); //액티비티 이동 구문
            }
        });

        sidedish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsideSidedish.class);
                startActivity(intent); //액티비티 이동 구문
            }
        });
        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsideVegetable.class);
                startActivity(intent); //액티비티 이동 구문
            }
        });

        dressing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsideRelish.class);
                startActivity(intent); //액티비티 이동 구문
            }
        });

        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsideDrink.class);
                startActivity(intent); //액티비티 이동 구문
            }
        });

        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsideSnack.class);
                startActivity(intent); //액티비티 이동 구문
            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsideOthers.class);
                startActivity(intent); //액티비티 이동 구문
            }
        });

        recipe_btn =  (ImageView) findViewById(R.id.recipes_book);
        recipe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecipesMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        camera_btn =  (ImageView) findViewById(R.id.plus_camera);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddfoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        user_btn =  (ImageView) findViewById(R.id.users);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = getIntent();
                String user_name = intent4.getStringExtra("EXTRA_MESSAGE");
                String user_email = intent4.getStringExtra("EXTRA_MESSAGE1");
                String user_phone = intent4.getStringExtra("EXTRA_MESSAGE2");
                String user_rename = intent4.getStringExtra("EXTRA_MESSAGE3");
                Intent intent5 = new Intent(MainActivity.this, UserSetMainActivity.class);

                intent5.putExtra("EXTRA_MESSAGE", user_name);
                intent5.putExtra("EXTRA_MESSAGE1", user_email);
                intent5.putExtra("EXTRA_MESSAGE2", user_phone);
                intent5.putExtra("EXTRA_MESSAGE3", user_rename);
                intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent5);
            }
        });


        //알림
        alarm = (ImageView) findViewById(R.id.alarm);
        alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotificationChannelCreate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                try {
                    // get all data from sqlite
                    Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM FOOD");

                    int count = 0; //인덱스번호

                    ArrayList<String> arrayName = new ArrayList<>(); //이름배열
                    ArrayList<String> arrayDate = new ArrayList<>(); //유통기한배열
                    ArrayList<byte[]> arrayImage = new ArrayList<>(); //유통기한배열

                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(0);
                        name = cursor.getString(1);
                        date = cursor.getString(2);
                        image = cursor.getBlob(3);
                        String category = cursor.getString(4);
                        String memo = cursor.getString(5);

                        arrayName.add(count, name);
                        arrayDate.add(count, date);
//                        arrayImage.add(count, image);

                        count++; //인덱스번호++

                    }
                    intent.putExtra("foodName", arrayName);
                    intent.putExtra("foodDate", arrayDate);

                }catch (Exception e){
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });


    }

    private void init(){
        checklist = (Button)findViewById(R.id.checklist);
        milk = (Button) findViewById(R.id.milk);
        egg = (Button) findViewById(R.id.egg);
        seafood = (Button) findViewById(R.id.seafood);
        dressing = (Button) findViewById(R.id.dressing);
        vegetable = (Button) findViewById(R.id.vegetable);
        snack = (Button) findViewById(R.id.snack);
        sidedish = (Button) findViewById(R.id.sidedish);
        drink = (Button) findViewById(R.id.drink);
        others = (Button) findViewById(R.id.others);
    }
}

