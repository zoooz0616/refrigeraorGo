package com.example.refrigeratorgo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//스크랩화면
public class RecipesFragment4 extends Fragment {

    ArrayList<ScrapData> arrayList;
    RecyclerView recyclerView;
    ScrapAdapter mainAdapter = null;
    LinearLayoutManager linearLayoutManager; //recycleView에서 사용하는 친구

    Button btn;

    Bundle bundle;

    String name, scrap, detail_url ,foodimgurl;
    View view;
    Context context;
    public static SQLiteHelper sqLiteHelper;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment4_recipes,null);
        recyclerView = (RecyclerView) view.findViewById(R.id.reView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        mainAdapter = new ScrapAdapter(arrayList);
        recyclerView.setAdapter(mainAdapter);

        context = getContext();

        //db초기화
        sqLiteHelper = new SQLiteHelper(context, "Refrigerator.sqlite", null, 1);

        String sql = "CREATE TABLE IF NOT EXISTS RECIPES (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, scrapbtn VARCHAR, detailurl VARCHAR, foodimgurl VARCHAR)";
        sqLiteHelper.queryData(sql);

        //bundle 값을 레시피DB에 추가
        try{
            Bundle bundle = getArguments();
            if(bundle != null){
                name = bundle.getString("name");
                scrap = bundle.getString("scrap");
                detail_url = bundle.getString("detail_url");
                foodimgurl = bundle.getString("food_img_url");
                Log.i(this.getClass().getName(), "보낸 argument 받음");
            }
            sqLiteHelper.insertDataRecipes(
                    name,
                    scrap,
                    detail_url,
                    foodimgurl
            );
            sqLiteHelper.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //추가된 레시피DB를 보여줘
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM RECIPES");
        arrayList.clear();
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String scrap = cursor.getString(2);
                String detail_url = cursor.getString(3);
                String foodimgurl = cursor.getString(4);
                arrayList.add(new ScrapData(name, scrap, detail_url, foodimgurl));
            }
            mainAdapter.notifyDataSetChanged();
        }

        return view;
    }
}