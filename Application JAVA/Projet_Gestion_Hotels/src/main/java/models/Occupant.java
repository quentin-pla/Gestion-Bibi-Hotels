package models;

import database.DatabaseColumns;
import database.DatabaseModel;

public class Occupant extends DatabaseModel {
    /**
     * Table liée à la base de données
     */
    private Tables table = Tables.OCCUPANTS;

    /**
     * ID de l'occupation liée
     */
    private int OCCUPATION_ID;

    /**
     * Nom
     */
    private String FIRSTNAME;

    /**
     * Prénom
     */
    private String LASTNAME;

    /**
     * Liste des colonnes
     */
    private enum Columns implements DatabaseColumns {
        OCCUPATION_ID,FIRSTNAME,LASTNAME
    }

    /**
     * Constructeur
     */
    public Occupant() {}

    /**
     * Constructeur surchargé
     * @param occupation_id
     * @param firstname
     * @param lastname
     */
    public Occupant(int occupation_id, String firstname, String lastname) {
        this.OCCUPATION_ID = occupation_id;
        this.FIRSTNAME = firstname;
        this.LASTNAME = lastname;
        this.save();
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getModelColumns() {
        return Columns.values();
    }

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
