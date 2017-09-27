-- Script de création du schéma BD_TRAFIC (Oracle)

DROP TABLE Parc;
DROP TABLE Mouvement;
DROP TABLE Transporteur;
DROP TABLE Container;
DROP TABLE Societe;
DROP TABLE Destination;

CREATE TABLE Destination
(
	Ville VARCHAR2(30) CONSTRAINT PK_Destination PRIMARY KEY,
	DistanceBateau NUMBER(5) CONSTRAINT NN_DestinationDistBateau NOT NULL,
	DistanceTrain NUMBER(5) CONSTRAINT NN_DestinationDistTrain NOT NULL,
	DistanceRoute NUMBER(5)
);

CREATE TABLE Societe
(
	idSociete VARCHAR(20) CONSTRAINT PK_Societe PRIMARY KEY,
	NomContact VARCHAR2(25) CONSTRAINT NN_ParcNomContact NOT NULL,
	EmailContact VARCHAR2(30) CONSTRAINT NN_ParcEmailContact NOT NULL,
	NumeroTelephone VARCHAR2(10) CONSTRAINT NN_ParcNumTel NOT NULL,
	AdresseSociete VARCHAR2(30) CONSTRAINT NN_ParcAdresseSociete NOT NULL
);

CREATE TABLE Container
(
	idContainer VARCHAR2(17) CONSTRAINT PK_Container PRIMARY KEY,
	NatureContenu VARCHAR2(20) CONSTRAINT NN_ContainerNature NOT NULL,
	Dangers VARCHAR2(20),
	idSociete VARCHAR(20) CONSTRAINT FK_Container_Societe REFERENCES Societe(idSociete)
);

CREATE TABLE Transporteur
(
	idTransporteur VARCHAR2(15) CONSTRAINT PK_Transporteur PRIMARY KEY,
	CaracteristiquesTechniques VARCHAR2(45) CONSTRAINT NN_TransporteurCaracTechnique NOT NULL,
	idSociete VARCHAR(20) CONSTRAINT FK_Transporteur_Societe REFERENCES Societe(idSociete)
);

CREATE TABLE Mouvement
(
	idMouvement VARCHAR2(15) CONSTRAINT PK_Mouvement PRIMARY KEY,
	DateArrivee VARCHAR2(10) CONSTRAINT NN_MouvementDateArr NOT NULL,
	DateDepart VARCHAR2(10),
	Poids NUMBER(11) CONSTRAINT NN_MouvementPoids NOT NULL,
	idContainer VARCHAR2(17) CONSTRAINT FK_Mouvement_Container REFERENCES Container(idContainer),
	idTransporteurIn VARCHAR2(15) CONSTRAINT FK_Mouvement_TranspoteurIn REFERENCES Transporteur(idTransporteur),
	idTransporteurOut VARCHAR2(15) CONSTRAINT FK_Mouvement_TransporteurOut REFERENCES Transporteur(idTransporteur),
	Destination VARCHAR2(30) CONSTRAINT FK_Mouvement_Destination REFERENCES Destination(Ville)
);

CREATE TABLE Parc
(
	Coordonnees VARCHAR2(7) CONSTRAINT PK_Parc PRIMARY KEY,
	EtatEmplacement NUMBER(1) CONSTRAINT CK_ParcEtatEmplacement CHECK(EtatEmplacement IN(0, 1, 2)),
	idContainer VARCHAR2(17) CONSTRAINT FK_Parc_Container REFERENCES Container(idContainer),
	DateReservation VARCHAR2(10),
	DateArrivee VARCHAR2(10) CONSTRAINT NN_ParcDateArr NOT NULL,
	Poids NUMBER(4, 1) CONSTRAINT NN_ParcPoids NOT NULL,
	Destination VARCHAR2(30) CONSTRAINT FK_Parc_Destination REFERENCES Destination(Ville),
	TypeTransport CHAR(1) CONSTRAINT CK_ParcTypeTransport CHECK(TypeTransport IN('B', 'T'))
);

CREATE TABLE Users
(
	Login VARCHAR2(15) CONSTRAINT PK_Users PRIMARY KEY,
	Password VARCHAR2(20) CONSTRAINT NN_UsersPassword NOT NULL
);