// -------------------------------------------------------------------------------
// -------------------------------------------------------------------------------
// PENSER A MODIFIER L'URL DU SERVICE WEB SaeService DANS LE FICHIER SUIVANT :
//     src/main/resources/sae.client.demo.webservice
// -------------------------------------------------------------------------------
// -------------------------------------------------------------------------------




// ------------------------------------------
// Partie src/main/java
// ------------------------------------------

- Package sae.client.demo.webservice.modele
Contient les classes générées par le plug-in Maven pour le framework Axis2, framework utilisé dans ce projet exemple pour consommer le service web du SAE


- Package sae.client.demo.webservice.security
Contient la mécanique de génération du Vecteur d'Identification, c'est à dire de l'Assertion SAML 2.0 signée électroniquement et insérée dans l'en-tête SOAP du message de request.



// ------------------------------------------
// Partie src/main/resources
// ------------------------------------------

- Fichier certificat/ApplicationTestSAE.p12
Magasin de certificat contenant la clé privée pour signer électroniquement l'assertion SAML, ainsi que sa clé publique associée, ainsi que toutes les clés publiques de la chaîne de certification

- Fichiers security/*
Requis pour la mécanique de génération du Vecteur d'Identification

- Fichier axis2.xml
Configuration pour axis2, chargé par la méthode sae.client.demo.webservice.factory.StubFactory.createStubAvecAuthentification(...)
Le fichier contient la référence à la classe sae.client.demo.webservice.security.VIHandler qui pilote l'insertion du VI lors des appels SOAP

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






