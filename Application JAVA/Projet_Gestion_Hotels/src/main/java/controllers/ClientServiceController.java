package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.ClientService;
import models.Occupation;
import models.Service;
import views.ClientServicePanel;
import views.SelectServicePanel;

import java.util.ArrayList;

public class ClientServiceController {
    /**
     * Fenêtre du service client
     */
    private ClientServicePanel panel;

    /**
     * Fenêtre de sélection du service
     */
    private SelectServicePanel serviceSelection;

    /**
     * Constructeur
     */
    public ClientServiceController() {
        panel = new ClientServicePanel();
        serviceSelection = new SelectServicePanel();
        //Initialisation des boutons
        initPanel();
    }

    /**
     * Initialiser les boutons de la fenêtre
     */
    public ClientServicePanel initPanel() {
        //Définition de l'action du bouton retour
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
        //Affichage des boutons liés à une occupation sélectionnée
        panel.getOccupations().setOnMouseClicked(e -> refreshPanel());
        //Définition de l'action du bouton de confirmation de l'arrivée
        panel.getArchiveButton().setOnAction(e -> {
            //Récupération de l'occupation sélectionnée
            Occupation occupation = panel.getOccupations().getSelectionModel().getSelectedItem();
            //Si l'occupation existe
            if (occupation != null)
                //Archivage de l'occupation
                ClientService.getInstance().archiveOccupation(occupation);
            //Suppression du focus sur l'occupation
            panel.getOccupations().getSelectionModel().clearSelection();
            //Suppression de l'occupation du tableau
            panel.getOccupations().getItems().remove(occupation);
            //Rafraichissement
            refreshPanel();
        });
        //Définition de l'action du bouton de paiement
        panel.getBillServiceButton().setOnAction(e -> {
            //Récupération de l'occupation sélctionnée
            Occupation occupation = panel.getOccupations().getSelectionModel().getSelectedItem();
            //Si l'occupation existe
            if (occupation != null)

            //Rafraichissement
            refreshPanel();
        });
        //Définition de l'action du bouton de calcul du montant total
        panel.getPresenceButton().setOnAction(e -> {
            //Récupération de l'occupation sélctionnée
            Occupation occupation = panel.getOccupations().getSelectionModel().getSelectedItem();
            //Si l'occupation existe
            if (occupation != null)
                //Changement de l'état de présence du client
                ClientService.getInstance().updatePresence(occupation, !occupation.getIS_CLIENT_PRESENT());
            //Rafraichissement
            refreshPanel();
        });
        //Liste des occupations
        ObservableList<Occupation> items = FXCollections.observableList(ClientService.getInstance().getOccupations());
        //Ajout des occupations à la table
        panel.getOccupations().setItems(items);
        //Retour de la fenêtre
        return panel;
    }

    /**
     * Rafraichir l'affichage des boutons et du tableau
     */
    public void refreshPanel() {
        //Rafraichissement des éléments du tableau
        panel.getOccupations().refresh();
        //Récupération de l'occupation sélectionnée
        Occupation occupation = panel.getOccupations().getSelectionModel().getSelectedItem();
        //Si l'occupation existe et que le nombre d'éléments du tableau est supérieur à zéro
        if (occupation != null && panel.getOccupations().getItems().size() > 0) {
            //Affichage de la liste contenant les boutons
            panel.getRefButtons().setVisible(true);
        }
        //Masquage de la liste contenant les boutons
        else panel.getRefButtons().setVisible(false);
    }

    /**
     * Initialiser la fenêtre de sélection de l'hotel
     */
    private void initServiceSelection(Occupation occupation) {
        //Initialisation de la fenêtre de sélection de l'hotel
        serviceSelection = new SelectServicePanel();
        //Récupération de la liste des services disponibles pour l'occupation
        ArrayList<Service> services = ClientService.getInstance().getOccupationServices(occupation);
        //Récupération de la liste des services disponibles
        for (Service service : services)
            //Ajout du nom du service dans la comboBox
            serviceSelection.getServices().getItems().add(service.getNAME());
        //Affichage de la fenêtre de sélection du service
        MainController.getInstance().setWindow(serviceSelection);
        //Afficher la fenêtre du service lorsque le service est sélectionné
        serviceSelection.getValidate().setOnAction(e -> {
            //Service sélectionné
            Service selectedService = services.get(serviceSelection.getServices().getSelectionModel().getSelectedIndex());
            //Facturation du service à l'occupation
            ClientService.getInstance().billService(occupation,selectedService);
            //Affichage de la liste des occupations
            MainController.getInstance().setWindow(panel);
        });
        //Lorsque l'utilisateur appuie sur le bouton retour, affichage fenêtre service client
        serviceSelection.getBack().setOnAction(e -> MainController.getInstance().setWindow(panel));
    }
}
