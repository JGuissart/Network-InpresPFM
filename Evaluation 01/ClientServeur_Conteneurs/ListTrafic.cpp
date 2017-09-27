

using namespace std;

#include "ListTrafic.h"

ListTrafic::ListTrafic()
{
    this->FillListByFile();
}

ListTrafic::~ListTrafic()
{

}

bool ListTrafic::isFull()
{
    int Cpt = 0;

    list<Trafic>::iterator it;
    for(it = TheList.begin(); it != TheList.end(); it++)
    {
        if(it->getEtat() == 0)
            Cpt++;
    }

    if(Cpt == 25)
        return true;
    else
        return false;
}

list<Trafic> ListTrafic::getByDestination(string Destination)
{
    list<Trafic> listTemp;
    list<Trafic>::iterator it;
    for(it = TheList.begin(); it != TheList.end(); it++)
    {
        if(it->getDestination() == Destination)
            listTemp.push_back(*it);
    }

    return listTemp;
}

Trafic ListTrafic::get(int Choix, std::string Critere)
{
    cout << "DEBUT GET_STRING" << endl;
    list<Trafic>::iterator it;
    if(!Choix)
    {
        for(it = TheList.begin(); it != TheList.end(); it++)
        {
            if(it->getIdContainer() == Critere)
                break;
        }
    }
    else
    {
        for(it = TheList.begin(); it != TheList.end(); it++)
        {
            if(it->getCoordonnees() == Critere)
                break;
        }
    }
    cout << "FIN GET_STRING" << endl;

    return *it;
}

Trafic ListTrafic::get(short Etat)
{
    cout << "DEBUT GET_SHORT" << endl;
    list<Trafic>::iterator it;
    for(it = TheList.begin(); it != TheList.end(); it++)
    {
        if(it->getEtat() == Etat)
            break;
    }
    cout << "FIN GET_SHORT" << endl;

    return *it;
}

void ListTrafic::Insert(Trafic& t)
{
    if(!this->isFull())
        TheList.push_back(t);
}

void ListTrafic::Update(Trafic& t)
{
    cout << "DEBUT UPDATE" << endl;
    list<Trafic>::iterator it;
    for(it = TheList.begin(); it != TheList.end(); it++)
    {
        if(it->getCoordonnees() == t.getCoordonnees())
            (*it) = t;
    }
    cout << "FIN UPDATE" << endl;
}

Trafic ListTrafic::Remove(string IdContainer)
{
    cout << "DEBUT REMOVE" << endl;
    list<Trafic>::iterator it;
    cout << "AVANT BOUCLE" << endl;
    for(it = TheList.begin(); it != TheList.end(); it++)
    {
        if(it->getIdContainer() == IdContainer)
            break;
    }
    cout << "Coordonnees it = " << it->getCoordonnees() << endl;
    cout << "FIN BOUCLE + initialisation trafic t" << endl;
    Trafic t(*it);
    cout << "FIN initialisation trafic t" << endl;
    TheList.erase(it);
    cout << "FIN REMOVE" << endl;
    return t;
}

void ListTrafic::FillListByFile()
{
    ifstream fListe("FICH_PARC.csv", ios::in);

    if (fListe.is_open())
    {
        string ligne;

        while(getline(fListe, ligne))
        {
            Trafic t;
            t.Deserialize(ligne);
            this->Insert(t);
        }
    }
    fListe.close();
}

void ListTrafic::SaveListIntoFile()
{
    cout << "DEBUT SAVE_LIST_INTO_FILE" << endl;
    ofstream fListe("FICH_PARC.csv", ios::out);

    if (fListe.is_open())
    {
        for(list<Trafic>::iterator it = TheList.begin(); it != TheList.end(); it++)
            it->Serialize(fListe);
    }
    fListe.close();
    cout << "FIN SAVE_LIST_INTO_FILE" << endl;
}

int ListTrafic::Count()
{
    int Cpt = 0;

    list<Trafic>::iterator it;
    for(it = TheList.begin(); it != TheList.end(); it++)
    {
        if(it->getEtat() == 0)
            Cpt++;
    }

    return Cpt;
}
