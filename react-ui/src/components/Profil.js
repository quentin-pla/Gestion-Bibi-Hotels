import React, {Component} from "react";
import {Form, Button, Card, Container, Row} from "react-bootstrap";
import {withRouter} from 'react-router-dom';
import {AuthContext} from "../context/AuthContext";
import socket from "../context/SocketIOInstance";
/**
 * Connexion
 */
class Profil extends Component {
    /**
     * Contexte d'authentification
     */
    static contextType = AuthContext;
    constructor(props,context) {
        super(props,context);

        /**
         * Initialisation de l'état
         */
        this.state = {
            //Erreur d'inscription
            isError: false,
            //Message d'erreur
            errorMessage: "",
            //Nom
            firstname: "",
            //Prénom
            lastname: "",
            //mail
            mail: "",
            //Rue
            street: "",
            //Ville
            city: "",
            //Confirmation du mot de passe
            password: "",
        };
    }

    componentDidMount() {
        const self = this;
        const auth = this.context;
        socket.emit("profil",auth.mail);
        socket.on("profil_info",(item)=>{
            this.setState({'firstname': item.firstname});
            this.setState({'lastname': item.lastname});
            this.setState({'street': item.street});
            this.setState({'city': item.city});
            this.setState({'mail': item.mail});
            this.setState({'password': item.mapasswordil});
            self.setState({data: item});
        });
        this.handleSubmit = this.handleSubmit.bind(this);

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
        let confirmCheck = this.context.password === this.state.password;
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
        //socket.emit("signup", data);
        socket.emit("update_user",data);
        // Récupération des informations d'inscription
        socket.on("update_result", (success, errorMessage) => {
            //Si c'est un succès
            if (success) {
                this.context.setMail(this.state.mail);
                this.context.setPassword(this.state.password);
                alert("Informations modifiée avec succes")
            }
            //Affichage d'une erreur
            else this.setState({isError: true, errorMessage: errorMessage});
        });
        return false;
    }

    render() {
        return (
            <Container fluid>
                <Row className="User_info">
                    {this.state.data ?
                        <UserInfo data={this.state.data}/> : <h1>loading</h1>
                    }
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
                </Row>
            </Container>
        );
    }
}

function UserInfo(data){
        return(
            <div className="container">
                <h2>Profile Utilisateur</h2>
                <p>Pour modifier vos informations veuillez remplir le formulaire et cliquer sur valider</p>
                <table className="table">
                    <thead>
                    <tr>
                        <th>Prenom </th>
                        <th>{data.data.firstname}</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Nom</td>
                        <td>{data.data.lastname}</td>
                    </tr>
                    <tr>
                        <td>Adresse</td>
                        <td>{data.data.street}</td>
                    </tr>
                    <tr>
                        <td>Ville</td>
                        <td>{data.data.city}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        )
}

//withRouter pour récupérer l'historique
export default withRouter(Profil);
