const connection = require("../dbconnection");
const {Model, DataTypes} = connection.sequelize;

class Client extends Model {}

Client.init({
    id: {
        type: DataTypes.INTEGER.UNSIGNED,
        field: 'ID',
        primaryKey: true
    },
    mail: {
        type: DataTypes.STRING,
        field: 'MAIL',
        unique: true,
        primaryKey: true
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

