const sequelize = require('sequelize');

const connection = new sequelize.Sequelize('gestion-hotel_projet','***REMOVED***','***REMOVED***', {
    host: 'mysql-gestion-hotel.alwaysdata.net',
    dialect: 'mysql',
    logging: false
});

connection.sequelize = sequelize;

module.exports = connection;
