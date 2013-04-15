// --------------------------------------------------------------
// Description
// --------------------------------------------------------------

sae-integration-meta est une application console pour la cellule intégration, permettant de :
	- créer des métadonnées
	- modifier des métadonnées
	- créer et ajouter des entrées aux dictionnaires
	- supprimer des entrées dans des dictionnaries

L'application s'appuie sur un fichier XML externe, contenant les métadonnées à créer, à modifier, 
les entrées de dictionaire à créer, et celles à supprimer.


// --------------------------------------------------------------
// Arguments de la ligne de commande
// --------------------------------------------------------------

L'application sae-integration-meta s'exécute de la sorte :

java -jar -Dlogback.configurationFile=[logback] sae-integration-meta.jar [0] [1]

avec :
   [logback] : le chemin complet du fichier de configuration Logback
   [0] : le chemin complet du fichier XML contenant les éléments de métadonnées
   [1] : le chemin complet du fichier de configuration Cassandra

Exemple :

java -jar /appl/sae/sae-integration-meta/sae-integration-meta.jar -Dlogback.configurationFile=/appl/sae/sae-integration-meta/logback-sae-integration-meta.xml /appl/sae/sae-integration-meta/saemetas.xml /appl/sae/sae-integration-meta/cassandra-config.properties



// --------------------------------------------------------------
// Fichier de configuration Logback
// --------------------------------------------------------------

Il est fortement recommandé de sortir au moins les traces de niveau INFO
sur la console (ou dans un fichier) pour le package fr.urssaf.image.sae.integration.meta



// --------------------------------------------------------------
// Fichier de configuration Cassandra
// --------------------------------------------------------------

Il s'agit du même format de fichier de configuration que celui du SAE.



// --------------------------------------------------------------
// Fichier contenant les données pour les métadonnées et les dictionnaires
// --------------------------------------------------------------

Il s'agit d'un fichier XML dont le XSD est fourni dans l'artéfact dans
le fichier saemeta.xsd
 









