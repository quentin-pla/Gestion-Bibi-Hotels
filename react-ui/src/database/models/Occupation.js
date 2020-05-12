const connection = require("../dbconnection");
const {Model, DataTypes} = connection.sequelize;
const Reservation = require("./Reservation");
const Room = require("./Room");

class Occupation extends Model {}

Occupation.init({
    id: {
        type: DataTypes.INTEGER,
        field: 'ID',
        unique: true
    },
    reservation_id: {
        type: DataTypes.INTEGER,
        field: 'RESERVATION_ID',
        references: {
            model: Reservation,
            key: 'ID'
        },
        primaryKey: true
    },
    room_id: {
        type: DataTypes.INTEGER,
        field: 'ROOM_ID',
        references: {
            model: Room,
            key: 'ID'
        },
        primaryKey: true
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

// Occupation.associate = (models) => {
//     Occupation.belongsTo(models.reservations, {foreignKey: "RESERVATION_ID"});
//     Occupation.belongsTo(models.rooms, {foreignKey: "ROOM_ID"});
// };

//Exportation du modèle
module.exports = Occupation;

