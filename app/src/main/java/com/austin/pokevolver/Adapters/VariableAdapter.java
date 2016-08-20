package com.austin.pokevolver.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.austin.pokevolver.Models.VariableModel;
import com.austin.pokevolver.R;

import java.util.ArrayList;

/**
 * Created by austin on 8/19/16.
 */
public class VariableAdapter extends ArrayAdapter<VariableModel> {

    private ArrayList<VariableModel> listOfvariables;
    private int mtextViewResourceId;
    private static LayoutInflater inflater = null;
    private Activity activity;
    private MenuItem searchView;

    public VariableAdapter(Activity activity, int textViewResourceId, ArrayList<VariableModel> variables) {
        super(activity, textViewResourceId, variables);
        this.activity = activity;
        listOfvariables = variables;
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
        final VariableModel variable = listOfvariables.get(position);
        try {
            if (convertView == null) {
                v = inflater.inflate(mtextViewResourceId, null);
                holder = new ViewHolder();
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            ((ImageView) v.findViewById(R.id.candy_icon)).setImageURI(variable.getCandyIcon());
        } catch (Exception e) {
            Log.e("VariableListAdapter", "Error constructing");
        }
        return v;
    }

    private void addTableRow() {

    }
}