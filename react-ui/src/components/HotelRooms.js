import React, {Component, useEffect, useState} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
import {Link} from "react-router-dom";
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import PeopleFill from "react-bootstrap-icons/dist/icons/people-fill";
import StarFill from "react-bootstrap-icons/dist/icons/star-fill";
import Star from "react-bootstrap-icons/dist/icons/star";
import Modal from "react-bootstrap/Modal";
import Check from "react-bootstrap-icons/dist/icons/check";
import XCircleFill from "react-bootstrap-icons/dist/icons/x-circle-fill";

class HotelRooms extends Component {
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
            rooms: [],
            selectedRoom: undefined,
            ville: "Sélectionner",
            capacite: 1,
            nbChambres: 1,
            nbPersonnes: 1,
            dateA: this.dateToString(this.addDaysToDate(new Date(),3)),
            dateD: this.dateToString(this.addDaysToDate(new Date(),4)),
            modalShow: false,
            reservationValidated: false,
            error: false,
            errorMessage: ""
        };

        this.applyFilter = this.applyFilter.bind(this);
        this.reserver = this.reserver.bind(this);
        this.addDaysToDate = this.addDaysToDate.bind(this);
        this.getRoomPreview = this.getRoomPreview.bind(this);
        this.updateArrivalDate = this.updateArrivalDate.bind(this);
        this.updateExitDate = this.updateExitDate.bind(this);
        this.dateToString = this.dateToString.bind(this);
        this.updateRoomsNumber = this.updateRoomsNumber.bind(this);
        this.updatePeopleNumber = this.updatePeopleNumber.bind(this);
    }

    /**
     * Fonction s'activant a l'initialisation du composant
     */
    componentDidMount() {
        socket.emit("rooms",this.state.dateA,this.state.dateD);
        socket.on('rooms_res', (rooms) => {
            this.setState({'rooms': rooms});
        });
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (!prevState.modalShow && this.state.modalShow) this.setState({"nbChambres": 1, "nbPersonnes": 1, "error": false});
        if (!this.state.modalShow && this.state.reservationValidated) {
            setTimeout(async () => await this.setState({"reservationValidated": false}), 1000);
        }
    }

    /**
     * Lorsque l'utilisateur appuie sur le boutton valider
     */
    applyFilter() {
        let data = {
            ville: (this.state.ville === 'Sélectionner') ? '' : this.state.ville,
            capacite: (this.state.capacite < 1) ? 1 : this.state.capacite,
            dateA: (this.state.dateA < this.addDaysToDate(new Date(),3)) ? this.addDaysToDate(new Date(),3) : this.state.dateA,
            dateD: (this.state.dateD < this.addDaysToDate(new Date(),4)) ? this.addDaysToDate(new Date(),4) : this.state.dateD
        };
        socket.emit("apply_filter",data);
        socket.on("rooms_res", (rooms) => {
            this.setState({"rooms" : rooms});
        });
    }

    /**
     * Réserver une chambre
     * @param room chambre
     */
    reserver(room) {
        let data = {
            mail: this.context.mail,
            hotel_id: room.hotel_id,
            roomtype_id: room.roomtype_id,
            arrival_date: this.state.dateA,
            exit_date: this.state.dateD,
            duration: room.duration,
            room_count: this.state.nbChambres,
            people_count: this.state.nbPersonnes,
        };
        socket.emit("reserver", data);
        socket.on("reservation_res", (result, err) => {
            this.setState({"reservationValidated": result});
            if (!result) this.setState({"error": true, "errorMessage": err});
        });
    }

    /**
     * Récupérer la date du jour (possibilité d'ajouter un nombre de jours)
     * @param date date
     * @param daysToAdd jours à ajouter
     * @returns {string}
     */
    addDaysToDate(date, daysToAdd) {
        date.setDate(date.getDate()+daysToAdd);
        return date;
    }

    /**
     * Convertir une date en chaine de caractères
     * @param date date
     * @returns {string}
     */
    dateToString(date) {
        const year = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(date);
        const month = new Intl.DateTimeFormat('en', { month: '2-digit' }).format(date);
        const day = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(date);
        return [year, month, day].join('-');
    }

    /**
     * Récupérer l'URL de l'image preview pour une chambre
     * @param room chambre
     */
    getRoomPreview(room) {
        const publicFolder = process.env.PUBLIC_URL;
        return room !== undefined ?
            publicFolder + "/roomTypes/" + room.roomtype.name.toLowerCase() + "-" + room.hotel.city.toLowerCase() + ".jpg"
        :
            null;
    }

    /**
     * Mettre à jour la date d'arrivée
     * @param dateA date d'arrivée
     */
    updateArrivalDate(dateA) {
        if (new Date(dateA) >= new Date(this.state.dateD))
            this.setState({"dateA": dateA, "dateD": this.dateToString(this.addDaysToDate(new Date(dateA),1))});
        else
            this.setState({"dateA": dateA});
    }

    /**
     * Mettre à jour la date de sortie
     * @param dateD date de sortie
     */
    updateExitDate(dateD) {
        this.setState({"dateD": dateD});
    }

    /**
     * Mettre à jour le nombre de chambres
     * @param nbChambres nombre de chambres à réserver
     */
    updateRoomsNumber(nbChambres) {
        this.setState({"nbChambres": nbChambres});
    }

    /**
     * Mettre à jour le nombre de personnes
     * @param nbPersonnes nombre de chambres à réserver
     */
    updatePeopleNumber(nbPersonnes) {
        this.setState({"nbPersonnes": nbPersonnes});
    }

    render() {
        const minArrivalDate = this.dateToString(this.addDaysToDate(new Date(),3));
        const minExitDate = this.dateToString(this.addDaysToDate(new Date(this.state.dateA),1));
        const availableCities = ['Sélectionner'];

        this.state.rooms.forEach((room) => {
            if (!availableCities.includes(room.hotel.city)) availableCities.push(room.hotel.city);
        });

        return (
            <Container fluid>
                <Row bsPrefix={"text-center mb-4"}>
                    <h1>Voyagez à travers le monde, grâce à Bibi.</h1>
                </Row>
                <Row bsPrefix={"mb-4 justify-content-center"}>
                    <Form className={"form-inline justify-content-center"}>
                        <Row className={"searchInput"}>
                            <Col>
                                <Form.Label column={"cityInput"} className={"text-left label-field"}>DESTINATION</Form.Label>
                                <Form.Control as="select" className="form-control-sm" id="cityInput" value={this.state.ville} onChange={e => this.setState({"ville": e.target.value})}>
                                    {availableCities.map((city) => {return (<option key={city}>{city}</option>);})}
                                </Form.Control>
                            </Col>
                            <Col>
                                <Form.Label column={"dateAInput"} className={"text-left label-field"}>ARRIVÉE</Form.Label>
                                <Form.Control className="form-control-sm" id="dateAInput" type="date" min={minArrivalDate} value={this.state.dateA} onChange={e => this.updateArrivalDate(e.target.value)}/>
                            </Col>
                            <Col>
                                <Form.Label column={"dateDInput"} className={"text-left label-field"}>DÉPART</Form.Label>
                                <Form.Control className="form-control-sm" id="dateDInput" type="date" min={minExitDate} value={this.state.dateD} onChange={e => this.setState({"dateD": e.target.value})}/>
                            </Col>
                            <Col>
                                <Form.Label column={"capacityInput"} className={"text-left label-field"}>CAPACITÉ</Form.Label>
                                <Form.Control className="form-control-sm no-carret number-input" id="capacityInput" type="number" min="1" value={this.state.capacite} onKeyDown={e => e.preventDefault()} onChange={e => this.setState({"capacite": parseInt(e.target.value)})}/>
                            </Col>
                            <Col className={"my-auto"}>
                                <Button variant="primary" onClick={() => this.applyFilter()}>Rechercher</Button>
                            </Col>
                        </Row>
                    </Form>
                </Row>
                <Row>
                    <Row className={"m-0 px-4 mb-4 w-100"}>
                        {this.state.rooms.length === 0 ?
                            <Col className="col-12 text-center mt-5">
                                <h3 className="display-4">Aucun résultat...</h3>
                            </Col>
                            :
                            this.state.rooms.map((room, index) => {
                                return (
                                    <Col key={index} className={"col-4 p-4"}>
                                        <RoomItem room={room} preview={this.getRoomPreview(room)}>
                                            <Link onClick={() => this.setState({"selectedRoom": room, "modalShow": true})} className="stretched-link" to={"#"}/>
                                        </RoomItem>
                                    </Col>
                                )
                            })
                        }
                    </Row>
                </Row>
                <BookModal
                    show={this.state.modalShow}
                    room={this.state.selectedRoom}
                    preview={this.getRoomPreview(this.state.selectedRoom)}
                    dateA={this.state.dateA}
                    dateD={this.state.dateD}
                    nbChambres={this.state.nbChambres}
                    nbPersonnes={this.state.nbPersonnes}
                    minArrivalDate={minArrivalDate}
                    minExitDate={minExitDate}
                    reservationValidated={this.state.reservationValidated}
                    error={this.state.error}
                    errorMessage={this.state.errorMessage}
                    updateArrivalDate={this.updateArrivalDate}
                    updateExitDate={this.updateExitDate}
                    updateRoomsNumber={this.updateRoomsNumber}
                    updatePeopleNumber={this.updatePeopleNumber}
                    reserver={this.reserver}
                    onHide={() => this.setState({"modalShow": false})}
                />
            </Container>
        );
    }
}

function RoomItem(props) {
    function getStars(stars) {
        let result = [];
        for (let i = 1; i <= 5; i++)
            result.push((i <= stars) ? <StarFill key={i} className={"star"}/> : <Star key={i} className={"star"}/>);
        return result;
    }

    return (
        <Card className={"room-card"}>
            <Card.Img variant="top" className="media-object" src={props.preview} alt="room preview"/>
            <Card.Body>
                {props.children}
                <div className="d-flex">
                    <div>
                        <Card.Title className={"m-0"}><strong>Bibi</strong> {props.room.roomtype.name.toUpperCase()}</Card.Title>
                    </div>
                    <div className="ml-auto">
                        <h5 className={"m-0"}>{getStars(props.room.hotel.stars)}</h5>
                    </div>
                </div>
                <div className="d-flex">
                    <div>
                        <h6 className={"hotel-city"}>{props.room.hotel.city}</h6>
                    </div>
                    <div className="ml-auto mr-1">
                        <PeopleFill style={{fontSize: 20}}/> {props.room.roomtype.bed_capacity*2}
                    </div>
                </div>
                <h6 className={"room-price"}><strong>{props.room.roomtype.price}€</strong>/nuit </h6>
            </Card.Body>
        </Card>
    );
}

function BookModal(props) {
    const {
        room,
        preview,
        dateA,
        dateD,
        nbChambres,
        nbPersonnes,
        minArrivalDate,
        minExitDate,
        reservationValidated,
        error,
        errorMessage,
        updateArrivalDate,
        updateExitDate,
        updateRoomsNumber,
        updatePeopleNumber,
        reserver,
        ...rest
    } = props;

    const [maxRoomsCount, setMaxRoomsCount] = useState(1);

    useEffect(() => {
        if (room !== undefined) {
            socket.emit("rooms_count", room.hotel_id, dateA, dateD, room.roomtype_id);
            socket.on("rooms_count_res", (count) => {
                setMaxRoomsCount(count);
            });
        }
    });

    function handleRoomsNumber(number, bed_capacity, maximum) {
        if (number <= maximum) {
            let maxPeopleNumber = (bed_capacity*2) * number;
            if (nbPersonnes > maxPeopleNumber) updatePeopleNumber(maxPeopleNumber);
            updateRoomsNumber(number);
        }
    }

    function handlePeopleNumber(number, bed_capacity, maximum) {
        if (number <= maximum) {
            let roomsNumber = Math.floor((number-1)/(bed_capacity*2))+1;
            if (nbChambres < roomsNumber) updateRoomsNumber(Math.floor((number-1)/(bed_capacity*2))+1);
            updatePeopleNumber(number);
        }
    }

    function handleReservation(room) {
        reserver(room);
    }

    function getReservationButton() {
        return reservationValidated ?
            <Button variant={"success"} className={"ml-auto"} onClick={props.onHide}>Effectuée <Check/></Button>
            :
            <Button className={"ml-auto"} onClick={() => handleReservation(room)}>Réserver</Button>
    }

    let modalContent = null;

    if (room !== undefined && !error) {
        const totalDays = Math.abs(new Date(dateD) - new Date(dateA)) / (1000 * 60 * 60 * 24);
        const maxPeopleCount = room.roomtype.bed_capacity * maxRoomsCount * 2;
        room.duration = totalDays;

        modalContent = (
            <Modal.Body>
                <Row>
                    <Col>
                        <Row>
                            <Col bsPrefix={"col-12"}>
                                <div className="d-flex flex-wrap">
                                    <h4>Hôtel</h4>
                                    <h4 className={"text-grey ml-2"}>{room.hotel.hotel_name}</h4>
                                </div>
                            </Col>
                            <Col bsPrefix={"col-12 ml-3"}>
                                <h5 className={"text-grey"}>{room.hotel.street + ", " + room.hotel.city}</h5>
                            </Col>
                            <Col bsPrefix={"col-12"}>
                                <div className="d-flex flex-wrap">
                                    <h4>Type de chambre</h4>
                                    <h4 className={"text-grey ml-2"}>{room.roomtype.name}</h4>
                                </div>
                            </Col>
                            {room.roomtype.has_tv ?
                                <Col bsPrefix={"col-12"}>
                                    <h5 className={"text-grey ml-3"}>1x Télévision</h5>
                                </Col>
                                :
                                null
                            }
                            {room.roomtype.has_phone ?
                                <Col bsPrefix={"col-12"}>
                                    <h5 className={"text-grey ml-3"}>1x Téléphone</h5>
                                </Col>
                                :
                                null
                            }
                            <Col bsPrefix={"col-12"}>
                                <h5 className={"text-grey ml-3"}>{room.roomtype.bed_capacity}x Lit double</h5>
                            </Col>
                            <hr/>
                            <Col bsPrefix={"col-12"}>
                                <h4>Détails du séjour</h4>
                            </Col>
                            <Col bsPrefix={"col-12 ml-3"}>
                                <h5 className={"text-grey"}>Du {new Date(dateA).toLocaleDateString("fr-FR")} au {new Date(dateD).toLocaleDateString("fr-FR")} ({totalDays} {totalDays > 1 ? "jours" : "jour"})</h5>
                            </Col>
                            <Col bsPrefix={"col-12 mt-1"}>
                                <div className="d-flex flex-wrap">
                                    <h4>Chambres</h4>
                                    <Form.Control className="form-control-sm no-carret number-input mx-2" type="number" min="1"
                                          max={maxRoomsCount} value={nbChambres > maxRoomsCount ? maxRoomsCount : nbChambres } onKeyDown={e => e.preventDefault()}
                                          onChange={e => handleRoomsNumber(parseInt(e.target.value), room.roomtype.bed_capacity, maxRoomsCount)}/>
                                    <h5 className={"text-grey"}>({maxRoomsCount} disponible{maxRoomsCount > 1 ? "s":""})</h5>
                                </div>
                            </Col>
                            <Col bsPrefix={"col-12 mt-2"}>
                                <div className="d-flex flex-wrap">
                                    <h4>Personnes</h4>
                                    <Form.Control className="form-control-sm no-carret number-input mx-2" type="number" min="1"
                                          max={maxPeopleCount} value={nbPersonnes} onKeyDown={e => e.preventDefault()}
                                          onChange={e => handlePeopleNumber(parseInt(e.target.value), room.roomtype.bed_capacity, maxPeopleCount)}/>
                                    <h5 className={"text-grey"}>({maxPeopleCount} maximum)</h5>
                                </div>
                            </Col>
                            <Col>
                                <hr/>
                            </Col>
                            <Col bsPrefix={"col-12"}>
                                <div className="d-flex flex-wrap align-content-center">
                                    <h4 className={"mb-0 my-auto"}>TOTAL</h4>
                                    <h4 className={"text-grey ml-2 mb-0 my-auto"}>
                                        <strong>{Math.round(room.roomtype.price * totalDays * nbChambres * 100) / 100}€</strong>
                                    </h4>
                                    {getReservationButton()}
                                </div>
                            </Col>
                        </Row>
                    </Col>
                    <Col><RoomItem room={room} preview={preview}/></Col>
                </Row>
            </Modal.Body>
        );
    } else {
        modalContent = (
            <Modal.Body>
                <Row bsPrefix={"m-4"}>
                    <Col bsPrefix={"col-12 text-center mb-3"}>
                        <h3 className={"text-danger"}>Réservation impossible <XCircleFill/></h3>
                    </Col>
                    <Col bsPrefix={"col-12 text-center"}>{errorMessage}</Col>
                </Row>
            </Modal.Body>
        );
    }

    return (<Modal {...rest} size={error ? "md":"lg"} aria-labelledby="contained-modal-title-vcenter" centered>{modalContent}</Modal>);
}

export default HotelRooms;
