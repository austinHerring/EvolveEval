package com.austin.pokevolver.Models;

import android.net.Uri;

/**
 * @author Austin Herring
 * @version 1.0
 *
 * Simple POJO Class for a single Pokemon
 */
public class PokemonModel {
    private String name;
    private int id;
    private Uri icon;

    public PokemonModel(int id, String name, String uri) {
        this.id = id;
        this.name = name;
        this.icon =Uri.parse(uri);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Uri getIcon() {
        return icon;
    }
}
