package com.example.refrigeratorgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.refrigeratorgo.R;

import java.util.ArrayList;

//백버튼이 장바구니 말고는 다 적용이 안됨. 코드는 원래 되던 코드랑 동일
//백버튼으로 이동하면 단점이 있음. 어짜피 뒤로가기 버튼이 있는데 달아 말아?ㅜㅜ !!상의해보기!!

public class InsideChecklist extends AppCompatActivity {

    Button add;
    Button delete;
    EditText editText;

    Context context;
    public static SQLiteHelper sqLiteHelper;

    int position;

    /*DB
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("list"); */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_checklist);

        context = getApplicationContext();

        //db초기화
        sqLiteHelper = new SQLiteHelper(context, "Refrigerator.sqlite", null, 1);

        final String sql = "CREATE TABLE IF NOT EXISTS CHECKLIST (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR)";
        sqLiteHelper.queryData(sql);

        // 빈 데이터 리스트 생성.
        final ArrayList<String> items = new ArrayList<String>();
        // ArrayAdapter 생성. 아이템 View를 선택(multiple choice)가능하도록 만듦.
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items);

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        editText = (EditText) findViewById(R.id.editTextitem);

        //add버튼 클릭 시 아이템 추가
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String checkname = editText.getText().toString().trim();
                    sqLiteHelper.insertDataCheck(
                            checkname
                    );
                    //다시 공백으로 초기화
                    editText.setText("");

                    items.add(checkname);
                    adapter.notifyDataSetChanged();

                    sqLiteHelper.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //추가된 체크리스트DB 보여줘
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM CHECKLIST");
        items.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String checkname = cursor.getString(1);

                items.add(checkname);
            }
            adapter.notifyDataSetChanged();
        }

        //delete버튼 클릭 시 아이템 삭제
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //list에서 삭제
                SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
                int count = adapter.getCount();

                for (int i = count - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        items.remove(i);

                        //db에서 삭제
                        Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM CHECKLIST");
                        ArrayList<Integer> arrID = new ArrayList<Integer>();
                        while (c.moveToNext()) {
                            arrID.add(c.getInt(0));
                        }
                        sqLiteHelper.deleteDataCheck(arrID.get(i));
                    }
                }
                // 모든 선택 상태 초기화.
                listview.clearChoices();

                adapter.notifyDataSetChanged();
                sqLiteHelper.close();
            }
        });
    }
}
//checklist 코드 참고 : https://recipes4dev.tistory.com/59