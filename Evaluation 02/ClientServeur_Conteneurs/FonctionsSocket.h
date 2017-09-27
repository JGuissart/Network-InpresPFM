#ifndef FONCTIONSSOCKET_H_INCLUDED
#define FONCTIONSSOCKET_H_INCLUDED

#include <stdio.h>
#include <stdlib.h> /* pour exit */
#include <unistd.h>
#include <string.h> /* pour memcpy */
#include <sys/types.h>
#include <sys/socket.h> /* pour les types de socket */
#include <netdb.h> /* pour la structure hostent */
#include <errno.h>
#include <string>
#include <netinet/in.h> /* conversions adresse reseau->format dot et local/ reseau */
#include <netinet/tcp.h> /* pour la conversion adresse reseau->format dot */
#include <arpa/inet.h> /* pour la conversion adresse reseau->format dot */
#include "SocketServeur.h"
#include "Properties.h"

#define LOGIN 1
#define INPUT_TRUCK 2
#define INPUT_DONE 3
#define OUTPUT_READY 4
#define OUTPUT_ONE 5
#define OUTPUT_DONE 6
#define LOGOUT 7

//void fctLogin(char* ,SocketServeur); 

				

#endif // FONCTIONSSOCKET_H_INCLUDED
