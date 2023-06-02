-- init database for weather
create database weather;

create user weather with encrypted password 'weather';
grant all privileges on database weather to weather;