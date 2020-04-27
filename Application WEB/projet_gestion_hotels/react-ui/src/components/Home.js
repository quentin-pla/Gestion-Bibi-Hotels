import {Container, Row, Col, Button, Form} from "react-bootstrap";
import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";

class Home extends Component {
    /**
     * Contexte d'authentification
     */
    static contextType = AuthContext;

    render() {
        const auth = this.context;

        return (
            <Container className="flex-center position-ref full-height" fluid>
                <Row className="justify-content-md-center">
                    <Col className="col-12 text-center">
                        <h1 className="display-3">{auth.authenticated ? "Bienvenue " + auth.mail : "Bienvenue"}</h1>
                    </Col>
                    <Col className="col-12 text-center">
                        <h3 className="text-muted">{auth.authenticated ? "Réservez un hôtel dès maintenant." : "Connectez-vous pour réserver un hôtel."}</h3>
                    </Col>
                </Row>
            </Container>
        );
    }
}

export default Home;
