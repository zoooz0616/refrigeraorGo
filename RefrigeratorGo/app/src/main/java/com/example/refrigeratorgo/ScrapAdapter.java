package com.example.refrigeratorgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//길게 눌렀을 때 리스트뷰를 삭제
public class ScrapAdapter extends RecyclerView.Adapter<ScrapAdapter.CustomViewHolder> {
    //리스트에 담을 배열
    private ArrayList<ScrapData> arrayList;

    public ScrapAdapter(ArrayList<ScrapData> arrayList) {
        this.arrayList = arrayList;
    }

    String fixurl = "http://www.10000recipe.com";

    public SQLiteHelper sqLiteHelper;

    //전역에서 사용할 수 있도록 전역변수 지정
    public static int id;

    //액티비티에 onCreate랑 비슷하게 리사이클뷰가 생성될 떄 ~ 임
    @NonNull
    @Override
    public ScrapAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scrap_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }
    //실제 추가될 때
    @Override
    public void onBindViewHolder(@NonNull final ScrapAdapter.CustomViewHolder holder, final int position) {
//        holder.iv_profile.setImageURI(arrayList.get(position).getIv_profile());//.setImageResource(arrayList.get(position).getIv_profile()); //이미지뷰를 생성하는 걸 가져옴.
        holder.foodName.setText(arrayList.get(position).getFoodName());
        holder.scrap.setText(arrayList.get(position).getScrap());
        String detail_url = arrayList.get(position).getDetail_url();
        final String finalurl = fixurl+detail_url;

        Glide.with(holder.itemView).load(arrayList.get(position).getImgUrl()).override(650,650).skipMemoryCache(true).into(holder.foodImage);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalurl));
                view.getContext().startActivity(intent);
            }
        });

        holder.scrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM RECIPES");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (c.moveToNext()){
                    arrID.add(c.getInt(0));
                }
                showDelete(arrID.get(position));
                remove(holder.getAdapterPosition());
            }
        });
    }

    private void showDelete(Integer integer) {
        try {
            MainActivity.sqLiteHelper.deleteDataRecipes(integer);
        } catch (Exception e){
            Log.e("error", e.getMessage());
        }
    }

    @Override
    public int getItemCount() { return (null!=arrayList ? arrayList.size() : 0); }

    public  void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView foodName;
        protected ImageView foodImage;
        protected TextView scrap;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.foodName = (TextView)itemView.findViewById(R.id.tv_text);
            this.foodImage = (ImageView)itemView.findViewById(R.id.foodimg);
            this.scrap = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}