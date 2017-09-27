#ifndef CMMP_H_INCLUDED
#define CMMP_H_INCLUDED

#include <iostream>
#include <string>
#include <cstdlib>
#include <sstream>

#include "Properties.h"
#include "Moment.h"
#include "ListTrafic.h"

#define LOGIN 1
#define INPUT_TRUCK 2
#define INPUT_DONE 3
#define OUTPUT_READY 4
#define OUTPUT_ONE 5
#define OUTPUT_DONE 6
#define LOGOUT 7

std::string VerificationLoginPassword(std::string Login, std::string Password, std::string Sep); // Pour LOGIN ET LOGOUT
std::string InputTruck(std::string ArrayContainers[], int Size, std::string Destination, std::string Sep);
std::string InputDone(std::string ArrayContainers[], float ArrayWeight[], int Size, std::string Sep);
std::string OutputReady(std::string strIdTypeTransport, std::string Destination, int Capacity, std::string Sep);
std::string OutputOne(std::string Message, std::string Sep);
std::string OutputDone(std::string Message, std::string Sep);

#endif // CMMP_H_INCLUDED
