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

using namespace std;

int iMaxString = 0;

int main()
{
	Properties props = Properties("properties.cfg");
	string Separator = props["Sep"].substr(0, props["Sep"].length() - 1);
	string MsgFromClient = "", MsgFromServeur = "";
	string login, password, reponse, message;
	stringstream ssMessageToServer;
	int choix, type;
	
	SocketClient mySocketClient = SocketClient(props["Host_Client"],props.getInt("PORT_MOUV"),props.getInt("PORT_ADMIN"));
	
	try
	{
		do
		{
			cout << "Entrez votre login : "<< endl;
			cin >> login;
			cout << "Entrez votre mot de passe: " << endl;
			cin >> password;
			
			ssMessageToServer.str("");
			ssMessageToServer << LOGIN << Separator << login << Separator << password ;
			MsgFromClient = ssMessageToServer.str();

			mySocketClient.Send((void*)MsgFromClient.c_str(), props.getInt("Max_String"));
			cout << MsgFromClient << endl;
			MsgFromServeur.clear();
			mySocketClient.Receive((void*)MsgFromServeur.c_str(), props.getInt("Max_String"));		
			cout << "Message : " << MsgFromServeur.c_str() << endl;

			char* pTemp;
			pTemp = strtok((char*)MsgFromServeur.c_str(), Separator.c_str());
			type = atoi(pTemp);
			cout << type << endl;
			pTemp = strtok(NULL, Separator.c_str());
			reponse = pTemp;
			cout << reponse << endl;
			pTemp = strtok(NULL, Separator.c_str());
			message = pTemp;
			cout << message << endl;

			if(reponse.compare("KO") == 0)
				cout << message << endl;
			
		}
		while(type != LOGIN || reponse.compare("OK") != 0);

		do
		{
			char* pTemp;
			do
			{
				cout << "Bienvenue " << login << endl;

				cout << "1.INPUT_TRUCK" << endl; // INPUT_TRUCK#Immatriculation#Destination#3#idContainer01#idContainer02#idContainer03
				cout << "2.OUTPUT_READY" << endl; // OUTPUT_READY#idTrainBateau#Destination#CapMaxConteneur
				cout << "3.OUTPUT_DONE" << endl; // OUTPUT_DONE#idTrainBateau#NombreContainersCharges
				cout << "4.LOGOUT" << endl;
			
				cout << "Que souhaitez-vous faire ?" << endl;
				cin >> choix; 
			}
			while(choix < 1 || choix > 4);

			string immatriculation;
			string destination;
			int numero;
			string strContainers[props.getInt("Max_Containers")];
			bool insertion = false;
			float poids = 0;
			char wait;
			string coordonnee;
			string response;
			string message;
			char TypeTransport;
			string IdTransport;
			int iCapaciteTransport;
			bool bOutputOne = false;
			int Cpt = 0;

			ssMessageToServer.str("");

			switch(choix)
			{
				case 1:
					cout << "Entrez l'immatriculation : " << endl;
					cin >> immatriculation;
					cout << "Entrez la destination : " << endl;
					cin >> destination;
					do
					{
						cout << "Entrez le nombre de containers : " << endl;
						cin >> numero;
						cout << "Max container ==> " << props.getInt("Max_Containers") << endl;
						if(numero > 0 && numero < props.getInt("Max_Containers"))
						{
							ssMessageToServer << INPUT_TRUCK << Separator << immatriculation << Separator << destination << Separator << numero;
							cout << "ssMsgToServer = " << ssMessageToServer.str() << endl;
							
							for(int i = 0; i < numero; i++)
							{
								string idContainer;
								cout << "Entrez l'immatriculation du container " << i+1 << endl;
								cin >> idContainer;
								ssMessageToServer << Separator << idContainer;
								strContainers[i] = idContainer;
							}

							insertion = true;
						}
						else
						{
							cout << "Nombre de containers invalide." << endl;
							insertion = false;
						}
					}
					while(!insertion);

					MsgFromClient = ssMessageToServer.str();

					mySocketClient.Send((void*)MsgFromClient.c_str(), props.getInt("Max_String"));
					MsgFromServeur.clear();
					mySocketClient.Receive((void*)MsgFromServeur.c_str(), props.getInt("Max_String"));

					pTemp = strtok((char*)MsgFromServeur.c_str(), Separator.c_str());
					type = atoi(pTemp);
					pTemp = strtok(NULL, Separator.c_str());
					message = string(pTemp);

					if(message.compare("KO")==0)
					{
						exit(0);
					}

					pTemp = strtok(NULL, Separator.c_str());
					numero = atoi(pTemp);
					for(int i = 0 ; i < numero ; i++)
					{
						pTemp = strtok(NULL, Separator.c_str());
						coordonnee = string(pTemp);
						cout << "Coordonnee du container "<< i + 1 <<": " << endl;  
					}

					cout << "Appuyer sur une touche pour effectuer l'input_done" << endl;
					fflush(stdin);
					wait = getchar();

					/************ PASSAGE INPUT_DONE ********************/
					
					ssMessageToServer.str("");
					ssMessageToServer << INPUT_DONE << Separator << numero;
					for(int i = 0; i < numero ; i++)
					{
						cout << "Entrez le poids du container " << strContainers[i] << ": ";
						cin >>poids;
						ssMessageToServer << Separator << strContainers[i] << Separator << poids;
					}

					cout << "ssMessageToServer + Size ==> " << ssMessageToServer.str() << " " << ssMessageToServer.str().length() << endl;

					MsgFromClient = ssMessageToServer.str();

					cout << "MsgFromClient = " << MsgFromClient << endl;

					mySocketClient.Send((void*)MsgFromClient.c_str(), props.getInt("Max_String"));
					MsgFromServeur.clear();
					mySocketClient.Receive((void*)MsgFromServeur.c_str(), props.getInt("Max_String"));

					cout << "Apres receive" << endl;

					if (MsgFromServeur.find("OK") != string::npos)
						cout << "INPUT_DONE OK" << endl;
					else
					{
						if(MsgFromServeur.find("KO") != string::npos)
						{
							pTemp = strtok((char*)MsgFromServeur.c_str(), Separator.c_str());
							pTemp = strtok(NULL, Separator.c_str());
							response = string(pTemp);
							pTemp = strtok(NULL, Separator.c_str());
							message = string(pTemp);
							cout << response + Separator + message << endl;
							cout << "On ferme boutique ! Bye bye !" << endl;
							close(mySocketClient.getSocket()); /* Fermeture de la socket */
							printf("Socket client fermee\n");

							exit(0);
						}
					}
					break;
				case 2:
					cout << "Un bateau ou un train arrive ? (B ou T) : ";
					cin >> TypeTransport;
					cout << "Entrez l'identifiant du transport : ";
					cin >> IdTransport;
					cout << "Entrez la destination : ";
					cin >> destination;
					cout << "Entrez la capacite max du transport : ";
					cin >> iCapaciteTransport;
					
					ssMessageToServer << OUTPUT_READY << Separator << TypeTransport << Separator << IdTransport << Separator << destination << Separator << iCapaciteTransport;
					cout << "ssMessageToServer = " << ssMessageToServer.str() << endl;
					MsgFromClient = ssMessageToServer.str();
					cout << "MsgFromClient = " << MsgFromClient.c_str() << endl;

					mySocketClient.Send((void*)MsgFromClient.c_str(), props.getInt("Max_String"));
					MsgFromServeur.clear();
					mySocketClient.Receive((void*)MsgFromServeur.c_str(), props.getInt("Max_String"));

					if (MsgFromServeur.find("OK") != string::npos) // OUTPUT_READY#OK#n#idContainer1#idContainer2#idContainern
					{
						pTemp = strtok((char*)MsgFromServeur.c_str(), Separator.c_str()); // La commmande
						pTemp = strtok(NULL, Separator.c_str()); // OK
						pTemp = strtok(NULL, Separator.c_str());
						numero = atoi(pTemp);

						for (int i = 0; i < numero; i++)
						{
							pTemp = strtok(NULL, Separator.c_str());
							strContainers[i] = string(pTemp);
						}
						bOutputOne = true;
					}
					else
					{
						if(MsgFromServeur.find("KO") != string::npos)
						{
							pTemp = strtok((char*)MsgFromServeur.c_str(), Separator.c_str()); // La commande
							pTemp = strtok(NULL, Separator.c_str());
							response = string(pTemp);
							pTemp = strtok(NULL, Separator.c_str());
							message = string(pTemp);
							cout << response + Separator + message << endl;
						}
					}

					if(bOutputOne)
					{
						cout << "Appuyer sur une touche pour effectuer l'output_one" << endl;
						fflush(stdin);
						wait = getchar();

						/************ PASSAGE OUTPUT_ONE ********************/
						
						for(int i = 0; i < numero ; i++)
						{
							ssMessageToServer.str("");
							ssMessageToServer << OUTPUT_ONE << Separator << strContainers[i];
							MsgFromClient = ssMessageToServer.str();
							mySocketClient.Send((void*)MsgFromClient.c_str(), props.getInt("Max_String"));
							MsgFromServeur.clear();
							mySocketClient.Receive((void*)MsgFromServeur.c_str(), props.getInt("Max_String"));

							pTemp = strtok((char*)MsgFromServeur.c_str(), Separator.c_str()); // La commande
							pTemp = strtok(NULL, Separator.c_str());
							response = string(pTemp);
							if(response == "KO")
							{
								pTemp = strtok(NULL, Separator.c_str());
								message = string(pTemp);
								cout << "Message ==> " << message << endl;
								break;
							}
							else
								Cpt++;
						}
					}
					break;
				case 3:
					ssMessageToServer.str("");
					ssMessageToServer << OUTPUT_DONE << Separator << IdTransport << Separator << Cpt;
					MsgFromClient = ssMessageToServer.str();
					mySocketClient.Send((void*)MsgFromClient.c_str(), props.getInt("Max_String"));
					MsgFromServeur.clear();
					mySocketClient.Receive((void*)MsgFromServeur.c_str(), props.getInt("Max_String"));
					pTemp = strtok((char*)MsgFromServeur.c_str(), Separator.c_str()); // La commande
					pTemp = strtok(NULL, Separator.c_str());
					cout << "Valeur de retour = " << *pTemp << endl;
					break;
				case 4:
					do
					{
						cout << "Entrez votre login : "<< endl;
						cin >> login;
						cout << "Entrez votre mot de passe: " << endl;
						cin >> password;
						
						ssMessageToServer.str("");
						ssMessageToServer << LOGOUT << Separator << login << Separator << password ;
						MsgFromClient = ssMessageToServer.str();

						mySocketClient.Send((void*)MsgFromClient.c_str(), props.getInt("Max_String"));
						cout << MsgFromClient << endl;
						MsgFromServeur.clear();
						mySocketClient.Receive((void*)MsgFromServeur.c_str(), props.getInt("Max_String"));		
						cout << "Message : " << MsgFromServeur.c_str() << endl;

						pTemp = strtok((char*)MsgFromServeur.c_str(), Separator.c_str());
						type = atoi(pTemp);
						cout << type << endl;
						pTemp = strtok(NULL, Separator.c_str());
						reponse = pTemp;
						cout << reponse << endl;
						pTemp = strtok(NULL, Separator.c_str());
						message = pTemp;
						cout << message << endl;

						if(reponse.compare("KO") == 0)
							cout << message << endl;
						
					}
					while(type != LOGOUT || reponse.compare("OK") != 0);
					close(mySocketClient.getSocket()); /* Fermeture de la socket */
					cout << "Socket client fermee" << endl;
					break;
			}
		}
		while(choix != 5);
	 
		close(mySocketClient.getSocket()); /* Fermeture de la socket */
		printf("Socket client fermee\n");

		exit(0);
	}
	catch(SocketException e)
	{
	    cout << e.getMessage() << endl;
	    if(e.getSocket() != -1)
	        close(e.getSocket());

	    exit(1);
	}
}
