package com.example.refrigeratorgo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class InsideSnack extends AppCompatActivity {

    GridView gridView;
    ArrayList<Food> list;
    FoodListAdapter adapter = null;
    private String name, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_snack);

        gridView = (GridView) findViewById(R.id.grid);
        list = new ArrayList<>();
        adapter = new FoodListAdapter(this, R.layout.food_items, list);
        gridView.setAdapter(adapter);

        try {
            // get all data from sqlite
            Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM FOOD");
            list.clear();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                name = cursor.getString(1);
                date = cursor.getString(2);
                byte[] image = cursor.getBlob(3);
                String category = cursor.getString(4);
                String memo = cursor.getString(5);

                list.add(new Food(name, date, image, category, memo, id));
            }
            adapter.notifyDataSetChanged();
            Log.i("List: ", String.valueOf(list));

            //인텐트 보내
            Intent intent = new Intent(InsideSnack.this, NotificationChannelCreate.class);
            intent.putExtra("foodName", name);
            intent.putExtra("foodDate", date);

        }catch (Exception e){
            e.printStackTrace();
        }

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"수정", "삭제"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(InsideSnack.this);

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM FOOD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            // show dialog update at here
                            showDialogUpdate(InsideSnack.this, arrID.get(position));

                        } else {
                            // delete
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT id FROM FOOD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }
    ImageView imageViewFood;
    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_food_activity);
        dialog.setTitle("수정");

        imageViewFood = (ImageView) dialog.findViewById(R.id.imageView);
        final EditText edtName = (EditText) dialog.findViewById(R.id.name);
        final EditText edtDate = (EditText) dialog.findViewById(R.id.dDate);
        final TextView edtCategory = (TextView) dialog.findViewById(R.id.category);
        final EditText edtMemo = (EditText) dialog.findViewById(R.id.dMemo);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        InsideSnack.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    MainActivity.sqLiteHelper.updateDataFood(
                            edtName.getText().toString().trim(),
                            edtDate.getText().toString().trim(),
                            AddfoodActivity.imageViewToByte(imageViewFood),
                            edtCategory.getText().toString().trim(),
                            edtMemo.getText().toString().trim(),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "수정 완료했습니다.",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
                updateFoodList();
            }
        });
    }

    private void showDialogDelete(final int idFood){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(InsideSnack.this);

        dialogDelete.setTitle("경고!!");
        dialogDelete.setMessage("정말로 삭제하시겠습니까?");
        dialogDelete.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Log.i("idFood: ", String.valueOf(idFood));
                    MainActivity.sqLiteHelper.deleteDataFood(idFood);
                    Toast.makeText(getApplicationContext(), "냉장고에서 완전히 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateFoodList();
            }
        });

        dialogDelete.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateFoodList(){
        // get all data from sqlite
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM FOOD");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            String category = cursor.getString(4);
            String memo = cursor.getString(5);

            list.add(new Food(name, price, image, category, memo, id));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewFood.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
