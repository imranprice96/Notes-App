package pricei.massey.com.stickynotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom adapter to display list view of notes
 * */

public class NotesAdapter extends BaseAdapter {

    ArrayList<Note> notes;
    Context context;
    NotesAdapter adapter;

    /** Constructor*/
    public NotesAdapter(ArrayList<Note> notes, Context context){
        super();
        this.notes = notes;
        this.context = context;
        this.adapter = this;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;


        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.note_tab,null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        /** setup iamge button views*/
        ImageView delete =  view.findViewById(R.id.deleteButton);
        ImageView edit = view.findViewById(R.id.editButton);

        final Note note = (Note) getItem(i);
        viewHolder.titleText.setText(note.getName());
        viewHolder.desText.setText(note.getDes());


        /**---------------------------------------------------------------*/
        /** Click listeners for edit and delete buttons
         * DELETE BUTTON*/
       delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Additional dialog for delete button*/
                final DatabaseManager dbManager = new DatabaseManager(context,null,null,1);
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete");
                alert.setMessage("Do you want to delete this entry?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int which){
                        dbManager.deleteNote(note.getName()); /**Delete the entry from the database */
                        notes.remove(i);
                        adapter.notifyDataSetChanged()/**trigger refresh */;
                    }
                });
                alert.setNegativeButton("No",new DialogInterface.OnClickListener(){
                   public void onClick(DialogInterface dialog, int which){
                       dialog.cancel();
                   }
                });
                alert.show();
            }
        });

        /**EDIT BUTTON
         * open Add_Note class to edit the entry*/
        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Add_Note.class);
                intent.putExtra("ID",note.getID());
                intent.putExtra("title",note.getName());
                intent.putExtra("des",note.getDes());
                ((Activity) context).startActivityForResult(intent, 1);
                adapter.notifyDataSetChanged();
            }
        });

        /**---------------------------------------------------------------*/

        return view;
    }

    /** Simple viewholder class*/
    private class ViewHolder{
        TextView titleText;
        TextView desText;

        public ViewHolder(View view){
            titleText = view.findViewById(R.id.titleTextView);
            desText = view.findViewById(R.id.noteTextView);
        }
    }


}
