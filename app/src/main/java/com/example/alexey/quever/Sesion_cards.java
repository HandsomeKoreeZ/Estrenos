package com.example.alexey.quever;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class Sesion_cards extends Fragment {
    private static final String Argument = "peli";
    private Peli peli;
    RecyclerView rv;
    RecyclerView.Adapter adapter;
    LinearLayoutManager llm;
    DBCines base;
    MainActivity ma;

    public Sesion_cards() {
        // Required empty public constructor
    }

    public static Sesion_cards newInstance(Peli p) {
        Sesion_cards fragment = new Sesion_cards();
        Bundle args = new Bundle();
        args.putSerializable(Argument, p);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ma = (MainActivity) getActivity();
        if (getArguments() != null) {
            peli = (Peli) getArguments().getSerializable(Argument);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.sesiones_recycler,container,false);
        Context context = getContext();

        //peli card
        UrlImageView img =(UrlImageView) vista.findViewById(R.id.peli_image_s);
        TextView titol = (TextView) vista.findViewById(R.id.tvTitol_s);
        TextView genero = (TextView) vista.findViewById(R.id.tvGenere_s);
        TextView director = (TextView) vista.findViewById(R.id.tvDirector_s);
        try {
            URL url = new URL(peli.getImagen());
            img.setImageURL(url);
        } catch (MalformedURLException e) {
            System.out.println("-------------------> error load img"+peli.getImagen());
        }
        titol.setText(peli.getTitol());
        genero.setText(peli.getGenere());
        director.setText(peli.getDirector());

        //base
        base = new DBCines(context);
        ArrayList<Sesion> listSes = base.getSesList(peli.getTitol());

        //controladores
        rv = (RecyclerView) vista.findViewById(R.id.rv_s);
        llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        //los sesiones
        adapter = new SesAdapter(listSes);
        rv.setAdapter(adapter);

        return vista;
    }

    public class SesAdapter extends RecyclerView.Adapter<SesAdapter.SesHolder>{
        ArrayList<Sesion> dataset;

        SesAdapter(ArrayList<Sesion> list){
            this.dataset=list;
        }

        @NonNull
        @Override
        public SesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sesion_card,parent,false);
            SesHolder sh = new SesHolder(v);
            return sh;
        }

        @Override
        public void onBindViewHolder(@NonNull SesHolder holder, int position) {
            final Sesion item = dataset.get(position);
            holder.nombreCine.setText(item.getCine().getNombre());
            holder.direccionCine.setText(item.getCine().getAddress());
            holder.fechas.setText(item.getDat().getDat());
            holder.tiempo.setText(item.getDat().getTimes());

            holder.sesCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String adr = item.getCine().getAddress().replaceAll(" ","+");
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q="+adr);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW,gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataset.size();
        }

        public class SesHolder extends RecyclerView.ViewHolder{
            CardView sesCard;
            TextView nombreCine;
            TextView direccionCine;
            TextView tiempo;
            TextView fechas;

            public SesHolder(View v) {
                super(v);
                sesCard = v.findViewById(R.id.sesCardView);
                nombreCine = v.findViewById(R.id.ses_cineNombre);
                direccionCine = v.findViewById(R.id.ses_cineAddress);
                tiempo = v.findViewById(R.id.ses_tiempos);
                fechas = v.findViewById(R.id.ses_fechas);
            }
        }
    }



}
