import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
import image from "./213502135.png"
import {Link} from "react-router-dom";

class Hotels extends Component {
    /**
     * Contexte d'authentification
     */

    static contextType = AuthContext;
    constructor(props,context) {
        super(props, context);
        /**
         * Initialisation de l'état
         */
        this.state = {
            hotels: []
        };
    }

    componentDidMount() {
        socket.emit("hotels");
        socket.on('hotels_res', (item)=> {
            this.setState({'hotels' : item})
            console.log(this.state.hotels);
        });
    }


    render() {
        return (
                <div className="justify-content-md-center row">
                    <ul className="ul">
                        {this.state.hotels.map(function (item, index) {
                            let link = "hotel/"+item.id;
                            return(
                                <Link key={item.id} className="nav-link text-black" to={link}>
                                    <div className="hotel-view">
                                        <div className="media">
                                            <div className="media-left media-middle">
                                                <img src={image} alt="image" className="media-object"/>
                                            </div>
                                            <div className="media-body">
                                                <h4 className="media-heading">{item.name}</h4>
                                                <h6>Adresse : {item.street}</h6>
                                                <h6>Ville : {item.city}</h6>
                                                <h6>Note : {item.stars}/5</h6>
                                            </div>
                                        </div>
                                    </div>
                                </Link>
                            )
                        })}
                    </ul>
                </div>
        );
    }
}

export default Hotels;
