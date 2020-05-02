import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
import luxe from "./img/luxe.png"
import stand from "./img/stand.png"
import tourism from "./img/tourism.png"
import comfort from "./img/comfort.png"
import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";

class Hotels extends Component {
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
            nblits: 0,
            dateA: "",
            dateD : "",
            merged: []
        };

        this.mergeArrayObjects = this.mergeArrayObjects.bind(this);
        this.applyFilter = this.applyFilter.bind(this);
    }

    componentDidMount() {
        socket.emit("rooms");
        socket.on('rooms_res', (rooms,hotels,roomtype) => {
            this.setState({'rooms': rooms ,'hotels': hotels,'roomtype': roomtype});
            this.mergeArrayObjects(this.state.rooms, this.state.roomtype, this.state.hotels);
        });
    }

    formatDate(date){
        let newDate = date.replace("T", " ");
        newDate += ":00";
        return newDate
    }

    applyFilter(){
        let data = {
            ville : this.state.ville,
            nblits: this.state.nblits,
            dateA : this.formatDate(this.state.dateA),
            dateD : this.formatDate(this.state.dateD)
        };
        console.log(data);
        socket.emit("apply_filter",data);
        socket.on("filter_res", (item) => {
            this.setState({"rooms" : item});
            console.log(this.state.rooms)
            this.mergeArrayObjects(this.state.rooms, this.state.roomtype, this.state.hotels);

        })
    }

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
        console.log(mergedF);
        this.setState({"merged": mergedF});
    }

    render() {
        return (
            <div>
                <nav>
                    <form className="form-inline">
                        <input placeholder="nombre de lits" type="number" onChange={e => {
                            this.setState({"nblits": e.target.value});
                        }}/>
                        <input placeholder="ville" type="text" onChange={e => {
                            this.setState({"ville": e.target.value});
                        }}/>

                        <label className="label">Date d'arrivée:</label>
                        <input type="datetime-local" onChange={e => {
                            this.setState({"dateA": e.target.value});
                        }} />
                        <label className="label">Date de départ:</label>
                        <input type="datetime-local" onChange={e => {
                            this.setState({"dateD": e.target.value});
                        }}/>
                        <Button variant="dark" onClick={(e) => this.applyFilter()}>
                            Valider
                        </Button>
                    </form>
                </nav>
                <div className="justify-content-md-center row">
                    {this.state.merged.length === 0 ? <h3>..Aucun resultat..</h3> :
                        <ul className="ul">
                            {this.state.merged.map(function (item, index) {
                                let link = "room/" + item.id;
                                return (
                                    <Link key={item.id} className="nav-link text-black" to={link}>
                                        <div className="hotel-view">
                                            <div className="media">
                                                <div className="media-left media-middle">
                                                    <img src={item.name === "STANDARD" ? stand : item.name === "LUXURY" ? luxe : item.name === "TOURISM" ? tourism : comfort } alt="image" className="media-object"/>
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
            </div>
        );
    }
}

export default Hotels;
