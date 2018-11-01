package org.drulabs.localdash.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.drulabs.localdash.model.CardModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Authored by KaushalD on 8/27/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "dash.db";

    static final String TABLE_DEVICES = "devices";
    static final String COL_DEV_ID = "deviceid";
    static final String COL_DEV_IP = "ipaddress";
    static final String COL_DEV_MODEL = "devicemodel";
    static final String COL_DEV_PORT = "port";
    static final String COL_DEV_VERSION = "osversion";
    static final String COL_DEV_PLAYER = "player";
    ///////////////////////////////////////////////////////////
    static final String TABLE_CARDS_ITEM = "cardsitem";
    static final String TABLE_CARDS_ENEMY = "cardsenemy";
    static final String COL_CARD_ID = "cardid";
    static final String COL_CARD_NAME = "cardname";
    static final String COL_CARD_DESCRIPTION = "carddescription";
    static final String COL_CARD_BONUS = "cardbonus";
    static final String COL_CARD_AVAILABLE = "cardavailable";
    static final String COL_CARD_POWER = "cardpower";
    static final String COL_CARD_BADSTUFF = "cardbadstuff";
    static final String COL_CARD_REWARD = "cardreward";
    static final String COL_CARD_TYPE = "cardtype";
    static int I;

    private static final String CREATE_DEVICE_TABLE = "CREATE TABLE " + TABLE_DEVICES + "("
            + COL_DEV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_DEV_IP + " TEXT NOT NULL, "
            + COL_DEV_MODEL + " TEXT NOT NULL, "
            + COL_DEV_PORT + " INTEGER DEFAULT -1, "
            + COL_DEV_VERSION + " TEXT, "
            + COL_DEV_PLAYER + " TEXT " + ");";

    //////////////////////////////////////////////////////////////////////////
    private static final String CREATE_CARDS_ITEM_TABLE = "CREATE TABLE " + TABLE_CARDS_ITEM + "("
            + COL_CARD_ID + " INTEGER PRIMARY KEY, "
            + COL_CARD_NAME + " TEXT NOT NULL, "
            + COL_CARD_DESCRIPTION + " TEXT NOT NULL, "
            + COL_CARD_BONUS + " INTEGER, "
            + COL_CARD_TYPE + " INTEGER DEFAULT " + CardModel.ITEM + ", "
            + COL_CARD_AVAILABLE + " INTEGER DEFAULT 1 " + ");";

    private static final String CREATE_CARDS_ENEMY_TABLE = "CREATE TABLE " + TABLE_CARDS_ENEMY + "("
            + COL_CARD_ID + " INTEGER PRIMARY KEY, "
            + COL_CARD_NAME + " TEXT NOT NULL, "
            + COL_CARD_DESCRIPTION + " TEXT NOT NULL, "
            + COL_CARD_POWER + " INTEGER, "
            + COL_CARD_BADSTUFF + " INTEGER, "
            + COL_CARD_REWARD + " INTEGER, "
            + COL_CARD_TYPE + " INTEGER DEFAULT " + CardModel.ENEMY + ", "
            + COL_CARD_AVAILABLE + " INTEGER DEFAULT 1 " + ");";

    public static List<ContentValues> CREATE_CARDS_ITEM (){
        List<ContentValues> valuesList = new ArrayList<>();
        Random rand = new Random();
        for (I = 1; I < 50; I++){
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_CARD_ID, I);
            initialValues.put(COL_CARD_NAME, "Name " + I);
            initialValues.put(COL_CARD_DESCRIPTION, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris nec quam volutpat, maximus eros in, lacinia orci. Fusce facilisis euismod massa in convallis. Etiam tincidunt at odio at mollis. Curabitur pretium velit at commodo condimentum. ");
            //initialValues.put(COL_CARD_POWER, rand.nextInt(4 + 1 + 5) - 5); // -5 a 4
            initialValues.put(COL_CARD_BONUS, rand.nextInt(4 + 1 - 1) + 1); // 1 a 4
            valuesList.add(initialValues);
        }

        return valuesList;
    }

    public static List<ContentValues> CREATE_CARDS_ENEMY (){
        List<ContentValues> valuesList = new ArrayList<>();
        Random rand = new Random();

        for (; I < 65; I++){
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_CARD_ID, I);
            initialValues.put(COL_CARD_NAME, "Name " + I);
            initialValues.put(COL_CARD_DESCRIPTION, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris nec quam volutpat, maximus eros in, lacinia orci. Fusce facilisis euismod massa in convallis. Etiam tincidunt at odio at mollis. Curabitur pretium velit at commodo condimentum. ");
            initialValues.put(COL_CARD_POWER, rand.nextInt(7 + 1 - 1) + 1); // 1 a 7
            initialValues.put(COL_CARD_REWARD, rand.nextInt(2 + 1 - 0) + 0); // 0 a 2
            initialValues.put(COL_CARD_BADSTUFF, rand.nextInt(0 + 1 + 2) - 2); // -2 a 0
            valuesList.add(initialValues);
        }

        for (; I < 80; I++){
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_CARD_ID, I);
            initialValues.put(COL_CARD_NAME, "Name " + I);
            initialValues.put(COL_CARD_DESCRIPTION, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris nec quam volutpat, maximus eros in, lacinia orci. Fusce facilisis euismod massa in convallis. Etiam tincidunt at odio at mollis. Curabitur pretium velit at commodo condimentum. ");
            initialValues.put(COL_CARD_POWER, rand.nextInt( 12 + 1 - 8) + 8); // 8 a 12
            initialValues.put(COL_CARD_REWARD, rand.nextInt(3 + 1 - 0) + 0); // 0 a 3
            initialValues.put(COL_CARD_BADSTUFF, rand.nextInt(0 + 1 + 3) - 3); // -3 a 0
            valuesList.add(initialValues);
        }

        for (; I < 95; I++){
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_CARD_ID, I);
            initialValues.put(COL_CARD_NAME, "Name " + I);
            initialValues.put(COL_CARD_DESCRIPTION, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris nec quam volutpat, maximus eros in, lacinia orci. Fusce facilisis euismod massa in convallis. Etiam tincidunt at odio at mollis. Curabitur pretium velit at commodo condimentum. ");
            initialValues.put(COL_CARD_POWER, rand.nextInt( 17 + 1 - 13) + 13); // 13 a 17
            initialValues.put(COL_CARD_REWARD, rand.nextInt(3 + 1 - 1) + 1); // 1 a 3
            initialValues.put(COL_CARD_BADSTUFF, rand.nextInt(-1 + 1 + 4) - 4); // -4 a -1
            valuesList.add(initialValues);
        }

        for (; I < 100; I++){
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_CARD_ID, I);
            initialValues.put(COL_CARD_NAME, "Name " + I);
            initialValues.put(COL_CARD_DESCRIPTION, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris nec quam volutpat, maximus eros in, lacinia orci. Fusce facilisis euismod massa in convallis. Etiam tincidunt at odio at mollis. Curabitur pretium velit at commodo condimentum. ");
            initialValues.put(COL_CARD_POWER, rand.nextInt( 20 + 1 - 18) + 18); // 18 a 20
            initialValues.put(COL_CARD_REWARD, rand.nextInt(3 + 1 - 2) + 2); // 2 a 3
            initialValues.put(COL_CARD_BADSTUFF, rand.nextInt(-2 + 1 + 4) - 4); // -4 a -2
            valuesList.add(initialValues);
        }

        return valuesList;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("CREATE");
        db.execSQL(CREATE_DEVICE_TABLE);
        db.execSQL(CREATE_CARDS_ITEM_TABLE);
        db.execSQL(CREATE_CARDS_ENEMY_TABLE);

        List<ContentValues> itens = CREATE_CARDS_ITEM();
        List<ContentValues> enemies = CREATE_CARDS_ENEMY();

        db.beginTransaction();
        try {
            for(int i = 0; i < itens.size(); i++) {
                db.insert(TABLE_CARDS_ITEM, null, itens.get(i));
                db.insert(TABLE_CARDS_ENEMY, null, enemies.get(i));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //TODO handle db upgrade for existing users here
    }
}
