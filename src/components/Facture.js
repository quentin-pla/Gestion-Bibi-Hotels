import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";


class Facture extends Component {

    static contextType = AuthContext;

    constructor(props, context) {
        super(props, context);
    }

    render() {
        return (
            <div>HELLO</div>
        )
    }
}
export default Facture;