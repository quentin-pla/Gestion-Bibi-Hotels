import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";

class Reservation extends Component {

    /**
     * Contexte d'authentification
     */
    static contextType = AuthContext;

    constructor(props, context) {
        super(props, context);
        this.state = {
            mail: this.context.mail,
            reservations: [],
            hotels: [],
            roomtypes: [],
            merged: []
        };
        this.mergeArrayObjects = this.mergeArrayObjects.bind(this);
    }

    mergeArrayObjects(reservations, hotels, roomtypes) {
        let merged = [];
        let mergedF = [];

        for (let i = 0; i < reservations.length; i++) {
            merged.push({
                    ...reservations[i],
                    ...(roomtypes.find((itmInner) => itmInner.id === reservations[i].roomtype_id))
                }
            );
            merged[i].id = reservations[i].id;
        }

        for (let i = 0; i < merged.length; i++) {
            mergedF.push({
                    ...merged[i],
                    ...(hotels.find((itmInner) => itmInner.id === reservations[i].hotel_id))
                }
            );
            mergedF[i].id = merged[i].id
        }
        console.log(mergedF);
        this.setState({"merged": mergedF});
    }

    componentDidMount() {
        socket.emit("user_reservation", this.state.mail);
        socket.on('user_reservation_res', (reservations, hotels, roomtypes) => {
            this.setState({"reservations": reservations, "hotels": hotels, "roomtypes": roomtypes});
            this.mergeArrayObjects(this.state.reservations, this.state.hotels, this.state.roomtypes)
        });
    }


    render() {
        return (
            <div>
                {this.state.reservations.length > 0 ? <h3>Vos Reservations :</h3> : <h3>Vous n'avez aucune réservation</h3>}
                <ul className="ul">
                    {this.state.merged.map(function (item, index) {
                        return (
                            <div key={item.id}>
                                <div>
                                    <h4>{item.hotel_name}</h4>
                                    <h6>{item.city}</h6>
                                    <h6>{item.street}</h6>
                                    <h6>Chambre {item.name}</h6>
                                    <h6>{item.people_count} Personnes</h6>
                                    <h6>{item.duration} Jours</h6>
                                    <h6>{item.price}€</h6>
                                </div>
                                {item.is_comfirmed === true || item.is_cancelled === true ? " " :
                                    <button type="button" className="btn btn-outline-success"
                                            onClick={() => confirmReservation(item.id)}>Confimer</button>}
                                {item.is_cancelled === true ? "Reservation Annulé" :
                                    <button type="button" className="btn btn-outline-danger"
                                            onClick={() => cancelReservation(item.id)}>Annuler</button>}
                                {item.is_payed === true || item.is_cancelled === true ? " " :
                                    <button type="button" className="btn btn-outline-warning"
                                            onClick={() => payReservation(item.id)}>Payer</button>}
                            </div>
                        )
                    })}
                </ul>
            </div>

        );
    }
}

function payReservation(id) {
    socket.emit("pay_reservation", id);
    alert("Règlement effectué");
    window.location.reload();
}

function confirmReservation(id) {
    socket.emit("confirm_reservation", id);
    alert('Reservation confirmée');
    window.location.reload();
}

function cancelReservation(id) {
    socket.emit("cancel_reservation", id);
    alert('Reservation annulée');
    window.location.reload();
}

export default Reservation;
