Lancement de l'exécutable :

	java -jar -Dlogback.configurationFile=<fichier> sae-regionalisation.jar [uuid] [dfce] [fichier] [first] [last] [mode]
	avec :
	[uuid] 		= identifiant unique du traitement
	[dfce] 		= chemin complet vers le fichier de configuration de connexion à DFCE
	[fichier] 	= chemin vers le fichier contenant les données à traiter
	[first] 	= index du premier enregistrement à traiter
	[last] 		= index du dernier enregistrement à traiter
	[mode] 		= TIR_A_BLANC ou MISE_A_JOUR