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
-- Table structure for table `primes`
--

DROP TABLE IF EXISTS `primes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `primes` (
  `idPrime` int(11) NOT NULL AUTO_INCREMENT,
  `Login` varchar(20) NOT NULL,
  `Montant` double NOT NULL DEFAULT '0',
  `DateOctroi` varchar(10) NOT NULL,
  `Motif` varchar(75) DEFAULT NULL,
  `Paye` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`idPrime`,`Login`),
  KEY `FK_Primes_Personnel_idx` (`Login`),
  CONSTRAINT `FK_Primes_Personnel` FOREIGN KEY (`Login`) REFERENCES `personnel` (`Login`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `primes`
--

/*!40000 ALTER TABLE `primes` DISABLE KEYS */;
INSERT INTO `primes` VALUES (1,'charlech',1525,'28/01/2015','Je suis de bonne humeur en cette nouvelle année',0),(2,'desartad',1525,'28/01/2015','Je suis de bonne humeur en cette nouvelle année',0),(3,'guissaju',1525,'28/01/2015','Je suis de bonne humeur en cette nouvelle année',0),(4,'MerceD',1525,'28/01/2015','Je suis de bonne humeur en cette nouvelle année',0),(5,'vilvencl',1525,'28/01/2015','Je suis de bonne humeur en cette nouvelle année',0),(6,'vilvencl',1500,'28/01/2015','Bon anniversaire m\'sieur V',0),(7,'wagnerje',1525,'28/01/2015','Je suis de bonne humeur en cette nouvelle année',0),(8,'charlech',5000,'25/02/2015','On a besoin de points, donc on file des primes comme ça',0),(9,'desartad',1800,'25/02/2015','Je suis copain avec le patron',0),(10,'guissaju',4999,'25/02/2015','Je suis le patron, donc j\'ai droit à une grosse prime !',0),(11,'MerceD',5000,'25/02/2015','Je suis de bonne humeur en cette nouvelle année',0),(12,'vilvencl',5000,'25/02/2015','On a besoin de points, donc on file des primes comme ça',0),(13,'wagnerje',5000,'25/02/2015','On a besoin de points, donc on file des primes comme ça',0),(14,'wagnerje',1500,'25/02/2015','Bon anniversaire maître',0),(15,'charlech',5000,'28/03/2015','On a besoin de points, donc on file des primes comme ça',0),(16,'MerceD',5000,'28/03/2015','On a besoin de points, donc on file des primes comme ça',0),(17,'vilvencl',5000,'28/03/2015','On a besoin de points, donc on file des primes comme ça',0),(18,'wagnerje',5000,'28/03/2015','On a besoin de points, donc on file des primes comme ça',0),(19,'desartad',1500,'28/03/2015','Bon anniversaire Didi',0),(20,'charlech',5000,'28/04/2015','On a besoin de points, donc on file des primes comme ça',0),(21,'MerceD',5000,'28/04/2015','On a besoin de points, donc on file des primes comme ça',0),(22,'vilvencl',5000,'28/04/2015','On a besoin de points, donc on file des primes comme ça',0),(23,'wagnerje',5000,'28/04/2015','On a besoin de points, donc on file des primes comme ça',0),(24,'charlech',5000,'28/05/2015','On a besoin de points, donc on file des primes comme ça',0),(25,'MerceD',5000,'28/05/2015','On a besoin de points, donc on file des primes comme ça',0),(26,'vilvencl',5000,'28/05/2015','On a besoin de points, donc on file des primes comme ça',0),(27,'wagnerje',5000,'28/05/2015','On a besoin de points, donc on file des primes comme ça',0),(28,'guissaju',1500,'28/05/2015','Bon anniversaire moi',0),(29,'charlech',5000,'28/06/2015','On a besoin de points, donc on file des primes comme ça',0),(30,'MerceD',5000,'28/06/2015','On a besoin de points, donc on file des primes comme ça',0),(31,'vilvencl',5000,'28/06/2015','On a besoin de points, donc on file des primes comme ça',0),(32,'wagnerje',5000,'28/06/2015','On a besoin de points, donc on file des primes comme ça',0),(33,'charlech',5000,'28/07/2015','On a besoin de points, donc on file des primes comme ça',0),(34,'MerceD',5000,'28/07/2015','On a besoin de points, donc on file des primes comme ça',0),(35,'vilvencl',5000,'28/07/2015','On a besoin de points, donc on file des primes comme ça',0),(36,'wagnerje',5000,'28/07/2015','On a besoin de points, donc on file des primes comme ça',0),(37,'charlech',5000,'28/08/2015','On a besoin de points, donc on file des primes comme ça',0),(38,'MerceD',5000,'28/08/2015','On a besoin de points, donc on file des primes comme ça',0),(39,'vilvencl',5000,'28/08/2015','On a besoin de points, donc on file des primes comme ça',0),(40,'wagnerje',5000,'28/08/2015','On a besoin de points, donc on file des primes comme ça',0),(41,'charlech',5000,'28/09/2015','On a besoin de points, donc on file des primes comme ça',0),(42,'MerceD',5000,'28/09/2015','On a besoin de points, donc on file des primes comme ça',0),(43,'vilvencl',5000,'28/09/2015','On a besoin de points, donc on file des primes comme ça',0),(44,'wagnerje',5000,'28/09/2015','On a besoin de points, donc on file des primes comme ça',0),(45,'charlech',5000,'28/10/2015','On a besoin de points, donc on file des primes comme ça',0),(46,'MerceD',5000,'28/10/2015','On a besoin de points, donc on file des primes comme ça',0),(47,'vilvencl',5000,'28/10/2015','On a besoin de points, donc on file des primes comme ça',0),(48,'wagnerje',5000,'28/10/2015','On a besoin de points, donc on file des primes comme ça',0),(49,'charlech',5000,'28/11/2015','On a besoin de points, donc on file des primes comme ça',0),(50,'MerceD',5000,'28/11/2015','On a besoin de points, donc on file des primes comme ça',0),(51,'vilvencl',5000,'28/11/2015','On a besoin de points, donc on file des primes comme ça',0),(52,'wagnerje',5000,'28/11/2015','On a besoin de points, donc on file des primes comme ça',0),(53,'charlech',5000,'31/12/2015','Prime de fin d\'année',0),(54,'desartad',5000,'31/12/2015','Prime de fin d\'année',0),(55,'guissaju',10000,'31/12/2015','Prime de fin d\'année',0),(56,'MerceD',5000,'31/12/2015','Prime de fin d\'année',0),(57,'vilvencl',5000,'31/12/2015','Prime de fin d\'année',0),(58,'wagnerje',5000,'31/12/2015','Prime de fin d\'année',0);
/*!40000 ALTER TABLE `primes` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-01-21 10:53:54
