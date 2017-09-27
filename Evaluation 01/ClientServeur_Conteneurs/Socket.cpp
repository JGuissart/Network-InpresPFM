

using namespace std;

#include "Socket.h"

Socket::Socket(string adresseIp, int portEcoute, int portUrgence)
{
    AdresseIp = adresseIp;
    PortEcoute = portEcoute;
    PortUrgence = portUrgence;
}

Socket::Socket(const Socket& s)
{
    AdresseIp = s.AdresseIp;
    PortEcoute = s.PortEcoute;
    PortUrgence = s.PortUrgence;
}

Socket::~Socket()
{

}

/*************** GETTERS ***************/

int Socket::getPortEcoute() const
{
	return PortEcoute;
}

int Socket::getPortUrgence() const
{
	return PortUrgence;
}

string Socket::getAdresseIp() const
{
    return AdresseIp;
}

/***************************************/
/*int Socket::Receive()
{

}

int Socket::Send(string dataToSend)
{
    int ret = send(hSocket, dataToSend.c_str(), MAXSTRING, 0);
    if (ret == -1)*/ /* pas message urgent */
 /*   {
        cerr << "Erreur sur le send de la socket " << errno << endl;
        close(hSocket); *//* Fermeture de la socket */
       /* exit(1);
    }
    else
        cout << "Send Socket OK !" << endl;

    cout << "Message envoye = " << dataToSend << endl;
    return ret;
}*/
