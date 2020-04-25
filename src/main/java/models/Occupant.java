package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Occupant extends DatabaseModel {
    //ID
    private int ID;
    //ID de l'occupation liée
    private int OCCUPATION_ID;
    //Nom
    private String FIRSTNAME;
    //Prénom
    private String LASTNAME;

    //Liste des colonnes
    private enum Columns implements DatabaseColumns {
        ID,OCCUPATION_ID,FIRSTNAME,LASTNAME
    }

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

    @Override
    public DatabaseModel newInstance() {
        return new Occupant();
    }

    public int getID() {
        return ID;
    }

    public void setID(String ID) { this.ID = Integer.parseInt(ID); }

    public int getOCCUPATION_ID() {
        return OCCUPATION_ID;
    }

    public void setOCCUPATION_ID(String OCCUPATION_ID) {
        this.OCCUPATION_ID = Integer.parseInt(OCCUPATION_ID);
    }

    public String getFIRSTNAME() {
        return FIRSTNAME;
    }

    public void setFIRSTNAME(String FIRSTNAME) {
        this.FIRSTNAME = FIRSTNAME;
    }

    public String getLASTNAME() {
        return LASTNAME;
    }

    public void setLASTNAME(String LASTNAME) {
        this.LASTNAME = LASTNAME;
    }
}
