package com.austin.pokevolver;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.austin.pokevolver.Adapters.VariableAdapter;
import com.austin.pokevolver.Listeners.PokemonQueryListener;
import com.austin.pokevolver.Models.DataBase;
import com.austin.pokevolver.Models.EquationModel;

public class MainActivity extends AppCompatActivity{
    private EquationModel model;
    public boolean inSuggestMode;
    public ListView listOfSuggestions, listOfVariables;
    public Activity activity;
    public Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        model = new EquationModel();
        DataBase.db = new DatabaseHelper(this);
        inSuggestMode = false;
        listOfSuggestions = (ListView) findViewById(R.id.suggestions);
        listOfVariables = (ListView) findViewById(R.id.variables);
        activity = this;

        VariableAdapter variableAdapter = new VariableAdapter(this, R.layout.layout_variable_row, EquationModel.variables);
        listOfVariables.setAdapter(variableAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        this.menu = menu;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new PokemonQueryListener(this));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
