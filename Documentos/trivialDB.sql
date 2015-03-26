CREATE DATABASE  IF NOT EXISTS `trivialbd` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `trivialbd`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: trivialbd
-- ------------------------------------------------------
-- Server version	5.6.16

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
-- Table structure for table `preguntas`
--

DROP TABLE IF EXISTS `preguntas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preguntas` (
  `pregunta` varchar(100) NOT NULL,
  `idTema` int(11) NOT NULL,
  `respuesta` varchar(45) NOT NULL,
  `incorrecta1` varchar(45) DEFAULT NULL,
  `incorrecta2` varchar(45) DEFAULT NULL,
  `incorrecta3` varchar(45) DEFAULT NULL,
  `incorrecta4` varchar(45) DEFAULT NULL,
  `incorrecta5` varchar(45) DEFAULT NULL,
  `incorrecta6` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pregunta`),
  UNIQUE KEY `pregunta_UNIQUE` (`pregunta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preguntas`
--

LOCK TABLES `preguntas` WRITE;
/*!40000 ALTER TABLE `preguntas` DISABLE KEYS */;
INSERT INTO `preguntas` VALUES ('¿Cual es la capital de Djibuti?',3,'Djibuti','El Cairo','Rabat','Lisboa','Ankara','Paramaribo','Johannesburgo'),('¿Cual es la respuesta universal?',4,'42','3','312','-25','101','56','0'),('¿En qué año salió World of Warcraft?',0,'2004','1990','2006','2001','2002','1990','1997'),('¿Quién era el guitarrista de Led Zeppelin?',5,'Jimmy Page','John Petrucci','Steve Vai','Keith Richards','James Hetfield','Paul McCartney','Phil Rudd'),('¿Quién es el fundador y ex-presidente de Microsoft?',2,'Bill Gates','Neil Armstrong','John Nash','Tony Blair','Michael Brown','James Dean','Tom Hanks'),('¿Quién fue el director de La Naranja Mecánica?',4,'Stanley Kubrick','Alfred Hitchkock','Woody Allen','Bram Stoker','Peter Jackson','Clint Eastwood','Tim Burton'),('¿Quién ganó el mundial del 2010?',1,'España','Portugal','Francia','Alemania','Argentina','Suecia','Brasil');
/*!40000 ALTER TABLE `preguntas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `nombre` varchar(45) NOT NULL,
  `contrasena` varchar(100) NOT NULL,
  `p_ganadas` int(11) DEFAULT NULL,
  `p_perdidas` int(11) DEFAULT NULL,
  PRIMARY KEY (`nombre`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES ('ADMINISTRADOR','73bd5366febe9f59b3cc4aa2fe183151',0,0);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-06-03 21:36:20
