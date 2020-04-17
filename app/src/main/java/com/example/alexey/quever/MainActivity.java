package com.example.alexey.quever;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
хранить в sharedPrefs последнюю дату обновления и на старте проверять.
если отличяется от текущей, стирать старые данные и обновлять базу


 */

public class MainActivity extends AppCompatActivity {
    FragmentManager fm;
    Peli_cards estrenos;
    public DBCines db;
    String source = "https://www.guiadelocio.com/barcelona/cine/estrenos";
    int screen = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBCines(this);
        fm = getSupportFragmentManager();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String hoy = format.format(new Date());
        SharedPreferences prefs = this.getSharedPreferences("hoy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String storedDate = prefs.getString("hoy",null);

        if(hoy.equalsIgnoreCase(storedDate)){
            //la base ya tiene dades actual
            mostrarPeli();
        }
        else {
            //actualizar la base
            db.clearPelis();
            db.clearDates();
            new baseUpdate().execute(source);
        }

    }

    public void mostrarPeli(){
        //fill cinema list
        screen = 1;
        estrenos = new Peli_cards();
        fm.beginTransaction().replace(R.id.main_frame,estrenos).commit();
    }

    public void mostrarSesiones(Peli item){
        screen = 2;
        Sesion_cards ses = Sesion_cards.newInstance(item);
        fm.beginTransaction().replace(R.id.main_frame,ses).commit();
    }

    ////
    //parse los estrenos
    class baseUpdate extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... strings) {
            //fill Database
            try {
                //parse web page
                Document doc = Jsoup.connect(strings[0]).get();
                //get pelis
                Elements Title = doc.select("section article");
                for(Element el: Title){
                    String url = el.child(1).child(1).child(1).child(1).attr("href");
                    String titol = el.child(0).child(0).child(0).child(0).text();
                    String genero = el.getElementsByTag("li").get(0).text();
                    String director = el.getElementsByTag("li").get(2).text();
                    String img = el.getElementsByClass("img-result").get(0).getElementsByTag("img").attr("src");

                    Peli p_temp = new Peli(titol,genero,director,img,url);
                    db.addPeli(p_temp);
                    System.out.println(titol+" added");
                }
                return doc;
            } catch (IOException e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(Document result){
            super.onPostExecute(result);
            mostrarPeli();
        }
    }


    @Override
    public void onBackPressed(){
        if(screen==2){
            mostrarPeli();
        }
        else super.onBackPressed();
    }

}
