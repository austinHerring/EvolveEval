package com.austin.pokevolver.Listeners;

import android.database.Cursor;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.austin.pokevolver.Views.MainActivity;
import com.austin.pokevolver.Models.DataBase;
import com.austin.pokevolver.Models.PokemonModel;
import com.austin.pokevolver.Adapters.PokemonSuggestionAdapter;
import com.austin.pokevolver.R;

import java.util.ArrayList;

/**
 * @author Austin Herring
 * @version 1.0
 *
 * Listens to the search bar and filters the suggestion list for the user until an evolution line
 * is selected.
 */
public class PokemonQueryListener implements SearchView.OnQueryTextListener {

    private MainActivity activity;

    public PokemonQueryListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        callSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        callSearch(newText);
        return true;
    }

    public void callSearch(String query) {
        if (query.equals("")) {
            activity.listOfSuggestions.setVisibility(View.GONE);
            activity.inSuggestMode = false;
            return;
        }

        Cursor c = DataBase.db.queryPokemonByName(query);
        if (c.getCount() > 0) {
            ArrayList<PokemonModel> queriedPokemon = new ArrayList<>();
            while (c.moveToNext())
                queriedPokemon.add(new PokemonModel(c.getInt(DataBase.ID), c.getString(DataBase.NAME), c.getString(DataBase.POKEMON_ICON)));

            PokemonSuggestionAdapter adapter =
                    new PokemonSuggestionAdapter(
                            activity,
                            R.layout.layout_suggestion_row,
                            queriedPokemon,
                            activity.menu
                    );

            activity.listOfSuggestions.setAdapter(adapter);

            if (!activity.inSuggestMode) {
                activity.listOfSuggestions.setVisibility(View.VISIBLE);
                activity.inSuggestMode = true;
            }
        } else if (activity.inSuggestMode){
            activity.listOfSuggestions.setVisibility(View.GONE);
            activity.inSuggestMode = false;
        }
    }
}
