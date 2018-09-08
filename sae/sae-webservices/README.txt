Les commandes
 -  mvn generate-sources -PgenerateSkeleton : génération du skeleton


Configuration JNDI
 - editer le fichier src/main/webapp/META-INF/context.xml
 - activer la variable SAE_Fichier_Configuration dans le bloc de commentaire pour Windows
 - cocher la case 'Publish module contexts to separate XML files' dans le configuration WTP de tomcat
 - rédemarrer tomcat
        