package com.austin.pokevolver.Models;

import com.austin.pokevolver.DatabaseHelper;

/**
 * Created by austin on 8/19/16.
 */
public class DataBase {
    private static long idAssignment = 1;
    public static DatabaseHelper db;
    public static final int ID = 0;
    public static final int NAME = 1;
    public static final int CANDIES_TO_EVOLVE = 2;
    public static final int EVOLUTION_ID = 3;
    public static final int IN_POKEDEX = 4;
    public static final int POKEMON_ICON = 5;
    public static final int CANDY_ICON = 6;

    public static long generateID() {
        return idAssignment++;
    }
}
