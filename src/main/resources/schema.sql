CREATE TABLE IF NOT EXISTS users
(
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  emailId varchar(255) NOT NULL ,
  password varchar(255) NOT NULL ,
  roleType VARCHAR(255) NOT NULL ,
  status BOOLEAN NOT NULL ,
  selfRegister BOOLEAN NOT NULL ,
  referalId int(11),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS jwt_token
(
  id INT(11) NOT NULL AUTO_INCREMENT,
  token VARCHAR(255) NOT NULL ,
  PRIMARY KEY (id)
);