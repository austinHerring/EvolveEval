package com.austin.pokevolver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.austin.pokevolver.Models.DataBase;

/**
 * Created by austin on 8/17/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PokemonModel.db";
    private static final String TABLE_NAME = "pokemon_table";
    private static final String COL_1 = "_ID";
    private static final String COL_2 = "NAME";
    private static final String COL_3 = "CANDIES_TO_EVOLVE";
    private static final String COL_4 = "EVOLUTION_ID";
    private static final String COL_5 = "IN_POKEDEX";
    private static final String COL_6 = "POKEMON_ICON";
    private static final String COL_7 = "CANDY_ICON";
    private static String URI_START;
    private SQLiteDatabase db;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        URI_START = "android.resource://"+ context.getPackageName()+ "/";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_2 + " TEXT, "
                + COL_3 + " INTEGER, "
                + COL_4 + " INTEGER, "
                + COL_5 + " INTEGER, "
                + COL_6 + " TEXT, "
                + COL_7 + " TEXT)"
        );
        this.db = db;
        fillTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void fillTable() {
        addPokemon("Bulbasaur", 25, 1, URI_START + R.drawable.bulbasaur, URI_START + R.drawable.bulbasaur_candy);
        addPokemon("Ivysaur", 100, 1, URI_START + R.drawable.ivysaur, URI_START + R.drawable.bulbasaur_candy);
        addPokemon("Venusaur", -1, 1, URI_START + R.drawable.venusaur, URI_START + R.drawable.bulbasaur_candy);
        addPokemon("Charmander", 25, 2, URI_START + R.drawable.charmander, URI_START + R.drawable.charmander_candy);
        addPokemon("Charmeleon", 100, 2, URI_START + R.drawable.charmeleon, URI_START + R.drawable.charmander_candy);
    }

    public boolean addPokemon(String name, int candiesToEvolve, int evolutionId, String pokemonIcon, String candyIcon) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, candiesToEvolve);
        contentValues.put(COL_4, evolutionId);
        contentValues.put(COL_5, 0);
        contentValues.put(COL_6, pokemonIcon);
        contentValues.put(COL_7, candyIcon);
        return db.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public Cursor queryPokemonByName(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + " LIKE '%" + query + "%'", null);
        return cursor;
    }

    public Cursor queryFamilyById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + "=" + id, null);
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            int evolutionId = cursor.getInt(DataBase.EVOLUTION_ID);
            return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_4 + "=" + evolutionId, null);
        }
        return null;
    }

    public boolean updatePokemon(String name, int inPokedex) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + "= '" + name + "'", null);
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, cursor.getString(DataBase.ID));
            contentValues.put(COL_2, name);
            contentValues.put(COL_3, cursor.getInt(DataBase.CANDIES_TO_EVOLVE));
            contentValues.put(COL_4, cursor.getInt(DataBase.EVOLUTION_ID));
            contentValues.put(COL_5, inPokedex);
            contentValues.put(COL_6, cursor.getString(DataBase.POKEMON_ICON));
            contentValues.put(COL_7, cursor.getString(DataBase.CANDY_ICON));
            cursor.close();
            return db.update(TABLE_NAME, contentValues, COL_2 + " = ?", new String[] { name }) > 0;
        }
        return false;
    }
}
