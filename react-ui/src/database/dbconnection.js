const sequelize = require('sequelize');

const timezone = 'UTC';
process.env.TZ = timezone;

const connection = new sequelize.Sequelize('base_de_donnees','identifiant','mot_de_passe', {
    host: 'host_mysql',
    dialect: 'mysql',
    logging: false,
    "timezone": "+02:00"
});

connection.sequelize = sequelize;

module.exports = connection;
