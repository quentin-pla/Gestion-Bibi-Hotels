import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
import {Link} from "react-router-dom";
import {Button, Col, Form, Row} from "react-bootstrap";

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
            hotels: [],
            roomtype: [],
            ville : "",
            nbPersonnes: 1,
            dateA: this.getActualDate(3),
            dateD : this.getActualDate(4),
            merged: []
        };

        this.mergeArrayObjects = this.mergeArrayObjects.bind(this);
        this.applyFilter = this.applyFilter.bind(this);
        this.getActualDate = this.getActualDate.bind(this);
        this.getRoomPreview = this.getRoomPreview.bind(this);
    }

    /**
     * Fonction s'activant a l'initialisation du composant
     */
    componentDidMount() {
        socket.emit("rooms");
        socket.on('rooms_res', (rooms,hotels,roomtype) => {
            this.setState({'rooms': rooms ,'hotels': hotels,'roomtype': roomtype});
            this.mergeArrayObjects(this.state.rooms, this.state.roomtype, this.state.hotels);
        });
    }

    /**
     * Lorsque l'utilisateur appuie sur le boutton valider
     */
    applyFilter(){
        let data = {
            ville : this.state.ville,
            nblits: this.state.nblits,
            dateA : this.state.dateA,
            dateD : this.state.dateD
        };
        socket.emit("apply_filter",data);
        socket.on("filter_res", (item) => {
            this.setState({"rooms" : item});
            this.mergeArrayObjects(this.state.rooms, this.state.roomtype, this.state.hotels);
        })
    }

    /**
     * Cette fonction réunit chaque array en un seul pour faciliter le traitement
     * @param rooms : liste des chambres
     * @param roomtypes : liste des types de chambres
     * @param hotels ; liste des hotels
     */
    mergeArrayObjects(rooms, roomtypes, hotels) {
        let merged = [];
        let mergedF = [];

        for (let i = 0; i < rooms.length; i++) {
            merged.push({
                    ...rooms[i],
                    ...(roomtypes.find((itmInner) => itmInner.id === rooms[i].roomtype_id))
                }
            );
            merged[i].id = rooms[i].id;
        }

        for (let i = 0; i < merged.length; i++) {
            mergedF.push({
                    ...merged[i],
                    ...(hotels.find((itmInner) => itmInner.id === rooms[i].hotel_id))
                }
            );
            mergedF[i].id = merged[i].id
        }
        this.setState({"merged": mergedF});
    }

    /**
     * Récupérer la date du jour (possibilité d'ajouter un nombre de jours)
     * @param daysToAdd jours à ajouter
     * @returns {string}
     */
    getActualDate(daysToAdd) {
        const actualDate = new Date();
        let day = actualDate.getDate() + daysToAdd;
        let month = actualDate.getMonth() + 1;
        let year = actualDate.getFullYear();
        if (day < 10) day = '0' + day;
        if (month < 10) month = '0' + month;
        return [year, month, day].join('-');
    }

    /**
     * Récupérer l'URL de l'image preview pour une chambre
     * @param item chambre
     */
    getRoomPreview(item) {
        const publicFolder = process.env.PUBLIC_URL;
        switch (item.name) {
            case "STANDARD":
                return publicFolder + "/roomTypes/standard.png";
            case "TOURISM":
                return publicFolder + "/roomTypes/tourism.png";
            case "COMFORT":
                return publicFolder + "/roomTypes/comfort.png";
            case "LUXURY":
                return publicFolder + "/roomTypes/luxe.png";
            default:
                return null;
        }
    }

    render() {
        const minArrivalDate = this.getActualDate(3);
        const minExitDate = this.getActualDate(4);

        return (
            <Row>
                <Col bsPrefix={"col-12 text-center mb-4"}>
                    <h1>Voyagez à travers le monde, grâce à Bibi.</h1>
                </Col>
                <Col bsPrefix={"col-12 mb-4 justify-content-center"}>
                    <Form className={"form-inline justify-content-center"}>
                        <Row className={"searchInput"}>
                            <Col>
                                <Form.Label column={"cityInput"} className={"text-left"}>Destination</Form.Label>
                                <Form.Control id="cityInput" placeholder="Marseille" type="text" onChange={e => this.setState({"ville": e.target.value})}/>
                            </Col>
                            <Col>
                                <Form.Label column={"dateAInput"} className={"text-left"}>Arrivée</Form.Label>
                                <Form.Control id="dateAInput" type="date" min={minArrivalDate} value={minArrivalDate} onChange={e => this.setState({"dateA": e.target.value})}/>
                            </Col>
                            <Col>
                                <Form.Label column={"dateDInput"} className={"text-left"}>Départ</Form.Label>
                                <Form.Control id="dateDInput" type="date" min={minExitDate} value={minExitDate} onChange={e => this.setState({"dateD": e.target.value})}/>
                            </Col>
                            <Col>
                                <Form.Label column={"peopleInput"} className={"text-left"}>Voyageurs</Form.Label>
                                <Form.Control id="peopleInput" className={"no-carret"} type="number" min="1" placeholder={this.state.nbPersonnes} onKeyDown={e => e.preventDefault()} onChange={e => this.setState({"nbPersonnes": e.target.value})}/>
                            </Col>
                            <Col className={"my-auto"}>
                                <Button variant="dark" onClick={() => this.applyFilter()}>Rechercher</Button>
                            </Col>
                        </Row>
                    </Form>
                </Col>
                <div className="justify-content-md-center row">
                    {this.state.merged.length === 0 ? <h3>Aucun resultat</h3> :
                        <ul className="ul">
                            {this.state.merged.map((item) => {
                                let link = "room/" + item.id;
                                return (
                                    <Link key={item.id} className="nav-link text-black" to={link}>
                                        <div className="hotel-view">
                                            <div className="media">
                                                <div className="media-left media-middle">
                                                    <img src={this.getRoomPreview(item)} className="media-object" alt={"room"}/>
                                                </div>
                                                <div className="media-body">
                                                    <h4 className="media-heading">{item.hotel_name}</h4>
                                                    <h6>Nombre de lit : {item.bed_capacity}</h6>
                                                    <h6>Ville : {item.city}</h6>
                                                    <h6>Prix : {item.price}</h6>
                                                    <h6>Note : {item.stars}/5</h6>
                                                </div>
                                            </div>
                                        </div>
                                    </Link>
                                )
                            })}
                        </ul>
                    }
                </div>
            </Row>
        );
    }
}

export default HotelList;
