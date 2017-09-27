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

#include "SocketClient.h"

SocketClient::SocketClient(string adresseIp, int portEcoute, int portUrgence) : Socket(adresseIp, portEcoute, portUrgence)
{
    struct hostent *infosHost; // nfos sur le host : pour gethostbyname
	struct in_addr adresseIP; // Adresse Internet au format reseau
	struct sockaddr_in adresseSocket; // Structure de type sockaddr - ici, cas de TCP
	unsigned int tailleSockaddr_in;
	int ret; // valeur de retour

	/* 1. Création de la socket */
	hSocket = socket(AF_INET, SOCK_STREAM, 0);
	if (hSocket == -1)
	    throw SocketException(errno, "SocketClient", "SocketClient", "Erreur de creation de la socket");
	else
        cout << "Creation de la socket OK !" << endl;

	/* 2. Acquisition des informations sur l'ordinateur distant */
	if ((infosHost = gethostbyname(adresseIp.c_str())) == NULL)
	    throw SocketException(errno, "SocketClient", "SocketClient", "Erreur d'acquisition d'infos sur le host distant");
	else
        cout << "Acquisition infos host OK !" << endl;

	memcpy(&adresseIP, infosHost->h_addr, infosHost->h_length);
    cout << "Adresse IP = " << inet_ntoa(adresseIP) << endl; // Conversion de l'adresse contenue dans le structure in_addr en une chaine comprehensible

	/* 3. Préparation de la structure sockaddr_in */
	memset(&adresseSocket, 0, sizeof(struct sockaddr_in));
	adresseSocket.sin_family = AF_INET; // Domaine
	adresseSocket.sin_port = htons(portEcoute); // conversion numéro de port au format réseau
	memcpy(&adresseSocket.sin_addr, infosHost->h_addr, infosHost->h_length);

	/* 4. Tentative de connexion */
	tailleSockaddr_in = sizeof(struct sockaddr_in);
	if((ret = connect(this->hSocket, (struct sockaddr *)&adresseSocket, tailleSockaddr_in)) == -1)
	    throw SocketException(errno, "SocketClient", "SocketClient", "Erreur d'acquisition d'infos sur le host distant", this->hSocket);
	else
        cout << "Connect socket OK" << endl;
}

SocketClient::SocketClient(const SocketClient& sc) : Socket(sc)
{
    hSocket = sc.hSocket;
}

SocketClient::~SocketClient()
{
    close(hSocket);
    cout << "Socket client fermee" << endl;
}

/*************** GETTERS ***************/

int SocketClient::getSocket() const
{
    return hSocket;
}

/***************************************/

int SocketClient::Send(void* dataToSend, int length)
{
    int iNbreSend = 0;
	iNbreSend = send(hSocket, dataToSend, length, 0);

	if(iNbreSend == -1)
	    throw SocketException(errno, "SocketServeur", "Send", "Erreur lors du send");

	return iNbreSend;
}

int SocketClient::Receive( void* dataToReceive,int length)
{
    int iNbreRecv;
	int iNombreRecu = 0;

	do
	{
	    iNbreRecv = recv(hSocket, dataToReceive + iNombreRecu, length - iNombreRecu, 0);

		if (iNbreRecv == -1)
		{
		    throw SocketException(errno, "SocketServeur", "Receive", "Erreur sur le recv de la socket");
			//iNombreRecu += iNbreRecv;
		}
		iNombreRecu += iNbreRecv;
	}
	while(iNombreRecu < length && iNombreRecu != 0 && iNombreRecu != -1);

    return iNombreRecu;
}
