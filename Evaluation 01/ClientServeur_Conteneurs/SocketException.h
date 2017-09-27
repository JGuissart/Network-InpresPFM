#ifndef SOCKETEXCEPTION_H_INCLUDED
#define SOCKETEXCEPTION_H_INCLUDED

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

class SocketException
{
    private:
        int ErrorCode;
        std::string ObjectError;
        std::string MethodError;
        std::string ProgrammerMessage;
        std::string ErrorMessage;
        std::string Message;
        int hSocket;
        void ErrorAnalysis();
    public:
        SocketException();
        SocketException(int ErrorCode, std::string ObjectError, std::string MethodError, std::string ProgrammerMessage);
        SocketException(int ErrorCode, std::string ObjectError, std::string MethodError, std::string ProgrammerMessage, int hSocket);
        SocketException(const SocketException& se);
        ~SocketException();

		/*************** GETTERS ***************/

		int getErrorCode() const;
        std::string getMessage() const;
        std::string getProgrammerMessage() const;
        int getSocket() const;

        /***************************************/
};

#endif // SOCKETEXCEPTION_H_INCLUDED
