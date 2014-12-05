// -----------------------------------------------------------
// Description du JAR
// -----------------------------------------------------------

sae-extraitdonnees est un JAR exécutable qui permet de parcourir le fonds documentaire de la GNS et d'écrire les UUID des documents dans un fichier texte en sortie, selon les paramètres spécifiés au JAR dans la ligne de commande.

Il est prévu de livrer le JAR au CSPP dans le cadre des tests de charge de la GNS.


// -----------------------------------------------------------
// Paramètres de la ligne de commande
// ----------------------------------------------------------- 

Les paramètres à spécifier dans la ligne de commande du JAR peuvent s'obtenir en lançant le JAR sans paramètre :

java -jar sae-extraitdonnees.jar


usage: sae-extraitdonnees.jar
    --cassandra-login <login>         login de connexion à Cassandra
    --cassandra-password <password>   password de connexion à Cassandra
    --cassandra-port <port>           port du service Cassandra (valeur
                                      par défaut: 9160)
    --cassandra-servers <serveurs>    adresses des serveurs Cassandra (les
                                      séparer par une virgule). Exemple:
                                      serveur1,serveur2
    --fichier-sortie <fichier>        chemin complet du fichier de sortie
                                      dans lequel écrire les UUID des
                                      documents (nouveau fichier)
    --help                            affiche ce message
    --nbMaxDoc <nombre>               nombre maximum de documents
                                      souhaités dans le fichier de sortie
    --virtuel                         extrait des UUID de documents
                                      virtuels au lieu de documents non
                                      virtuels


// -----------------------------------------------------------
// Exemples de ligne de commande
// -----------------------------------------------------------


// extraction des UUID de tous les documents de la plateforme de développement, uniquement pour des documents non virtuels 

java -jar sae-extraitdonnees.jar --cassandra-servers=cer69imageint9.cer69.recouv --fichier-sortie=c:/divers/extraction.txt



// extraction des UUID de 20 000 documents au maximum de la plateforme de développement, uniquement pour des documents non virtuels
// ajout du paramètres --nbMaxDoc pour spécifier la limite du nombre de documents 

java -jar sae-extraitdonnees.jar --cassandra-servers=cer69imageint9.cer69.recouv --fichier-sortie=c:/divers/extraction.txt --nbMaxDoc=20000



// extraction des UUID de 20 000 documents au maximum de la plateforme de développement, uniquement pour des documents virtuels 
// ajout du paramètres --virtuel pour spécifier que l'on souhaite des documents virtuels

java -jar sae-extraitdonnees.jar --cassandra-servers=cer69imageint9.cer69.recouv --fichier-sortie=c:/divers/extraction.txt --nbMaxDoc=20000 --virtuel



// extraction des UUID de 100 000 documents au maximum de la plateforme de pré-production, uniquement pour des documents non virtuels
// utilisation d'une liste de serveurs dans --cassandra-servers, les séparer par une virgule
// ajout des paramètres --cassandra-login et --cassandra-password car la pf de pré-prod est sécurisée par authentification
  
java -jar sae-extraitdonnees.jar --cassandra-servers=cnp69pprodsaecas1.cer69.recouv:9160,cnp69pprodsaecas2.cer69.recouv:9160,cnp69pprodsaecas3.cer69.recouv:9160 --cassandra-login=root --cassandra-password=lePassword --fichier-sortie=c:/divers/extraction.txt --nbMaxDoc=100000








