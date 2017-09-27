CC = g++ -Wall

all: Client Serveur

Client:		Client.cpp Socket.o SocketClient.o Properties.o SocketException.o Properties.o FonctionsSocket.o Trafic.o ListTrafic.o Moment.o CMMP.o
		echo Creation du Client ...
		$(CC) -o Client Client.cpp Socket.o SocketClient.o SocketException.o Properties.o FonctionsSocket.o Trafic.o ListTrafic.o Moment.o CMMP.o -lsocket -lnsl -lrt -lpthread

Serveur:	Serveur.cpp Socket.o SocketServeur.o Properties.o SocketException.o Properties.o FonctionsSocket.o Trafic.o ListTrafic.o Moment.o CMMP.o
		echo Creation du Serveur ...
		$(CC) -o Serveur Serveur.cpp Socket.o SocketServeur.o SocketException.o Properties.o FonctionsSocket.o Trafic.o ListTrafic.o Moment.o CMMP.o -lsocket -lnsl -lrt -lpthread

Socket.o:	Socket.cpp Socket.h SocketException.o
		echo creation de Socket.o...
		$(CC) Socket.cpp -c

SocketClient.o:	SocketClient.cpp SocketClient.h
		echo creation de SocketClient.o...
		$(CC) SocketClient.cpp -c

SocketServeur.o:	SocketServeur.cpp SocketServeur.h
			echo creation de SocketServeur.o...
			$(CC) SocketServeur.cpp -c

SocketException.o:	SocketException.cpp SocketException.h
			echo creation de SocketException.o...
			$(CC) SocketException.cpp -c

Properties.o:	Properties.cpp Properties.h
		echo creation de Properties.o
		$(CC) Properties.cpp -c

FonctionsSocket.o:	FonctionsSocket.cpp FonctionsSocket.h
			echo creation de FonctionsSocket.o...
			$(CC) FonctionsSocket.cpp -c

ListTrafic.o:	ListTrafic.cpp ListTrafic.h
		echo creation de ListTrafic.o...
		$(CC) ListTrafic.cpp -c

Trafic.o:	Trafic.cpp Trafic.h
		echo creation de Trafic.o
		$(CC) Trafic.cpp -c

Moment.o:	Moment.cpp Moment.h
		echo Creation de Moment.o
		$(CC) Moment.cpp -c

CMMP.o:	CMMP.cpp CMMP.h
	echo creation de CMMP.o...
	$(CC) CMMP.cpp -c

clean:
	rm -f *.o Serveur Client core
