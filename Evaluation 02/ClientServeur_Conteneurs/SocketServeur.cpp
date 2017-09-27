#include <iostream>
#include <unistd.h>
#include <sys/types.h>
#include <errno.h>
#include <cstdio>
#include <cstring> // memcpy
#include <cstdlib> // exit
#include <pthread.h>
#include <string>

#ifdef WIN32
    #include <winsock2.h>
    #include <windows.h>
#else
	#include <sys/socket.h> // pour les types de socket
	#include <netinet/in.h> // pour la conversion adresse reseau->format dot ainsi que la conversion format local/format reseau
	#include <netinet/tcp.h> // Pour la conversion adresse reseau->format dot
	#include <arpa/inet.h> // pour la conversion adresse reseau->format dot
	#include <netdb.h> // gethostbyname
#endif

using namespace std;

#include "SocketServeur.h"

SocketServeur::SocketServeur(string adresseIp, int portEcoute, int portUrgence) : Socket(adresseIp, portEcoute, portUrgence)
{
    struct hostent * infosHost; // Infos sur le host : pour gethostbyname
	struct in_addr adresseIP; // Adresse Internet au format reseau

    // 1. Creation de la socket d'ecoute
    hSocketEcoute = socket(AF_INET, SOCK_STREAM, 0);
    if(hSocketEcoute == -1)
        throw SocketException(errno, "SocketServeur", "SocketServeur", "Erreur de creation de la socket");
    else
        cout << "Creation de la socket OK !" << endl;

    // 2. Acquisition des informations sur l'ordinateur local
    if((infosHost = gethostbyname(adresseIp.c_str())) == 0)
        throw SocketException(errno, "SocketServeur", "SocketServeur", "Erreur d'acquisition d'infos sur le host");
    else
        cout << "Acquisition infos host OK !" << endl;

    memcpy(&adresseIP, infosHost->h_addr, infosHost->h_length);
    cout << "Adresse IP = " << inet_ntoa(adresseIP) << endl; // Conversion de l'adresse contenue dans le structure in_addr en une chaine comprehensible

    // 3. Préparation de la structure sockaddr_in
    memset(&adresseSocket, 0, sizeof(struct sockaddr_in));
    adresseSocket.sin_family = AF_INET;
    adresseSocket.sin_port = htons(portEcoute);
    memcpy(&adresseSocket.sin_addr, infosHost->h_addr, infosHost->h_length);

    // 4. Le système prend connaissance de l'adresse et du port de la socket
    if(bind(hSocketEcoute, (struct sockaddr *)&adresseSocket, sizeof(struct sockaddr_in)) == -1)
        throw SocketException(errno, "SocketServeur", "SocketServeur", "Erreur sur le bind de la socket");
    else
        cout << "Bind adresse et port socket OK !" << endl;
}

SocketServeur::SocketServeur(const SocketServeur& ss) : Socket(ss)
{
    hSocketEcoute = ss.hSocketEcoute;
    hSocketService = ss.hSocketService;
}

SocketServeur::~SocketServeur()
{
    /* Fermeture des sockets */
	close(hSocketEcoute);
	cout << "Socket d'ecoute fermee" << endl;
	close(hSocketService);
	cout << "Socket de service fermee" << endl;
}

/*************** GETTERS ***************/

int SocketServeur::getSocketEcoute() const
{
    return hSocketEcoute;
}
int SocketServeur::getSocketService() const
{
    return hSocketService;
}

void SocketServeur::setSocketService(const int service)
{
	hSocketService=service;
}
/***************************************/

/*SocketServeur::Bind()
{
    // 5. Le système prend connaissance de l'adresse et du port de la socket
    if(bind(hSocketEcoute, (struct sockaddr *)&adresseSocket, sizeof(struct sockaddr_in)) == -1)
    {
        cerr << "Erreur sur le bind de la socket " << errno << endl;
        exit(1);
    }
    else
        cout << "Bind adresse et port socket OK !" << endl;
}*/

void SocketServeur::Listening()
{
    if(listen(hSocketEcoute, SOMAXCONN) == -1)
        throw SocketException(errno, "SocketServeur", "Listen", "Erreur sur le listen de la socket", hSocketEcoute);
    else
        cout << "Listen socket OK !" << endl;
}

void SocketServeur::Accepting()
{
    // 8. Acceptation d'une connexion
    socklen_t tailleSockaddr_in = sizeof(struct sockaddr_in);   
if((hSocketService = accept(hSocketEcoute, (struct sockaddr *)&adresseSocket, &tailleSockaddr_in)) == -1)
        throw SocketException(errno, "SocketServeur", "Accept", "Erreur sur l'accept de la socket", hSocketEcoute);
    else
        cout << "Accept socket OK !" << endl;
}

int SocketServeur::Send(void* dataToSend, int length)
{
    int iNbreSend = 0;
	iNbreSend = send(hSocketService, dataToSend, length, 0);

	if(iNbreSend == -1)
	    throw SocketException(errno, "SocketServeur", "Send", "Erreur lors du send");

	return iNbreSend;
}

int SocketServeur::Receive(void* dataToReceive, int length)
{
    int iNbreRecv;
	int iNombreRecu = 0;

	do
	{
	    iNbreRecv = recv(hSocketService, dataToReceive + iNombreRecu, length - iNombreRecu, 0);
		if (iNbreRecv == -1)
		{
		
		    throw SocketException(errno, "SocketServeur", "Receive", "Erreur sur le recv de la socket");
			//iNombreRecu += iNbreRecv;
		}
		iNombreRecu += iNbreRecv;
	}
	while(iNombreRecu < length && iNombreRecu != 0 && iNombreRecu != -1);

	/**
        Je sais pas s'il faut renvoyer iNbreRecv ou iNombreRecu ...
	*/

	return iNombreRecu;
}
