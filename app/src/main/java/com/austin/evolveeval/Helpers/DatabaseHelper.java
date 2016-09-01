package com.austin.evolveeval.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.austin.evolveeval.Models.DataBase;
import com.austin.evolveeval.R;

/**
 * @author Austin Herring
 * @version 1.0
 *
 * This class handles interactions with the Pokemon stored in the SQLite database. It hydrates the
 * table once on install.
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
    private static final String COL_8 = "EVOLUTION_INDEX";
    private static final String COL_9 = "IS_MIN_EVOLUTION";
    private static String URI_START;
    private SQLiteDatabase db;


    private static final String TABLE_NAME_2 = "preference_table";
    private static final String COL_1P = "_ID";
    private static final String COL_2P = "LAUNCH_PREF";


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
                + COL_7 + " TEXT, "
                + COL_8 + " INTEGER, "
                + COL_9 + " INTEGER)"
        );

        db.execSQL("CREATE TABLE " + TABLE_NAME_2 + "("
                + COL_1P + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_2P + " INTEGER)"
        );

        this.db = db;
        fillTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void fillTables() {
        addPreference(0);
        addGenerationOne();
    }

    private boolean addPokemon(
            String name,
            int candiesToEvolve,
            int evolutionId,
            String pokemonIcon,
            String candyIcon,
            int evolutionIndex,
            int isMinEvolution)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, candiesToEvolve);
        contentValues.put(COL_4, evolutionId);
        contentValues.put(COL_5, 0);
        contentValues.put(COL_6, pokemonIcon);
        contentValues.put(COL_7, candyIcon);
        contentValues.put(COL_8, evolutionIndex);
        contentValues.put(COL_9, isMinEvolution);
        return db.insert(TABLE_NAME, null, contentValues) != -1;
    }

    private boolean addPreference(int hideLaunchMessage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2P, hideLaunchMessage);
        return db.insert(TABLE_NAME_2, null, contentValues) != -1;
    }

    public Cursor queryPokemonByName(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + " LIKE '%" + query + "%' LIMIT 5", null);
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
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_6 + "= '" + name + "'", null);
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, cursor.getString(DataBase.ID));
            contentValues.put(COL_2, cursor.getString(DataBase.NAME));
            contentValues.put(COL_3, cursor.getInt(DataBase.CANDIES_TO_EVOLVE));
            contentValues.put(COL_4, cursor.getInt(DataBase.EVOLUTION_ID));
            contentValues.put(COL_5, inPokedex);
            contentValues.put(COL_6, cursor.getString(DataBase.POKEMON_ICON));
            contentValues.put(COL_7, cursor.getString(DataBase.CANDY_ICON));
            contentValues.put(COL_8, cursor.getInt(DataBase.EVOLUTION_INDEX));
            contentValues.put(COL_9, cursor.getInt(DataBase.MIN_EVOLUTION));
            cursor.close();
            return db.update(TABLE_NAME, contentValues, COL_6 + " = ?", new String[] { name }) > 0;
        }
        return false;
    }

    public boolean hideLaunch() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_2, null);
        cursor.moveToNext();
        boolean hide = cursor.getInt(DataBase.LAUNCH_PREF) == 1;
        cursor.close();
        return hide;
    }

    public boolean setHideLaunchPreference() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_2, null);
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            String id = cursor.getString(DataBase.ID);
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1P, id);
            contentValues.put(COL_2P, 1);
            cursor.close();
            return db.update(TABLE_NAME_2, contentValues, COL_1P + " = ?", new String[] { id }) > 0;
        }
        return false;
    }

    private void addGenerationOne() {
        addPokemon("Bulbasaur", 25, 1, URI_START + R.drawable.bulbasaur, URI_START + R.drawable.bulbasaur_candy, 0, 0);
        addPokemon("Ivysaur", 100, 1, URI_START + R.drawable.ivysaur, URI_START + R.drawable.bulbasaur_candy, 1, 1);
        addPokemon("Venusaur", -1, 1, URI_START + R.drawable.venusaur, URI_START + R.drawable.bulbasaur_candy, 2, 0);
        addPokemon("Charmander", 25, 2, URI_START + R.drawable.charmander, URI_START + R.drawable.charmander_candy, 0, 0);
        addPokemon("Charmeleon", 100, 2, URI_START + R.drawable.charmeleon, URI_START + R.drawable.charmander_candy, 1, 1);
        addPokemon("Charizard", -1, 2, URI_START + R.drawable.charizard, URI_START + R.drawable.charmander_candy, 2, 0);
        addPokemon("Squirtle", 25, 3, URI_START + R.drawable.squirtle, URI_START + R.drawable.squirtle_candy, 0, 0);
        addPokemon("Wartortle", 100, 3, URI_START + R.drawable.wartortle, URI_START + R.drawable.squirtle_candy, 1, 1);
        addPokemon("Blastoise", -1, 3, URI_START + R.drawable.blastoise, URI_START + R.drawable.squirtle_candy, 2, 0);
        addPokemon("Caterpie", 12, 4, URI_START + R.drawable.caterpie, URI_START + R.drawable.caterpie_candy, 0, 0);
        addPokemon("Metapod", 50, 4, URI_START + R.drawable.metapod, URI_START + R.drawable.caterpie_candy, 1, 1);
        addPokemon("Butterfree", -1, 4, URI_START + R.drawable.butterfree, URI_START + R.drawable.caterpie_candy, 2, 0);
        addPokemon("Weedle", 12, 5, URI_START + R.drawable.weedle, URI_START + R.drawable.weedle_candy, 0, 0);
        addPokemon("Kakuna", 50, 5, URI_START + R.drawable.kakuna, URI_START + R.drawable.weedle_candy, 1, 1);
        addPokemon("Beedrill", -1, 5, URI_START + R.drawable.beedrill, URI_START + R.drawable.weedle_candy, 2, 0);
        addPokemon("Pidgey", 12, 6, URI_START + R.drawable.pidgey, URI_START + R.drawable.pidgey_candy, 0, 0);
        addPokemon("Pidgeotto", 50, 6, URI_START + R.drawable.pidgeotto, URI_START + R.drawable.pidgey_candy, 1, 1);
        addPokemon("Pidgeot", -1, 6, URI_START + R.drawable.pidgeot, URI_START + R.drawable.pidgey_candy, 2, 0);
        addPokemon("Rattata", 25, 7, URI_START + R.drawable.rattata, URI_START + R.drawable.rattata_candy, 0, 0);
        addPokemon("Raticate", -1, 7, URI_START + R.drawable.raticate, URI_START + R.drawable.rattata_candy, 1, 1);
        addPokemon("Spearow", 50, 8, URI_START + R.drawable.spearow, URI_START + R.drawable.spearow_candy, 0, 0);
        addPokemon("Fearow", -1, 8, URI_START + R.drawable.fearow, URI_START + R.drawable.spearow_candy, 1, 1);
        addPokemon("Ekans", 50, 9, URI_START + R.drawable.ekans, URI_START + R.drawable.ekans_candy, 0, 0);
        addPokemon("Arbok", -1, 9, URI_START + R.drawable.arbok, URI_START + R.drawable.ekans_candy, 1, 1);
        addPokemon("Pikachu", 50, 10, URI_START + R.drawable.pikachu, URI_START + R.drawable.pikachu_candy, 0, 0);
        addPokemon("Raichu", -1, 10, URI_START + R.drawable.raichu, URI_START + R.drawable.pikachu_candy, 1, 1);
        addPokemon("Sandshrew", 50, 11, URI_START + R.drawable.sandshrew, URI_START + R.drawable.sandshrew_candy, 0, 0);
        addPokemon("Sandslash", -1, 11, URI_START + R.drawable.sandslash, URI_START + R.drawable.sandshrew_candy, 1, 1);
        addPokemon("Nidoran♀", 25, 12, URI_START + R.drawable.nidoranfemale, URI_START + R.drawable.nidoranfemale_candy, 0, 0);
        addPokemon("Nidorina", 100, 12, URI_START + R.drawable.nidorina, URI_START + R.drawable.nidoranfemale_candy, 1, 1);
        addPokemon("Nidoqueen", -1, 12, URI_START + R.drawable.nidoqueen, URI_START + R.drawable.nidoranfemale_candy, 2, 0);
        addPokemon("Nidoran♂", 25, 13, URI_START + R.drawable.nidoranmale, URI_START + R.drawable.nidoranmale_candy, 0, 0);
        addPokemon("Nidorino", 100, 13, URI_START + R.drawable.nidorino, URI_START + R.drawable.nidoranmale_candy, 1, 1);
        addPokemon("Nidoking", -1, 13, URI_START + R.drawable.nidoking, URI_START + R.drawable.nidoranmale_candy, 2, 0);
        addPokemon("Clefairy", 50, 14, URI_START + R.drawable.clefairy, URI_START + R.drawable.clefairy_candy, 0, 0);
        addPokemon("Clefable", -1, 14, URI_START + R.drawable.clefable, URI_START + R.drawable.clefairy_candy, 1, 1);
        addPokemon("Vulpix", 50, 15, URI_START + R.drawable.vulpix, URI_START + R.drawable.vulpix_candy, 0, 0);
        addPokemon("Ninetales", -1, 15, URI_START + R.drawable.ninetales, URI_START + R.drawable.vulpix_candy, 1, 1);
        addPokemon("Jigglypuff", 50, 16, URI_START + R.drawable.jigglypuff, URI_START + R.drawable.jigglypuff_candy, 0, 0);
        addPokemon("Wigglytuff", -1, 16, URI_START + R.drawable.wigglytuff, URI_START + R.drawable.jigglypuff_candy, 1, 1);
        addPokemon("Zubat", 50, 17, URI_START + R.drawable.zubat, URI_START + R.drawable.zubat_candy, 0, 0);
        addPokemon("Golbat", -1, 17, URI_START + R.drawable.golbat, URI_START + R.drawable.zubat_candy, 1, 1);
        addPokemon("Oddish", 25, 18, URI_START + R.drawable.oddish, URI_START + R.drawable.oddish_candy, 0, 0);
        addPokemon("Gloom", 100, 18, URI_START + R.drawable.gloom, URI_START + R.drawable.oddish_candy, 1, 1);
        addPokemon("Vileplume", -1, 18, URI_START + R.drawable.vileplume, URI_START + R.drawable.oddish_candy, 2, 0);
        addPokemon("Paras", 50, 19, URI_START + R.drawable.paras, URI_START + R.drawable.paras_candy, 0, 0);
        addPokemon("Parasect", -1, 19, URI_START + R.drawable.parasect, URI_START + R.drawable.paras_candy, 1, 1);
        addPokemon("Venonat", 50, 20, URI_START + R.drawable.venonat, URI_START + R.drawable.venonat_candy, 0, 0);
        addPokemon("Venomoth", -1, 20, URI_START + R.drawable.venomoth, URI_START + R.drawable.venonat_candy, 1, 1);
        addPokemon("Diglett", 50, 21, URI_START + R.drawable.diglett, URI_START + R.drawable.diglett_candy, 0, 0);
        addPokemon("Dugtrio", -1, 21, URI_START + R.drawable.dugtrio, URI_START + R.drawable.diglett_candy, 1, 1);
        addPokemon("Meowth", 50, 22, URI_START + R.drawable.meowth, URI_START + R.drawable.meowth_candy, 0, 0);
        addPokemon("Persian", -1, 22, URI_START + R.drawable.persian, URI_START + R.drawable.meowth_candy, 1, 1);
        addPokemon("Psyduck", 50, 23, URI_START + R.drawable.psyduck, URI_START + R.drawable.psyduck_candy, 0, 0);
        addPokemon("Golduck", -1, 23, URI_START + R.drawable.golduck, URI_START + R.drawable.psyduck_candy, 1, 1);
        addPokemon("Mankey", 50, 24, URI_START + R.drawable.mankey, URI_START + R.drawable.mankey_candy, 0, 0);
        addPokemon("Primeape", -1, 24, URI_START + R.drawable.primeape, URI_START + R.drawable.mankey_candy, 1, 1);
        addPokemon("Growlithe", 50, 25, URI_START + R.drawable.growlithe, URI_START + R.drawable.growlithe_candy, 0, 0);
        addPokemon("Arcanine", -1, 25, URI_START + R.drawable.arcanine, URI_START + R.drawable.growlithe_candy, 1, 1);
        addPokemon("Poliwag", 25, 26, URI_START + R.drawable.poliwag, URI_START + R.drawable.poliwag_candy, 0, 0);
        addPokemon("Poliwhirl", 100, 26, URI_START + R.drawable.poliwhirl, URI_START + R.drawable.poliwag_candy, 1, 1);
        addPokemon("Poliwrath", -1, 26, URI_START + R.drawable.poliwrath, URI_START + R.drawable.poliwag_candy, 2, 0);
        addPokemon("Abra", 25, 27, URI_START + R.drawable.abra, URI_START + R.drawable.abra_candy, 0, 0);
        addPokemon("Kadabra", 100, 27, URI_START + R.drawable.kadabra, URI_START + R.drawable.abra_candy, 1, 1);
        addPokemon("Alakazam", -1, 27, URI_START + R.drawable.alakazam, URI_START + R.drawable.abra_candy, 2, 0);
        addPokemon("Machop", 25, 28, URI_START + R.drawable.machop, URI_START + R.drawable.machop_candy, 0, 0);
        addPokemon("Machoke", 100, 28, URI_START + R.drawable.machoke, URI_START + R.drawable.machop_candy, 1, 1);
        addPokemon("Machamp", -1, 28, URI_START + R.drawable.machamp, URI_START + R.drawable.machop_candy, 2, 0);
        addPokemon("Bellsprout", 25, 29, URI_START + R.drawable.bellsprout, URI_START + R.drawable.bellsprout_candy, 0, 0);
        addPokemon("Weepinbell", 100, 29, URI_START + R.drawable.weepinbell, URI_START + R.drawable.bellsprout_candy, 1, 1);
        addPokemon("Victreebel", -1, 29, URI_START + R.drawable.victreebel, URI_START + R.drawable.bellsprout_candy, 2, 0);
        addPokemon("Tentacool", 50, 30, URI_START + R.drawable.tentacool, URI_START + R.drawable.tentacool_candy, 0, 0);
        addPokemon("Tentacruel", -1, 30, URI_START + R.drawable.tentacruel, URI_START + R.drawable.tentacool_candy, 1, 1);
        addPokemon("Geodude", 25, 31, URI_START + R.drawable.geodude, URI_START + R.drawable.geodude_candy, 0, 0);
        addPokemon("Graveler", 100, 31, URI_START + R.drawable.graveler, URI_START + R.drawable.geodude_candy, 1, 1);
        addPokemon("Golem", -1, 31, URI_START + R.drawable.golem, URI_START + R.drawable.geodude_candy, 2, 0);
        addPokemon("Ponyta", 50, 32, URI_START + R.drawable.ponyta, URI_START + R.drawable.ponyta_candy, 0, 0);
        addPokemon("Rapidash", -1, 32, URI_START + R.drawable.rapidash, URI_START + R.drawable.ponyta_candy, 1, 1);
        addPokemon("Slowpoke", 50, 33, URI_START + R.drawable.slowpoke, URI_START + R.drawable.slowpoke_candy, 0, 0);
        addPokemon("Slowbro", -1, 33, URI_START + R.drawable.slowbro, URI_START + R.drawable.slowpoke_candy, 1, 1);
        addPokemon("Magnemite", 50, 34, URI_START + R.drawable.magnemite, URI_START + R.drawable.magnemite_candy, 0, 0);
        addPokemon("Magneton", -1, 34, URI_START + R.drawable.magneton, URI_START + R.drawable.magnemite_candy, 1, 1);
        addPokemon("Farfetch'd", -1, 35, URI_START + R.drawable.farfetchd, URI_START + R.drawable.farfetchd_candy, 0, 0);
        addPokemon("Doduo", 50, 36, URI_START + R.drawable.doduo, URI_START + R.drawable.doduo_candy, 0, 0);
        addPokemon("Dodrio", -1, 36, URI_START + R.drawable.dodrio, URI_START + R.drawable.doduo_candy, 1, 1);
        addPokemon("Seel", 50, 37, URI_START + R.drawable.seel, URI_START + R.drawable.seel_candy, 0, 0);
        addPokemon("Dewgong", -1, 37, URI_START + R.drawable.dewgong, URI_START + R.drawable.seel_candy, 1, 1);
        addPokemon("Grimer", 50, 38, URI_START + R.drawable.grimer, URI_START + R.drawable.grimer_candy, 0, 0);
        addPokemon("Muk", -1, 38, URI_START + R.drawable.muk, URI_START + R.drawable.grimer_candy, 1, 1);
        addPokemon("Shellder", 50, 39, URI_START + R.drawable.shellder, URI_START + R.drawable.shellder_candy, 0, 0);
        addPokemon("Cloyster", -1, 39, URI_START + R.drawable.cloyster, URI_START + R.drawable.shellder_candy, 1, 1);
        addPokemon("Gastly", 25, 40, URI_START + R.drawable.gastly, URI_START + R.drawable.gastly_candy, 0, 0);
        addPokemon("Haunter", 100, 40, URI_START + R.drawable.haunter, URI_START + R.drawable.gastly_candy, 1, 1);
        addPokemon("Gengar", -1, 40, URI_START + R.drawable.gengar, URI_START + R.drawable.gastly_candy, 2, 0);
        addPokemon("Onix", -1, 41, URI_START + R.drawable.onix, URI_START + R.drawable.onix_candy, 0, 0);
        addPokemon("Drowzee", 50, 42, URI_START + R.drawable.drowzee, URI_START + R.drawable.drowzee_candy, 0, 0);
        addPokemon("Hypno", -1, 42, URI_START + R.drawable.hypno, URI_START + R.drawable.drowzee_candy, 1, 1);
        addPokemon("Krabby", 50, 43, URI_START + R.drawable.krabby, URI_START + R.drawable.krabby_candy, 0, 0);
        addPokemon("Kingler", -1, 43, URI_START + R.drawable.kingler, URI_START + R.drawable.krabby_candy, 1, 1);
        addPokemon("Voltorb", 50, 44, URI_START + R.drawable.voltorb, URI_START + R.drawable.voltorb_candy, 0, 0);
        addPokemon("Electrode", -1, 44, URI_START + R.drawable.electrode, URI_START + R.drawable.voltorb_candy, 1, 1);
        addPokemon("Exeggcute", 50, 45, URI_START + R.drawable.exeggcute, URI_START + R.drawable.exeggcute_candy, 0, 0);
        addPokemon("Exeggutor", -1, 45, URI_START + R.drawable.exeggutor, URI_START + R.drawable.exeggcute_candy, 1, 1);
        addPokemon("Cubone", 50, 46, URI_START + R.drawable.cubone, URI_START + R.drawable.cubone_candy, 0, 0);
        addPokemon("Marowak", -1, 46, URI_START + R.drawable.marowak, URI_START + R.drawable.cubone_candy, 1, 1);
        addPokemon("Hitmonlee", -1, 47, URI_START + R.drawable.hitmonlee, URI_START + R.drawable.hitmonlee_candy, 0, 0);
        addPokemon("Hitmonchan", -1, 48, URI_START + R.drawable.hitmonchan, URI_START + R.drawable.hitmonchan_candy, 0, 0);
        addPokemon("Lickitung", -1, 49, URI_START + R.drawable.lickitung, URI_START + R.drawable.lickitung_candy, 0, 0);
        addPokemon("Koffing", 50, 50, URI_START + R.drawable.koffing, URI_START + R.drawable.koffing_candy, 0, 0);
        addPokemon("Weezing", -1, 50, URI_START + R.drawable.weezing, URI_START + R.drawable.koffing_candy, 1, 1);
        addPokemon("Rhyhorn", 50, 51, URI_START + R.drawable.rhyhorn, URI_START + R.drawable.rhyhorn_candy, 0, 0);
        addPokemon("Rhydon", -1, 51, URI_START + R.drawable.rhydon, URI_START + R.drawable.rhyhorn_candy, 1, 1);
        addPokemon("Chansey", -1, 52, URI_START + R.drawable.chansey, URI_START + R.drawable.chansey_candy, 0, 0);
        addPokemon("Tangela", -1, 53, URI_START + R.drawable.tangela, URI_START + R.drawable.tangela_candy, 0, 0);
        addPokemon("Kangaskhan", -1, 54, URI_START + R.drawable.kangaskhan, URI_START + R.drawable.kangaskhan_candy, 0, 0);
        addPokemon("Horsea", 50, 55, URI_START + R.drawable.horsea, URI_START + R.drawable.horsea_candy, 0, 0);
        addPokemon("Seadra", -1, 55, URI_START + R.drawable.seadra, URI_START + R.drawable.horsea_candy, 1, 1);
        addPokemon("Goldeen", 50, 56, URI_START + R.drawable.goldeen, URI_START + R.drawable.goldeen_candy, 0, 0);
        addPokemon("Seaking", -1, 56, URI_START + R.drawable.seaking, URI_START + R.drawable.goldeen_candy, 1, 1);
        addPokemon("Staryu", 50, 57, URI_START + R.drawable.staryu, URI_START + R.drawable.staryu_candy, 0, 0);
        addPokemon("Starmie", -1, 57, URI_START + R.drawable.starmie, URI_START + R.drawable.staryu_candy, 1, 1);
        addPokemon("Mr. Mime", -1, 58, URI_START + R.drawable.mrmime, URI_START + R.drawable.mrmime_candy, 0, 0);
        addPokemon("Scyther", -1, 59, URI_START + R.drawable.scyther, URI_START + R.drawable.scyther_candy, 0, 0);
        addPokemon("Jynx", -1, 60, URI_START + R.drawable.jynx, URI_START + R.drawable.jynx_candy, 0, 0);
        addPokemon("Electabuzz", -1, 61, URI_START + R.drawable.electabuzz, URI_START + R.drawable.electabuzz_candy, 0, 0);
        addPokemon("Magmar", -1, 62, URI_START + R.drawable.magmar, URI_START + R.drawable.magmar_candy, 0, 0);
        addPokemon("Pinsir", -1, 63, URI_START + R.drawable.pinsir, URI_START + R.drawable.pinsir_candy, 0, 0);
        addPokemon("Tauros", -1, 64, URI_START + R.drawable.tauros, URI_START + R.drawable.tauros_candy, 0, 0);
        addPokemon("Magikarp", 400, 65, URI_START + R.drawable.magikarp, URI_START + R.drawable.magikarp_candy, 0, 0);
        addPokemon("Gyarados", -1, 65, URI_START + R.drawable.gyarados, URI_START + R.drawable.magikarp_candy, 1, 1);
        addPokemon("Lapras", -1, 66, URI_START + R.drawable.lapras, URI_START + R.drawable.lapras_candy, 0, 0);
        addPokemon("Ditto", -1, 67, URI_START + R.drawable.ditto, URI_START + R.drawable.ditto_candy, 0, 0);
        addPokemon("Eevee", 25, 68, URI_START + R.drawable.eevee, URI_START + R.drawable.eevee_candy, 0, 0);
        addPokemon("Vaporeon", -1, 68, URI_START + R.drawable.vaporeon, URI_START + R.drawable.eevee_candy, 1, 1);
        addPokemon("Jolteon", -1, 68, URI_START + R.drawable.jolteon, URI_START + R.drawable.eevee_candy, 2, 1);
        addPokemon("Flareon", -1, 68, URI_START + R.drawable.flareon, URI_START + R.drawable.eevee_candy, 3, 1);
        addPokemon("Porygon", -1, 69, URI_START + R.drawable.porygon, URI_START + R.drawable.porygon_candy, 0, 0);
        addPokemon("Omanyte", 50, 70, URI_START + R.drawable.omanyte, URI_START + R.drawable.omanyte_candy, 0, 0);
        addPokemon("Omastar", -1, 70, URI_START + R.drawable.omastar, URI_START + R.drawable.omanyte_candy, 1, 1);
        addPokemon("Kabuto", 50, 71, URI_START + R.drawable.kabuto, URI_START + R.drawable.kabuto_candy, 0, 0);
        addPokemon("Kabutops", -1, 71, URI_START + R.drawable.kabutops, URI_START + R.drawable.kabuto_candy, 1, 1);
        addPokemon("Aerodactyl", -1, 72, URI_START + R.drawable.aerodactyl, URI_START + R.drawable.aerodactyl_candy, 0, 0);
        addPokemon("Snorlax", -1, 73, URI_START + R.drawable.snorlax, URI_START + R.drawable.snorlax_candy, 0, 0);
        addPokemon("Articuno", -1, 74, URI_START + R.drawable.articuno, URI_START + R.drawable.articuno_candy, 0, 0);
        addPokemon("Zapdos", -1, 75, URI_START + R.drawable.zapdos, URI_START + R.drawable.zapdos_candy, 0, 0);
        addPokemon("Moltres", -1, 76, URI_START + R.drawable.moltres, URI_START + R.drawable.moltres_candy, 0, 0);
        addPokemon("Dratini", 25, 77, URI_START + R.drawable.dratini, URI_START + R.drawable.dratini_candy, 0, 0);
        addPokemon("Dragonair", 100, 77, URI_START + R.drawable.dragonair, URI_START + R.drawable.dratini_candy, 1, 1);
        addPokemon("Dragonite", -1, 77, URI_START + R.drawable.dragonite, URI_START + R.drawable.dratini_candy, 2, 0);
        addPokemon("Mewtwo", -1, 78, URI_START + R.drawable.mewtwo, URI_START + R.drawable.mewtwo_candy, 0, 0);
        addPokemon("Mew", -1, 79, URI_START + R.drawable.mew, URI_START + R.drawable.mew_candy, 0, 0);
    }
}
