import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

class UserBills extends Component {

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
            bills: []
        };
    }

    /**
     * Fonction s'activant a l'initialisation du composant
     */
    componentDidMount() {
        socket.emit("user_bills", this.state.mail);
        socket.on('user_bills_res', (bills) => {
            this.setState({ "bills": bills});
        });
    }


    render() {
        return (
            <Row className={"m-0 px-4 mb-4 w-100"}>
                {this.state.bills.length === 0 ?
                    <Col className="col-12 text-center mt-5">
                        <h3 className="display-4">Aucun résultat...</h3>
                    </Col>
                    :
                    this.state.bills.map((bill, index) => {
                        return (
                            <h2>{bill.id}</h2>
                        )
                    })
                }
            </Row>
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

export default UserBills;
