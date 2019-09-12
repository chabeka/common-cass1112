// -------------------------------------------------------------------------
// Génération du code client pour attaquer le service web SaeService
// -------------------------------------------------------------------------
mvn generate-sources -PgenererCodeClientSaeService

// -------------------------------------------------------------------------
// Génération du code client JAXB à partir des fichiers xsd des sommaires et résultats
// -------------------------------------------------------------------------
mvn generate-sources -PgenererCodeXML

