package com.example.refrigeratorgo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class RecipesInside extends Fragment {
    private ArrayList<RecipesData> arrayList;
    private MyAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager; //recycleView에서 사용하는 친구
    private RecipesData mainData;
    private View view;
    String url; //전역변수로 바꿈
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if(bundle != null){
            String btnId = bundle.getString("btnId"); // 전달한 key 값
            initUrl(btnId); //url 초기화
        }

        view = inflater.inflate(R.layout.recipes_inside, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
//
        mainAdapter = new MyAdapter(arrayList);
        recyclerView.setAdapter(mainAdapter);

        //AsyncTask 작동(파싱) - (파싱은 네트워크 작업을 해야하기 때문에 AsyncTask 이용)
        new Description().execute();

        return view;
    }

    public void initUrl(String btnId) {
        switch (btnId){
            case "r_btn_sidedish" :
                url = "http://www.10000recipe.com/recipe/list.html?cat4=56&order=accuracy&page=";
                break;
            case "r_btn_soup" :
                url="http://www.10000recipe.com/recipe/list.html?cat4=54&order=accuracy&page=";
                break;
            case "r_btn_rice" :
                url="http://www.10000recipe.com/recipe/list.html?cat4=52&order=accuracy&page=";
                break;

            case "r_btn_noodles" :
                url="http://www.10000recipe.com/recipe/list.html?cat4=53&order=accuracy&page=";
                break;
            case "r_btn_pizza" :
                url="http://www.10000recipe.com/recipe/list.html?cat4=65&order=accuracy&page=";
                break;
            case "r_btn_baking" :
                url="http://www.10000recipe.com/recipe/list.html?cat4=66&order=accuracy&page=";
                break;

            case "r_btn_dessert" :
                url="http://www.10000recipe.com/recipe/list.html?cat4=60&order=accuracy&page=";
                break;
            case "r_btn_jam" :
                url="http://www.10000recipe.com/recipe/list.html?cat4=58&order=accuracy&page=";
                break;
            case "r_btn_tea" :
                url="http://www.10000recipe.com/recipe/list.html?cat4=59&order=accuracy&page="; //프레그먼트1 끝!
                break;

            case "r_btn_daily" :
                url="http://www.10000recipe.com/recipe/list.html?cat2=12&order=accuracy&page=";
                break;
            case "r_btn_night" :
                url="http://www.10000recipe.com/recipe/list.html?cat2=45&order=accuracy&page=";
                break;
            case "r_btn_diet" :
                url="http://www.10000recipe.com/recipe/list.html?cat2=21&order=accuracy&niresource=%5C&page=";
                break;

            case "r_btn_guest" :
                url="http://www.10000recipe.com/recipe/list.html?cat2=13&order=accuracy&page=";
                break;
            case "r_btn_health" :
                url="http://www.10000recipe.com/recipe/list.html?cat2=43&order=accuracy&page=";
                break;
            case "r_btn_simple" :
                url="http://www.10000recipe.com/recipe/list.html?cat2=18&order=accuracy&page=";
                break;
            case "r_btn_dessert2" :
                url="http://www.10000recipe.com/recipe/list.html?cat2=17&order=accuracy&page="; //프레그먼트2 끝!
                break;

            case "r_btn_oven" :
                url="http://www.10000recipe.com/recipe/list.html?q=오븐&order=accuracy&page=";
                break;
            case "r_btn_microwave" :
                url="http://www.10000recipe.com/recipe/list.html?q=전자레인지&order=accuracy&page=";
                break;
            case "r_btn_air" :
                url="http://www.10000recipe.com/recipe/list.html?q=에어프라이&order=accuracy&page=";
                break;
            case "r_btn_Nofire" :
                url="http://www.10000recipe.com/recipe/list.html?q=불%20사용없는&order=accuracy&page="; //프레그먼트3 끝!
                break;
        }
    }

    //반찬 파싱
    //  밑에코드  https://forteleaf.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-HTML-%ED%8C%8C%EC%8B%B1%ED%95%98%EA%B8%B0-JSOUP 참고함
    private class Description extends AsyncTask<Void, Void, Void> {
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                for(int i = 1; i <= 10; i++) { //i=1부터 임을 주의!! 영진 추가
                    url += i;
                    Document document = Jsoup.connect(url).get();
                    //필요한거만 선택하여 지정(li)
                    Elements mElementDataSize = ((Document) document).select("div.col-xs-4").select("a");
                    int mElementSize = 2; //mElementDataSize.size();

                    for (Element elem : mElementDataSize) {
                        String food_name = elem.select("div.caption h4").text();
                        String img_url = elem.select("img[style=width:275px; height:275px;]").attr("src");
                        String detail_url = elem.attr("href");
                        String scrap = "스크랩";
                        arrayList.add(new RecipesData(img_url, food_name, scrap, detail_url));
                        //Log.e("/*****detailURL******/", detail_url);
                        Log.i(this.getClass().getName(), detail_url);
//                        Log.d("디버그 출력: ", "List \n" + mElementDataSize);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
                Log.w("예외발생", "catch문 읽음");
            }

            return null;
        }

        @SuppressLint("WrongConstant")
        @Override
        protected void onPostExecute(Void result) {
            //doInBackground 작업이 끝나고 난뒤의 작업

            mainAdapter.notifyDataSetChanged(); //새로고침 완료 ㅇㅇ add 나 remove 이후엔 이 거 꼭 해줘야 새로고침 정상 됨 ㅇㅇ
            mainAdapter = new MyAdapter(arrayList);
            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(mainAdapter);
        }

    }

}