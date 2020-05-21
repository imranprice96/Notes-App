package pricei.massey.com.stickynotes;

/** class to hold a note object*/

public class Note {
    private int _ID;
    private String _name;
    private String _description;

    public Note(){

    }

    public Note(int id, String name, String desscription){
        _ID = id;
        _name = name;
        _description = desscription;
    }

    public String getName(){
        return this._name;
    }

    public String getDes(){
        return this._description;
    }

    public int getID(){return this._ID;}
}
