import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
import {Col, Row, Table, Container} from "react-bootstrap";
import Button from "react-bootstrap/Button";

class UserReservations extends Component {

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
            mail: this.context.mail,
            reservations: [],
            loaded: false
        };

        this.formatDate = this.formatDate.bind(this);
        this.payReservation = this.payReservation.bind(this);
        this.cancelReservation = this.cancelReservation.bind(this);

        this._isMounted = false;
    }

    /**
     * Fonction s'activant a l'initialisation du composant
     */
    componentDidMount() {
        this._isMounted = true;
        socket.emit("user_reservation", this.state.mail);
        socket.on('user_reservation_res', (reservations) => {
            if (this._isMounted) this.setState({"reservations": reservations, "loaded": true});
        });
    }

    /**
     * Destruction du composant
     */
    componentWillUnmount() {
        this._isMounted = false;
    }

    /**
     * Formatter une date
     * @param date
     */
    formatDate(date) {
        const newdate = new Date(date.toString());
        return newdate.toLocaleDateString();
    }

    /**
     * Permet de payer une reservation
     * @param id : identifiant de la reservation
     */
    payReservation(id) {
        socket.emit("pay_reservation", id);
        alert("Règlement effectué");
    }

    /**
     * Permet d'annuler une reservation
     * @param id : identifiant de la reservation
     */
    cancelReservation(id) {
        socket.emit("cancel_reservation", id);
        alert('Reservation annulée');
    }

    render() {
        return (
            <Container fluid>
                <Row className={"text-center"}>
                    <Col className={"col-12 mb-4"}><h1>Vos réservations</h1></Col>
                {this.state.loaded ?
                    this.state.reservations.length === 0 ?
                    <Col className="col-12 text-center mt-5 fade-effect">
                        <h3 className={"text-grey"}>Aucune réservation effectuée.</h3>
                    </Col>
                    :
                    <Col className={"m-0 px-4 mb-4 w-100 col-12 fade-effect"}>
                        <Table responsive>
                            <thead>
                            <tr>
                                <th>Hôtel</th>
                                <th>Type de chambre</th>
                                <th>Arrivée</th>
                                <th>Départ</th>
                                <th>Durée</th>
                                <th>Chambres</th>
                                <th>Personnes</th>
                                <th/>
                            </tr>
                            </thead>
                            <tbody>
                                {this.state.reservations.map((reservation, index) => {
                                    return (
                                        <tr key={index}>
                                            <td>{reservation.hotel.hotel_name}</td>
                                            <td>{reservation.roomtype.name}</td>
                                            <td>{this.formatDate(reservation.arrival_date)}</td>
                                            <td>{this.formatDate(reservation.exit_date)}</td>
                                            <td>{reservation.duration}</td>
                                            <td>{reservation.room_count}</td>
                                            <td>{reservation.people_count}</td>
                                            <td className={"text-right"}>
                                                {!reservation.is_payed ? <Button variant={"outline-success m-2"} onClick={() => this.payReservation(reservation.id)}>Payer</Button> : null}
                                                {!reservation.is_cancelled ? <Button variant={"outline-danger m-2"} onClick={() => this.cancelReservation(reservation.id)}>Annuler</Button> : null}
                                            </td>
                                        </tr>
                                    )
                                })}
                            </tbody>
                        </Table>
                    </Col>
                    :
                    null
                }
                </Row>
            </Container>
        );
    }
}

export default UserReservations;
