package com.austin.evolveeval.Models;

import com.austin.evolveeval.Helpers.DatabaseHelper;

/**
 * @author Austin Herring
 * @version 1.0
 *
 * A model that holds the static database interaction with SQLite and some final Column names.
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
    public static final int EVOLUTION_INDEX = 7;
    public static final int MIN_EVOLUTION = 8;

    public static final int LAUNCH_PREF = 1;

    public static long generateID() {
        return idAssignment++;
    }
}
