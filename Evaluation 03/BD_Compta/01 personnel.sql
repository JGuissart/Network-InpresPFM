CREATE DATABASE  IF NOT EXISTS `bd_compta` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `bd_compta`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bd_compta
-- ------------------------------------------------------
-- Server version	5.6.17

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

--
-- Table structure for table `personnel`
--

DROP TABLE IF EXISTS `personnel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personnel` (
  `Login` varchar(20) NOT NULL,
  `Password` varchar(20) NOT NULL,
  `Nom` varchar(25) NOT NULL,
  `Prenom` varchar(25) NOT NULL,
  `MailInterne` varchar(45) NOT NULL DEFAULT 'aaa@aaa.be',
  `TelInterne` varchar(10) NOT NULL DEFAULT '00/0000000',
  `MailPrive` varchar(45) NOT NULL DEFAULT 'aaa@aaa.be',
  `TelPrive` varchar(10) NOT NULL DEFAULT '00/0000000',
  `Affectation` varchar(20) NOT NULL DEFAULT 'Utilisateur',
  `Fonction` varchar(30) NOT NULL DEFAULT 'Directeur',
  `NiveauBaremique` varchar(45) NOT NULL DEFAULT '0',
  `DateEntreeSociete` varchar(45) NOT NULL DEFAULT '00/00/0000',
  `NumCompteBancaire` varchar(45) NOT NULL DEFAULT 'BE00000000000000',
  PRIMARY KEY (`Login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personnel`
--

/*!40000 ALTER TABLE `personnel` DISABLE KEYS */;
INSERT INTO `personnel` VALUES ('charlech','routeur123','CHARLET','Christophe','christophe.charlet@inpres-pfm.be','043621234','christophe.charlet@hepl.be','0490123456','','Chef-comptable','','','000-0000000-01'),('desartad','qwerty123','DESART','Adrien','adrien.desart@inpres-pfm.be','043622345','adrien.desart@student.hepl.be','0490234567','','Directeur','','','000-0000000-02'),('guissaju','azerty123','GUISSART','Julien','julien.guissart@inpres-pfm.be','043623456','julien.guissart@student.hepl.be','0490345678','','Master','','','000-0000000-03'),('MerceD','unix123','MERCENIER','Denys','denys.mercenier@inpres-pfm.be','043624567','denys.mercenier@hepl.be','0490456789','','Comptable','','','000-0000000-04'),('vilvencl','crypto123','VILVENS','Claude','claude.vilvens@inpres-pfm.be','043625678','claude.vilvens@hepl.be','0490567890','','Master','','','000-0000000-05'),('wagnerje','reseaux123','WAGNER','Jean-Marc','jean-marc.wagner@inpres-pfm.be','043626789','jean-marc.wagner@hepl.be','0490678901','','Master','','','000-0000000-06');
/*!40000 ALTER TABLE `personnel` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-21 10:53:54
