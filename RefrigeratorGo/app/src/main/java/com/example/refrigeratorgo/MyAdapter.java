package com.example.refrigeratorgo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;

//길게 눌렀을 때 리스트뷰를 삭제
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {
    //리스트에 담을 배열
    private ArrayList<RecipesData> arrayList;

    public MyAdapter(ArrayList<RecipesData> arrayList) {
        this.arrayList = arrayList;
    }

    String curName, btn, detail_url, img_url;
    RecipesData data;
    String finalurl;
    //액티비티에 onCreate랑 비슷하게 리사이클뷰가 생성될 떄 ~ 임
    @NonNull
    @Override
    public MyAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }
    //실제 추가될 때
    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.CustomViewHolder holder, final int position) {
//        holder.iv_profile.setImageURI(arrayList.get(position).getIv_profile());//.setImageResource(arrayList.get(position).getIv_profile()); //이미지뷰를 생성하는 걸 가져옴.
        holder.foodName.setText(arrayList.get(position).getFoodName());
        Glide.with(holder.itemView).load(arrayList.get(position).getImgUrl()).override(650,650).skipMemoryCache(true).into(holder.foodImg);

        holder.scrap.setText(arrayList.get(position).getScrap());

        holder.itemView.setTag(position);

        holder.scrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                curName = holder.foodName.getText().toString();
                btn = "스크랩 취소";
                detail_url = arrayList.get(position).getDetail_url();
                img_url = arrayList.get(position).getImgUrl();

                bundle.putString("name", curName);
                bundle.putString("scrap", btn);
                bundle.putString("detail_url", detail_url);
                bundle.putString("food_img_url", img_url);

                Log.i(this.getClass().getName(), "클릭성공");
                Log.i(String.valueOf(bundle), "클릭성공");


                RecipesFragment4 fragment4 = new RecipesFragment4();
                fragment4.setArguments(bundle);
                ((FragmentActivity)view.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment4).commit();
                Log.i(this.getClass().getName(), "fragment 이동 성공");
            }
        });
        holder.foodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detail_url = arrayList.get(position).getDetail_url();
                finalurl = "http://www.10000recipe.com"+detail_url;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalurl));
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() { return (null!=arrayList ? arrayList.size() : 0); }

    public  void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position); //notifiny 는 새로고침 같은 ㅇㅇ 거임
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView foodImg;
        protected TextView foodName;
        protected TextView scrap;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.foodImg = (ImageView)itemView.findViewById(R.id.img_profile);
            this.foodName = (TextView)itemView.findViewById(R.id.tv_text);
            this.scrap = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}

