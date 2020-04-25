package models;

import database.DatabaseModel;

public class Client implements DatabaseModel {
    //ID
    private int ID;
    //Nom
    private String FIRSTNAME;
    //Prénom
    private String LASTNAME;
    //Adresse
    private String STREET;
    //Ville
    private String CITY;
    //Adresse mail
    private String MAIL;
    //Mot de passe
    private String PASSWORD;
    //Régulier
    private boolean IS_REGULAR;

    //Liste des colonnes
    private enum columns {
        ID,FIRSTNAME,LASTNAME,STREET,CITY,MAIL,PASSWORD,IS_REGULAR
    }

    @Override
    public DatabaseModel newInstance() {
        return new Client();
    }

    @Override
    public void setColumn(String columnItem, String value) {
        columns column = columns.valueOf(columnItem);
        switch (column) {
            case ID:
                this.setID(Integer.parseInt(value));
                break;
            case FIRSTNAME:
                this.setFIRSTNAME(value);
                break;
            case LASTNAME:
                this.setLASTNAME(value);
                break;
            case STREET:
                this.setSTREET(value);
                break;
            case CITY:
                this.setCITY(value);
                break;
            case MAIL:
                this.setMAIL(value);
                break;
            case PASSWORD:
                this.setPASSWORD(value);
                break;
            case IS_REGULAR:
                this.setIS_REGULAR(Boolean.parseBoolean(value));
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

    public String getSTREET() {
        return STREET;
    }

    public void setSTREET(String STREET) {
        this.STREET = STREET;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getMAIL() {
        return MAIL;
    }

    public void setMAIL(String MAIL) {
        this.MAIL = MAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public boolean isIS_REGULAR() {
        return IS_REGULAR;
    }

    public void setIS_REGULAR(boolean IS_REGULAR) {
        this.IS_REGULAR = IS_REGULAR;
    }
}
