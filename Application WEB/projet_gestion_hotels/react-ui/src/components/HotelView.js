import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
import {Button, Card} from "react-bootstrap";

class HotelView extends Component {
    /**
     * Contexte d'authentification
     */

    static contextType = AuthContext;

    constructor(props, context) {
        super(props, context);
        /**
         * Initialisation de l'état
         */
        this.state = {
            id: "",
            nbchambres: 0,
            nbpersonnes: 0,
            dateA: "",
            dateD: "",
            room: [],
            hotel: [],
            roomtype: [],
            isError : false,
            errorMessage: "",
            merged: []
        };
        this.reserver = this.reserver.bind(this);
    }

    /**
     * Fonction s'activant a l'initialisation du composant
     */
    componentDidMount() {
        let id = window.location.href.substring(window.location.href.lastIndexOf("/") + 1);
        this.setState({"id": id});

        socket.emit("room_id", id);
        socket.on('roomId_res', (room, roomtype, hotel) => {
            this.setState({'room': room});
            this.setState({'roomtype': roomtype});
            this.setState({'hotel': hotel});
        });

    }

    /**
     * Verifie que le text ne soit pas vide
     * @param text
     * @returns {boolean|*}
     */
    isValid(text) {
        //Si le texte n'est pas définit
        if (text === null) return false;
        return text;
    }

    /**
     * Formate la date saisie par l'utilisateur afin qu'elle soit compatible avec la BD
     * @param date : date a formater
     * @returns {*} : la date formater
     */
    formatDate(date){
        let newDate = date.replace("T", " ");
        newDate += ":00";
        return newDate
    }

    /**
     * Calcule l'intervalle de jours entre deux dates
     * @param date1
     * @param date2
     * @returns {number} : nombres de jours
     */
    calculateNbDays(date1,date2){
        date1 = date1.substr(0,10);
        date2 = date2.substr(0,10);

        let newDate1 = new Date(date1);
        let newDate2 = new Date(date2);

        const utc1 = Date.UTC(newDate1.getFullYear(), newDate1.getMonth(), newDate1.getDate());
        const utc2 = Date.UTC(newDate2.getFullYear(), newDate2.getMonth(), newDate2.getDate());
        const _MS_PER_DAY = 1000 * 60 * 60 * 24;
        return Math.floor((utc2 - utc1) / _MS_PER_DAY);
    }

    /**
     * Lorsque l'utilisateur appuie sur le bouton réservé
     * @returns {boolean}
     */
    reserver() {
        let fieldsCheck = (
            this.isValid(this.state.nbchambres) &&
            this.isValid(this.state.nbpersonnes) &&
            this.isValid(this.state.dateA) &&
            this.isValid(this.state.dateD)
        );

        if (!fieldsCheck) {
            this.setState({isError: true, errorMessage: "Veuillez remplir tous les champs"});
            return false;
        }else
            this.setState({isError: false, errorMessage: ""});


        let data = {
            mail: this.context.mail,
            hotel_id : this.state.hotel.id,
            roomtype_id: this.state.roomtype.id,
            nbchambres: this.state.nbchambres,
            nbpersonnes: this.state.nbpersonnes,
            dateA: this.formatDate(this.state.dateA),
            dateD: this.formatDate(this.state.dateD),
            duree : this.calculateNbDays(this.formatDate(this.state.dateA),this.formatDate(this.state.dateD))
        };

        socket.emit('reserver',data);
        socket.on('reservation_res',(success, msg) =>{
            if (success === false){
                this.setState({'isError': true, 'errorMessage': msg});
            }else
                alert('Réservation effectuée avec succès');
        });
        console.log(data)
    }

    render() {
        return (
            <div>
                <h4 className="media-heading">{this.state.hotel.hotel_name}</h4>
                <h6>Ville : {this.state.hotel.city}</h6>
                <h6>Adresse : {this.state.hotel.street}</h6>
                <h6>Type de chambre : {this.state.roomtype.name}</h6>
                <h6>Nombre de lit : {this.state.roomtype.bed_capacity}</h6>
                <h6>Prix : {this.state.roomtype.price}</h6>
                <h6>Note : {this.state.hotel.stars}/5</h6>
                <form className="form-inline">
                    <input placeholder="nombre de chambre" type="number" required onChange={e => {
                        this.setState({"nbchambres": e.target.value});
                    }}/>
                    <input placeholder="nombre de personnes" type="number" required onChange={e => {
                        this.setState({"nbpersonnes": e.target.value});
                    }}/>
                    <label className="label">Date d'arrivée:</label>
                    <input type="datetime-local" required onChange={e => {
                        this.setState({"dateA": e.target.value});
                    }}/>
                    <label className="label">Date de départ:</label>
                    <input type="datetime-local" required onChange={e => {
                               this.setState({"dateD": e.target.value});
                    }}/>
                    <Button variant="dark" onClick={(e) => this.reserver()}>
                        Reserver
                    </Button>
                </form>
                <Card.Text
                    className="text-danger error-message mt-3">{this.state.isError ? this.state.errorMessage : ""}
                </Card.Text>
            </div>

        );
    }
}

export default HotelView;
