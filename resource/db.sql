CREATE TABLE pengguna (
  username varchar(100) NOT NULL,
  email varchar(100) NOT NULL,
  ponsel varchar(20) NOT NULL,
  password varchar(255) NOT NULL,
  PRIMARY KEY (username,email)
);
