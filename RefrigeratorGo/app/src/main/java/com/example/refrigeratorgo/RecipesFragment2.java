package com.example.refrigeratorgo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

//상황별
public class RecipesFragment2 extends Fragment {

    ImageView r_btn2_1, r_btn2_2, r_btn2_3,r_btn2_4,r_btn2_5,r_btn2_6, r_btn2_7;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // return inflater.inflate(R.layout.fragment2_recipes,container,false);
        View view = inflater.inflate(R.layout.fragment2_recipes, null);

        //일상 버튼
        r_btn2_1  = (ImageView)view.findViewById(R.id.r_btn_daily);
        r_btn2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(this.getClass().getName(), "클릭성공"); //클릭이 됐는지 로그 확인
                //프레그먼트 간 이동 RecipesFragment2 -> RecipesInside
                Bundle bundle = new Bundle(); //영진 추가 : 프레그먼트간 데이터 이동
                bundle.putString("btnId", "r_btn_daily"); //전달할 key,value(버튼 ID값)설정
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RecipesInside Recipes_inside = new RecipesInside();
                Recipes_inside.setArguments(bundle); //Recipes_inside로 버튼ID값 전달완료

                transaction.replace(R.id.frame, Recipes_inside);
                transaction.commit(); //저장해라 commit
            }
        });
        //안주,야식 버튼
        r_btn2_2  = (ImageView)view.findViewById(R.id.r_btn_night);
        r_btn2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(this.getClass().getName(), "클릭성공");
                Bundle bundle = new Bundle();
                bundle.putString("btnId", "r_btn_night");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RecipesInside Recipes_inside = new RecipesInside();
                Recipes_inside.setArguments(bundle);

                transaction.replace(R.id.frame, Recipes_inside);
                transaction.commit();
            }
        });
        //다이어트 버튼
        r_btn2_3  = (ImageView)view.findViewById(R.id.r_btn_diet);
        r_btn2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(this.getClass().getName(), "클릭성공");
                Bundle bundle = new Bundle();
                bundle.putString("btnId", "r_btn_diet");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RecipesInside Recipes_inside = new RecipesInside();
                Recipes_inside.setArguments(bundle);

                transaction.replace(R.id.frame, Recipes_inside);
                transaction.commit();
            }
        });
        //손님상 버튼
        r_btn2_4  = (ImageView)view.findViewById(R.id.r_btn_guest);
        r_btn2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(this.getClass().getName(), "클릭성공");
                Bundle bundle = new Bundle();
                bundle.putString("btnId", "r_btn_guest");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RecipesInside Recipes_inside = new RecipesInside();
                Recipes_inside.setArguments(bundle);

                transaction.replace(R.id.frame, Recipes_inside);
                transaction.commit();
            }
        });
        //건강식 버튼
        r_btn2_5  = (ImageView)view.findViewById(R.id.r_btn_health);
        r_btn2_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(this.getClass().getName(), "클릭성공");
                Bundle bundle = new Bundle();
                bundle.putString("btnId", "r_btn_health");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RecipesInside Recipes_inside = new RecipesInside();
                Recipes_inside.setArguments(bundle);

                transaction.replace(R.id.frame, Recipes_inside);
                transaction.commit();
            }
        });
        //간편식 버튼
        r_btn2_6  = (ImageView)view.findViewById(R.id.r_btn_simple);
        r_btn2_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(this.getClass().getName(), "클릭성공");
                Bundle bundle = new Bundle();
                bundle.putString("btnId", "r_btn_simple");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RecipesInside Recipes_inside = new RecipesInside();
                Recipes_inside.setArguments(bundle);

                transaction.replace(R.id.frame, Recipes_inside);
                transaction.commit();
            }
        });
        //간식 버튼
        r_btn2_7  = (ImageView)view.findViewById(R.id.r_btn_dessert2);
        r_btn2_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(this.getClass().getName(), "클릭성공");
                Bundle bundle = new Bundle();
                bundle.putString("btnId", "r_btn_dessert2");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RecipesInside Recipes_inside = new RecipesInside();
                Recipes_inside.setArguments(bundle);

                transaction.replace(R.id.frame, Recipes_inside);
                transaction.commit();
            }
        });
        return view;
    }


}
