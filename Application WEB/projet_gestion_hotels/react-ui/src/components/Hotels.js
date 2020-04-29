import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
import image from "./213502135.png"
import {Link} from "react-router-dom";
import {Button, Form} from "react-bootstrap";

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
            confort : "",
            nblits: 0,
            merged: []
        };

        this.mergeArrayObjects = this.mergeArrayObjects.bind(this);
        this.applyFilter = this.applyFilter.bind(this);
    }

    componentDidMount() {
        socket.emit("rooms");
        socket.on('rooms_res', (item1,item2,item3) => {
            this.setState({'rooms': item1});
            this.setState({'hotels': item2});
            this.setState({'roomtype': item3});
            this.mergeArrayObjects(this.state.rooms, this.state.roomtype, this.state.hotels);
        });
    }

    applyFilter(){
        let data = {
            ville : this.state.ville,
            confort: this.state.confort,
            nblits: this.state.nblits
        };
        socket.emit("apply_filter",data);
        socket.on("filter_res", (item) => {
            this.setState({"rooms" : item});
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
                        <select name="type de chambre" onChange={e => {
                            this.setState({"confort": e.target.value});
                        }}>
                            <option value="business">Business</option>
                            <option value="classic">Classic</option>
                            <option value="luxe">Luxe</option>
                        </select>
                        <input placeholder="nombre de lits" type="number" onChange={e => {
                            this.setState({"nblits": e.target.value});
                        }}/>
                        <input placeholder="ville" type="text" onChange={e => {
                            this.setState({"ville": e.target.value});
                        }}/>
                        <label className="label">Date d'arrivée:</label>
                        <input type="datetime-local" />
                        <label className="label">Date de départ:</label>
                        <input type="datetime-local"/>
                        <Button variant="dark" onClick={(e) => this.applyFilter()}>
                            Valider
                        </Button>
                    </form>
                </nav>
                <div className="justify-content-md-center row">
                    <ul className="ul">
                        {this.state.merged.map(function (item, index) {
                            let link = "room/" + item.id;
                            return (
                                <Link key={item.id} className="nav-link text-black" to={link}>
                                    <div className="hotel-view">
                                        <div className="media">
                                            <div className="media-left media-middle">
                                                <img src={image} alt="image" className="media-object"/>
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
                </div>
            </div>
        );
    }
}

export default Hotels;
