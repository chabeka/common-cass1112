Il s'agit d'un "fork" du projet spring-batch-admin, basé sur la version
git 8acbc340ec53985b02853f1dece1b2fc9ec6ed3c, du 03/02/2012


Modifications apportées par rapport à l'original :
	- dans l'écran des exécutions : ajout d'une colonne "server" qui permet d'afficher
	  sur quel serveur a (eu) lieu l'exécution
	- utilisation des dao de commons-cassandra-spring-batch



Contextes spring	
================
Les contextes spring du projet d'origine étaient ultra complexes, et étaient sensés faire en sorte que
les contextes définis dans spring-batch-admin-manager soient surchargables dans spring-batch-admin-sample.
Ca ne fonctionnait en réalité pas bien dans le cas des re-définitions de DAO.

On a donc simplifié un peu la gestion des contextes (qui reste malgré tout compliquée).
On a fait en sorte que sae-spring-batch-manager et sae-spring-batch-sample utilisent des serveurs
cassandra et zookeeper locaux pour les tests unitaires.

Pour l'exécution en mode webapp, le fichier de propriétés à modifier est ici :
sae-spring-batch-admin-sample/src/main/resources/datasources.properties
