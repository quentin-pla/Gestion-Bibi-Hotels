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
 * Modèle Client de la base de données
 */
const Client = require(path.join(__dirname, "../react-ui/src/database/models/Client"));

const Hotel = require(path.join(__dirname,"../react-ui/src/database/models/Hotel"));

const Room = require(path.join(__dirname,"../react-ui/src/database/models/Room"));

const Reservation = require(path.join(__dirname,"../react-ui/src/database/models/Reservation"));

const RoomType = require(path.join(__dirname,"../react-ui/src/database/models/RoomType"));
/**
 * Connexion à la base de données
 */
const connection = require(path.join(__dirname, "../react-ui/src/database/dbconnection"));

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
            attributes: ['MAIL','PASSWORD'],
            //L'adresse mail du client doit être égale à celle passée en paramètres
            where: {
                MAIL: mail
            }
        //Une fois le tuple trouvé dans la BDD
        }).then((item) => {
            //Vérification que le client a bien été trouvé dans la base de données
            if (item !== null) {
                //Comparaison du mot de passe avec celui passé en paramètre
                if (item.dataValues.PASSWORD === password){
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

    socket.on("update_user", function (data) {
        Client.update({
            mail:       data.mail,
            firstname:  data.firstname,
            lastname:   data.lastname,
            street:     data.street,
            city:       data.city,
            password:   data.password

        }, {where: {MAIL: data.mail}}).then((item) => {
            if (item !== null){
                socket.emit('update_result',true);
            }else socket.emit('update_result',false);
        });
    });


    /**
     * Inscription de l'utilisateur
     */
    socket.on('signup', function (data) {
        //Création d'un nouveau client dans la base de données
        Client.create({
            mail:       data.mail,
            firstname:  data.firstname,
            lastname:   data.lastname,
            street:     data.street,
            city:       data.city,
            password:   data.password
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

    socket.on('profil', function(mail){
       Client.findOne({
           where: {
               MAIL : mail
           }
       }).then((item) => {
           if (item !== null)
               socket.emit('profil_info', item);
       })
    });

    //SELECT * from ROOMS JOIN ROOMTYPES on ROOMS.ROOMTYPE_ID = ROOMTYPES.ID JOIN HOTELS ON ROOMS.HOTEL_ID = HOTELS.ID

    socket.on('rooms', function(){
        Room.findAll({
        }).then((rooms) => {
            RoomType.findAll({
            }).then((roomTypes) =>{
                Hotel.findAll({
                }).then((hotels) => {
                    socket.emit('rooms_res',rooms,hotels,roomTypes);
                })
            })
        })
    });


    socket.on('room_id',function (id) {
        Room.findOne({
            where : {
                ID : id
            }
        }).then((room) => {
            RoomType.findOne({
                where : {
                    ID : room.roomtype_id
                }
            }).then((roomtype) => {
                Hotel.findOne({
                    where : {
                        ID : room.hotel_id
                    }
                }).then((hotel) => {
                    socket.emit("roomId_res", room, roomtype, hotel);
                })
            })
        })
    });


    socket.on('reserver', function (data) {
        Client.findOne({
            where: {
                MAIL: data.mail
            }
        }).then((client) => {
            console.log(data);
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
            }).then((reservation) => {
                socket.emit('reservation_res', true);
            }).catch((err) => {
                socket.emit('reservation_res', false, 'Erreur verifiée que les champs ont été remplis correctement');
            })
        })
    });


    //SELECT * from ROOMS JOIN ROOMTYPES on ROOMS.ROOMTYPE_ID = ROOMTYPES.ID JOIN HOTELS ON ROOMS.HOTEL_ID = HOTELS.ID WHERE ROOMTYPES.NAME ="LUXURY" AND HOTELS.CITY = "Nice" AND ROOMTYPES.BED_CAPACITY >= 1
    socket.on('apply_filter', function (data) {

    });

    socket.on('user_reservation',function(mail){
        Client.findOne({
            where : {
                MAIL : mail
            }
        }).then((client) => {
            Reservation.findAll({
                where : {
                    CLIENT_ID : client.id
                }
            }).then((reservation) => {
                Hotel.findAll({
                }).then((hotel)=> {
                    RoomType.findAll({
                    }).then((roomtype) => {
                        socket.emit("user_reservation_res", reservation, hotel, roomtype);
                    })
                })
            })
        })
    });

    socket.on("pay_reservation", function (id){
        console.log("pay");
        Reservation.update({
            is_payed : true
        }, {where : {ID : id}}).catch((err) => {
            console.log(err)
        })
    });

    socket.on("confirm_reservation", function(id){
        Reservation.update({
            is_comfirmed : true
        }, {where : {ID : id}})
    });

    socket.on("cancel_reservation", function(id){
        console.log("canc");
        console.log(id);

        Reservation.update({
            is_cancelled : true
        }, {where : {ID : id}})
    });

    /**
     * Lorsque le client se déconnecte
     */
    socket.on('disconnect', () => {
        socket.removeAllListeners();
    });
});
