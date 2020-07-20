package com.example.locker.Models;

import android.graphics.drawable.Drawable;

public class App {

    private String name;
    private String package_name;
    Drawable icon;
    int locked;

    public App(String name, Drawable icon, int locked,String package_name) {
        this.name = name;
        this.icon = icon;
        this.locked = locked;
        this.package_name  = package_name;
    }

    public String getName() {
        return name;
    }



    public Drawable getIcon() {
        return icon;
    }


    public int getLocked(){return this.locked;}
    public void setLocked(int i){this.locked = i;}

    public  String getPackage_name(){return  this.package_name;}
}
