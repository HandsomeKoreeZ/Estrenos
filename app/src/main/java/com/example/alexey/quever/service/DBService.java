package com.example.alexey.quever.service;

import android.content.Context;

import com.example.alexey.quever.Entities.*;
import com.example.alexey.quever.database.*;

import java.util.ArrayList;


/**
 * A singleton helper class for interactions with Cinema Database
 */

public class DBService {
    private static DBCines db;
    private static DBService dbs;


    private DBService() {
    }

    public static DBService getInstance(){
        if (dbs == null) dbs = new DBService();
        return DBService.dbs;
    }


    public void setDB(DBCines db) {
        DBService.db = db;
    }
////////////////////////////////////////////////////////////////////////////////////

    // fill database with entities
    public void addCinema(Cine cine){
        db.addCine(cine);
    }

    public void  addMovie(Peli peli){
        db.addPeli(peli);
    }

    public void addDate(Data d){
        db.addDate(d);
    }

/////////////////////////////////////////////////////////////////////////////////////
//recollection various lists

    public ArrayList<Peli> getMoviesList(){
        return db.getListPelis();
    }

    public ArrayList<Sesion> getSessionsList(String movie){
        return db.getSesList(movie);
    }

//////////////////////////////////////////////////////////////////////////////////////
//updates

    public void updateMoviesFrom(String url){
        new movieBaseUpdate().execute(url);
    }

    public void updateSessionsOf(Peli movie){
        new sessionBaseUpdate().execute(movie);
    }


    // removes dates and movies records
    public void dropDB(){
        db.clearPelis();
        db.clearDates();
    }

    //

}
