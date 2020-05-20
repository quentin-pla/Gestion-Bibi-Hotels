package org.gestion_bibi_hotels.views;

import org.gestion_bibi_hotels.controllers.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

/**
 * Fenêtre d'administration
 */
public class AdministrationPanel extends BorderPane {
    private Button back = new Button("←");
    private Label title = new Label();
    private VBox centerContent = new VBox();
    private VBox roomTypesContainer = new VBox();
    private VBox billedAmountsContainer = new VBox();
    private VBox billedServicesAmountsContainer = new VBox();
    private HBox ratioContainer = new HBox();
    private HBox servicesContainer = new HBox();
    private HBox hotelsContainer = new HBox();

    /**
     * Constructeur
     */
    public AdministrationPanel() {
        setMinSize(MainController.width, MainController.height);
        title.getStyleClass().add("h3");
        back.getStyleClass().add("back");

        roomTypesContainer.setSpacing(15);
        Label roomTypesContainerTitle = new Label("Types de chambres (nuits disponibles/facturées)");
        roomTypesContainerTitle.getStyleClass().add("h2");
        roomTypesContainer.getChildren().add(roomTypesContainerTitle);
        ratioContainer.setSpacing(30);
        roomTypesContainer.getChildren().add(ratioContainer);

        billedAmountsContainer.setSpacing(15);
        Label billedAmountsContainerTitle = new Label("Bénéfice de chaque hôtel");
        billedAmountsContainerTitle.getStyleClass().add("h2");
        billedAmountsContainer.getChildren().add(billedAmountsContainerTitle);
        hotelsContainer.setSpacing(30);
        billedAmountsContainer.getChildren().add(hotelsContainer);

        billedServicesAmountsContainer.setSpacing(15);
        Label billedServicesAmountsContainerTitle = new Label("Facturation de chaque service");
        billedServicesAmountsContainerTitle.getStyleClass().add("h2");
        billedServicesAmountsContainer.getChildren().add(billedServicesAmountsContainerTitle);
        servicesContainer.setSpacing(30);
        billedServicesAmountsContainer.getChildren().add(servicesContainer);

        BorderPane topContent = new BorderPane();
        topContent.setLeft(back);
        topContent.setCenter(title);
        BorderPane.setAlignment(title, Pos.CENTER);

        centerContent.setSpacing(50);
        centerContent.setAlignment(Pos.CENTER_LEFT);
        centerContent.getChildren().add(roomTypesContainer);
        centerContent.getChildren().add(billedAmountsContainer);
        centerContent.getChildren().add(billedServicesAmountsContainer);

        setTop(topContent);
        setCenter(centerContent);
        setMargin(centerContent, new Insets(20,40,10,40));
    }

    /**
     * Initialiser les ratios pour chaque type de chambre
     * @param roomTypesRatios données provenant du service administration
     */
    public void initRoomTypesRatios(Map<String,Double> roomTypesRatios) {
        ratioContainer.getChildren().clear();
        for (Map.Entry<String,Double> roomType : roomTypesRatios.entrySet())
            ratioContainer.getChildren().add(
                    generateItem(roomType.getKey(),String.valueOf((double) Math.round(roomType.getValue() * 100) / 100), "%")
            );
    }

    /**
     * Initialiser les bénéfices totaux de chaque hotel
     * @param billedAmounts données provenant du service administration
     */
    public void initTotalBilledAmounts(Map<String,Double> billedAmounts) {
        hotelsContainer.getChildren().clear();
        for (Map.Entry<String,Double> hotel : billedAmounts.entrySet())
            hotelsContainer.getChildren().add(
                generateItem(hotel.getKey(),String.valueOf((double) Math.round(hotel.getValue() * 100) / 100), "€")
            );
    }

    /**
     * Initialiser le nombre de facture effectuée pour chaque service
     * @param billedServicesAmounts données provenant du service administration
     */
    public void initBilledServicesAmounts(Map<String,Integer> billedServicesAmounts) {
        servicesContainer.getChildren().clear();
        if (!billedServicesAmounts.isEmpty()) {
            for (Map.Entry<String, Integer> service : billedServicesAmounts.entrySet())
                servicesContainer.getChildren().add(
                        generateItem(service.getKey(), String.valueOf(service.getValue()), "")
                );
        } else {
            Label nothing = new Label("Aucun service facturé");
            nothing.getStyleClass().add("h4");
            servicesContainer.getChildren().add(nothing);
        }
    }

    /**
     * Générer un élément
     * @param title titre
     * @param value valeur
     * @param format unité
     * @return élément
     */
    private VBox generateItem(String title, String value, String format) {
        VBox itemData = new VBox();
        itemData.setAlignment(Pos.CENTER);
        Label itemTitle = new Label(title);
        itemTitle.getStyleClass().add("h4");
        Label itemValue = new Label(value + format);
        itemValue.getStyleClass().add("h2");
        itemData.getChildren().addAll(itemTitle,itemValue);
        return itemData;
    }

    //************* GETTERS & SETTERS ***************//

    public Button getBack() {
        return back;
    }

    public void setPanelTitle(String value) { title.setText(value); }
}
