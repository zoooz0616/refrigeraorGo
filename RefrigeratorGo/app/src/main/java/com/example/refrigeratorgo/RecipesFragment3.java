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

//조리별
public class RecipesFragment3 extends Fragment {

    ImageView r_btn3_1, r_btn3_2, r_btn3_3,r_btn3_4;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment3_recipes,container,false);

        //오븐요리 버튼
        r_btn3_1  = (ImageView)view.findViewById(R.id.r_btn_oven);
        r_btn3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(this.getClass().getName(), "클릭성공"); //클릭이 됐는지 로그 확인
                //프레그먼트 간 이동 RecipesFragment3 -> RecipesInside
                Bundle bundle = new Bundle();
                bundle.putString("btnId", "r_btn_oven");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RecipesInside Recipes_inside = new RecipesInside();
                Recipes_inside.setArguments(bundle);

                transaction.replace(R.id.frame, Recipes_inside); //프레임 레이아웃에서 프레그먼트 1로 변경(replace)해라
                transaction.commit(); //저장해라 commit
            }
        });
        //전자레인지요리 버튼
        r_btn3_2  = (ImageView)view.findViewById(R.id.r_btn_microwave);
        r_btn3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(this.getClass().getName(), "클릭성공");
                Bundle bundle = new Bundle();
                bundle.putString("btnId", "r_btn_microwave");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RecipesInside Recipes_inside = new RecipesInside();
                Recipes_inside.setArguments(bundle);

                transaction.replace(R.id.frame, Recipes_inside);
                transaction.commit();
            }
        });
        //에어프라이기요리 버튼
        r_btn3_3  = (ImageView)view.findViewById(R.id.r_btn_air);
        r_btn3_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(this.getClass().getName(), "클릭성공");
                Bundle bundle = new Bundle();
                bundle.putString("btnId", "r_btn_air");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                RecipesInside Recipes_inside = new RecipesInside();
                Recipes_inside.setArguments(bundle);

                transaction.replace(R.id.frame, Recipes_inside);
                transaction.commit();
            }
        });
        //불없이 하는 요리 버튼
        r_btn3_4  = (ImageView)view.findViewById(R.id.r_btn_Nofire);
        r_btn3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(this.getClass().getName(), "클릭성공");
                Bundle bundle = new Bundle();
                bundle.putString("btnId", "r_btn_Nofire");
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
