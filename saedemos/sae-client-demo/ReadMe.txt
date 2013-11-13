// -------------------------------------------------------------------------------
// -------------------------------------------------------------------------------
// PENSER A MODIFIER L'URL DU SERVICE WEB SaeService DANS LE FICHIER SUIVANT :
//     src/main/resources/sae.client.demo.properties
// -------------------------------------------------------------------------------
// -------------------------------------------------------------------------------




// ------------------------------------------
// Partie src/main/java
// ------------------------------------------

- Package sae.client.demo.webservice.modele
Contient les classes générées par le plug-in Maven pour le framework Axis2, framework utilisé dans ce projet exemple pour consommer le service web du SAE


- Package sae.client.demo.webservice.security
Contient une classe exposant le certificat de signature du Vecteur d'Identification.
Un objet de cette classe est requis pour l'utilisation de la librairie sae-client-vi
fournie par la MOE SAE et permettant la génération du Vecteur d'Identification, ainsi
que sa signature électronique.



// ------------------------------------------
// Partie src/main/resources
// ------------------------------------------

- Fichier ApplicationTestSAE.p12
Magasin de certificat contenant la clé privée pour signer électroniquement le Vecteur d'Identification, 
ainsi que sa clé publique associée, ainsi que toutes les clés publiques de la chaîne de certification

- Fichiers sae-client-demo-security.properties
Fichier de configuration contenant les informations d'accès au magasin ApplicationTestSAE.p12

- Fichier sae-client-demo.properties
Fichier de configuration contenant l'URL d'accès au service web SaeService
A modifier selon l'URL est fournie par la MOE SAE.



// ------------------------------------------
// Partie src/test/java
// ------------------------------------------

- Package sae.client.demo.webservice
Exemples de consommation des opérations du service web SaeService.
Une classe par opération.



// ------------------------------------------
// Partie src/test/resources
// ------------------------------------------

- Répertoire SaeService_WSDL
Contient les fichiers SaeService.wsdl et SaeService.xsd
Il s'agit du contrat technique d'utilisation du service web SaeService
Ces fichiers sont utilisés uniquement lors de la phase de génération du code client par le plug-in Maven Axis2






