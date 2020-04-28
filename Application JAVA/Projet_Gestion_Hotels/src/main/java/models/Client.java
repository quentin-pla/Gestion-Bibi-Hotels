package models;

import database.DatabaseColumns;
import database.DatabaseModel;

//TODO - hash password avec bcrypt

public class Client extends DatabaseModel {
    /**
     * Nom
     */
    private String FIRSTNAME;

    /**
     * Prénom
     */
    private String LASTNAME;

    /**
     * Adresse
     */
    private String STREET;

    /**
     * Ville
     */
    private String CITY;

    /**
     * Adresse mail
     */
    private String MAIL;

    /**
     * Mot de passe
     */
    private String PASSWORD;

    /**
     * Régulier
     */
    private boolean IS_REGULAR;

    /**
     * Liste des colonnes
     */
    public enum Columns implements DatabaseColumns {
        FIRSTNAME,LASTNAME,STREET,CITY,MAIL,PASSWORD,IS_REGULAR
    }

    /**
     * Constructeur
     */
    public Client() {
        super(Tables.CLIENTS);
    }

    /**
     * Constructeur surchargé
     * @param FIRSTNAME nom
     * @param LASTNAME prénom
     * @param STREET adresse
     * @param CITY ville
     * @param MAIL adresse mail
     * @param PASSWORD mot de passe
     */
    public Client(String FIRSTNAME, String LASTNAME, String STREET, String CITY, String MAIL, String PASSWORD) {
        super(Tables.CLIENTS);
        this.FIRSTNAME = FIRSTNAME;
        this.LASTNAME = LASTNAME;
        this.STREET = STREET;
        this.CITY = CITY;
        this.MAIL = MAIL;
        this.PASSWORD = PASSWORD;
        this.save();
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getColumns() {
        return Columns.values();
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

    public boolean getIS_REGULAR() {
        return IS_REGULAR;
    }

    public void setIS_REGULAR(boolean IS_REGULAR) {
        this.IS_REGULAR = IS_REGULAR;
    }
}