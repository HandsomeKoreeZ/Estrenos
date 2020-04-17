package com.example.alexey.quever;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Peli_cards extends Fragment {
    RecyclerView rv;
    RecyclerView.Adapter adapter;
    LinearLayoutManager llm;
    DBCines base;
    MainActivity ma;

    public Peli_cards() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ma = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.recycler,container,false);
        Context context = getContext();

        //base
        base = new DBCines(context);
        ArrayList<Peli> listPeli = base.getListPelis();

        //controladores
        rv = (RecyclerView) vista.findViewById(R.id.rv);
        llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        //los pelis
        adapter = new PeliAdapter(listPeli);
        rv.setAdapter(adapter);

        return vista;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //adapter
    public class PeliAdapter extends  RecyclerView.Adapter<PeliAdapter.PeliHolder>{
        ArrayList<Peli> dataset;

        PeliAdapter(ArrayList pelis){
            this.dataset = pelis;
        }

        @Override
        public PeliHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.peli_card,parent,false);
            PeliHolder ph = new PeliHolder(v);
            return ph;
        }

        @Override
        public void onBindViewHolder(PeliHolder holder, int position) {
            final Peli item = dataset.get(position);
            holder.director.setText(item.getDirector());
            holder.titol.setText(item.getTitol());
            holder.genero.setText(item.getGenere());
            try {
                URL url = new URL(item.getImagen());
                holder.img.setImageURL(url);
            } catch (MalformedURLException e) {

            }
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //parse data only if the database empty
                    if(base.getSesList(item.getTitol()).size()>0){
                        ma.mostrarSesiones(item);
                    } else new getSesiones().execute(item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        //holder
        public class PeliHolder extends RecyclerView.ViewHolder{
            CardView card;
            UrlImageView img;
            TextView titol;
            TextView genero;
            TextView director;

            PeliHolder(View v){
                super(v);
                card = v.findViewById(R.id.pelicardView);
                img = v.findViewById(R.id.peli_image);
                titol = v.findViewById(R.id.tvTitol);
                genero = v.findViewById(R.id.tvGenere);
                director = v.findViewById(R.id.tvDirector);

            }
        }

        //////////////////////////////////////////////////////////////////////////////
        //poner los sesiones
        class getSesiones extends AsyncTask<Peli, Void, Peli> {
            @Override
            protected Peli doInBackground(Peli... peli) {
                //fill Database
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
                        base.addCine(c);
                        base.addDate(pTemp.getTitol(),address,fechas,tiempo);
                    }
                    return peli[0];
                } catch (IOException e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(Peli p){
                super.onPostExecute(p);
                ma.mostrarSesiones(p);
            }
        }
    }
}
