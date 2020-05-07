import React, {Component} from 'react';
import {BrowserRouter as Router, Switch, Route, Link, Redirect,} from "react-router-dom";
import {Navbar, Nav} from "react-bootstrap";
import "./App.css";
import PrivateRoute from "./components/routing/PrivateRoute";
import Home from "./components/Home";
import Login from "./components/auth/Login";
import Profil from './components/Profil'
import Signup from './components/auth/Signup';
import AuthButton from './components/auth/AuthButton';
import {AuthContext} from "./context/AuthContext";
import HotelView from "./components/HotelView";
import Reservation from "./components/Reservation";
import Facture from "./components/Facture";

class App extends Component {
    /**
     * Contexte d'authentification
     */
    static contextType = AuthContext;

    render() {
        return (
            <Router>
                <Navbar sticky="top" className="navbar-top mb-5" bg={"light"} variant={"light"}>
                    <Navbar.Brand>
                        <Link className="navbar-brand text-primary" to="/">
                            <img alt="logo" src={process.env.PUBLIC_URL + '/bibi.PNG'} width="30" height="30"
                                 className="d-inline-block mb-1 mr-2"/>
                                 Hôtels Bibi
                        </Link>
                    </Navbar.Brand>
                    <NavLinks authenticated={this.context.authenticated}/>
                    <Navbar.Collapse className="justify-content-end">
                        <Navbar.Text>
                            <AuthButton/>
                        </Navbar.Text>
                    </Navbar.Collapse>
                </Navbar>
                <Switch>
                    <Route exact path="/">
                        <Home />
                    </Route>
                    <Route path="/login">
                        <Login/>
                    </Route>
                    <Route path="/signup">
                        <Signup/>
                    </Route>
                    <Route path="/room/:id" component={HotelView}>
                        <HotelView/>
                    </Route>
                    <PrivateRoute path="/profil">
                        <Profil/>
                    </PrivateRoute>
                    <PrivateRoute path="/reservation">
                        <Reservation/>
                    </PrivateRoute>
                    <PrivateRoute path="/facture">
                        <Facture />
                    </PrivateRoute>
                    <Redirect to="/"/>
                </Switch>
            </Router>
        );
    }
}

/**
 * Liens vers les différentes pages de l'application
 */
function NavLinks(props) {
    //Si l'utilisateur est authentifié
    return props.authenticated ? (
        <Nav className="mr-auto">
            <Link className="nav-link" to="/profil">Profil</Link>
            <Link className="nav-link" to="/reservation">Réservations</Link>
            <Link className="nav-link" to="/facture">Factures</Link>
        </Nav>
    ) : (
        <Nav className="mr-auto"/>
    );
}

export default App;
