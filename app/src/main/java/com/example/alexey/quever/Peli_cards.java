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
import android.widget.TextView;

import com.example.alexey.quever.Entities.Cine;
import com.example.alexey.quever.Entities.Peli;
import com.example.alexey.quever.service.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Peli_cards extends Fragment {
    RecyclerView rv;
    RecyclerView.Adapter adapter;
    LinearLayoutManager llm;
    DBService base;
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
        base = DBService.getInstance();
        ArrayList<Peli> listPeli = base.getMoviesList();

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
                    if(base.getSessionsList(item.getTitol()).size()>0){
                        fragmentHelper.mostrarSesiones(item);
                    } else base.updateSessionsOf(item);
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



    }
}
