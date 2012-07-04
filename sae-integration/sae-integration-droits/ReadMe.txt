// --------------------------------------------------------------
// Description
// --------------------------------------------------------------

sae-integration-droits est une application console pour la cellule intégration,
permettant de créer les jeux de tests pour les droits, c'est à dire les PRMD et
les contrats de services qui sont requis pour exécuter les tests d'intégration du SAE.

L'application s'appuie sur un fichier XML externe, contenant les PRMD et les CS à créer.

Ainsi, il est possible d'ajouter des éléments dans ce fichier XML sans avoir à faire
évolution l'application pour ajouter de nouveaux PRMD/CS.



// --------------------------------------------------------------
// Arguments de la ligne de commande
// --------------------------------------------------------------

L'application sae-integration-droits s'exécute de la sorte :

java -jar -Dlogback.configurationFile=[logback] sae-integration-droits.jar [0] [1]

avec :
   [logback] : le chemin complet du fichier de configuration Logback
   [0] : le chemin complet du fichier XML contenant les PRMD et les CS à créer
   [1] : le chemin complet du fichier de configuration Cassandra

Exemple :

java -jar /appl/sae/sae-integration-droits/sae-integration-droits.jar -Dlogback.configurationFile=/appl/sae/sae-integration-droits/logback-sae-integration-droits.xml /appl/sae/sae-integration-droits/saedroits.xml /appl/sae/sae-integration-droits/cassandra-config.properties



// --------------------------------------------------------------
// Fichier de configuration Logback
// --------------------------------------------------------------

Il est fortement recommandé de sortir au moins les traces de niveau INFO
sur la console (ou dans un fichier) pour le package fr.urssaf.image.sae.integration.droits



// --------------------------------------------------------------
// Fichier de configuration Cassandra
// --------------------------------------------------------------

Il s'agit du même format de fichier de configuration que celui du SAE.



// --------------------------------------------------------------
// Fichier contenant les PRMD et les CS à créer
// --------------------------------------------------------------

Il s'agit d'un fichier XML dont le XSD est fourni dans l'artéfact dans
le fichier saedroits.xsd
 









