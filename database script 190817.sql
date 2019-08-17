

-- Skripta za pravljenje baze podataka za UNDPOfflineStore projekat, 17.08.2019., 22:16

-- u odnosu na prethodnu verziju dodat je atribut proizvodjac_id u tabelu komponenata, kao i 
-- tabela proizvodjac




-- kreiranje baze podataka 'undpofflinestore' ukoliko ona vec ne postoji
CREATE DATABASE  IF NOT EXISTS `undpofflinestore` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `undpofflinestore`;


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Struktura tabele `detaljifakture`

DROP TABLE IF EXISTS `detaljifakture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detaljifakture` (
  `faktura_id` int(11) NOT NULL,
  `komponenta_id` int(11) NOT NULL,
  `cena` decimal(10,0) NOT NULL,
  `kolicina` int(11) NOT NULL,
  KEY `fk_faktura_id` (`faktura_id`),
  KEY `fk_komponenta_id` (`komponenta_id`),
  CONSTRAINT `fk_faktura_id` FOREIGN KEY (`faktura_id`) REFERENCES `faktura` (`faktura_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_komponenta_id` FOREIGN KEY (`komponenta_id`) REFERENCES `komponenta` (`komponenta_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


-- Ubacivanje podataka u tabelu `detaljifakture` (za sada ih nema)

LOCK TABLES `detaljifakture` WRITE;
/*!40000 ALTER TABLE `detaljifakture` DISABLE KEYS */;
/*!40000 ALTER TABLE `detaljifakture` ENABLE KEYS */;
UNLOCK TABLES;

-- Struktura tabele `dobavljac`

DROP TABLE IF EXISTS `dobavljac`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dobavljac` (
  `dobavljac_id` int(11) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  `ulica` varchar(45) NOT NULL,
  `broj` varchar(10) NOT NULL,
  `grad` varchar(45) NOT NULL,
  `postanski_broj` varchar(6) NOT NULL,
  `drzava` varchar(45) NOT NULL,
  `telefon` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`dobavljac_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Ubacivanje podataka u tabelu `dobavljac`

LOCK TABLES `dobavljac` WRITE;
/*!40000 ALTER TABLE `dobavljac` DISABLE KEYS */;
INSERT INTO `dobavljac` VALUES (1,'Alti d.o.o.','Dunavska','bb','Beograd','11000','Srbija','0116555705'),(2,'DS Computers','Bulevar Revolucije','71','Beograd','11274','Srbija','0113085542');
/*!40000 ALTER TABLE `dobavljac` ENABLE KEYS */;
UNLOCK TABLES;

-- Struktura tabele `faktura`

DROP TABLE IF EXISTS `faktura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `faktura` (
  `faktura_id` int(11) NOT NULL AUTO_INCREMENT,
  `kupac_id` int(11) NOT NULL,
  `datum` date NOT NULL,
  PRIMARY KEY (`faktura_id`),
  KEY `fk_kupac_id` (`kupac_id`),
  CONSTRAINT `fk_kupac_id` FOREIGN KEY (`kupac_id`) REFERENCES `kupac` (`kupac_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Ubacivanje podataka u tabelu `faktura` (za sada ih nema)

LOCK TABLES `faktura` WRITE;
/*!40000 ALTER TABLE `faktura` DISABLE KEYS */;
/*!40000 ALTER TABLE `faktura` ENABLE KEYS */;
UNLOCK TABLES;

-- Struktura tabele `komponenta`

DROP TABLE IF EXISTS `komponenta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `komponenta` (
  `komponenta_id` int(11) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  `proizvodjac_id` int(11) NOT NULL,
  `dobavljac_id` int(11) NOT NULL,
  `tip_id` int(11) NOT NULL,
  `kolicina` int(11) NOT NULL,
  `cena` decimal(10,2) NOT NULL,
  `slika` varchar(45) DEFAULT NULL,
  `aktuelna` binary(1) NOT NULL,
  PRIMARY KEY (`komponenta_id`),
  KEY `fk_proizvodjac` (`proizvodjac_id`),
  KEY `fk_dobavljac` (`dobavljac_id`),
  KEY `fk_tip` (`tip_id`),
  CONSTRAINT `fk_dobavljac` FOREIGN KEY (`dobavljac_id`) REFERENCES `dobavljac` (`dobavljac_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_proizvodjac` FOREIGN KEY (`proizvodjac_id`) REFERENCES `proizvodjac` (`proizvodjac_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_tip` FOREIGN KEY (`tip_id`) REFERENCES `tip` (`tip_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Ubacivanje podataka u tabelu `komponenta`

LOCK TABLES `komponenta` WRITE;
/*!40000 ALTER TABLE `komponenta` DISABLE KEYS */;
INSERT INTO `komponenta` VALUES (1,'AM4 APU 220GE, 3.4GHz BOX',2,1,1,10,6890.00,'AthlonAM4_3_4','1'),(2,'Intel LGA1151 i3-9100F 3.6GHZ',1,2,1,5,11890.00,'Inteli39100F','1'),(3,'NVidia GeForce GTX1050Ti',3,1,4,6,21990.00,'GeForceGTX1050TiAsus','1'),(4,'NVidia GeForce GTX1050Ti',4,1,4,6,21990.00,'GeForceGTX1050TiMSI','1');
/*!40000 ALTER TABLE `komponenta` ENABLE KEYS */;
UNLOCK TABLES;

-- Struktura tabele `kupac`

DROP TABLE IF EXISTS `kupac`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kupac` (
  `kupac_id` int(11) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  `ulica` varchar(45) NOT NULL,
  `broj` varchar(10) NOT NULL,
  `grad` varchar(45) NOT NULL,
  `postanski_broj` varchar(6) NOT NULL,
  `drzava` varchar(45) NOT NULL,
  `telefon` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`kupac_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Ubacivanje podataka u tabelu `kupac`

LOCK TABLES `kupac` WRITE;
/*!40000 ALTER TABLE `kupac` DISABLE KEYS */;
INSERT INTO `kupac` VALUES (1,'Nike shop','Kralja Petra I','28','Kragujevac','34000','Srbija','034308749'),(2,'Djak sport','Kralja Petra I','26','Kragujevac','34000','Srbija','034308222'),(3,'Lilly','Atinska','54','Kragujevac','34000','Srbija','034343233');
/*!40000 ALTER TABLE `kupac` ENABLE KEYS */;
UNLOCK TABLES;

-- Struktura tabele `proizvodjac`

DROP TABLE IF EXISTS `proizvodjac`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proizvodjac` (
  `proizvodjac_id` int(11) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(20) NOT NULL,
  PRIMARY KEY (`proizvodjac_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Ubacivanje podataka u tabelu `proizvodjac`

LOCK TABLES `proizvodjac` WRITE;
/*!40000 ALTER TABLE `proizvodjac` DISABLE KEYS */;
INSERT INTO `proizvodjac` VALUES (1,'Intel'),(2,'AMD'),(3,'Asus'),(4,'MSI'),(5,'HP'),(6,'Dell'),(7,'Kingston'),(8,'Philips');
/*!40000 ALTER TABLE `proizvodjac` ENABLE KEYS */;
UNLOCK TABLES;

-- Struktura tabele `tip`

DROP TABLE IF EXISTS `tip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tip` (
  `tip_id` int(11) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`tip_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

-- Ubacivanje podataka u tabelu `tip`

LOCK TABLES `tip` WRITE;
/*!40000 ALTER TABLE `tip` DISABLE KEYS */;
INSERT INTO `tip` VALUES (1,'procesori'),(2,'ram memorije'),(3,'maticne ploce'),(4,'graficke kartice'),(5,'hard diskovi'),(6,'ssd'),(7,'opticki uredjaji'),(8,'kucista'),(9,'napajanja'),(10,'hladnjaci, ventilatori'),(11,'adapteri'),(12,'kablovi'),(13,'konektori');
/*!40000 ALTER TABLE `tip` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

