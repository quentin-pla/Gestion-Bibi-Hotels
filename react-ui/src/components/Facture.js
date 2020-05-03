import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";

class Facture extends Component {

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
            msg: "",
            mail: this.context.mail,
            reservations: [],
            hotels: [],
            bills: [],
            merged: []
        };
        this.mergeArrayObjects = this.mergeArrayObjects.bind(this);
    }

    /**
     * Cette fonction réunit chaque array en un seul pour faciliter le traitement
     * @param bills : liste des factures
     * @param reservations : liste des resercvations
     * @param hotels : liste des hotels
     */
    mergeArrayObjects(bills, reservations, hotels) {
        let merged = [];
        let mergedF = [];

        for (let i = 0; i < bills.length; i++) {
            merged.push({
                    ...bills[i],
                    ...(reservations.find((itmInner) => itmInner.id === bills[i].reservation_id))
                }
            );
            merged[i].id = bills[i].id;
            merged[i].is_archived = bills[i].is_archived;
        }

        for (let i = 0; i < merged.length; i++) {
            mergedF.push({
                    ...merged[i],
                    ...(hotels.find((itmInner) => itmInner.id === merged[i].hotel_id))
                }
            );
            mergedF[i].id = merged[i].id
        }
        console.log(mergedF);
        this.setState({"merged": mergedF});
    }

    /**
     * Fonction s'activant a l'initialisation du composant
     */
    componentDidMount() {
        socket.emit("bills", this.state.mail);
        socket.on('bills_res', (res ,bills, reservations, hotels) => {
            if (res === true){
                this.setState({ "bills": bills,"reservations": reservations, "hotels": hotels});
                this.mergeArrayObjects(this.state.bills, this.state.reservations, this.state.hotels)
            }else
                this.setState({'bills' : null , "msg": "Vous n'avez pas de factures à régler"});
        });
    }


    render() {
        return (
            <div>
                {this.state.bills.length === 0 ? <h3>Vous n'avez pas de factures à régler</h3> : <h3>Vos Factures :</h3>}
                <ul className="ul">
                    {this.state.merged.map(function (item, index) {
                        return (
                            <div key={item.id}>
                                <div>
                                    <h4>{item.hotel_name}</h4>
                                    <h6>{item.city}</h6>
                                    <h6>{item.street}</h6>
                                    <h6>{item.amount}€</h6>
                                </div>
                                {item.is_archived === true ? " " :
                                    <button type="button" className="btn btn-outline-warning"
                                            onClick={() => payBill(item.id)}>Payer</button>}
                            </div>
                        )
                    })}
                </ul>
            </div>

        );
    }
}

/**
 * Permet de payer une facture
 * @param id : identifiant de la facture
 */
function payBill(id) {
    socket.emit("pay_bill", id);
}

export default Facture;
