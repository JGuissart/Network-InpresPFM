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
-- Table structure for table `salaires`
--

DROP TABLE IF EXISTS `salaires`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `salaires` (
  `idSalaire` int(11) NOT NULL AUTO_INCREMENT,
  `Login` varchar(20) NOT NULL,
  `Mois` varchar(2) NOT NULL DEFAULT '01',
  `Annee` varchar(4) NOT NULL DEFAULT '1970',
  `MontantBrut` double NOT NULL DEFAULT '0',
  `RetraitONSS` double NOT NULL DEFAULT '0',
  `RetraitPrecompte` double NOT NULL DEFAULT '0',
  `SalaireValide` tinyint(1) NOT NULL DEFAULT '0',
  `FicheEnvoyee` tinyint(1) NOT NULL DEFAULT '0',
  `SalaireVerse` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idSalaire`),
  KEY `FK_Salaires_Personnel_idx` (`Login`),
  CONSTRAINT `FK_Salaires_Personnel` FOREIGN KEY (`Login`) REFERENCES `personnel` (`Login`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salaires`
--

/*!40000 ALTER TABLE `salaires` DISABLE KEYS */;
/*!40000 ALTER TABLE `salaires` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-21 10:53:55
