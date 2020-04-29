const {Model, DataTypes} = require('sequelize');
const connection = require("../dbconnection");

class Occupation extends Model {}
Occupation.init({
    id: {
        type: DataTypes.INTEGER,
        field: 'ID',
        primaryKey: true
    },
    reservation_id: {
        type: DataTypes.INTEGER,
        field: 'RESERVATION_ID'
    },
    room_id: {
        type: DataTypes.INTEGER,
        field: 'ROOM_ID'
    },
    is_client_present: {
        type: DataTypes.BOOLEAN,
        field: 'IS_CLIENT_PRESENT'
    },
    is_archived: {
        type: DataTypes.BOOLEAN,
        field: 'IS_ARCHIVED'
    },
}, {
    modelName: 'occupation',
    tableName: 'OCCUPATIONS',
    sequelize: connection,
    timestamps: false
});

//Exportation du modèle
module.exports = Occupation;

