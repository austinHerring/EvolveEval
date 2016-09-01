package com.austin.evolveeval.Models;

import android.database.Cursor;
import android.net.Uri;

/**
 * @author Austin Herring
 * @version 1.0
 *
 * A model that holds a single 'variable.' A variable is a given Pokemon evolution line and the
 * numbers that go along with it. Such as the number of occurrences of each pokemon and candies to
 * evolve. It plays a larger role when calculating the total deliverables.
 * NOTE: The arrays are sorted by the evolution line and how many candies are needed to evolve.
 */
public class VariableModel {
    public static final int MAX_FAMILY_SIZE = 4;
    public int totalPokemon;
    private int candyCount, candiesToEvolve, minEvolutionCount;
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
        minEvolutionCount = 0;
        hydrateModel(pokemonId);
        id = DataBase.generateID();
    }

    private void hydrateModel(int pokemonId) {
        Cursor c = DataBase.db.queryFamilyById(pokemonId);
        if (c.getCount() > 0 && c.getCount() <= MAX_FAMILY_SIZE) {
            totalPokemon = 0;
            while (c.moveToNext()) {
                int index = c.getInt(DataBase.EVOLUTION_INDEX);
                pokemonIcons[index] = Uri.parse(c.getString(DataBase.POKEMON_ICON));
                pokemonNames[index] = c.getString(DataBase.NAME);
                inPokedex[index] = c.getInt(DataBase.IN_POKEDEX) == 1;

                if (c.getInt(DataBase.MIN_EVOLUTION) == 1) {
                    minEvolutionCount++;
                }

                if (index == 0) {
                    candiesToEvolve = c.getInt(DataBase.CANDIES_TO_EVOLVE);
                    candyIcon = Uri.parse(c.getString(DataBase.CANDY_ICON));
                }

                totalPokemon++;
            }
        }
    }

    public int[] calculateOldAndNewEvolutions(int adjustment) {
        int[] oldNewEvolutionCount = new int[]{0,0};
        if (minEvolutionCount == 0) {
            return oldNewEvolutionCount;
        }

        int basicCount = pokemonCount[0];
        int newEvolutions = 0;

        for (int i = 1; i <= minEvolutionCount; i ++) {
            newEvolutions += (inPokedex[i]) ? 0 : 1;
        }

        int obtainedCandies = candyCount + pokemonCount[1] + pokemonCount[2] + pokemonCount[3];
        int potentialEvolutions = (obtainedCandies - adjustment) / (candiesToEvolve - adjustment);

        //Add candies from extra basic pokemon only if transferring
        if (potentialEvolutions < basicCount && adjustment == 2) {
            int extraEvolutions = (basicCount - 1 + obtainedCandies - potentialEvolutions * candiesToEvolve) / (candiesToEvolve);
            potentialEvolutions += extraEvolutions;
        }

        if (potentialEvolutions <= basicCount) {
            if (newEvolutions > potentialEvolutions) {
                oldNewEvolutionCount[0] = 0;
                oldNewEvolutionCount[1] = potentialEvolutions;
            } else {
                oldNewEvolutionCount[0] = potentialEvolutions - newEvolutions;
                oldNewEvolutionCount[1] = newEvolutions;
            }
        } else {
            if (newEvolutions > basicCount) {
                oldNewEvolutionCount[0] = 0;
                oldNewEvolutionCount[1] = basicCount;

            } else {
                oldNewEvolutionCount[0] = pokemonCount[0] - newEvolutions;
                oldNewEvolutionCount[1] = newEvolutions;
            }
        }

        return oldNewEvolutionCount;
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

    public String getPokemonCountText(int index) {
        return ((pokemonCount[index] == 0) ? "" : Integer.toString(pokemonCount[index]));
    }

    public void setInPokedex(int stage, boolean inPokedex) {
        this.inPokedex[stage] = inPokedex;
        DataBase.db.updatePokemon(pokemonIcons[stage].toString(), (inPokedex) ? 1 : 0);
    }

    public void setCandyCount(int candyCount) {
        this.candyCount = candyCount;
    }

    public void setPokemonCount(int stage, int count) {
        pokemonCount[stage] = count;
    }
}
