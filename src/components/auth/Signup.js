import React, {Component} from "react";
import {Link, Redirect} from 'react-router-dom';
import {Card, Form, Button, Row, Container} from "react-bootstrap";
import socket from "../../context/SocketIOInstance";
import {AuthContext} from "../../context/AuthContext";

/**
 * Inscription
 */
class Signup extends Component {
    /**
     * Contexte d'authentification
     */
    static contextType = AuthContext;

    constructor(props,context) {
        super(props,context);

        //Initialisation de l'état
        this.state = {
            //Erreur d'inscription
            isError: false,
            //Message d'erreur
            errorMessage: "",
            //Nom
            firstname: "",
            //Prénom
            lastname: "",
            //Rue
            street: "",
            //Ville
            city: "",
            //Confirmation du mot de passe
            password_confirm: ""
        };

        this.handleSubmit = this.handleSubmit.bind(this);
        this.submitLoginEnter = this.submitLoginEnter.bind(this);
    }

    /**
     * Vérifier qu'un texte valide une syntaxe
     * @param text
     * @param syntax syntaxe autorisée
     */
    isValid(text, syntax) {
        //Si le texte n'est pas définit
        if (text === null) return false;
        //Renvoi vrai si le texte respecte la syntaxe
        return text.match(syntax);
    }

    /**
     * Demande d'inscription
     */
    handleSubmit() {
        //Syntaxe alphanumérique avec espace
        const alphanumSyntax = /^[0-9a-zA-Z ]+$/;
        //Syntaxe seulement caractères de l'alphabet
        const streetSyntax = /^[a-zA-Z /']+$/;
        //Vérification des champs nom, prénom, rue, ville
        let fieldsCheck = (
            this.isValid(this.state.lastname, streetSyntax) &&
            this.isValid(this.state.firstname, streetSyntax) &&
            this.isValid(this.state.street, alphanumSyntax) &&
            this.isValid(this.state.city, streetSyntax)
        );
        //Vérifications du mot de passe
        let passwordCheck = this.context.password.length >= 3;
        //Vérification de la confirmation du mot de passe
        let confirmCheck = this.context.password === this.state.password_confirm;
        //Validation des champs nom, prénom, rue, ville
        if (!fieldsCheck) {
            this.setState({isError: true, errorMessage: "Utilisation de caractères invalides"});
            return false;
        }
        //Validation du mot de passe
        if (!passwordCheck) {
            this.setState({isError: true, errorMessage: "Mot de passe invalide"});
            return false;
        }
        //Validation de la confirmation
        if (!confirmCheck) {
            this.setState({isError: true, errorMessage: "Confirmation invalide"});
            return false;
        }
        //Compression des données dans un objet
        let data = {
            mail: this.context.mail,
            firstname: this.state.firstname,
            lastname: this.state.lastname,
            street: this.state.street,
            city: this.state.city,
            password: this.context.password
        };
        //Émission d'une demande d'inscription
        socket.emit("signup", data);
        //Récupération des informations d'inscription
        socket.on("auth_info", (success, errorMessage) => {
            //Si c'est un succès
            if (success) {
                //Authentification validée
                this.context.setAuthenticated(true);
                alert("Informations modifiée avec succès");
            }
            //Affichage d'une erreur
            else this.setState({isError: true, errorMessage: errorMessage});
        });
        return false;
    }

    /**
     * Connexion en appuyant sur entrée
     * @param event touche du clavier
     */
    submitLoginEnter(event) {
        if (event.which === 13) this.postLogin();
    }

    render() {
        //Si l'utilisateur est authentifié, redirection vers la page d'accueil
        if (this.context.authenticated) return <Redirect to="/"/>;

        return (
            <Container className="flex-center position-ref full-height" fluid>
                <Row className="justify-content-md-center">
                    <Card style={{width: '20rem'}} className="text-center no-boder fade-effect" body>
                        <Card.Title className="display-4 text-left mb-4">
                            Inscription
                        </Card.Title>
                        <Form noValidate validated={this.state.validated}>
                            <Form.Group>
                                <Form.Control
                                    required
                                    type="email"
                                    min="5"
                                    onChange={e => {
                                        this.context.setMail(e.target.value);
                                    }}
                                    placeholder="Adresse Mail"
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
                                    placeholder="Nom"
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
                                        this.setState({password_confirm: e.target.value});
                                    }}
                                    onKeyPress={e => this.submitLoginEnter(e)}
                                    placeholder="Confirmation"
                                />
                            </Form.Group>
                            <Button variant="dark" className="mt-4 mb-4" onClick={this.handleSubmit} block>
                                Valider
                            </Button>
                        </Form>
                        <Link to="/login">Vous avez déjà un compte ?</Link>
                        <Card.Text
                            className="text-danger error-message mt-3">{this.state.isError ? this.state.errorMessage : ""}</Card.Text>
                    </Card>
                </Row>
            </Container>
        );
    }
}

export default Signup;
