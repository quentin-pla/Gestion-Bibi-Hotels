module org.gestion_bibi_hotels {
    requires java.desktop;
    requires java.sql;
    requires javafx.controls;

    opens org.gestion_bibi_hotels to javafx.graphics;
}
