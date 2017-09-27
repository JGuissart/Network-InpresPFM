#ifndef SOCKETSERVEUR_H_INCLUDED
#define SOCKETSERVEUR_H_INCLUDED

#include "Socket.h"

class SocketServeur : public Socket
{
    private:
        int hSocketEcoute;
        int hSocketService;
        struct sockaddr_in adresseSocket; // Structure de type sockaddr - ici, cas de TCP
    public:
        SocketServeur(std::string adresseIp, int portEcoute, int portUrgence);
        SocketServeur(const SocketServeur& ss);
        ~SocketServeur();

        /*************** GETTERS ***************/

        int getSocketEcoute() const;
        int getSocketService() const;
	
	void setSocketService(const int service);
        /***************************************/
        // void Bind(); 
		void Listening();
		void Accepting();
		int Send(void* dataToSend, int length);
		int Receive(void* dataToReceive, int length);
};

#endif // SOCKETSERVEUR_H_INCLUDED
