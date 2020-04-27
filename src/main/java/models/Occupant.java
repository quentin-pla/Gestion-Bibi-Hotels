package models;

import database.DatabaseColumns;
import database.DatabaseData;
import database.DatabaseModel;

public class Occupant extends DatabaseModel {
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
    public enum Columns implements DatabaseColumns {
        OCCUPATION_ID,FIRSTNAME,LASTNAME
    }

    /**
     * Constructeur
     */
    public Occupant() {
        super(Tables.OCCUPANTS);
    }

    /**
     * Constructeur surchargé
     * @param occupation_id
     * @param firstname
     * @param lastname
     */
    public Occupant(int occupation_id, String firstname, String lastname) {
        super(Tables.OCCUPANTS);
        this.OCCUPATION_ID = occupation_id;
        this.FIRSTNAME = firstname;
        this.LASTNAME = lastname;
        this.save();
    }

    //************* REFERENCES ***************//

    /**
     * Occupation liée
     * @return occupation
     */
    public Occupation getHotel() {
        return (Occupation) DatabaseData.getInstance().getReferenceFromID(Tables.OCCUPATIONS,OCCUPATION_ID);
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getColumns() {
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
