import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
import {Link} from "react-router-dom";
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import PeopleFill from "react-bootstrap-icons/dist/icons/people-fill";
import StarFill from "react-bootstrap-icons/dist/icons/star-fill";
import Star from "react-bootstrap-icons/dist/icons/star";
import Modal from "react-bootstrap/Modal";

class HotelList extends Component {
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
            ville : "Sélectionner",
            nbPersonnes: 1,
            dateA: this.dateToString(this.addDaysToDate(new Date(),3)),
            dateD : this.dateToString(this.addDaysToDate(new Date(),4)),
            modalShow: false
        };

        this.applyFilter = this.applyFilter.bind(this);
        this.addDaysToDate = this.addDaysToDate.bind(this);
        this.getRoomPreview = this.getRoomPreview.bind(this);
        this.updateArrivalDate = this.updateArrivalDate.bind(this);
        this.updateExitDate = this.updateExitDate.bind(this);
        this.dateToString = this.dateToString.bind(this);
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

    /**
     * Lorsque l'utilisateur appuie sur le boutton valider
     */
    applyFilter(){
        let data = {
            ville : (this.state.ville === 'Sélectionner') ? '' : this.state.ville,
            nbPersonnes: (this.state.nbPersonnes < 1) ? 1 : this.state.nbPersonnes,
            dateA : (this.state.dateA < this.addDaysToDate(new Date(),3)) ? this.addDaysToDate(new Date(),3) : this.state.dateA,
            dateD : (this.state.dateD < this.addDaysToDate(new Date(),4)) ? this.addDaysToDate(new Date(),4) : this.state.dateD
        };
        socket.emit("apply_filter",data);
        socket.on("rooms_res", (rooms) => {
            this.setState({"rooms" : rooms});
        })
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
     * @param dateD de de sortie
     */
    updateExitDate(dateD) {
        this.setState({"dateD": dateD});
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
                                <Form.Label column={"peopleInput"} className={"text-left label-field"}>VOYAGEURS</Form.Label>
                                <Form.Control className="form-control-sm no-carret" id="peopleInput" type="number" min="1" placeholder={this.state.nbPersonnes} onKeyDown={e => e.preventDefault()} onChange={e => this.setState({"nbPersonnes": parseInt(e.target.value)})}/>
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
                    minArrivalDate={minArrivalDate}
                    minExitDate={minExitDate}
                    updateArrivalDate={this.updateArrivalDate}
                    updateExitDate={this.updateExitDate}
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
    const totalDays = Math.abs(new Date(props.dateD) - new Date(props.dateA)) / (1000 * 60 * 60 * 24);

    return props.room !== undefined ?
        (<Modal {...props} size="lg" aria-labelledby="contained-modal-title-vcenter" centered>
                <Modal.Body>
                    <Row>
                        <Col>
                            <Row>
                                <Col bsPrefix={"col-12"}>
                                    <div className="d-flex flex-wrap">
                                        <h4>Hôtel</h4>
                                        <h4 className={"text-grey ml-2"}>{props.room.hotel.hotel_name}</h4>
                                    </div>
                                </Col>
                                <Col bsPrefix={"col-12 ml-3"}>
                                    <h5 className={"text-grey"}>{props.room.hotel.street + ", " + props.room.hotel.city}</h5>
                                </Col>
                                <Col bsPrefix={"col-12"}>
                                    <div className="d-flex flex-wrap">
                                        <h4>Type de chambre</h4>
                                        <h4 className={"text-grey ml-2"}>{props.room.roomtype.name}</h4>
                                    </div>
                                </Col>
                                {props.room.roomtype.has_tv ?
                                    <Col bsPrefix={"col-12"}>
                                        <h5 className={"text-grey ml-3"}>1x Télévision</h5>
                                    </Col>
                                    :
                                    null
                                }
                                {props.room.roomtype.has_phone ?
                                    <Col bsPrefix={"col-12"}>
                                        <h5 className={"text-grey ml-3"}>1x Téléphone</h5>
                                    </Col>
                                    :
                                    null
                                }
                                <Col bsPrefix={"col-12"}>
                                    <h5 className={"text-grey ml-3"}>{props.room.roomtype.bed_capacity}x Lit double</h5>
                                </Col>
                                <hr/>
                                <Col bsPrefix={"col-6"}>
                                    <div className="d-flex flex-wrap">
                                        <h4>Arrivée</h4>
                                        <Form.Control className="form-control-sm mb-3" type="date" min={props.minArrivalDate} value={props.dateA} onChange={e => props.updateArrivalDate(e.target.value)}/>
                                    </div>
                                </Col>
                                <Col bsPrefix={"col-6"}>
                                    <div className="d-flex flex-wrap">
                                        <h4>Départ</h4>
                                        <Form.Control className="form-control-sm mb-3" type="date" min={props.minExitDate} value={props.dateD} onChange={e => props.updateExitDate(e.target.value)}/>
                                    </div>
                                </Col>
                                <Col bsPrefix={"col-12"}>
                                    <div className="d-flex flex-wrap">
                                        <h4>Durée</h4>
                                        <h4 className={"text-grey ml-2"}>{totalDays} {totalDays > 1 ? "jours" : "jour"}</h4>
                                    </div>
                                </Col>
                                <Col><hr/></Col>
                                <Col bsPrefix={"col-12"}>
                                    <div className="d-flex flex-wrap align-content-center">
                                        <h4 className={"mb-0 my-auto"}>TOTAL</h4>
                                        <h4 className={"text-grey ml-2 mb-0 my-auto"}><strong>{Math.round(props.room.roomtype.price * totalDays * 100) / 100}€</strong></h4>
                                        <Button className={"ml-auto"} onClick={console.log('Réservée')}>Réserver</Button>
                                    </div>
                                </Col>
                            </Row>
                        </Col>
                        <Col><RoomItem room={props.room} preview={props.preview}/></Col>
                    </Row>
                </Modal.Body>
            </Modal>
        )
    :
        <div/>;
}

export default HotelList;
