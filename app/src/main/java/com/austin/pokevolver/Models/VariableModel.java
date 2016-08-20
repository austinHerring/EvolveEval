package com.austin.pokevolver.Models;

import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by austin on 8/19/16.
 */
public class VariableModel {
    public int totalPokemon, totalEvolutions, totalExperience;
    private int id, candyCount, candiesToEvolve;
    private int[] pokemonCount;
    private String[] pokemonNames;
    private Uri[] pokemonIcons;
    private boolean inPokedex;
    private Uri candyIcon;

    public VariableModel(int pokemonId) {
        candiesToEvolve = -1;
        pokemonCount = new int[3];
        pokemonIcons = new Uri[3];
        pokemonNames = new String[3];
        hydrateModel(pokemonId);
        calculateExperience();
    }

    private void hydrateModel(int pokemonId) {
        Cursor c = DataBase.db.queryFamilyById(pokemonId);
        if (c.getCount() > 0 && c.getCount() < 4) {
            totalPokemon = 0;
            while (c.moveToNext()) {
                pokemonIcons[totalPokemon] = Uri.parse(c.getString(DataBase.POKEMON_ICON));
                pokemonNames[totalPokemon] = c.getString(DataBase.NAME);
                if (totalPokemon == 1) {
                    inPokedex = c.getInt(DataBase.IN_POKEDEX) == 1;
                } else if (totalPokemon == 0) {
                    candyCount = c.getInt(DataBase.CANDIES_TO_EVOLVE);
                    candyIcon = Uri.parse(c.getString(DataBase.CANDY_ICON));
                    id = c.getInt(DataBase.ID);
                }

                totalPokemon++;
            }
        }
    }

    public void calculateExperience() {

    }

    public Uri getCandyIcon() {
        return candyIcon;
    }

    public String[] getPokemonNames() {
        return pokemonNames;
    }

    public Uri[] getPokemonIcons() {
        return pokemonIcons;
    }

    public void setInPokedex(boolean inPokedex) {
        this.inPokedex = inPokedex;
        DataBase.db.updatePokemon(pokemonNames[1], (inPokedex) ? 1 : 0);
    }

    public void setCandyCount(int candyCount) {
        this.candyCount = candyCount;
    }

    public void setPokemonCount(int stage, int count) {
        pokemonCount[stage] = count;
    }
}
