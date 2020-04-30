const {Model, DataTypes} = require('sequelize');
const connection = require("../dbconnection");

class Client extends Model {}
Client.init({
    mail: {
        type: DataTypes.STRING,
        field: 'MAIL'
    },
    firstname: {
        type: DataTypes.STRING,
        field: 'FIRSTNAME'
    },
    lastname: {
        type: DataTypes.STRING,
        field: 'LASTNAME'
    },
    street: {
        type: DataTypes.STRING,
        field: 'STREET'
    },
    city: {
        type: DataTypes.STRING,
        field: 'CITY'
    },
    password: {
        type: DataTypes.STRING,
        field: 'PASSWORD'
    },
    is_regular: {
        type: DataTypes.BOOLEAN,
        field: 'IS_REGULAR'
    }
}, {
    modelName: 'client',
    tableName: 'CLIENTS',
    sequelize: connection,
    timestamps: false
});

//Exportation du modèle
module.exports = Client;

