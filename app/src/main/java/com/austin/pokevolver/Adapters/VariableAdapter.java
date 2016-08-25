package com.austin.pokevolver.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.austin.pokevolver.Views.MainActivity;
import com.austin.pokevolver.Models.EquationModel;
import com.austin.pokevolver.Models.VariableModel;
import com.austin.pokevolver.R;

import java.util.ArrayList;

/**
 * @author Austin Herring
 * @version 1.0
 *
 * Formats the list of variables - to be used in the equation model - into an easy to use UI
 */
public class VariableAdapter extends ArrayAdapter<VariableModel> {

    private ArrayList<VariableModel> listOfvariables;
    private int mtextViewResourceId;
    private static LayoutInflater inflater = null;
    private MainActivity activity;

    public VariableAdapter(Activity activity, int textViewResourceId, ArrayList<VariableModel> variables) {
        super(activity, textViewResourceId, variables);
        listOfvariables = variables;
        this.activity = (MainActivity) activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mtextViewResourceId = textViewResourceId;
    }

    public static class ViewHolder {
        public ImageView candy_icon;
        public EditText candy_count;
        public ImageButton remove;
        public TableRow[] rows = new TableRow[VariableModel.MAX_FAMILY_SIZE];
        public ImageView[] pokemon_icons = new ImageView[VariableModel.MAX_FAMILY_SIZE];
        public TextView[] pokemon_names = new TextView[VariableModel.MAX_FAMILY_SIZE];
        public EditText[] pokemon_counts = new EditText[VariableModel.MAX_FAMILY_SIZE];
        public CheckBox[] pokedex_checkboxes = new CheckBox[VariableModel.MAX_FAMILY_SIZE];

        public void linkHolder(View v) {
            candy_icon = ((ImageView) v.findViewById(R.id.candy_icon));
            candy_count = ((EditText) v.findViewById(R.id.candy_count));
            remove = ((ImageButton) v.findViewById(R.id.remove_variable));

            rows[0] = ((TableRow) v.findViewById(R.id.row1));
            rows[1] = ((TableRow) v.findViewById(R.id.row2));
            rows[2] = ((TableRow) v.findViewById(R.id.row3));
            rows[3] = ((TableRow) v.findViewById(R.id.row4));
            pokemon_icons[0] = ((ImageView) v.findViewById(R.id.pokemon_icon1));
            pokemon_icons[1] = ((ImageView) v.findViewById(R.id.pokemon_icon2));
            pokemon_icons[2] = ((ImageView) v.findViewById(R.id.pokemon_icon3));
            pokemon_icons[3] = ((ImageView) v.findViewById(R.id.pokemon_icon4));
            pokemon_names[0] = ((TextView) v.findViewById(R.id.pokemon_name1));
            pokemon_names[1] = ((TextView) v.findViewById(R.id.pokemon_name2));
            pokemon_names[2] = ((TextView) v.findViewById(R.id.pokemon_name3));
            pokemon_names[3] = ((TextView) v.findViewById(R.id.pokemon_name4));
            pokemon_counts[0] = ((EditText) v.findViewById(R.id.pokemon_count1));
            pokemon_counts[1] = ((EditText) v.findViewById(R.id.pokemon_count2));
            pokemon_counts[2] = ((EditText) v.findViewById(R.id.pokemon_count3));
            pokemon_counts[3] = ((EditText) v.findViewById(R.id.pokemon_count4));
            pokedex_checkboxes[0] = ((CheckBox) v.findViewById(R.id.pokedex_checkbox1));
            pokedex_checkboxes[1] = ((CheckBox) v.findViewById(R.id.pokedex_checkbox2));
            pokedex_checkboxes[2] = ((CheckBox) v.findViewById(R.id.pokedex_checkbox3));
            pokedex_checkboxes[3] = ((CheckBox) v.findViewById(R.id.pokedex_checkbox4));
        }

        public void hydrateTableRows(final VariableModel variable) {
            for (int i = 0; i < variable.totalPokemon; i++) {
                rows[i].setVisibility(View.VISIBLE);
                pokemon_icons[i].setImageURI(variable.getPokemonIcon(i));
                pokemon_names[i].setText(variable.getPokemonName(i));
                pokemon_counts[i].setText(variable.getPokemonCountText(i));
                pokedex_checkboxes[i].setChecked(variable.getInPokedex(i));
                final int iListener = i;

                pokemon_counts[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        String text = pokemon_counts[iListener].getText().toString();
                        if(!hasFocus && !text.equals("")) {
                            variable.setPokemonCount(iListener, Integer.parseInt(text));
                        }
                    }
                });

                pokedex_checkboxes[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        variable.setInPokedex(iListener, ((CheckBox) v).isChecked());

                    }
                });
            }

            for (int i = variable.totalPokemon; i < VariableModel.MAX_FAMILY_SIZE; i ++) {
                rows[i].setVisibility(View.GONE);
                pokemon_icons[i].setImageURI(null);
                pokemon_names[i].setText("");
                pokemon_counts[i].setText("");
                pokedex_checkboxes[i].setChecked(false);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;
        final VariableModel variable = listOfvariables.get(position);
        try {
            if (convertView == null) {
                v = inflater.inflate(mtextViewResourceId, null);
                holder = new ViewHolder();
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            holder.linkHolder(v);

            holder.candy_icon.setImageURI(variable.getCandyIcon());
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < EquationModel.variables.size(); i ++) {
                        if (EquationModel.variables.get(i).getId() == variable.getId()) {
                            EquationModel.variables.remove(i);
                            ((BaseAdapter) activity.listOfVariables.getAdapter()).notifyDataSetChanged();
                        }
                    }
                }
            });

            holder.candy_count.setText(variable.getCandyCountText());
            holder.candy_count.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    String text = holder.candy_count.getText().toString();
                    if(!hasFocus && !text.equals("")) {
                        variable.setCandyCount(Integer.parseInt(text));
                    }
                }
            });

            holder.hydrateTableRows(variable);

        } catch (Exception e) {
            Log.e("VariableListAdapter", "Error constructing");
        }
        return v;
    }
}