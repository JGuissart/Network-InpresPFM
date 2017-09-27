#ifndef LISTTRAFIC_H_INCLUDED
#define LISTTRAFIC_H_INCLUDED

#include <list>

#include "Trafic.h"

class ListTrafic
{
    private:
        std::list<Trafic> TheList;
        bool isFull();
        void FillListByFile();
    public:
        ListTrafic();
		~ListTrafic();

        /****************************************/

        list<Trafic> getByDestination(std::string Destination);
        Trafic get(int Choix, std::string Critere);
        Trafic get(short Etat);
        void Insert(Trafic& t);
        void Update(Trafic& t);
        Trafic Remove(std::string IdContainer);
        void SaveListIntoFile();
        int Count();
};

#endif // LISTTRAFIC_H_INCLUDED
