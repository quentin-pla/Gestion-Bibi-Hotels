const connection = require("../dbconnection");
const {Model, DataTypes} = connection.sequelize;
const Client = require("./Client");
const Hotel = require("./Hotel");
const RoomType = require("./RoomType");
const Bill = require("./Bill");
const Occupation = require("./Occupation");

class Reservation extends Model {}

Reservation.init({
    id: {
        type: DataTypes.INTEGER,
        field: 'ID',
        unique: true
    },
    client_id: {
        type: DataTypes.INTEGER,
        field: 'CLIENT_ID',
        references: {
            model: Client,
            key: 'ID'
        },
        primaryKey: true
    },
    hotel_id: {
        type: DataTypes.INTEGER,
        field: 'HOTEL_ID',
        references: {
            model: Hotel,
            key: 'ID'
        },
        primaryKey: true
    },
    roomtype_id: {
        type: DataTypes.INTEGER,
        defaultValue: 1,
        field: 'ROOMTYPE_ID',
        references: {
            model: RoomType,
            key: 'ID'
        },
        primaryKey: true
    },
    arrival_date: {
        type: DataTypes.DATE,
        field: 'ARRIVAL_DATE',
        defaultValue: DataTypes.NOW,
        primaryKey: true
    },
    exit_date: {
        type: DataTypes.DATE,
        field: 'EXIT_DATE',
        defaultValue: DataTypes.NOW
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

// Reservation.associate = (models) => {
//     Reservation.belongsTo(models.clients, {foreignKey: 'CLIENT_ID'});
//     Reservation.belongsTo(models.hotels, {foreignKey: 'HOTEL_ID'});
//     Reservation.belongsTo(models.roomtypes, {foreignKey: 'ROOMTYPE_ID'});
// };

//Exportation du modèle
module.exports = Reservation;

