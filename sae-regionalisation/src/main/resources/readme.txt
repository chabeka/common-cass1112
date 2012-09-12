Lancement de l'exécutable :
Deux modes sont à distinguer selon la source d'information.

- Source base de données :
	java -jar -Dlogback.configurationFile=<fichier> sae-regionalisation.jar BASE [dfce] [postgres] [offset] [count] [mode]
	avec :
	[dfce] = chemin complet vers le fichier de configuration de connexion à DFCE
	[postgres] = chemin complet vers le fichier de connexion à la base POSTGRESQL
	[offset] = index de l'enregistrement de départ
	[count] = nombre d'enregistrements à traiter
	[mode] = TIR_A_BLANC ou MISE_A_JOUR
- Source fichier :
	java -jar -Dlogback.configurationFile=<fichier> sae-regionalisation.jar CSV [fichier]
	avec :
	[fichier] = chemin vers le fichier contenant les données à traiter