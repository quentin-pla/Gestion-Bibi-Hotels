const {Model, DataTypes} = require('sequelize');
const connection = require("../dbconnection");

class Bill extends Model {}
Bill.init({
    id: {
        type: DataTypes.INTEGER,
        field: 'ID',
        primaryKey: true
    },
    reservation_id: {
        type: DataTypes.INTEGER,
        field: 'RESERVATION_ID'
    },
    client_id: {
        type: DataTypes.INTEGER,
        field: 'CLIENT_ID'
    },
    amount: {
        type: DataTypes.INTEGER,
        field: 'AMOUNT'
    },
    is_archived: {
        type: DataTypes.BOOLEAN,
        field: 'IS_ARCHIVED'
    }
}, {
    modelName: 'bills',
    tableName: 'BILLS',
    sequelize: connection,
    timestamps: false
});

//Exportation du modèle
module.exports = Bill;

