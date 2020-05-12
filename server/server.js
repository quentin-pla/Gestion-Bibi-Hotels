/**
 * Utilisation d'express
 */

const express = require('express');

/**
 * Chemins
 */
const path = require('path');

/**
 * Port utilisé pour déployer l'application
 */
const port = process.env.PORT || 3001;

/**
 * Application express
 */
const app = express()
    .use(express.static(path.join(__dirname, '../react-ui/build')))
    .listen(port, () => console.log(`Listening on ${port}`));

/**
 * Socket IO
 */
const io = require('socket.io')(app);

/************************************************/
/************** BASE DE DONNÉES *****************/
/************************************************/

/**
 * Connexion à la base de données
 */
const connection = require(path.join(__dirname, "../react-ui/src/database/dbconnection"));

/**
 * Modèle Client de la base de données
 */
const Client = require(path.join(__dirname, "../react-ui/src/database/models/Client"));

/**
 * Modèle Hotel de la base de données
 */
const Hotel = require(path.join(__dirname, "../react-ui/src/database/models/Hotel"));

/**
 * Modèle Room de la base de données
 */
const Room = require(path.join(__dirname, "../react-ui/src/database/models/Room"));
Room.associate();

/**
 * Modèle Reservation de la base de données
 */
const Reservation = require(path.join(__dirname, "../react-ui/src/database/models/Reservation"));

/**
 * Modèle RoomType de la base de données
 */
const RoomType = require(path.join(__dirname, "../react-ui/src/database/models/RoomType"));

/**
 * Modèle Bill de la base de données
 */
const Bill = require(path.join(__dirname, "../react-ui/src/database/models/Bill"));

/*************************************************/
/************** CLIENT SOCKET IO *****************/
/*************************************************/

/**
 * Lorsque qu'un nouveau client se connecte à l'application
 */
io.sockets.on('connection', function (socket) {

    /******************************************/
    /*********** AUTHENTIFICATION *************/
    /******************************************/

    /**
     * Authentification de l'utilisateur
     */
    socket.on('login', function (mail, password) {
        //On essaye de trouver un client dans la base de données
        Client.findOne({
            //Sélection de l'adresse mail et du mot de passe
            attributes: ['MAIL', 'PASSWORD'],
            //L'adresse mail du client doit être égale à celle passée en paramètres
            where: {
                MAIL: mail
            }
            //Une fois le tuple trouvé dans la BDD
        }).then((item) => {
            //Vérification que le client a bien été trouvé dans la base de données
            if (item !== null) {
                //Comparaison du mot de passe avec celui passé en paramètre
                if (item.dataValues.PASSWORD === password) {
                    //Renvoi d'un message de validation de l'authentification vers la classe Login
                    socket.emit('auth_info', true);
                }
                //Renvoi d'un message d'erreur si mot de passe invalide
                else socket.emit('auth_info', false);
            }
            //Renvoi d'un message d'erreur si le client est introuvable
            else socket.emit('auth_info', false);
        });
    });

    /**
     * Mise a jour du profil
     */
    socket.on("update_user", function (data) {
        Client.update({
            mail: data.mail,
            firstname: data.firstname,
            lastname: data.lastname,
            street: data.street,
            city: data.city,
            password: data.password
        }, {where: {MAIL: data.mail}}).then((item) => {
            if (item !== null) {
                socket.emit('update_result', true);
            } else socket.emit('update_result', false);
        });
    });


    /**
     * Inscription de l'utilisateur
     */
    socket.on('signup', function (data) {
        //Création d'un nouveau client dans la base de données
        Client.create({
            mail: data.mail,
            firstname: data.firstname,
            lastname: data.lastname,
            street: data.street,
            city: data.city,
            password: data.password
        }).then(() => {
            //Émission d'un message de succès à la classe Signup
            socket.emit('auth_info', true, '');
        }).catch((err) => {
            //Si l'erreur provient de la duplication d'une clé primaire
            if (err.name === "SequelizeUniqueConstraintError") {
                //Émission d'un message d'erreur, forcément l'adresse mail
                socket.emit('auth_info', false, 'Adresse mail déjà utilisée');
            }
        });
    });

    /**
     * Recuperation des infos de l'utilisateur
     */
    socket.on('profil', function (mail) {
        Client.findOne({
            where: {
                MAIL: mail
            }
        }).then((item) => {
            if (item !== null)
                socket.emit('profil_info', item);
        })
    });

    /**
     * Récupération de toutes les chambres disponibles pour la date du jour
     */
    socket.on('rooms', async (dateA,dateD) => {
        const availableRooms = await connection.query("call getAvailableRooms('" + dateA + "','" + dateD + "')");
        const roomIds = [];
        availableRooms.forEach((room) => roomIds.push(room.ID));
        Room.findAll({
            where: {ID: roomIds},
            include: ['hotel','roomtype'],
            group: ['hotel.hotel_name','roomtype.name']
        }).then((rooms) => {
            socket.emit('rooms_res', rooms);
        })
    });

    /**
     * Recuperation d'une chambre par rapport a son identifiant
     */
    socket.on('room_id', function (id) {
        Room.findOne({
            where: {ID: id},
            include: ['hotel','roomtype']
        }).then((room) => {
            socket.emit("roomId_res", room);
        });
    });

    /**
     * Ajoute une nouvelle reservation dans la BD
     */
    socket.on('reserver', function (data) {
        Client.findOne({
            where: {MAIL: data.mail}
        }).then((client) => {
            Reservation.create({
                client_id: client.id,
                hotel_id: data.hotel_id,
                roomtype_id: data.room_id,
                arrival_date: data.dateA,
                exit_date: data.dateD,
                duration: data.duree,
                room_count: data.nbchambres,
                people_count: data.nbpersonnes,
                is_payed: 0,
                is_comfirmed: 0,
                is_cancelled: 0,
                is_archived: 0
            }).then(() => {
                socket.emit('reservation_res', true);
            }).catch(() => {
                socket.emit('reservation_res', false, 'Erreur, verifiez que les champs ont été remplis correctement');
            })
        })
    });

    /**
     * Renvoie les chambres correspondant aux criteres choisis par l'utilisateur
     */
    socket.on('apply_filter', async (data) => {
        const {Op} = require('sequelize');
        const query = (data.ville.length > 0)
            ? "call getAvailableRoomsByCity('" + data.ville + "','" + data.dateA + "','" + data.dateD + "')"
            : "call getAvailableRooms('" + data.dateA + "','" + data.dateD + "')";
        const availableRooms = await connection.query(query);
        const roomIds = [];
        availableRooms.forEach((room) => roomIds.push(room.ID));
        Room.findAll({
            where: {ID: roomIds},
            include: [{model: Hotel},{model: RoomType, where: {BED_CAPACITY: {[Op.gte]: data.nbPersonnes/2}}}], // {[Op.gte]: data.nbPersonnes/2}
            group: ['hotel.hotel_name','roomtype.name']
        }).then((rooms) => {
            socket.emit('rooms_res', rooms);
        })
    });

    /**
     * Recupere les réservations de l'utilisateur
     */
    socket.on('user_reservation', function (mail) {
        Client.findOne({
            where: {
                MAIL: mail
            }
        }).then((client) => {
            Reservation.findAll({
                where: {
                    CLIENT_ID: client.id
                }
            }).then((reservation) => {
                Hotel.findAll({}).then((hotel) => {
                    RoomType.findAll({}).then((roomtype) => {
                        socket.emit("user_reservation_res", reservation, hotel, roomtype);
                    })
                })
            })
        })
    });

    /**
     * Permet de payer une reservation
     */
    socket.on("pay_reservation", function (id) {
        Reservation.update({
            is_payed: true
        }, {where: {ID: id}}).catch((err) => {
            console.log(err)
        })
    });
    /**
     * Permet de confirmer une reservation
     */
    socket.on("confirm_reservation", function (id) {
        Reservation.update({
            is_comfirmed: true
        }, {where: {ID: id}})
    });

    /**
     * Permet d'annuler une reservation
     */
    socket.on("cancel_reservation", function (id) {
        Reservation.update({
            is_cancelled: true
        }, {where: {ID: id}})
    });

    /**
     * Recupere les factures de l'utilisateur
     */
    socket.on("bills", function (mail) {
        Client.findOne({
            where: {
                MAIL: mail
            }
        }).then((client) => {
            Bill.findAll({
                where: {
                    CLIENT_ID: client.id
                }
            }).then((bill) => {
                if (bill !== null) {
                    Reservation.findAll({}).then((reservation) => {
                        Hotel.findAll({}).then((hotel) => {
                            socket.emit('bills_res', true, bill, reservation, hotel);
                        })
                    })
                } else {
                    socket.emit('bills_res', false);
                    return false
                }
            })
        })
    });

    /**
     * Lorsque le client se déconnecte
     */
    socket.on('disconnect', () => {
        socket.removeAllListeners();
    });
});
