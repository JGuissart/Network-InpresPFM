#include "Properties.h"


Properties::Properties()
{

}

Properties::Properties(const char* nF)
{
	cout << "CTOR PROPERTIES" << endl;
    configFile = initialisationFichier(nF);
}

Properties::~Properties()
{

}

/********* Getters *********/



/********* Setters *********/



/********* Surcharges d'operateurs *********/

string Properties::operator[] (string p)
{
    if (configFile.find(p) != configFile.end())
        return configFile[p];
    else
        return "NULL";
}

/*******************************************/

map<string, string> Properties::initialisationFichier(const char* nF)
{
    map<string, string> config;
	cout << "DEBUT INITIALISATIONFICHIER PROPERTIES" <<endl;
    ifstream f(nF);
	string ligne;

	if(f)
	{
		while(getline(f, ligne))
		{
            char* sVal;
            char buffer[256];

            strcpy(buffer, (char*)ligne.c_str());
            sVal = strtok(buffer, "=");
            string cle(sVal);
            sVal = strtok(NULL, "=");
            string valeur(sVal);

			// ajouter le couple {clé,valeur} à la map
			if(config.find(cle) != config.end())
				cout << "Attention, la clé " << cle << " est déjà présente" << endl;
			else
				config.insert(map<string, string>::value_type(cle, valeur));
		}
		f.close();
	}
	cout << "FIN INITIALISATIONFICHIER PROPERTIES" <<endl;
	return config;
}

int Properties::getInt(string prop)
{
	return atoi(configFile[prop].c_str());
}
