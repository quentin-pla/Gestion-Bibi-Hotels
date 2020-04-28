import React, {Component} from "react";
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
import {Button, Card, Form} from "react-bootstrap";

class HotelView extends Component {
    /**
     * Contexte d'authentification
     */

    static contextType = AuthContext;
    constructor(props,context) {
        super(props, context);
        this.state = {
            id: ""
        }

    }

    componentDidMount() {
        let id = window.location.href.substring(window.location.href.lastIndexOf("/")+1);
        this.setState({"id": id});

    }

    render() {
        return (
            <div>{this.state.id}
                <Form noValidate validated={this.state.validated}>
                    <Form.Group>
                        <Form.Control
                            required
                            type="email"
                            min="5"
                            onChange={e => {
                                this.context.setMail(e.target.value);
                            }}
                            value={this.state.mail}
                            placeholder="Adresse Mail"
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Control
                            required
                            type="text"
                            min="3"
                            onChange={e => {
                                this.setState({lastname: e.target.value});
                            }}
                            value={this.state.lastname}
                            placeholder="Nom"
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Control
                            required
                            type="text"
                            min="3"
                            onChange={e => {
                                this.setState({firstname: e.target.value});
                            }}
                            value={this.state.firstname}
                            placeholder="Prénom"
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Control
                            required
                            type="text"
                            min="3"
                            onChange={e => {
                                this.setState({street: e.target.value});
                            }}
                            value={this.state.street}
                            placeholder="Adresse"
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Control
                            required
                            type="text"
                            min="3"
                            onChange={e => {
                                this.setState({city: e.target.value});
                            }}
                            value={this.state.city}
                            placeholder="Ville"
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Control
                            required
                            type="password"
                            min="3"
                            onChange={e => {
                                this.context.setPassword(e.target.value);
                            }}
                            placeholder="Mot de passe"
                        />
                    </Form.Group>
                    <Form.Group>
                        <Form.Control
                            required
                            type="password"
                            min="3"
                            onChange={e => {
                                this.setState({password: e.target.value});
                            }}
                            placeholder="Confirmation"
                        />
                    </Form.Group>
                    <Button variant="dark" className="mt-4 mb-4" onClick={this.handleSubmit} block>
                        Modifier
                    </Button>
                    <Card.Text className="text-danger error-message mt-3">{this.state.isError ? this.state.errorMessage : ""} </Card.Text>
                </Form>

            </div>

        );
    }
}

export default HotelView;
