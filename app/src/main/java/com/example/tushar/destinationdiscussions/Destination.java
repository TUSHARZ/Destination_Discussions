package com.example.tushar.destinationdiscussions;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by TUSHAR on 23-05-2017.
 */

public class Destination extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(FirebaseApp.getApps(this).isEmpty())
        {  FirebaseDatabase.getInstance().setPersistenceEnabled(true);}
        Picasso.Builder builder=new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built=builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
    }

