import {Container, Row, Col} from "react-bootstrap";
import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import HotelList from "./HotelList";

class Home extends Component {
    /**
     * Contexte d'authentification
     */
    static contextType = AuthContext;

    render() {
        const auth = this.context;

        return (
            auth.authenticated ?
                <HotelList/>
            :
                <Container fluid className={"flex-center full-height"}>
                    <Row className="justify-content-md-center">
                        <Col className="col-12 text-center">
                            <h1 className="display-3">Bienvenue</h1>
                        </Col>
                        <Col className="col-12 text-center">
                            <h3 className="text-muted">Connectez-vous pour réserver un hôtel.</h3>
                        </Col>
                    </Row>
                </Container>
        );
    }
}

export default Home;
