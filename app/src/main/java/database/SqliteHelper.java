package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import common.UserInfo;

/**
 * Created by mac on 15/5/4.
 */
public class SqliteHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String TB_NAME = "user";

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SqliteHelper(Context context, String name) {
        this(context, name , VERSION);
    }

    public SqliteHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    // create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE IF NOT EXISTS "+
                        TB_NAME+ "("+
                        UserInfo. ID+ " integer primary key,"+
                        UserInfo. USERID+ " varchar,"+
                        UserInfo. TOKEN+ " varchar,"+
                        UserInfo. TOKENSECRET+ " varchar,"+
                        UserInfo. USERNAME+ " varchar,"+
                        UserInfo. PASSWORD + " varchar," +
                        UserInfo. USERICON+ " blob"+
                        ")"
        );
        Log. e("Database", "onCreate");
    }

    // update table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TB_NAME );
        onCreate(db);
        Log. e("Database" ,"onUpgrade" );

    }

    // update column
    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
        try{
            db.execSQL( "ALTER TABLE " +
                            TB_NAME + " CHANGE " +
                            oldColumn + " "+ newColumn +
                            " " + typeColumn
            );
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
