package com.example.alexey.quever.database;

import android.os.AsyncTask;

import com.example.alexey.quever.Entities.Cine;
import com.example.alexey.quever.Entities.Data;
import com.example.alexey.quever.Entities.Peli;
import com.example.alexey.quever.service.DBService;
import com.example.alexey.quever.service.fragmentHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class sessionBaseUpdate  extends AsyncTask<Peli, Void, Peli> {
        @Override
        protected Peli doInBackground(Peli... peli) {

            //fill Database with dates time of the sessions
            DBService db = DBService.getInstance();
            StringBuffer sb;
            Elements eTemp;

            try {
                //parse web page
                Peli pTemp = peli[0];
                Document doc = Jsoup.connect(pTemp.getURL()).get();
                //get pelis
                Elements Title = doc.select("article section .mb10");
                for(Element el: Title){
                    //cine
                    String nombreCine = el.child(0).child(0).child(0).text();
                    String address =  el.child(0).child(1).text();

                    //data
                    eTemp = el.getElementsByTag("li");
                    sb = new StringBuffer();
                    for(Element dd: el.getElementsByTag("li")){
                        sb.append(dd.child(0).text().replace('.',' ')+" | ");
                    }
                    String fechas = sb.toString();

                    //tiempo
                    eTemp = el.getElementsByClass("timeTable");
                    sb = new StringBuffer();
                    for(Element tt: el.getElementsByClass("timeTable")){
                        sb.append(tt.text().replace(")",")\n"));
                    }
                    String tiempo = sb.toString();


                    Cine c = new Cine(nombreCine,address);
                    Data d = new Data(pTemp.getTitol(),address,fechas,tiempo);
                    db.addCinema(c);
                    db.addDate(d);
                }
                return peli[0];
            } catch (IOException e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(Peli p){
            super.onPostExecute(p);
            fragmentHelper.mostrarSesiones(p);
        }
}
