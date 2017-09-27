#ifndef SOCKETCLIENT_H_INCLUDED
#define SOCKETCLIENT_H_INCLUDED

#include "Socket.h"

class SocketClient : public Socket
{
    private:
        int hSocket;
    public:
        SocketClient(std::string adresseIp, int portEcoute, int portUrgence);
        SocketClient(const SocketClient& sc);
        ~SocketClient();

        /*************** GETTERS ***************/

        int getSocket() const;

        /***************************************/

        int Send(void* dataToSend, int length);
		int Receive(void* dataToReceive,int length);
};

#endif // SOCKETCLIENT_H_INCLUDED
