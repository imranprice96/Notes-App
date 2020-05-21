package pricei.massey.com.stickynotes;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/** launches a singletop task that allows the user to search through their notes by matching title*/

public class SearchableActivity extends Activity {

    NotesAdapter adapter;
    ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.listView);
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
    /**uses the findnote function from database manager to return the notes that match the search squery */
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            DatabaseManager dbManager = new DatabaseManager(this,null,null,1);
            ArrayList<Note> notes =  dbManager.findNote(query,getApplicationContext());

            if(!notes.isEmpty()) {
                adapter = new NotesAdapter(notes,this);
                lv.setAdapter(adapter);
            }
        }
    }

}