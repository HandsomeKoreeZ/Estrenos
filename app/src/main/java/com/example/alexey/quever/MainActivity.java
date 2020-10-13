package com.example.alexey.quever;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alexey.quever.Entities.Peli;
import com.example.alexey.quever.database.*;
import com.example.alexey.quever.service.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
хранить в sharedPrefs последнюю дату обновления и на старте проверять.
если отличяется от текущей, стирать старые данные и обновлять базу


 */

public class MainActivity extends AppCompatActivity {
    FragmentManager fm;
    public DBService db;
    String source = "https://www.guiadelocio.com/barcelona/cine/estrenos";
    int screen = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = DBService.getInstance();
        db.setDB(new DBCines(this));                //show db for operate
        fm = getSupportFragmentManager();
        fragmentHelper.setManager(fm);                      //set fragment manager for swap fragments

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String hoy = format.format(new Date());
        SharedPreferences prefs = this.getSharedPreferences("hoy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String storedDate = prefs.getString("hoy",null);

        if(hoy.equalsIgnoreCase(storedDate)){
            //la base ya tiene dades actual
            fragmentHelper.mostrarPeli();
        }
        else {
            //actualizar la base
            db.dropDB();
            db.updateMoviesFrom(source);
        }

    }


    @Override
    public void onBackPressed(){
        if(screen==2){
            fragmentHelper.mostrarPeli();
        }
        else super.onBackPressed();
    }

}
