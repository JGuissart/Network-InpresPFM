#include <cstring>
#include <cstdlib>

using namespace std;

#include "Trafic.h"

Trafic::Trafic()
{
    this->IdContainer = "NULL";
    this->Coordonnees = "NULL";
    this->Etat = 0;
    this->DateReservation = "10/10/2015";
    this->DateArrivee = "10/10/2015";
    this->Poids = 0.0;
    this->Destination = "NULL";
    this->TypeTransport = 'B';
}

Trafic::Trafic(string Coordonnees, string IdContainer, short Etat, string DateReservation, string DateArrivee, float Poids, string Destination, char TypeTransport)
{
    this->IdContainer = IdContainer;
    this->Coordonnees = Coordonnees;
    this->Etat = Etat;
    this->DateReservation = DateReservation;
    this->DateArrivee = DateArrivee;
    this->Poids = Poids;
    this->Destination = Destination;
    this->TypeTransport = TypeTransport;
}

Trafic::Trafic(const Trafic& t)
{
    this->IdContainer = t.getIdContainer();
    this->Coordonnees = t.getCoordonnees();
    this->Etat = t.getEtat();
    this->DateReservation = t.getDateReservation();
    this->DateArrivee = t.getDateArrivee();
    this->Poids = t.getPoids();
    this->Destination = t.getDestination();
    this->TypeTransport = t.getTypeTransport();
}

Trafic::~Trafic()
{

}

/*************** GETTERS ***************/

string Trafic::getIdContainer() const
{
    return IdContainer;
}

string Trafic::getCoordonnees() const
{
    return Coordonnees;
}

short Trafic::getEtat() const
{
    return Etat;
}

string Trafic::getDateReservation() const
{
    return DateReservation;
}

string Trafic::getDateArrivee() const
{
    return DateArrivee;
}

float Trafic::getPoids() const
{
    return Poids;
}

string Trafic::getDestination() const
{
    return Destination;
}

char Trafic::getTypeTransport() const
{
    return TypeTransport;
}

/*************** SETTERS ***************/

void Trafic::setIdContainer(const string IdContainer)
{
    this->IdContainer = IdContainer;
}

void Trafic::setCoordonnees(const string Coordonnees)
{
    this->Coordonnees = Coordonnees;
}

void Trafic::setEtat(const short Etat)
{
    this->Etat = Etat;
}

void Trafic::setDateReservation(const string DateReservation)
{
    this->DateReservation = DateReservation;
}

void Trafic::setDateArrivee(const string DateArrivee)
{
    this->DateArrivee = DateArrivee;
}

void Trafic::setPoids(const float Poids)
{
    this->Poids = Poids;
}

void Trafic::setDestination(const string Destination)
{
    this->Destination = Destination;
}

void Trafic::setTypeTransport(const char TypeTransport)
{
    this->TypeTransport = TypeTransport;
}

/***************************************/

void Trafic::Serialize(ofstream& f)
{
    cout << "DEBUT SERIALIZE" << endl;
    f << this->Coordonnees << ';' << this->IdContainer << ';' << this->Etat << ';' << this->DateReservation << ';' << this->DateArrivee << ';' << this->Poids << ';' << this->Destination << ';' << this->TypeTransport << endl;
    cout << "FIN SERIALIZE" << endl;
}

void Trafic::Deserialize(string ligne)
{
    cout << "DEBUT DESERIALIZE" << endl;
    char* pTemp;
    pTemp = strtok((char*) ligne.c_str(), ";");
    this->Coordonnees = string(pTemp);
    pTemp = strtok(NULL, ";");
    this->IdContainer = string(pTemp);
    pTemp = strtok(NULL, ";");
    this->Etat = atoi(pTemp);
    pTemp = strtok(NULL, ";");
    this->DateReservation = string(pTemp);
    pTemp = strtok(NULL, ";");
    this->DateArrivee = string(pTemp);
    pTemp = strtok(NULL, ";");
    this->Poids = atof((char*) pTemp);
    pTemp = strtok(NULL, ";");
    this->Destination = string(pTemp);
    pTemp = strtok(NULL, ";");
    this->TypeTransport = *pTemp;
    cout << "FIN DESERIALIZE" << endl;
}
