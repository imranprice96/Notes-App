package pricei.massey.com.stickynotes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/** SQLite database manager to store the note entries*/


public class DatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "note.db";
    private static final String TABLE_NOTES = "notes";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";

    /** Constructor*/
    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }


    /** Create the table*/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_NOTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TITLE
                + " TEXT," + COLUMN_DESCRIPTION + " INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);

    }

    /** simple upgrade implementation*/
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(sqLiteDatabase);
    }

    /** insert new row into the database*/
    public void addNote(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NOTES,null,values);
        db.close();
    }

    /**Used for the search function
     * searches for and returns an array list of notes matching the query
     * search term has to match the title of the note exactly */
    public ArrayList<Note> findNote(String title,Context context){
        String query = "Select * FROM " + TABLE_NOTES + " WHERE " + COLUMN_TITLE + " = \"" + title + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        ArrayList<Note> notes = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String titleAdd = cursor.getString(1);
                String desAdd = cursor.getString(2);
                notes.add(new Note(id,titleAdd,desAdd));
            }while (cursor.moveToNext());
        }else{
            Toast.makeText(context, "no matches", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
        return notes;
    }

    /** returns the cursor the for main activity to load the database on creation*/
    public Cursor Query(String title){
        String query = "Select * FROM " + TABLE_NOTES + " WHERE " + COLUMN_TITLE + " = \"" + title + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }


    /** function to  search for and delete member from the database*/
    public boolean deleteNote(String title){

        boolean result = false;

        String query = "Select * FROM " + TABLE_NOTES + " WHERE " + COLUMN_TITLE + " =  \"" + title + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        Note note;

        if(cursor.moveToFirst()){
            note = new Note(Integer.parseInt(
                    cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2));
            db.delete(TABLE_NOTES, COLUMN_ID + " = ?", new String[]{String.valueOf(note.getID())}); /** delete*/
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

    /** Updates the database when a note is edited*/
    public boolean updateNote(String i , ContentValues values){
        boolean result = false;
        String query = "Select * FROM " + TABLE_NOTES + " WHERE " + COLUMN_ID + " =  \"" + String.valueOf(i) + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        Note note;

        if(cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String titleAdd = cursor.getString(1);
            String desAdd = cursor.getString(2);
            note = new Note(id,titleAdd,desAdd);
            db.update(TABLE_NOTES, values, COLUMN_ID + " = ?",new String[]{String.valueOf(note.getID())});
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

}
