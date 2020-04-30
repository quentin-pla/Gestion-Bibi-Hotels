const {Model, DataTypes} = require('sequelize');
const connection = require("../dbconnection");

class Room extends Model {}
Room.init({
    id: {
        type: DataTypes.INTEGER,
        field: 'ID',
        primaryKey: true
    },
    hotel_id: {
        type: DataTypes.INTEGER,
        field: 'HOTEL_ID'
    },
    roomtype_id: {
        type: DataTypes.STRING,
        field: 'ROOMTYPE_ID'
    }
}, {
    modelName: 'rooms',
    tableName: 'ROOMS',
    sequelize: connection,
    timestamps: false
});

//Exportation du modèle
module.exports = Room;

