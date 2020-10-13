package com.example.alexey.quever.database;

import android.os.AsyncTask;

import com.example.alexey.quever.Entities.Peli;
import com.example.alexey.quever.database.DBCines;
import com.example.alexey.quever.service.DBService;
import com.example.alexey.quever.service.fragmentHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Movies parser
 */
public class movieBaseUpdate extends AsyncTask<String, Void, Document> {

    @Override
    protected Document doInBackground(String... strings) {
        //fill Database
        DBService db = DBService.getInstance();
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
                db.addMovie(p_temp);
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
        fragmentHelper.mostrarPeli();
    }
}