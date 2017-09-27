#ifndef SOCKET_H_INCLUDED
#define SOCKET_H_INCLUDED

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

#include "SocketException.h"

class Socket
{
	protected:
        int PortEcoute;
        int PortUrgence;
        std::string AdresseIp;
	public:
		Socket(std::string adresseIp, int portEcoute, int portUrgence);
		Socket(const Socket &s);
		~Socket();

		/*************** GETTERS ***************/

		int getPortEcoute() const;
		int getPortUrgence() const;
		std::string getAdresseIp() const;

		/***************************************/
		virtual int Send(void* dataToSend, int length) = 0;
		virtual int Receive(void* dataToReceive, int length) = 0;
};

#endif // SOCKET_H_INCLUDED
