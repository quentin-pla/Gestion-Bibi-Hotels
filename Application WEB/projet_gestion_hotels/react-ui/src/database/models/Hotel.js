const {Model, DataTypes} = require('sequelize');
const connection = require("../dbconnection");

class Hotel extends Model {}
Hotel.init({
    id: {
        type: DataTypes.INTEGER,
        field: 'ID',
        primaryKey: true
    },
    name: {
        type: DataTypes.STRING,
        field: 'NAME'
    },
    street: {
        type: DataTypes.STRING,
        field: 'STREET'
    },
    city: {
        type: DataTypes.STRING,
        field: 'CITY'
    },
    stars: {
        type: DataTypes.INTEGER,
        field: 'STAR_RATING'
    },
}, {
    modelName: 'hotels',
    tableName: 'HOTELS',
    sequelize: connection,
    timestamps: false
});

//Exportation du modèle
module.exports = Hotel;

