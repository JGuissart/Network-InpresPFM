#ifndef TRAFIC_H_INCLUDED
#define TRAFIC_H_INCLUDED

#include <iostream>
#include <string>
#include <fstream>

class Trafic
{
    private:
        std::string Coordonnees;
        std::string IdContainer;
        short Etat;
        std::string DateReservation;
        std::string DateArrivee;
        float Poids;
        std::string Destination;
        char TypeTransport;
    public:
        Trafic();
        Trafic(std::string Coordonnees, std::string IdContainer, short Etat, std::string DateReservation, std::string DateArrivee, float Poids, std::string Destination, char TypeTransport);
        Trafic(const Trafic& t);
        ~Trafic();

        /*************** GETTERS ***************/

		std::string getIdContainer() const;
		std::string getCoordonnees() const;
		short getEtat() const;
		std::string getDateReservation() const;
        std::string getDateArrivee() const;
        float getPoids() const;
        std::string getDestination() const;
        char getTypeTransport() const;

        /*************** SETTERS ***************/

        void setIdContainer(const std::string IdContainer);
        void setCoordonnees(const std::string Coordonnees);
        void setEtat(const short Etat);
        void setDateReservation(const std::string DateReservation);
        void setDateArrivee(const std::string DateArrivee);
        void setPoids(const float Poids);
        void setDestination(const std::string Destination);
        void setTypeTransport(const char TypeTransport);

		/***************************************/

        void Serialize(std::ofstream& f);
        void Deserialize(std::string ligne);
};

#endif // TRAFIC_H_INCLUDED
