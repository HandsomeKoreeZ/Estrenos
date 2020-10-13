package com.example.alexey.quever.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alexey.quever.Entities.Cine;
import com.example.alexey.quever.Entities.Data;
import com.example.alexey.quever.Entities.Peli;
import com.example.alexey.quever.Entities.Sesion;

import java.util.ArrayList;

/**
 * Created by alexey on 20.05.2018.
 */

public class DBCines extends SQLiteOpenHelper {
    private static final String dbname = "DBCines";
    private static final int version = 1;
    private static final String tablePelis = "tablePelis";
    private static final String tableCines = "tableCines";
    private static final String tableDates = "tableDates";

    public DBCines(Context context){
        super(context, dbname, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase base) {
        //construir la tabla
        String tablePeli = "CREATE TABLE " +
                tablePelis + " (" +
                "titol text primary key," +
                "genere varchar(30)," +
                "director varchar(30)," +
                "imagen text," +
                "url text)";

        String tableCine = "CREATE TABLE "+
                tableCines +" ("+
                "address text  primary key," +
                "nombre varchar(50))";

        String tableDate = "CREATE TABLE " +
                tableDates+" (" +
                "addressCine text," +
                "titolPeli text," +
                "date text," +
                "times text," +
                "FOREIGN KEY(addressCine) REFERENCES " +tableCines+"(address)," +
                "FOREIGN KEY(titolPeli) REFERENCES " +tablePelis+"(titol)," +
                "PRIMARY KEY (addressCine,titolPeli)"+
                ")";

        base.execSQL(tablePeli);
        base.execSQL(tableCine);
        base.execSQL(tableDate);

    }

    ///////////////////////////////////////////////////////////////////////
    //añadir nuevo pelicula
    public void addPeli(Peli pelicula){
        SQLiteDatabase bb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("titol",pelicula.getTitol());
        cv.put("genere",pelicula.getGenere());
        cv.put("director",pelicula.getDirector());
        cv.put("imagen",pelicula.getImagen());
        cv.put("url",pelicula.getURL());
        bb.insert(tablePelis,null,cv);
    }

    ///////////////////////////////////////////////////////////////////////
    //generar la lista de los pelis para cartas
    public ArrayList<Peli> getListPelis(){
        ArrayList<Peli> list = new ArrayList<>();
        SQLiteDatabase bb = this.getWritableDatabase();

        Cursor cursor = bb.query(tablePelis, new String[]{"titol","genere","director","imagen","url"},null,null,null,null,null);
        cursor.moveToFirst();

        Peli tempPeli;
        do{
            tempPeli = new Peli();
            tempPeli.setTitol(cursor.getString(cursor.getColumnIndex("titol")));
            tempPeli.setGenere(cursor.getString(cursor.getColumnIndex("genere")));
            tempPeli.setDirector(cursor.getString(cursor.getColumnIndex("director")));
            tempPeli.setImagen(cursor.getString(cursor.getColumnIndex("imagen")));
            tempPeli.setURL(cursor.getString(cursor.getColumnIndex("url")));
            list.add(tempPeli);
        }while(cursor.moveToNext());
        cursor.close();
        return list;
    }

    public void clearPelis(){
        System.out.println("eliminar PELIS");
        SQLiteDatabase bb = this.getWritableDatabase();
        String sql = "DELETE FROM "+tablePelis;
        bb.execSQL(sql);
    }


    ///////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////
    public void addDate(Data date){
        SQLiteDatabase bb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("addressCine",date.getAddressCine());
        cv.put("titolPeli",date.getTitolPeli());
        cv.put("date",date.getDat());
        cv.put("times",date.getTimes());
        bb.insert(tableDates,null,cv);
    }

    public ArrayList<Sesion> getSesList(String titol){
        SQLiteDatabase bb = this.getWritableDatabase();
        String sql = "select * from "+tableDates+" where titolPeli='"+titol+"'";
        String address;
        String sqlCine;
        ArrayList<Sesion> list = new ArrayList<>();
        Data d;
        Cine c;

        Cursor cursor = bb.rawQuery(sql,null);
        while(cursor.moveToNext()){
            //crea el objeto data
            String fecha = cursor.getString(cursor.getColumnIndex("date"));
            String tiempo = cursor.getString(cursor.getColumnIndex("times"));
            String adr = cursor.getString(cursor.getColumnIndex("addressCine"));
            d = new Data(adr,titol,fecha,tiempo);

            //crea el objeto Cine
            Cursor findCine = bb.query(tableCines,new String[]{"nombre"},"address=?",new String[]{adr},null,null,null);
            findCine.moveToNext();
            String nombre = findCine.getString(0);
            c = new Cine(nombre,adr);
            findCine.close();

            list.add(new Sesion(c,d));
        }

        cursor.close();
        return list;
    }

    public void clearDates(){
        System.out.println("eliminar FECHAS");
        SQLiteDatabase bb = this.getWritableDatabase();
        String sql = "DELETE FROM "+tableDates;
        bb.execSQL(sql);
    }

    ///////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////
    public void addCine(Cine cine){
        SQLiteDatabase bb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("address",cine.getAddress());
        cv.put("nombre",cine.getNombre());
        bb.insert(tableCines,null,cv);
    }

    public void clearCines(){
        System.out.println("eliminar Cines");
        SQLiteDatabase bb = this.getWritableDatabase();
        String sql = "DELETE FROM "+tableCines;
        bb.execSQL(sql);
    }



    public int maxId(SQLiteDatabase bb, String table){
        String sql = "select max(id) from "+table;
        Cursor cursor = bb.rawQuery(sql,null);
        cursor.moveToFirst();
        int resultat = cursor.getInt(0)+1;
        cursor.close();
        return resultat;
    }

    public void clearBase(){
        clearPelis();
        clearDates();
        clearCines();
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+tablePelis);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+tableCines);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+tableDates);
        onCreate(sqLiteDatabase);
    }
}
