package database;

public interface DatabaseModel {
    //Générer une nouvelle instance
    public DatabaseModel newInstance();
    //Définir un attribut à partir d'une colonne
    public void setColumn(String columnItem, String value);
}
