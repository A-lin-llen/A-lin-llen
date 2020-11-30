package com.example.widgetapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    private String[] data ={"image1","image2","image3","image3","image4","image5","image6","image7","image8","image9",
            "image10","image11","image12","image13","image14","image15","image16"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        ArrayAdapter<String> adapter =new ArrayAdapter<>(ListViewActivity.this,android.R.layout.simple_list_item_1,data);
        ListView listView=findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }
}