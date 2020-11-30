package com.example.widgetapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import java.util.List;

public class ListViewActivity02 extends AppCompatActivity {

    private List<Fruit> fruitList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        initFruits();
        FruitAdapter myFruitAdapter=new FruitAdapter(ListViewActivity02.this,R.layout.fruit_item,fruitList);
        ListView listView=findViewById(R.id.list_view);
        listView.setAdapter(myFruitAdapter);
    }
    private void initFruits(){
        Fruit image1=new Fruit("apple",R.drawable.apple);
        fruitList.add(image1);
        Fruit image2=new Fruit("banana",R.drawable.banana);
        fruitList.add(image2);
        Fruit image3=new Fruit("castle",R.drawable.castle);
        fruitList.add(image3);
        Fruit image4=new Fruit("grape",R.drawable.grape);
        fruitList.add(image4);
        Fruit image5=new Fruit("lemon",R.drawable.lemon);
        fruitList.add(image5);
        Fruit image6=new Fruit("mango",R.drawable.mango);
        fruitList.add(image6);
        Fruit image7=new Fruit("orange",R.drawable.orange);
        fruitList.add(image7);
        Fruit image8=new Fruit("peach",R.drawable.peach);
        fruitList.add(image8);
        Fruit image9=new Fruit("pear",R.drawable.pear);
        fruitList.add(image9);
        Fruit image10=new Fruit("smile",R.drawable.smile);
        fruitList.add(image10);
        Fruit image11=new Fruit("strawberry",R.drawable.strawberry);
        fruitList.add(image11);
    }
}