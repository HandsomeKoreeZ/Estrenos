package com.example.alexey.quever.service;

import android.support.v4.app.FragmentManager;

import com.example.alexey.quever.Entities.Peli;
import com.example.alexey.quever.Peli_cards;
import com.example.alexey.quever.R;
import com.example.alexey.quever.Sesion_cards;

public class fragmentHelper {
    private static int screen;
    private static FragmentManager fm;

    public fragmentHelper() {
    }

    public static void setManager(FragmentManager fm){
        fragmentHelper.fm = fm;
    }


    public static void mostrarPeli(){
        //fill cinema list
        screen = 1;
        Peli_cards estrenos = new Peli_cards();
        fm.beginTransaction().replace(R.id.main_frame, estrenos).commit();
    }

    public static void mostrarSesiones(Peli item){
        screen = 2;
        Sesion_cards ses = Sesion_cards.newInstance(item);
        fm.beginTransaction().replace(R.id.main_frame,ses).commit();
    }

    public static int getScreen(){
        return screen;
    }
}
