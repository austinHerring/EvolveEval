package com.austin.pokevolver.Models;

import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by austin on 8/19/16.
 */
public class VariableModel {
    public static final int MAX_FAMILY_SIZE = 4;
    public int totalPokemon, totalEvolutions, totalExperience;
    private int candyCount, candiesToEvolve;
    private int[] pokemonCount;
    private String[] pokemonNames;
    private Uri[] pokemonIcons;
    private boolean[] inPokedex;
    private Uri candyIcon;
    private long id;

    public VariableModel(int pokemonId) {
        candiesToEvolve = Integer.MAX_VALUE;
        pokemonCount = new int[MAX_FAMILY_SIZE];
        pokemonIcons = new Uri[MAX_FAMILY_SIZE];
        pokemonNames = new String[MAX_FAMILY_SIZE];
        inPokedex = new boolean[MAX_FAMILY_SIZE];
        candyCount = 0;
        hydrateModel(pokemonId);
        id = DataBase.generateID();
    }

    private void hydrateModel(int pokemonId) {
        Cursor c = DataBase.db.queryFamilyById(pokemonId);
        if (c.getCount() > 0 && c.getCount() <= MAX_FAMILY_SIZE) {
            totalPokemon = 0;
            while (c.moveToNext()) {
                pokemonIcons[totalPokemon] = Uri.parse(c.getString(DataBase.POKEMON_ICON));
                pokemonNames[totalPokemon] = c.getString(DataBase.NAME);
                inPokedex[totalPokemon] = c.getInt(DataBase.IN_POKEDEX) == 1;
                if (totalPokemon == 0) {
                    int newCandiesToEvolve = c.getInt(DataBase.CANDIES_TO_EVOLVE);
                    if (newCandiesToEvolve < candiesToEvolve) {
                        candiesToEvolve = newCandiesToEvolve;
                    }
                    candyIcon = Uri.parse(c.getString(DataBase.CANDY_ICON));
                }

                totalPokemon++;
            }
        }
    }

    public void calculateExperience(boolean includeTransfers) {

    }

    public long getId() {
        return id;
    }

    public Uri getCandyIcon() {
        return candyIcon;
    }

    public String getPokemonName(int index) {
        return pokemonNames[index];
    }

    public Uri getPokemonIcon(int index) {
        return pokemonIcons[index];
    }

    public boolean getInPokedex(int index) {
        return inPokedex[index];
    }

    public String getCandyCountText() {
        return ((candyCount == 0) ? "" : Integer.toString(candyCount));
    }

    public int getPokemonCount(int index) {
        return pokemonCount[index];
    }

    public String getPokemonCountText(int index) {
        int count = getPokemonCount(index);
        return ((count == 0) ? "" : Integer.toString(count));
    }

    public void setInPokedex(int stage, boolean inPokedex) {
        this.inPokedex[stage] = inPokedex;
        DataBase.db.updatePokemon(pokemonNames[stage], (inPokedex) ? 1 : 0);
    }

    public void setCandyCount(int candyCount) {
        this.candyCount = candyCount;
    }

    public void setPokemonCount(int stage, int count) {
        pokemonCount[stage] = count;
    }
}
