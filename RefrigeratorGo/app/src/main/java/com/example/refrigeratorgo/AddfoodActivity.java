package com.example.refrigeratorgo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddfoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public String editValue = null;
    private String url = "https://www.cvslove.com/product/product_view.asp?pcode=";
    private ImageView home_btn, recipe_btn, camera_btn, alarm_btn, user_btn;

    TextView editCategory;
    EditText editName, editDate, editMemo;
    Button btnAdd;
    Spinner spinner;
    String[] item;
    String barcode;
    ImageView imageView;
    private ImageButton btnChoose,  scan_btn;

    private TextView contentTxt;
    private static final  int IMAGE_PICK_CODE = 1000;
    private static final  int PERMISSION_CODE = 1001;
    private static int PICK_IMAGE_REQUEST = 1;
    private static int PICK_FROM_BARCODE = 1;
    final int PICK_FROM_ALBUM = 999;
    private IntentIntegrator integrator;

    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfood);
        init();

        sqLiteHelper = new SQLiteHelper(this, "FoodDB.sqlite", null, 1); // DB 이름

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS FOOD (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, date VARCHAR, image BLOB, category VARCHAR, memo VARCHAR)");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //final Activity activity = this;
        integrator = new IntentIntegrator(this);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);  이거때문에 오류남(바코드를 qr코드로 한정)
                integrator.setCaptureActivity(CaptureActivityAnyOrientation.class); // 바코드 세로모드 지원
                integrator.setOrientationLocked(false);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false); //true로 설정하면 인식시 '삐'소리남
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        //Views

        //handle button click
        btnChoose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //check runtime permission
                ActivityCompat.requestPermissions(
                        AddfoodActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PICK_FROM_ALBUM
                );
            }
        });

        //등록 버튼
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    sqLiteHelper.insertDataFood(
                            editName.getText().toString().trim(),
                            editDate.getText().toString().trim(),
                            imageViewToByte(imageView),
                            editCategory.getText().toString().trim(),
                            editMemo.getText().toString().trim()
                    );
                    Toast.makeText(getApplicationContext(), "냉장고에 성공적으로 추가되었습니다!", Toast.LENGTH_SHORT).show();
                    editName.setText("");
                    editDate.setText("");
                    imageView.setImageResource(R.drawable.background5);
                    editCategory.setText("");
                    editMemo.setText("");
                    sqLiteHelper.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        home_btn = (ImageView) findViewById(R.id.page_home);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddfoodActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        recipe_btn = (ImageView) findViewById(R.id.recipes_book);
        recipe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddfoodActivity.this, RecipesMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        camera_btn = (ImageView) findViewById(R.id.plus_camera);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddfoodActivity.this, AddfoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        //알림
        alarm_btn = (ImageView) findViewById(R.id.alarm);
        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddfoodActivity.this, NotificationChannelCreate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });

        user_btn = (ImageView) findViewById(R.id.users);
        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddfoodActivity.this, UserSetMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //스택 관리(.setFlags)
                startActivity(intent);
            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ( (BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

   /* @Override
    protected void onResume() { // 나중에 필요할 수 있으므로 지우지 말기!
        super.onResume();
        Description task = new Description();
        task.execute();
        Description2 task2 = new Description2();
        task2.execute();
    }*/

    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PICK_FROM_ALBUM){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
            else{
                Toast.makeText(getApplicationContext(), "You dont have permission to access file locationl", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //handle result of picked image
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
        editCategory.setText(item[i]);
        if(editCategory.getText().toString().equals("선택하세요")){
            editCategory.setText("");
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView){
        editCategory.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //갤러리에서 이미지 데려오기
        if (requestCode == PICK_FROM_ALBUM) {
            if (resultCode == RESULT_OK && null != data){
                Uri uri = data.getData(); // data에서 절대경로 이미지 가져옴

                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 150, 150, true); //150로 이미지 크기 고정
                    imageView.setImageBitmap(scaled);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        //바코드스캔 후 상품 정보 파싱
        else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            barcode = result.getContents();
            if (barcode == null) {
                Toast.makeText(this, "You cancelled this scanning", Toast.LENGTH_LONG).show();
            } else {
                contentTxt.setText(barcode);
                new Description().execute(); // 상품명 파싱
                new Description2().execute(); // 상품 이미지 파싱
            }
        }
    }

    private class Description extends AsyncTask<String, String, String> { // 비코드로 인식한 상품의 이름 파싱
        String productName;
        //  private ImageView Image = null;
        @Override
        protected String doInBackground(String... params) {
            try {
                String URL = url+barcode;
                Document document = Jsoup.connect(URL).get();
                Elements mElementDataSize = ((Document) document).select("table[ID=Table4]"); //"table[ID=Table3]"
                for (Element elem : mElementDataSize) {
                    productName = elem.select("td[width=60%]").first().text(); //.toString();
                    //String img_url = elem.select("td[style=padding-left:20px;]").attr("src");
                    //Glide.with(AddfoodActivity.this).load(img_url).override(150,150).skipMemoryCache(true).into(mImageView);
                    //pdtName.add(productName);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.w("예외발생", "catch문 읽음");
            }
            return productName;
        }

        @Override
        protected void onPostExecute(String productName) {
            //doInBackground 작업이 끝나고 난뒤의 작업
            editName.setText(productName);
        }
    }

    private class Description2 extends AsyncTask<String, String, String> { // 바코드로 인식한 상품의 이미지 파싱
        String img_url;
        //  private ImageView Image = null;
        @Override
        protected String doInBackground(String... params) {
            try {
                String URL = url+barcode;
                Document document = Jsoup.connect(URL).get();
                Elements mElementDataSize = ((Document) document).select("table[ID=Table3]"); //"table[ID=Table3]"
                for (Element elem : mElementDataSize) {
                    img_url = elem.select("img").attr("src");
                    //String img_url = elem.select("td[style=padding-left:20px;]").attr("src");
                    //Glide.with(AddfoodActivity.this).load(img_url).override(150,150).skipMemoryCache(true).into(mImageView);
                    //pdtName.add(productName);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.w("예외발생", "catch문 읽음");
            }
            return img_url;
        }

        @Override
        protected void onPostExecute(String img_url) {
            Glide.with(AddfoodActivity.this).load(img_url).override(150,150).skipMemoryCache(true).into(imageView); //glide로 파싱한 이미지 적용
        }
    }

    private void init(){
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        item= new String[]{"선택하세요", "우유 / 유제품", "정육 / 계란", "수산물 / 건어물","반찬 / 냉장 / 냉동식품","채소 / 과일", "간식류","조미료 / 드레싱","음료 / 커피 / 차","기타"};
        contentTxt = (TextView)findViewById(R.id.scan_content);
        scan_btn =(ImageButton)findViewById(R.id.scan_btn);
        imageView = (ImageView) findViewById(R.id.image_view);

        editName = (EditText)findViewById(R.id.product_name);
        editDate = (EditText)findViewById(R.id.mYear);
        editCategory =(TextView)findViewById(R.id.selectedText);
        editMemo = (EditText)findViewById(R.id.edtMemo);

        btnChoose = (ImageButton)findViewById(R.id.choose_image_btn);
        btnAdd = (Button)findViewById(R.id.textbutton);
    }
}
