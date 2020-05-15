const sequelize = require('sequelize');

const timezone = 'UTC';
process.env.TZ = timezone;

const connection = new sequelize.Sequelize('gestion-hotel_projet','***REMOVED***','***REMOVED***', {
    host: 'mysql-gestion-hotel.alwaysdata.net',
    dialect: 'mysql',
    logging: false,
    "timezone": "+02:00"
});

connection.sequelize = sequelize;

module.exports = connection;
