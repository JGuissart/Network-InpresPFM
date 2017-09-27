#include <iostream>
#include <unistd.h>
#include <sys/types.h>
#include <errno.h>
#include <cstdio>
#include <cstring> // memcpy
#include <cstdlib> // exit
#include <pthread.h>
#include <string>
#include <list>
#include <sstream>

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
#include "SocketServeur.h"
#include "SocketException.h"
#include "CMMP.h"
#include "Trafic.h"
#include "ListTrafic.h"
#include "Properties.h"
#define DOC "DENY_OF_CONNEXION"
#define EOC "END_OF_CONNEXION"

/************** Threads **************/

pthread_t* threadHandle;

/* =========== Fonctions threads =========== */

void* fctThread(void * param);
char* getThreadIdentity();

/* =========== Mutex et variables de conditions  =========== */

pthread_mutex_t mutexSwitchFctThread;
pthread_mutex_t mutexIndiceCourant;
pthread_cond_t condIndiceCourant;


int * hSocketConnectee;
int indiceCourant = -1;
Properties props = Properties("properties.cfg");
SocketServeur mySocketServeur(props["Host_Server"],props.getInt("PORT_MOUV"),props.getInt("PORT_ADMIN"));
string Separator = props["Sep"].substr(0, props["Sep"].length()-1);

int main()
{
	cout << "SEPARATOR ==> " << Separator << endl;
	string msgFromClient = "", msgFromServer = "";
    int ret, j;
    int* retThread;

	try
	{
		threadHandle = (pthread_t *)malloc (sizeof(pthread_t) * props.getInt("Max_Clients"));
		hSocketConnectee = new int[props.getInt("Max_Clients")];
		pthread_mutex_init(&mutexSwitchFctThread, NULL);
		pthread_mutex_init(&mutexIndiceCourant,NULL);
		pthread_cond_init(&condIndiceCourant, NULL);

		for(int i = 0; i < props.getInt("Max_Clients"); i++)
			hSocketConnectee[i] = -1;

		for(int i = 0; i < props.getInt("Max_Clients"); i++)
		{
			ret = pthread_create(&threadHandle[i], NULL, fctThread, (void*)i);
			cout << "Thread " << i + 1  << " cree" << endl;
			ret = pthread_detach(threadHandle[i]);
		}

		do
		{
			mySocketServeur.Listening();
			mySocketServeur.Accepting();

			cout << "Recherche d'une socket de libre..." << endl;

			for(j = 0; j < props.getInt("Max_Clients") && hSocketConnectee[j] != -1; j++);

			if(j == props.getInt("Max_Clients"))
			{
				cout << "Plus de connexion disponible" << endl;
				msgFromServer = DOC;
				mySocketServeur.Send((void*)msgFromServer.c_str(), props.getInt("Max_String"));
				close(mySocketServeur.getSocketService());
			}
			else
			{
				cout << "Connexion sur la socket num " << j + 1 << endl;
				pthread_mutex_lock(&mutexIndiceCourant);
				hSocketConnectee[j] = mySocketServeur.getSocketService();
				indiceCourant = j;
				pthread_mutex_unlock(&mutexIndiceCourant);
				pthread_cond_signal(&condIndiceCourant);
			}
		}
		while(1);

		/* 11. Fermeture des sockets */

		close(mySocketServeur.getSocketService()); /* Fermeture de la socket */
		printf("Socket connectee au client fermee\n");
		close(mySocketServeur.getSocketEcoute()); /* Fermeture de la socket */
		printf("Socket serveur fermee\n");
		return 0;
	}
	catch(SocketException e)
    {
        cout << e.getMessage() << endl;
        if(e.getSocket() != -1)
            close(e.getSocket());

        exit(1);
    }
}

/****************************************************************************************/

void* fctThread(void* param)
{
	string msgFromClient = "", msgFromServer = "";
	int iCliTraite, finDialogue = 0, ret, type;
	stringstream ssResponseToClient;
	string reponse, message;

	while(1)
	{
		pthread_mutex_lock(&mutexIndiceCourant);
		while(indiceCourant == -1)
			pthread_cond_wait(&condIndiceCourant, &mutexIndiceCourant);
		iCliTraite = indiceCourant;
		indiceCourant = -1;
		mySocketServeur.setSocketService(hSocketConnectee[iCliTraite]);
		pthread_mutex_unlock(&mutexIndiceCourant);

		finDialogue = 0;
		do
		{
			msgFromClient.clear();
			if((ret = mySocketServeur.Receive((void*)msgFromClient.c_str(), props.getInt("Max_String"))) == 0)
			{
				cout << "Le client est parti ! "<< endl ;
				finDialogue = 1;
				break;
			}

			if(msgFromClient.compare(EOC) == 0)
			{
				cout << "EOC "<< endl ;
				finDialogue=1;
				break;
			}
			cout << "MessageC : "<< msgFromClient.c_str() << endl;
            string strMessageFromClient(msgFromClient);
			int pos = strMessageFromClient.find_first_of(Separator);
            string Message = strMessageFromClient.substr(pos + 1);
            string Command = strMessageFromClient.substr(0, pos);
            string retVerifUserPassword, retInputTruck, retInputDone, retOutputReady, retOutputOne, retOutputDone;
            

            cout << "Message restant => " << Message.c_str() << endl;
            cout << "Commande => " << Command.c_str() << endl;
            cout << "Separator => " << Separator << "-" << endl;

            char* pTemp;
			string Login, Password;

			int iContainers = 0;
			string strImmatriculation;
			string strDestination;
			string strContainers[props.getInt("Max_Containers")];

			string strTypeTransport;
			string Destination;
			int iCapacite = 0;

			float fPoidsContainers[props.getInt("Max_Containers")];

			string strIdTransport;

            pthread_mutex_lock(&mutexSwitchFctThread);
            ssResponseToClient.str("");
			switch(atoi(Command.c_str()))
			{
				case LOGIN: // LOGIN#User#Password
					cout << "Je suis dans case LOGIN" << endl;
					pTemp = strtok((char*)msgFromClient.c_str(), Separator.c_str());
					pTemp = strtok(NULL, Separator.c_str());
					Login = string(pTemp);
					pTemp = strtok(NULL, Separator.c_str());
					Password = string(pTemp);
					retVerifUserPassword = VerificationLoginPassword(Login, Password, Separator);                  
					ssResponseToClient << LOGIN << Separator << retVerifUserPassword;
                    msgFromServer = ssResponseToClient.str();
					cout <<"Separator:" << Separator << "-" << endl;
					cout << "Message du serveur au client ==> " << msgFromServer << endl;
					
					if((ret = mySocketServeur.Send((void*)msgFromServer.c_str(), props.getInt("Max_String"))) == 0)
					{
						cout << "Le client est parti !" << endl ;
						finDialogue = 1;
						break;
					}
					cout << "Je sors de case LOGIN" << endl;
					break;

				case INPUT_TRUCK: // INPUT_TRUCK#Immatriculation#Destination#3#idContainer01#idContainer02#idContainer03
					cout << "Je suis dans case INPUT_TRUCK" << endl;
					pTemp = strtok((char*)msgFromClient.c_str(), Separator.c_str()); // La commande
					pTemp = strtok(NULL, Separator.c_str());
					strImmatriculation = string(pTemp);
					pTemp = strtok(NULL, Separator.c_str());
					strDestination = string(pTemp);
					pTemp = strtok(NULL, Separator.c_str());
					iContainers = atoi(pTemp);
					for (int i = 0; i < iContainers; i++)
					{
						pTemp = strtok(NULL, Separator.c_str());
						strContainers[i] = string(pTemp);
					}

				    retInputTruck = InputTruck(strContainers, iContainers, strDestination, Separator);
				    cout << "Retour fct InputTruck = " << retInputTruck << endl;
				    ssResponseToClient << INPUT_TRUCK << Separator << retInputTruck;
				    msgFromServer = ssResponseToClient.str();
					cout <<"Separator:" << Separator << "-" << endl;
					cout << "Message du serveur au client ==> " << msgFromServer << endl;
					
					if((ret = mySocketServeur.Send((void*)msgFromServer.c_str(), props.getInt("Max_String"))) == 0)
					{
						cout << "Le client est parti !" << endl ;
						finDialogue = 1;
						break;
					}
				    cout << "Je sors de case INPUT_TRUCK" << endl;
					break;

				case INPUT_DONE: // INPUT_DONE#3#idContainer01#Poids01#idContainer02#Poids02#idContainer03#Poids03
					cout << "Je suis dans case INPUT_DONE" << endl;
					cout << "Coucou" << endl;
					cout << "iContainers = " << iContainers << endl;
					cout << "msgFromClient = " << msgFromClient << endl;
					cout << "char msgFromClient = " << msgFromClient.c_str() << endl;

					pTemp = strtok((char*)msgFromClient.c_str(), Separator.c_str()); // La commande
					cout << "1er pTemp = " << *pTemp << endl;
					pTemp = strtok(NULL, Separator.c_str());
					cout << "3e pTemp = " << *pTemp << endl;
					iContainers = atoi(pTemp);
					cout << "iContainers = " << iContainers << endl;
					
					for (int i = 0; i < iContainers; i++)
					{
						pTemp = strtok(NULL, Separator.c_str());
						strContainers[i] = string(pTemp);
						pTemp = strtok(NULL, Separator.c_str());
						fPoidsContainers[i] = atof(pTemp);
					}
				    retInputDone = InputDone(strContainers, fPoidsContainers, iContainers, Separator);
				   	cout << "Retour fct InputDone = " << retInputDone << endl;
				    ssResponseToClient << INPUT_DONE << Separator << retInputDone;
				    msgFromServer = ssResponseToClient.str();
					cout <<"Separator:" << Separator << "-" << endl;
					cout << "Message du serveur au client ==> " << msgFromServer << endl;
					
					if((ret = mySocketServeur.Send((void*)msgFromServer.c_str(), props.getInt("Max_String"))) == 0)
					{
						cout << "Le client est parti !" << endl ;
						finDialogue = 1;
						break;
					}
				    cout << "Je sors de case INPUT_DONE" << endl;
					break;

				case OUTPUT_READY: // OUTPUT_READY#TypeTransport#IdTransport#Destination#CapMaxConteneur
					cout << "Je suis dans case OUTPUT_READY" << endl;
					pTemp = strtok((char*)msgFromClient.c_str(), Separator.c_str()); // La commande
					pTemp = strtok(NULL, Separator.c_str());
					strTypeTransport = string(pTemp);
					pTemp = strtok(NULL, Separator.c_str());
					strIdTransport = string(pTemp);
					pTemp = strtok(NULL, Separator.c_str());
					Destination = string(pTemp);
					pTemp = strtok(NULL, Separator.c_str());
					iCapacite = atoi(pTemp);
				    retOutputReady = OutputReady(strTypeTransport, Destination, iCapacite, Separator);
				    ssResponseToClient.str("");
				    ssResponseToClient << OUTPUT_READY << Separator << retOutputReady;
				    msgFromServer = ssResponseToClient.str();
				    if((ret = mySocketServeur.Send((void*)msgFromServer.c_str(), props.getInt("Max_String"))) == 0)
					{
						cout << "Le client est parti !" << endl ;
						finDialogue = 1;
						break;
					}
				    cout << "Je sors de case OUTPUT_READY" << endl;
					break;

				case OUTPUT_ONE: // OUTPUT_ONE#idContainer
					cout << "Je suis dans case OUTPUT_ONE" << endl;
					pTemp = strtok((char*)msgFromClient.c_str(), Separator.c_str()); // La commande
					pTemp = strtok(NULL, Separator.c_str());
					strTypeTransport = string(pTemp); 
				    retOutputOne = OutputOne(Message, Separator);
				    cout << "Je sors de case OUTPUT_ONE" << endl;
					break;

				case OUTPUT_DONE: // OUTPUT_DONE#idTrainBateau#NombreContainersCharges
					cout << "Je suis dans case OUTPUT_DONE" << endl;
				    retOutputDone = OutputDone(Message, Separator);
				    cout << "Je sors de case OUTPUT_DONE" << endl;
					break;

				case LOGOUT:
					cout << "Je suis dans case LOGOUT" << endl;
					pTemp = strtok((char*)msgFromClient.c_str(), Separator.c_str());
					pTemp = strtok(NULL, Separator.c_str());
					Login = string(pTemp);
					pTemp = strtok(NULL, Separator.c_str());
					Password = string(pTemp);
					retVerifUserPassword = VerificationLoginPassword(Login, Password, Separator);
			  		ssResponseToClient.str("");                  
					ssResponseToClient << LOGOUT << Separator << retVerifUserPassword;
                    msgFromServer = ssResponseToClient.str();
					cout <<"Separator:" << Separator << "-" << endl;
					cout << "Message du serveur au client ==> " << msgFromServer << endl;
					
					if((ret = mySocketServeur.Send((void*)msgFromServer.c_str(), props.getInt("Max_String"))) == 0)
					{
						cout << "Le client est parti !" << endl ;
						finDialogue = 1;
						break;
					}

					pthread_mutex_lock(&mutexIndiceCourant);
					hSocketConnectee[iCliTraite] = -1;
					finDialogue = 1;
					pthread_mutex_unlock(&mutexIndiceCourant);

					cout << "Je sors de case LOGOUT" << endl;
					break;
			}
			pthread_mutex_unlock(&mutexSwitchFctThread);
			cout << "fin" << endl;
		}
		while(!finDialogue);
	}
	return 0;
}
