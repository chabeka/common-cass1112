commandes:
	- creation du war : mvn package
	
archives:
	- répertoire :  src\main\webapp\WEB-INF\services 
	- liste des services : 
			- sae-webservices.aar
			
axis2
	- configuration : src\main\webapp\WEB-INF\conf\axis2.xml

configuration poste sur windows
 	- creation des répertoires : 
 		-  C:\sae
 		-  C:\sae\certificats\ACRacine
 		-  C:\sae\certificats\CRL
 	- copier les fichiers :
 		- \\cer69-donnees\TECHNOCOM\produits\Qualite\Projet_ae\Documentation refonte\Refonte\Intégration\Certificats AC racine\pseudo_IGCA.crt
 		  dans C:\sae\certificats\ACRacine
 		- src\main\config\sae-dfce-config.properties dans C:\sae
 		- src\main\config\windows\sae-igc-config.xml dans C:\sae
 	- modifier fichier C:\sae\sae-dfce-config.properties
 		- modifier db.hostName si nécessaire 
 		- modifier db.baseName si nécessaire
 		
configuration poste sur linux
 	- creation des répertoires : 
 		-  /appl/sae
 		-  /appl/sae/certificats/ACRacine
 		-  /appl/sae/certificats/CRL
 	- copier les fichiers :
 		- \\cer69-donnees\TECHNOCOM\produits\Qualite\Projet_ae\Documentation refonte\Refonte\Intégration\Certificats AC racine\pseudo_IGCA.crt
 		  dans /appl/sae/certificats/ACRacine
 		- src\main\config\sae-dfce-config.properties dans /appl/sae
 		- src\main\config\linux\sae-igc-config.xml dans /appl/sae
 	- modifier fichier /appl/sae/sae-dfce-config.properties
 		- modifier db.hostName si nécessaire 
 		- modifier db.baseName si nécessaire

configuration contexte web : 
	- remplacer src\main\webapp\META-INF\context.xml:
		- windows :  par src\config\windows\context.xml
 		- linux : par src\config\linux\context.xml
