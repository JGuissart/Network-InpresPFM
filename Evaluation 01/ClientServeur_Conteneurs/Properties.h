#ifndef PROPERTIES_H_INCLUDED
#define PROPERTIES_H_INCLUDED

#include <string>
#include <map>
#include <stdio.h>      /* printf, fgets */
#include <stdlib.h>     /* atoi */
#include <iostream>
#include <map>
#include <fstream>
#include <cstring>

using namespace std;

class Properties
{
    private:
        std::map<std::string, std::string> configFile;
    public:
        Properties();
        Properties(const char* nF);
        ~Properties();

        /********* Getters *********/



        /********* Setters *********/



        /********* Surcharges d'operateurs *********/

        std::string operator[] (std::string p);

        /*******************************************/

        std::map<std::string, std::string> initialisationFichier(const char* nF);
    	int getInt(std::string prop);
};

#endif // PROPERTIES_H_INCLUDED
