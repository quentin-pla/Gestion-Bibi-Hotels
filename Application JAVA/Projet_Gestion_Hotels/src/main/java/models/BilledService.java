package models;

import database.DatabaseColumns;
import database.DatabaseData;
import database.DatabaseModel;

/**
 * Modèle service facturé de la base de données
 */
public class BilledService extends DatabaseModel {
    /**
     * ID de l'occupation liée
     */
    private int OCCUPATION_ID;

    /**
     * ID du service lié
     */
    private int SERVICE_ID;

    /**
     * Archivage
     */
    private boolean IS_ARCHIVED;

    /**
     * Liste des colonnes
     */
    public enum Columns implements DatabaseColumns {
        OCCUPATION_ID,SERVICE_ID,IS_ARCHIVED
    }

    /**
     * Constructeur
     */
    public BilledService() {
        super(Tables.BILLEDSERVICES);
    }

    /**
     * Constructeur surchargé
     * @param OCCUPATION_ID id de l'occupation liée
     * @param SERVICE_ID id du service lié
     */
    public BilledService(int OCCUPATION_ID, int SERVICE_ID, boolean IS_ARCHIVED) {
        super(Tables.BILLEDSERVICES);
        this.OCCUPATION_ID = OCCUPATION_ID;
        this.SERVICE_ID = SERVICE_ID;
        this.IS_ARCHIVED = IS_ARCHIVED;
        this.save();
    }

    //************* REFERENCES ***************//

    /**
     * Occupation liée
     * @return occupation
     */
    public Occupation getOccupation() {
        return (Occupation) DatabaseData.getInstance().getReferenceFromID(Tables.OCCUPATIONS,OCCUPATION_ID);
    }

    /**
     * Service lié
     * @return service
     */
    public Service getService() {
        return (Service) DatabaseData.getInstance().getReferenceFromID(Tables.SERVICES,SERVICE_ID);
    }

    //************* GETTERS & SETTERS ***************//

    @Override
    public DatabaseColumns[] getColumns() {
        return Columns.values();
    }

    public int getOCCUPATION_ID() {
        return OCCUPATION_ID;
    }

    public void setOCCUPATION_ID(int OCCUPATION_ID) {
        this.OCCUPATION_ID = OCCUPATION_ID;
    }

    public int getSERVICE_ID() { return SERVICE_ID; }

    public void setSERVICE_ID(int SERVICE_ID) { this.SERVICE_ID = SERVICE_ID;}

    public boolean getIS_ARCHIVED() { return IS_ARCHIVED; }

    public void setIS_ARCHIVED(boolean IS_ARCHIVED) { this.IS_ARCHIVED = IS_ARCHIVED; }
}
