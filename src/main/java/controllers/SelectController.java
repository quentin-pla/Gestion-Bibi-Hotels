package controllers;

import database.DatabaseData;
import models.Hotel;
import views.SelectHotelPanel;
import views.SelectPanel;

import java.util.ArrayList;

public class SelectController {
    /**
     * Fenêtre de sélection
     */
    private SelectPanel panel;

    /**
     * Fenêtre de sélection de l'hotel
     */
    private SelectHotelPanel hotelSelection;

    /**
     * Liste des fenêtres précédentes
     */
    private enum Services {
        RESERVATION,BILLING
    }

    /**
     * Constructeur
     */
    public SelectController() {
        panel = new SelectPanel();
        //Initialisation des boutons
        initPanel();
    }

    /**
     * Initialiser les boutons de la fenêtre
     */
    private void initPanel() {
        //Lorsque l'utilisateur clique sur le bouton service client
        panel.getService_client().setOnAction(e -> MainController.getInstance().switchToClientService());
        //Lorsque l'utilisateur clique sur le bouton service réservation
        panel.getService_reservation().setOnAction(e -> initHotelSelection(Services.RESERVATION));
        //Lorsque l'utilisateur clique sur le bouton service facturation
        panel.getService_facturation().setOnAction(e -> initHotelSelection(Services.BILLING));
        //Lorsque l'utilisateur clique sur le bouton administration
        panel.getAdministration().setOnAction(e -> MainController.getInstance().switchToAdministration());
    }

    /**
     * Initialiser la fenêtre de sélection de l'hotel
     */
    private void initHotelSelection(Services service) {
        //Initialisation de la fenêtre de sélection de l'hotel
        hotelSelection = new SelectHotelPanel();
        //Récupération de la liste des hotels
        ArrayList<Hotel> hotels = new ArrayList<>(DatabaseData.getInstance().getHotels().values());
        //Récupération de la liste des hotels disponibles
        for (Hotel hotel : hotels)
            //Ajout du nom de l'hotel dans la comboBox
            hotelSelection.getHotels().getItems().add(hotel.getHOTEL_NAME());
        //Affichage de la fenêtre de sélection de l'hotel
        MainController.getInstance().setWindow(hotelSelection);
        //Afficher la fenêtre du service lorsque l'hotel est sélectionné
        hotelSelection.getValidate().setOnAction(e -> {
            //Hotel sélectionné
            Hotel selectedHotel = hotels.get(hotelSelection.getHotels().getSelectionModel().getSelectedIndex());
            //Selon le service
            switch (service) {
                //Facturation
                case BILLING:
                    //Rafraichissement du contenu
                    selectedHotel.getBillingService().refreshBills();
                    //Affichage de la fenêtre service facturation
                    MainController.getInstance().switchToBillingService(selectedHotel);
                    break;
                //Réservation
                case RESERVATION:
                    //Rafraichissement du contenu
                    selectedHotel.getReservationService().initReservations();
                    //Affichage de la fenêtre service réservation
                    MainController.getInstance().switchToReservationService(selectedHotel);
                    break;
                default:
                    break;
            }
        });
        //Lorsque l'utilisateur appuie sur le bouton retour, affichage fenêtre sélection
        hotelSelection.getBack().setOnAction(e -> MainController.getInstance().switchToSelect());
    }

    //************* GETTERS & SETTERS ***************//

    public SelectPanel getPanel() { return panel; }
}
