

using namespace std;

#include "SocketException.h"

SocketException::SocketException()
{
    this->ErrorCode = 0;
    this->ObjectError = "";
    this->MethodError = "";
    this->ErrorMessage = "";
    this->ProgrammerMessage = "";
    this->Message = "";
    this->hSocket = -1;
}

SocketException::SocketException(int ErrorCode, string ObjectError, string MethodError, string ProgrammerMessage)
{
    this->ErrorCode = ErrorCode;
    this->ObjectError = ObjectError;
    this->MethodError = MethodError;
    this->ProgrammerMessage = ProgrammerMessage;
    ErrorAnalysis();
    Message = "[" + ObjectError + "]" + "(" + MethodError + ")" + " ==> " + ErrorMessage;
    this->hSocket = -1;
}

SocketException::SocketException(int ErrorCode, std::string ObjectError, std::string MethodError, string ProgrammerMessage, int hSocket)
{
    this->ErrorCode = ErrorCode;
    this->ObjectError = ObjectError;
    this->MethodError = MethodError;
    this->ProgrammerMessage = ProgrammerMessage;
    ErrorAnalysis();
    Message = "[" + ObjectError + "]" + "(" + MethodError + ")" + " ==> " + ErrorMessage;
    this->hSocket = hSocket;
}

SocketException::SocketException(const SocketException& se)
{
    this->ErrorCode = se.ErrorCode;
    this->ObjectError = se.ObjectError;
    this->MethodError = se.MethodError;
    this->ProgrammerMessage = se.ProgrammerMessage;
    this->ErrorMessage = se.ErrorMessage;
    this->Message = se.Message;
    this->hSocket = se.hSocket;
}

SocketException::~SocketException()
{

}

/*************** GETTERS ***************/

int SocketException::getErrorCode() const
{
    return ErrorCode;
}

string SocketException::getMessage() const
{
    return Message;
}

string SocketException::getProgrammerMessage() const
{
    return ProgrammerMessage;
}

int SocketException::getSocket() const
{
    return hSocket;
}

/***************************************/

void SocketException::ErrorAnalysis()
{
    switch(ErrorCode)
	{
        case EBADF:
            ErrorMessage = "EBADF --Bad file number-- le descripteur est invalide";
			break;
		case ENOTSOCK:
			ErrorMessage = "ENOTSOCK --Socket operation on non-socket-- le descripteurn'est pas associé à une socket, mais à un fichier";
			break;
		case EINTR:
			ErrorMessage = "EINTR --interruption par signal--";
			break;
		case EFAULT:
			ErrorMessage = "EFAULT --Bad address-- l'adresse où placer la suite de caractères est incorrecte";
			break;
		case EINVAL:
			ErrorMessage = "EINVAL --Invalid argument-- le nombre de caractères à lire est négatif";
			break;
		case EAFNOSUPPORT:
			ErrorMessage = "EAFNOTSUPPORT - adresse ne correspond pas famille";
			break;
		case EISCONN:
			ErrorMessage = "EISCONN - socket deja connectee";
			break;
		case ECONNREFUSED:
			ErrorMessage = "ECONNREFUSED - connexion refusee par le serveur";
			break;
		case ETIMEDOUT:
			ErrorMessage = "ETIMEDOUT - time out sur connexion";
			break;
		case ENETUNREACH:
			ErrorMessage = "ENETUNREACH - cible hors d'atteinte";
			break;
		default:
			ErrorMessage = "Erreur inconnue ?";
	}
}
