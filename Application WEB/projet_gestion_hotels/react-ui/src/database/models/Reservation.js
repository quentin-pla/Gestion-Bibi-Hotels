const {Model, DataTypes} = require('sequelize');
const connection = require("../dbconnection");

class Reservation extends Model {}
Reservation.init({
    id: {
        type: DataTypes.INTEGER,
        field: 'ID',
        primaryKey: true
    },
    client_id: {
        type: DataTypes.INTEGER,
        field: 'CLIENT_ID'
    },
    hotel_id: {
        type: DataTypes.INTEGER,
        field: 'HOTEL_ID'
    },
    roomtype_id: {
        type: DataTypes.INTEGER,
        defaultValue: 1,
        field: 'ROOMTYPE_ID'
    },
    arrival_date: {
        type: DataTypes.DATE,
        field: 'ARRIVAL_DATE'
    },
    exit_date: {
        type: DataTypes.DATE,
        field: 'EXIT_DATE'
    },
    duration: {
        type: DataTypes.INTEGER,
        field: 'DURATION'
    },
    room_count: {
        type: DataTypes.INTEGER,
        field: 'ROOM_COUNT'
    },
    people_count: {
        type: DataTypes.INTEGER,
        field: 'PEOPLE_COUNT'
    },
    is_payed: {
        type: DataTypes.BOOLEAN,
        field: 'IS_PAYED'
    },
    is_comfirmed: {
        type: DataTypes.BOOLEAN,
        field: 'IS_COMFIRMED'
    },
    is_cancelled: {
        type: DataTypes.BOOLEAN,
        field: 'IS_CANCELLED'
    },
    is_archived: {
        type: DataTypes.BOOLEAN,
        field: 'IS_ARCHIVED'
    },
}, {
    modelName: 'reservation',
    tableName: 'RESERVATIONS',
    sequelize: connection,
    timestamps: false
});

//Exportation du modèle
module.exports = Reservation;

