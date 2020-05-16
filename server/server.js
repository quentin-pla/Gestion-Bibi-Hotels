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
 * Modèle RoomType de la base de données
 */
const RoomType = require(path.join(__dirname, "../react-ui/src/database/models/RoomType"));

/**
 * Modèle Room de la base de données
 */
const Room = require(path.join(__dirname, "../react-ui/src/database/models/Room"));

/**
 * Modèle Reservation de la base de données
 */
const Reservation = require(path.join(__dirname, "../react-ui/src/database/models/Reservation"));

/**
 * Modèle Bill de la base de données
 */
const Bill = require(path.join(__dirname, "../react-ui/src/database/models/Bill"));

//Association des modèles
Room.associate();
Reservation.associate();
Bill.associate();

/**
 * Messages d'erreur
 */
const errorMessages = {
    "OVERLAPPING": "Une réservation est déjà affectée à cette période",
    "NO_ROOM_AVAILABLE": "Pas assez de chambres disponibles"
};

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
            where: {MAIL: mail}
            //Une fois le tuple trouvé dans la BDD
        }).then((client) => {
            //Vérification que le client a bien été trouvé dans la base de données
            if (client !== null) {
                //Comparaison du mot de passe avec celui passé en paramètre
                if (client.checkPassword(password)) {
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
     * Mise a jour du profil
     */
    socket.on("update_user",(data, mail) => {
        Client.update(data, {where: {MAIL: mail}}).then((item) => {
            socket.emit('update_result', item !== null);
        });
    });

    /**
     * Recuperation des infos de l'utilisateur
     */
    socket.on('profil',(mail) => {
        Client.findOne({
            where: {MAIL: mail}
        }).then((client) => {
            if (client !== null) socket.emit('profil_info', client);
        })
    });

    /**
     * Récupération de toutes les chambres disponibles pour la date du jour
     */
    socket.on('rooms', async (dateA,dateD) => {
        const query = "call getAvailableRooms('" + dateA + "','" + dateD + "')";
        const availableRooms = await connection.query(query);
        const roomIds = [];
        availableRooms.forEach((room) => roomIds.push(room.ID));
        Room.findAll({
            where: {ID: roomIds},
            include: ['hotel','roomtype'],
            group: ['hotel.hotel_name','roomtype.name']
        }).then((rooms) => {
            socket.emit('rooms_res', rooms);
        });
    });

    /**
     * Récupérer le nombre de chambres disponibles dans un hotel précis, pour un type de chambre donné et une période
     */
    socket.on('rooms_count', async (hotel_id,dateA,dateD,roomtype_id) => {
        const roomsCount = await connection.query(
            "call checkRoomAvailablility("
            + hotel_id + ",'"
            + dateA + "','" + dateD + "',"
            + roomtype_id + ")"
        );
        socket.emit('rooms_count_res', roomsCount.length);
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
    socket.on('reserver', async (data) => {
        const availableRooms = await connection.query(
            "call checkRoomAvailablility("
            + data.hotel_id + ",'"
            + data.arrival_date + "','" + data.exit_date + "',"
            + data.roomtype_id + ")"
        );
        const roomIds = [];
        availableRooms.forEach((room) => roomIds.push(room.ID));
        if (roomIds.length === 0) socket.emit('reservation_res', false, "Chambre indisponible pour cette période");

        Client.findOne({
            where: {MAIL: data.mail}
        }).then((client) => {
            Reservation.create({
                client_id: client.id,
                hotel_id: data.hotel_id,
                roomtype_id: data.roomtype_id,
                arrival_date: new Date(data.arrival_date + " 00:00:00 +02:00"),
                exit_date: new Date(data.exit_date + " 00:00:00 +02:00"),
                duration: data.duration,
                room_count: data.room_count,
                people_count: data.people_count,
                is_payed: 0,
                is_comfirmed: 0,
                is_cancelled: 0,
                is_archived: 0
            }).then((result) => {
                for (let i = 1; i <= data.room_count; i++) {
                    Occupation.create({
                        reservation_id: result.null,
                        room_id: roomIds.shift(),
                        is_client_present: 0,
                        is_archived: 0
                    }).catch((err) => socket.emit('reservation_res', false, err.parent.sqlMessage));
                }
                socket.emit('reservation_res', true, null);
            }).catch((err) => socket.emit('reservation_res', false, errorMessages[err.parent.sqlMessage]));
        }).catch((err) => socket.emit('reservation_res', false, errorMessages[err.parent.sqlMessage]));
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
            include: [{model: Hotel},{model: RoomType, where: {BED_CAPACITY: {[Op.gte]: data.capacite/2}}}],
            group: ['hotel.hotel_name','roomtype.name']
        }).then((rooms) => {
            socket.emit('rooms_res', rooms);
        })
    });

    /**
     * Récupèrer les réservations de l'utilisateur
     */
    socket.on('user_reservations', (mail) => {
        Client.findOne({
            where: {MAIL: mail}
        }).then((client) => {
            Reservation.findAll({
                where: {CLIENT_ID: client.id},
                include: [{model: Hotel},{model: RoomType}]
            }).then((reservations) => {
                socket.emit("user_reservations_res", reservations);
            })
        });
    });

    /**
     * Recupere les factures de l'utilisateur
     */
    socket.on("user_bills", (mail) => {
        Client.findOne({
            where: {MAIL: mail}
        }).then((client) => {
            Bill.findAll({
                where: {CLIENT_ID: client.id},
                include: [{model: Reservation, include: [{model: Hotel},{model: RoomType}]}]
            }).then((bills) => {
                socket.emit("user_bills_res", bills);
            })
        })
    });

    /**
     * Permet de payer une reservation
     */
    socket.on("pay_reservation", (id) => {
        Reservation.update({
            is_payed: true
        }, {where: {ID: id}}).catch((err) => {
            console.log(err)
        });
    });
    /**
     * Permet de confirmer une reservation
     */
    socket.on("confirm_reservation", function (id) {
        Reservation.update({
            is_comfirmed: true
        }, {where: {ID: id}}).catch((err) => {
            console.log(err)
        });
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
     * Lorsque le client se déconnecte
     */
    socket.on('disconnect', () => {
        socket.removeAllListeners();
    });
});
