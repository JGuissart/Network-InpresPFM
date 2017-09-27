#ifndef MOMENT_H_INCLUDED
#define MOMENT_H_INCLUDED

#include <iostream>
#include <time.h> // Fonction time(...), localtime(...), mktime(...)
#include <string>

class Moment
{
    private:
        int Value;
        bool bDate;
    public:
        Moment(); // Constructeur par défaut
        Moment(int duree); // Constructeur d'initialisation avec durée
        Moment(int y, int m, int d); // Construct Init avaec date yyyy:mm:dd
        Moment(int y, int mon, int d, int h, int min); // Construct init avec date et heure yyyy:mm:dd hh:mm
        Moment(const Moment& m); // Constructeur de copie
        ~Moment(); // Destructeur

        /********* Getters *********/

        int getValue() const;
        bool IsDate() const;
        int getAnnee() const;
        int getMois() const;
        int getJour() const;
        int getHeure() const;
        int getMinute() const;
        int getSeconde() const;

        /********* Setters *********/

        void setValue(int v);
        void setDate(bool d);
        void setAnnee(int y);
        void setMois(int m);
        void setJour(int d);
        void setHeure(int h);
        void setMinute(int m);
        void setSeconde(int s);

        /********* Surcharges d'operateurs *********/

        Moment& operator=(const Moment &m);
        Moment operator+(int min);
        Moment operator-(int min);
        Moment operator+(const Moment &d);
        Moment operator-(const Moment &d);
        friend std::ostream &operator<<(std::ostream &o, const Moment &m);
        bool operator<(const Moment &m);
        bool operator >(const Moment &m);
        bool operator==(const Moment &m);
        Moment operator++(); // Pré-incrémentation
        Moment operator++(int); // Post-incrémentation

        /*******************************************/

        std::string ToString() const;
        static Moment Now();
};

#endif // MOMENT_H_INCLUDED
