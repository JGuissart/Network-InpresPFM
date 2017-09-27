

using namespace std;

#include "CMMP.h"

string VerificationLoginPassword(string Login, string Password, string Sep)
{
    cout << "Login => " << Login << endl;
    cout << "Password => " << Password << endl;

    Properties p("Login.csv");
	
    if(p[Login] == "NULL")
    {
        cout << "Je suis dans login == NULL" << endl;
        cout << p[Login] << " =?= " << "NULL" << endl;
        return "KO" + Sep + "User_inconnu"; // L'utilisateur n'existe pas
    }
    else if(p[Login].compare(Password) != 0)
    {
        cout << "Je suis dans Login != Password" << endl;
        cout << p[Login] << " =?= " << Password << endl;
        return "KO" + Sep + "Bad_password"; // Le mot de passe est incorrect
    }
    else
    {
        cout << "Je suis dans Login == Password" << endl;
        cout << p[Login] << " =?= " << Password << endl;
        return "OK"+ Sep + "Authentification_reussie" ; // Authentification ok
    }
}

string InputTruck(string ArrayContainers[], int Size, string Destination, string Sep)
{
    ListTrafic lt;
    string Coordonnees;
    stringstream ssConvertIntToString;
    ssConvertIntToString << Size;
    Coordonnees = ssConvertIntToString.str();

    if(lt.Count() < Size) // S'il n'y a pas assez de place pour accueillir tous les containers apportés par le camion
        return string("KO" + Sep + "Plus_de_place");

    for (int i = 0; i < Size; i++)
    {
        Trafic t = lt.get(0);
        t.setIdContainer(ArrayContainers[i]);
        t.setEtat(1);
        t.setDestination(Destination);
        t.setDateReservation(Moment::Now().ToString());
        Coordonnees += Sep + t.getCoordonnees();
        lt.Update(t);
        lt.SaveListIntoFile();
    }

    cout << "Coordonnees renvoyees = " << Coordonnees << endl;

    // Renvoi chaîne du style #OK#N#CoordonneesContainer1#CoordonneesContainer2#CoordonneesContainerN (en fonction du nombre de container arrivant)
    return string("OK" + Sep + Coordonnees);
}

string InputDone(string ArrayContainers[], float ArrayWeight[], int Size, string Sep)
{
    cout << "Je suis dans INPUTDONE" << endl;
    int random = rand() % 100;
    cout << "Random = " << random << endl;
    if(random % 2 == 0)
    {
        cout << "Random modulo 2 = " << random % 2 << endl;
        ListTrafic lt;

        for (int i = 0; i < Size; i++)
        {
            Trafic t = lt.get(0, ArrayContainers[i]);
            cout << "idContainer du trafic recup = " << t.getIdContainer() << endl;
            t.setPoids(ArrayWeight[i]);
            t.setEtat(2);
            t.setDateArrivee(Moment::Now().ToString());
            lt.Update(t);
            lt.SaveListIntoFile();
        }
        return string("OK");
    }
    else
    {
        random = rand() % 100;

        if(random % 2 == 0)
            return string("KO" + Sep + "Container_non_conforme");
        else if(random % 3 == 0)
            return string("KO" + Sep + "Container_trop_grand");
        else
        {
            if(random % 5 == 0)
                return string("KO" + Sep + "Container_trop_lourd");
        }
    }
}

string OutputReady(string strIdTypeTransport, string Destination, int Capacity, string Sep)
{
    string strReturn = "";
    ListTrafic lt;
    list<Trafic> listTraf = lt.getByDestination(Destination);

    if(listTraf.size() == 0)
        strReturn = "KO" + Sep + "Pas_de_container_pour_cette_destination";
    else
    {
        list<Trafic>::iterator it;
        stringstream ssStrReturn;
        ssStrReturn << "OK" << Sep << listTraf.size();
        for(it = listTraf.begin(); it != listTraf.end(); it++)
            ssStrReturn << Sep << it->getIdContainer();

        strReturn = ssStrReturn.str();
    }

    return strReturn;
}

string OutputOne(string Message, string Sep)
{
    // même style que fonctions du dessus

    // Utilisation de la classe ListTrafic
    return string("");
}

string OutputDone(string Message, string Sep)
{
    // même style que fonctions du dessus
    return string("");
}
