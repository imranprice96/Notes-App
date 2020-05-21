package pricei.massey.com.stickynotes;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Note> notes;
    NotesAdapter adapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Arrarylist that is displayed on the main screen*/
        notes = new ArrayList<>();
        loadDataBase("title");
    }

    /** Use the database manager query to return a cursor that is used to load all the
     * notes from the database into the arraylist*/
    public void loadDataBase(String title){
        DatabaseManager dbManager = new DatabaseManager(this,null,null,1);
        Cursor cursor = dbManager.Query(title);
        notes.clear();

        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String titleAdd = cursor.getString(1);
                String desAdd = cursor.getString(2);
                notes.add(new Note(id,titleAdd,desAdd));
            }while (cursor.moveToNext());
        }

        /** set Listciew adapter containing the arraylist*/
        adapter = new NotesAdapter(notes,this);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
        cursor.close();
    }

    /** setup the menu toolbar options*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        /**Pair searchable activity with the searchview */
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(this,SearchableActivity.class);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchMenu).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        return true;
    }

    /**listener for when the add button is pressed
     * opens add note class*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.addNoteMenu):
                addNote();
                return true;
            case R.id.searchMenu:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addNote(){
        Intent intent = new Intent(this, Add_Note.class);
        startActivityForResult(intent, 1);
    }

    /**to refresh the activity when the database is updated*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }

}

