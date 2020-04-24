import React, {Component} from 'react';
import {BrowserRouter as Router, Switch, Route, Link, Redirect,} from "react-router-dom";
import {Navbar, Nav} from "react-bootstrap";
import "./App.css";
import PrivateRoute from "./components/routing/PrivateRoute";
import Home from "./components/Home";
import Login from "./components/auth/Login";
import Signup from './components/auth/Signup';
import AuthButton from './components/auth/AuthButton';
import {AuthContext} from "./context/AuthContext";

class App extends Component {
    /**
     * Contexte d'authentification
     */
    static contextType = AuthContext;

    constructor(props, context) {
        super(props, context);
    }

    render() {
        return (
            <Router>
                <Navbar bg="dark">
                    <Navbar.Brand>
                        <Link className="navbar-brand text-white" to="/">
                            BestHotels
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
                    <PrivateRoute path="/profile">
                        <h3>Profil utilisateur</h3>
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
            <Link className="nav-link text-white" to="/profile">Profil</Link>
        </Nav>
    ) : (
        <Nav className="mr-auto"/>
    );
}

export default App;
