package controllers;

import database.DatabaseData;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import models.*;
import views.ClientHistoryPanel;
import views.ClientServicePanel;
import views.SelectClientPanel;
import views.SelectServicePanel;

import java.util.ArrayList;

/**
 * Controleur gérant la fenêtre du service client
 */
public class ClientServiceController {
    /**
     * Fenêtre du service client
     */
    private ClientServicePanel panel;

    /**
     * Fenêtre de sélection du service
     */
    private SelectServicePanel selectServicePanel;

    /**
     * Fenêtre de sélection du client
     */
    private SelectClientPanel selectClientPanel;

    /**
     * Fenêtre affichant l'historique des réservations d'un client
     */
    private ClientHistoryPanel clientHistoryPanel;

    /**
     * Constructeur
     */
    public ClientServiceController() {
        panel = new ClientServicePanel();
    }

    /**
     * Initialiser la fenêtre
     */
    public ClientServicePanel initPanel() {
        //Initialisation des occupations
        ClientService.getInstance().initOccupations();
        //Définition de l'action du bouton retour
        panel.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
        //Affichage des boutons liés à une occupation sélectionnée
        panel.getOccupations().setOnMouseClicked(e -> refreshPanel());
        //Définition de l'action du bouton pour facturer un service à une occupation
        panel.getBillServiceButton().setOnAction(e -> {
            //Récupération de l'occupation sélectionnée
            Occupation occupation = panel.getOccupations().getSelectionModel().getSelectedItem();
            //Si l'occupation existe et qu'il reste des services disponibles à facturer
            if (occupation != null && ClientService.getInstance().getAvailableServices(occupation).size() > 0)
                //Initialisation du panneau pour sélectionner un service
                initServiceSelection(occupation);
        });
        //Définition de l'action du bouton pour définir la présence
        panel.getPresenceButton().setOnAction(e -> {
            //Récupération de l'occupation sélectionnée
            Occupation occupation = panel.getOccupations().getSelectionModel().getSelectedItem();
            //Si l'occupation existe
            if (occupation != null)
                //Changement de l'état de présence du client
                ClientService.getInstance().updatePresence(occupation, !occupation.getIS_CLIENT_PRESENT());
            //Rafraichissement
            refreshPanel();
        });
        //Définition de l'action du bouton pour voir l'historique d'un client
        panel.getClientHistoryButton().setOnAction(e -> {
            //Initialisation du panneau pour sélectionner un client
            initClientSelection();
        });
        //Récupération des occupations
        ObservableList<Occupation> items = ClientService.getInstance().getOccupations();
        //Ajout d'un listener pour mettre à jour automatiquement le tableau
        items.addListener((ListChangeListener<Occupation>) change -> {
            //Définition d'une variable contenant la position de l'élément sélectionné
            int selected = -1;
            //Si un élément est sélectionné
            if (!panel.getOccupations().getItems().isEmpty() && !panel.getOccupations().getSelectionModel().isEmpty())
                //Récupération de l'index de l'élément sélectionné
                selected = panel.getOccupations().getSelectionModel().getFocusedIndex();
            //Rafraichissement
            refreshPanel();
            //Si l'index a été définit
            if (selected != -1)
                //Positionnement sur l'index
                panel.getOccupations().getSelectionModel().select(selected);
        });
        //Ajout des occupations à la table
        panel.getOccupations().setItems(items);
        //Refraichissement
        refreshPanel();
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
            //Savoir s'il reste des services disponibles à facturer pour l'occupation
            boolean isAvailableServices = !ClientService.getInstance().getAvailableServices(occupation).isEmpty();
            //Client présent ou pas dans l'occupation
            boolean isClientPresent = occupation.getIS_CLIENT_PRESENT();
            //Définition de l'affichage du bouton pour facturer un service
            panel.getBillServiceButton().setVisible(isAvailableServices && isClientPresent);
            panel.getBillServiceButton().setManaged(isAvailableServices && isClientPresent);
            //Définition de l'affichage du bouton pour définir la présence
            panel.getPresenceButton().setVisible(true);
            panel.getPresenceButton().setManaged(true);
        } else {
            //Masquage des boutons
            panel.getPresenceButton().setVisible(false);
            panel.getPresenceButton().setManaged(false);
            panel.getBillServiceButton().setVisible(false);
            panel.getBillServiceButton().setManaged(false);
        }
    }

    /**
     * Initialiser la fenêtre de sélection du service
     */
    private void initServiceSelection(Occupation occupation) {
        //Initialisation de la fenêtre de sélection du service
        selectServicePanel = new SelectServicePanel();
        //Récupération de la liste des services disponibles pour l'occupation
        ArrayList<Service> services = ClientService.getInstance().getAvailableServices(occupation);
        //Pour chaque service
        for (Service service : services)
            //Ajout du nom du service dans la comboBox
            selectServicePanel.getServices().getItems().add(service.getNAME());
        //Affichage de la fenêtre de sélection du service
        MainController.getInstance().setWindow(selectServicePanel);
        //Définition de l'action du bouton de validation
        selectServicePanel.getValidate().setOnAction(e -> {
            //Service sélectionné
            Service selectedService = services.get(selectServicePanel.getServices().getSelectionModel().getSelectedIndex());
            //Facturation du service à l'occupation
            ClientService.getInstance().billService(occupation,selectedService);
            //Affichage de la liste des occupations
            MainController.getInstance().setWindow(panel);
            //Rafraichissement
            refreshPanel();
        });
        //Lorsque l'utilisateur appuie sur le bouton retour, affichage fenêtre service client
        selectServicePanel.getBack().setOnAction(e -> MainController.getInstance().setWindow(panel));
    }

    /**
     * Initialiser la fenêtre de sélection du client
     */
    private void initClientSelection() {
        //Initialisation de la fenêtre de sélection du client
        selectClientPanel = new SelectClientPanel();
        //Récupération de la liste des clients
        ArrayList<Client> clients = new ArrayList<>(DatabaseData.getInstance().getClients().values());
        //Récupération de la liste des clients disponibles
        for (Client client : clients)
            //Ajout du nom du client dans la comboBox
            selectClientPanel.getClients().getItems().add(client.getLASTNAME() + " " + client.getFIRSTNAME());
        //Affichage de la fenêtre de sélection du client
        MainController.getInstance().setWindow(selectClientPanel);
        //Afficher la fenêtre du client lorsque le service est sélectionné
        selectClientPanel.getValidate().setOnAction(e -> {
            //Client sélectionné
            Client selectedClient = clients.get(selectClientPanel.getClients().getSelectionModel().getSelectedIndex());
            //Affichage de l'hitorique du client
            MainController.getInstance().setWindow(initClientHistoryPanel(selectedClient));
            //Rafraichissement
            refreshPanel();
        });
        //Lorsque l'utilisateur appuie sur le bouton retour, affichage fenêtre service client
        selectClientPanel.getBack().setOnAction(e -> MainController.getInstance().setWindow(panel));
    }

    /**
     * Initialiser la fenêtre contenant l'historique du client
     * @param client client
     * @return fenêtre
     */
    private BorderPane initClientHistoryPanel(Client client) {
        //Initialisation de la fenêtre
        clientHistoryPanel = new ClientHistoryPanel();
        //Définition du titre
        clientHistoryPanel.setPanelTitle("Historique des réservations - "
                + client.getFIRSTNAME() + " " + client.getLASTNAME() + (client.getIS_REGULAR() ? " (Régulier)" : ""));
        //Lorsque l'on appuie sur le bouton retour, affichage fenêtre service client
        clientHistoryPanel.getBack().setOnAction(e -> MainController.getInstance().setWindow(panel));
        //Si le client n'est pas régulier
        if (!client.getIS_REGULAR()) {
            //Définition de l'action du bouton pour définir un client comme étant régulier
            clientHistoryPanel.getRegularClientButton().setOnAction(e -> {
                //Définition du client comme étant régulier
                ClientService.getInstance().setRegularClient(client);
                //Masquage du bouton
                clientHistoryPanel.getRegularClientButton().setVisible(false);
                clientHistoryPanel.getRegularClientButton().setManaged(false);
                //Ajout du mot régulier au titre
                clientHistoryPanel.getTitle().setText(clientHistoryPanel.getTitle().getText() + " (Régulier)");
            });
        } else {
            //Masquage du bouton
            clientHistoryPanel.getRegularClientButton().setVisible(false);
            clientHistoryPanel.getRegularClientButton().setManaged(false);
        }
        //Récupération de l'historique du client
        ObservableList<Reservation> clientHistory = FXCollections.observableList(ClientService.getInstance().getClientHistory(client));
        //Définition des réservations à afficher
        clientHistoryPanel.getClientReservations().setItems(clientHistory);
        //Retour de la fenêtre
        return clientHistoryPanel;
    }
}