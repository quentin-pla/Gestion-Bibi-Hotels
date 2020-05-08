import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
import {Link} from "react-router-dom";
import {Button, Card, Col, Form, Row} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import PeopleFill from "react-bootstrap-icons/dist/icons/people-fill";
import StarFill from "react-bootstrap-icons/dist/icons/star-fill";
import Star from "react-bootstrap-icons/dist/icons/star";

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
            nblits: this.state.nbPersonnes*2,
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
            <Container fluid>
                <Row bsPrefix={"text-center mb-4"}>
                    <h1>Voyagez à travers le monde, grâce à Bibi.</h1>
                </Row>
                <Row bsPrefix={"mb-4 justify-content-center"}>
                    <Form className={"form-inline justify-content-center"}>
                        <Row className={"searchInput"}>
                            <Col>
                                <Form.Label column={"cityInput"} className={"text-left label-field"}>DESTINATION</Form.Label>
                                <Form.Control className="form-control-sm" id="cityInput" placeholder="Marseille" type="text" onChange={e => this.setState({"ville": e.target.value})}/>
                            </Col>
                            <Col>
                                <Form.Label column={"dateAInput"} className={"text-left label-field"}>ARRIVÉE</Form.Label>
                                <Form.Control className="form-control-sm" id="dateAInput" type="date" min={minArrivalDate} value={minArrivalDate} onChange={e => this.setState({"dateA": e.target.value})}/>
                            </Col>
                            <Col>
                                <Form.Label column={"dateDInput"} className={"text-left label-field"}>DÉPART</Form.Label>
                                <Form.Control className="form-control-sm" id="dateDInput" type="date" min={minExitDate} value={minExitDate} onChange={e => this.setState({"dateD": e.target.value})}/>
                            </Col>
                            <Col>
                                <Form.Label column={"peopleInput"} className={"text-left label-field"}>VOYAGEURS</Form.Label>
                                <Form.Control className="form-control-sm no-carret" id="peopleInput" type="number" min="1" placeholder={this.state.nbPersonnes} onKeyDown={e => e.preventDefault()} onChange={e => this.setState({"nbPersonnes": e.target.value})}/>
                            </Col>
                            <Col className={"my-auto"}>
                                <Button variant="primary" onClick={() => this.applyFilter()}>Rechercher</Button>
                            </Col>
                        </Row>
                    </Form>
                </Row>
                <Row>
                    <Row className={"m-0 px-4 mb-4 w-100"}>
                        {this.state.merged.length === 0 ? <h3>Aucun resultat</h3> :
                            this.state.merged.map((item) => {
                                let link = "room/" + item.id;
                                return (
                                    <Col className={"col-4 p-4"}>
                                        <RoomItem item={item} preview={this.getRoomPreview(item)}>
                                            <Link to={link} className="stretched-link"/>
                                        </RoomItem>
                                    </Col>
                                )
                            })
                        }
                    </Row>
                </Row>
            </Container>
        );
    }
}


function RoomItem(props) {
    function getStars(stars) {
        let result = [];
        for (let i = 1; i <= 5; i++)
            result.push((i <= stars) ? <StarFill className={"star"}/> : <Star className={"star"}/>);
        return result;
    }

    return (
        <Card className={"room-card"}>
            <Card.Img variant="top" className="media-object" src={props.preview} alt="room preview"/>
            <Card.Body>
                {props.children}
                <div className="d-flex">
                    <div>
                        <Card.Title className={"m-0"}><strong>Bibi</strong> {props.item.hotel_name}</Card.Title>
                    </div>
                    <div className="ml-auto">
                        <h5 className={"m-0"}>{getStars(props.item.stars)}</h5>
                    </div>
                </div>
                <div className="d-flex">
                    <div>
                        <h6 className={"hotel-city"}>{props.item.city}</h6>
                    </div>
                    <div className="ml-auto mr-1">
                        <PeopleFill style={{fontSize: 20}}/> {props.item.bed_capacity*2}
                    </div>
                </div>
                <h6 className={"room-price"}><strong>{props.item.price}€</strong>/nuit </h6>
            </Card.Body>
        </Card>
    );
}

export default HotelList;
