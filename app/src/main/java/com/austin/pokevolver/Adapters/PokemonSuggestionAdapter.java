package com.austin.pokevolver.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.austin.pokevolver.MainActivity;
import com.austin.pokevolver.Models.EquationModel;
import com.austin.pokevolver.Models.PokemonModel;
import com.austin.pokevolver.R;
import com.austin.pokevolver.Models.VariableModel;

import java.util.ArrayList;

/**
 * Created by austin on 8/18/16.
 */
public class PokemonSuggestionAdapter extends ArrayAdapter<PokemonModel> {

    private ArrayList<PokemonModel> listOfSuggestions;
    private int mtextViewResourceId;
    private static LayoutInflater inflater = null;
    private MainActivity activity;
    private MenuItem searchView;

    public PokemonSuggestionAdapter(Activity activity, int textViewResourceId, ArrayList<PokemonModel> suggestions, Menu menu) {
        super(activity, textViewResourceId, suggestions);
        this.activity = (MainActivity) activity;
        this.searchView = menu.findItem(R.id.search);
        listOfSuggestions = suggestions;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mtextViewResourceId = textViewResourceId;
    }

    public static class ViewHolder {
        public TextView display_info;
        public ImageView display_icon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;
        final PokemonModel pokemon = listOfSuggestions.get(position);
        try {
            if (convertView == null) {
                v = inflater.inflate(mtextViewResourceId, null);
                holder = new ViewHolder();
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            holder.display_info = (TextView) v.findViewById(R.id.pokemonName);
            holder.display_info.setText(pokemon.getName());

            holder.display_icon = (ImageView) v.findViewById(R.id.pokemonIcon);
            holder.display_icon.setImageURI(pokemon.getIcon());
        } catch (Exception e) {
            Log.e("PokemonListAdapter", "Error constructing");
        }
        v.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activity.findViewById(R.id.suggestions).setVisibility(View.GONE);
                searchView.collapseActionView();
                EquationModel.addVariable(new VariableModel(pokemon.getId()));
                ((BaseAdapter) activity.listOfVariables.getAdapter()).notifyDataSetChanged();
            }
        });
        return v;
    }
}
