DROP SEQUENCE sequenceCommande;
DROP TABLE langue;
DROP TABLE commande_res;
DROP TABLE commande_prod;
DROP TABLE commande;
DROP TABLE reservations;
DROP TABLE produits;
DROP TABLE clients;


CREATE TABLE clients
(
	login VARCHAR2(50) CONSTRAINT PK_clients PRIMARY KEY,
	password VARCHAR2(50)
);

CREATE TABLE produits
(
	idProduit NUMBER CONSTRAINT PK_produits PRIMARY KEY,
	nom VARCHAR2(50) NOT NULL,
	description VARCHAR2(100) NOT NULL,
	quantite NUMBER NOT NULL,
	prix NUMBER(4,2) NOT NULL
);

CREATE TABLE reservations
(
	dateReservation DATE  CONSTRAINT PK_reservations PRIMARY KEY,
	nbrReservation NUMBER
);

CREATE TABLE commande
(
	idCommande NUMBER CONSTRAINT PK_commande PRIMARY KEY,
	dateCommande DATE,
	login VARCHAR2(50) CONSTRAINT FK_commande_clients_login REFERENCES clients(login)
);

CREATE TABLE commande_prod
(
	idCommande NUMBER CONSTRAINT FK_commande_prod_idCommande REFERENCES commande(idCommande),
	idProduit NUMBER CONSTRAINT FK_commande_prod_idProduit REFERENCES produits(idProduit),
	quantite NUMBER,
	CONSTRAINT PK_commande_prod PRIMARY KEY(idCommande,idProduit)
);

CREATE TABLE commande_res
(
	idCommande NUMBER CONSTRAINT FK_commande_res_idCommande REFERENCES commande(idCommande),
	dateReservation DATE,
	quantite NUMBER,
	CONSTRAINT PK_commande_res PRIMARY KEY(idCommande,dateReservation)
);

CREATE TABLE langue
(
	idLangue VARCHAR2(7) CONSTRAINT PK_langue PRIMARY KEY,
	langue VARCHAR2(50)
);

create sequence sequenceCommande;

INSERT INTO clients VALUES('adrien','desart');
INSERT INTO clients VALUES('julien','guissart');

INSERT INTO produits VALUES('1','Ours en peluche','Ours',40,10.50);
INSERT INTO produits VALUES('2','Livre papillon','Décrouvrez les plus beau papillon du monde',15,8);
INSERT INTO produits VALUES('3','Invertebrés d"eau douce','Livre sur les invertebrés',5,8);
INSERT INTO produits VALUES('4','Chapeau','Chapeau de cow-boy',5,12);
INSERT INTO produits VALUES('5','Renne','Petite chouette en peluche',8,10.50);

INSERT INTO langue VALUES ('fr_FR','Français');
INSERT INTO langue VALUES ('en_UK','Anglais');

commit;