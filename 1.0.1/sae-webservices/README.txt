Les commandes
 -  mvn generate-sources -PgenerateSkeleton : génération du skeleton
 -  mvn package : génération de l'archive .aar
 
Configuration JNDI pour l'intégration
 - copier le fichier src/conf/axis2.xml dans le répertoire ${CATALINA_HOME}/conf/Catalina/localhost
 - compléter les valeurs
 - redémarrer tomcat
        
Configuration JNDI avec Eclipse
 - le projet axis2 est déployé avec WTP
 - copier le fichier src/conf/axis2.xml dans le projet axis2 de éclipse dans le répertoire WebContent/META-INF
 - renommer le fichier 'context.xml'
 - compléter les valeurs pour indiquer les bons chemins pour les deux variables
        - SAE_Repertoire_Certificats_ACRacine
        - SAE_Repertoire_Certificats_CRL
 - cocher la case 'Publish module contexts to separate XML files' dans le configuration WTP de tomcat
 - rédemarrer tomcat
        