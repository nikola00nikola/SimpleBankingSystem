CREATE SCHEMA `podsistem1` ;

CREATE TABLE `podsistem1`.`mesto` (
  `IdMes` int NOT NULL AUTO_INCREMENT,
  `PostBr` int NOT NULL,
  `Naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`IdMes`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE `podsistem1`.`filijala` (
  `IdFil` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdFil`),
  KEY `FK_IdMes_filijala_idx` (`IdMes`),
  CONSTRAINT `FK_IdMes_filijala` FOREIGN KEY (`IdMes`) REFERENCES `podsistem1`.`mesto` (`IdMes`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE `podsistem1`.`komitent` (
  `IdKom` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdKom`),
  KEY `FK_IdMes_komitent_idx` (`IdMes`),
  CONSTRAINT `FK_IdMes_komitent` FOREIGN KEY (`IdMes`) REFERENCES `podsistem1`.`mesto` (`IdMes`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1;



CREATE SCHEMA `podsistem2`;

CREATE TABLE `podsistem2`.`racun` (
  `IdRac` int NOT NULL AUTO_INCREMENT,
  `Stanje` double NOT NULL,
  `Status` varchar(1) NOT NULL,
  `DozvMinus` int NOT NULL,
  `DatumVreme` datetime NOT NULL,
  `BrojTrans` int NOT NULL,
  `IdKom` int NOT NULL,
  `IdFil` int NOT NULL,
  PRIMARY KEY (`IdRac`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE `podsistem2`.`transakcija` (
  `IdTra` int NOT NULL AUTO_INCREMENT,
  `DatumVreme` datetime NOT NULL,
  `Iznos` double NOT NULL,
  `Svrha` varchar(20) NOT NULL,
  `Vrsta` varchar(1) NOT NULL,
  `IdFil` int DEFAULT '-1',
  PRIMARY KEY (`IdTra`)
) ENGINE=InnoDB AUTO_INCREMENT=1 ;

CREATE TABLE `podsistem2`.`ucestvuje` (
  `IdTra` int NOT NULL,
  `IdRac` int NOT NULL,
  `RedBroj` int NOT NULL,
  `Prvi` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdTra`,`IdRac`),
  KEY `FK_ucestvuje_IdRac_idx` (`IdRac`),
  KEY `FK_ucestvuje_IdTra_idx` (`IdTra`),
  CONSTRAINT `FK_ucestvuje_IdRac` FOREIGN KEY (`IdRac`) REFERENCES `podsistem2`.`racun` (`IdRac`) ON UPDATE CASCADE,
  CONSTRAINT `FK_ucestvuje_IdTra` FOREIGN KEY (`IdTra`) REFERENCES `podsistem2`.`transakcija` (`IdTra`) ON UPDATE CASCADE
) ENGINE=InnoDB ;



CREATE SCHEMA `podsistem3`;

CREATE TABLE `podsistem3`.`mesto` (
  `IdMes` int NOT NULL AUTO_INCREMENT,
  `PostBr` int NOT NULL,
  `Naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`IdMes`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE `podsistem3`.`filijala` (
  `IdFil` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdFil`),
  KEY `FK_IdMes_filijala_idx` (`IdMes`),
  CONSTRAINT `FK_IdMes_filijala` FOREIGN KEY (`IdMes`) REFERENCES `podsistem3`.`mesto` (`IdMes`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE `podsistem3`.`komitent` (
  `IdKom` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdKom`),
  KEY `FK_IdMes_komitent_idx` (`IdMes`),
  CONSTRAINT `FK_IdMes_komitent` FOREIGN KEY (`IdMes`) REFERENCES `podsistem3`.`mesto` (`IdMes`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE `podsistem3`.`racun` (
  `IdRac` int NOT NULL AUTO_INCREMENT,
  `Stanje` double NOT NULL,
  `Status` varchar(1) NOT NULL,
  `DozvMinus` int NOT NULL,
  `DatumVreme` datetime NOT NULL,
  `BrojTrans` int NOT NULL,
  `IdKom` int NOT NULL,
  `IdFil` int NOT NULL,
  PRIMARY KEY (`IdRac`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE `podsistem3`.`transakcija` (
  `IdTra` int NOT NULL AUTO_INCREMENT,
  `DatumVreme` datetime NOT NULL,
  `Iznos` double NOT NULL,
  `Svrha` varchar(20) NOT NULL,
  `Vrsta` varchar(1) NOT NULL,
  `IdFil` int DEFAULT '-1',
  PRIMARY KEY (`IdTra`)
) ENGINE=InnoDB AUTO_INCREMENT=1 ;

CREATE TABLE `podsistem3`.`ucestvuje` (
  `IdTra` int NOT NULL,
  `IdRac` int NOT NULL,
  `RedBroj` int NOT NULL,
  `Prvi` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`IdTra`,`IdRac`),
  KEY `FK_ucestvuje_IdRac_idx` (`IdRac`),
  KEY `FK_ucestvuje_IdTra_idx` (`IdTra`),
  CONSTRAINT `FK_ucestvuje_IdRac` FOREIGN KEY (`IdRac`) REFERENCES `podsistem3`.`racun` (`IdRac`) ON UPDATE CASCADE,
  CONSTRAINT `FK_ucestvuje_IdTra` FOREIGN KEY (`IdTra`) REFERENCES `podsistem3`.`transakcija` (`IdTra`) ON UPDATE CASCADE
) ENGINE=InnoDB ;