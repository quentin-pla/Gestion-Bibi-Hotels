package models;

import database.DatabaseModel;

public class Occupant implements DatabaseModel {
    //ID
    private int ID;
    //ID de l'occupation liée
    private int OCCUPATION_ID;
    //Nom
    private String FIRSTNAME;
    //Prénom
    private String LASTNAME;

    //Liste des colonnes
    private enum columns {
        ID,OCCUPATION_ID,FIRSTNAME,LASTNAME
    }

    @Override
    public DatabaseModel newInstance() {
        return new Occupant();
    }

    @Override
    public void setColumn(String columnItem, String value) {
        Occupant.columns column = Occupant.columns.valueOf(columnItem);
        switch (column) {
            case ID:
                this.setID(Integer.parseInt(value));
                break;
            case OCCUPATION_ID:
                this.setOCCUPATION_ID(Integer.parseInt(value));
                break;
            case FIRSTNAME:
                this.setFIRSTNAME(value);
                break;
            case LASTNAME:
                this.setLASTNAME(value);
                break;
            default:
                break;
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getOCCUPATION_ID() {
        return OCCUPATION_ID;
    }

    public void setOCCUPATION_ID(int OCCUPATION_ID) {
        this.OCCUPATION_ID = OCCUPATION_ID;
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
