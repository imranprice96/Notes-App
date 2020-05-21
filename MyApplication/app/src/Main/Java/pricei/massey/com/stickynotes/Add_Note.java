package pricei.massey.com.stickynotes;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Class to implement the ADD button when adding/editing a note
 * Adds or updates the database using DatabaseManager.java
 */


public class Add_Note extends AppCompatActivity {

    EditText titleBox;
    EditText desBox;
    Boolean edit;
    int id;

    /**Setup the note editing layout views*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__note);
        titleBox = (EditText) findViewById(R.id.addTitleEditText);
        desBox = (EditText) findViewById(R.id.addDesEditText);
        desBox.setMovementMethod(new ScrollingMovementMethod());
        edit = false;

        /**handles information passed about note to be editied */
        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("title") || getIntent().hasExtra("des")) {
            id = getIntent().getIntExtra("ID",0);
            titleBox.setText(bundle.getString("title"));
            desBox.setText(bundle.getString("des"));
            edit = true;
        }

    }

    /**
     * Main functionality for adding a new note to the database from the layout.
     * Adds the inputed text to the layout and to the database
     * */
    public void Add(View view){

        /**if both text boxes are empty cancel*/
        if(titleBox.getText().toString().isEmpty() && desBox.getText().toString().isEmpty()){
            setResult(RESULT_CANCELED,null);
            finish();

            /** else if no intent was passed
             * add a new note to database*/
        }else if(!edit) {
            //TODO tidy up repeated code
            DatabaseManager dbManager = new DatabaseManager(this, null, null, 1);
            ContentValues values = new ContentValues();

            values.put("title", titleBox.getText().toString());
            values.put("description", desBox.getText().toString());
            dbManager.addNote(values); /**Function to add to database */
            Toast.makeText(getApplicationContext(),"Saved", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK, null);
            finish();

            /** if intent was passed update the database*/
        } else{
            //TODO tidy repeated code
            DatabaseManager dbManager = new DatabaseManager(this,null,null,1);
            ContentValues values = new ContentValues();

            values.put("title",titleBox.getText().toString());
            values.put("description",desBox.getText().toString());
            dbManager.updateNote(String.valueOf(id), values);/**Funtcion that updates the database */
            Toast.makeText(getApplicationContext(),"Saved", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK,null);
            edit = false;
            finish();
        }

    }

}
