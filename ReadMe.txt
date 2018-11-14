Pour faire fonctionner l'application, il faut copier le contenu du répertoire 
src/main/resources/config sous C:\sae\ihm-web-exploit

Fichier configurationEnvironnement.xml
======================================
Contient la configuration des environnements (WS et Cassandra)

Ex :
<configurations>
   <configuration>
      <nom>Developpement #1</nom>
      <urlWs>http://localhost:8080/sae-webservices/services/SaeService</urlWs>
      <zookeeperHost>cer69-ds4int.cer69.recouv:2181</zookeeperHost>
      <zookeeperNameSpace>SAE</zookeeperNameSpace>
      <cassandraHost>cer69imageint9.cer69.recouv:9160</cassandraHost>
      <cassandraUserName>root</cassandraUserName>
      <cassandraPwd>regina4932</cassandraPwd>
      <cassandraKeySpace>SAE</cassandraKeySpace>
      <dfceAddress>http://cer69-ds4int.cer69.recouv:8080/dfce-webapp/toolkit/</dfceAddress>
      <dfceLogin>_ADMIN</dfceLogin>
      <dfcePwd>DOCUBASE</dfcePwd>
      <dfceBaseName>SAE-TEST</dfceBaseName>
   </configuration>
</configurations>

Les élements sont divisés ainsi :
-	configurations possède de 0 à * configuration
-	configuration est composé de :
		1 et 1 seul nom
		1 et 1 seul urlWs
		1 et 1 seul zookeeperHost
		1 et 1 seul zookeeperNameSpace
		1 et 1 seul cassandraHost
		1 et 1 seul cassandraUserName
		1 et 1 seul cassandraPwd
		1 et 1 seul cassandraKeySpace
		1 et 1 seul dfceAddress
		1 et 1 seul dfceLogin
		1 et 1 seul dfcePwd
		1 et 1 seul dfceBaseName


Fichier droits.xml
==================
Contient la liste des PAGM et les actions unitaires associées

Ex :
<droits xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">	
	<droit>
		<pagm>ADMIN_TECHNIQUE</pagm>
		<actionsUnitaires>
			<action>rechercherDocuments</action>
            		<action>consulterDocuments</action>
            		<action>visualiserPileTravaux</action>
            		<action>administrerPileTravaux</action>
		</actionsUnitaires>
	</droit>
</droits>

Les éléments se composent ainsi :
-	droits possède de 1 à * droit 
-	droit possède :
		1 et 1 seul pagm
		1 et seul élément actions
-	actions possède de 1 à * action


Fichier metadonneeAffichage.xml
===============================
Contient la liste des métadonnées disponibles pour la recherche de documents

Ex :
<metadonnees>
   <metadonnee>
      <code>ApplicationProductrice</code>
      <libelle>Application Productrice du document</libelle>
      <indexee>Non</indexee>
   </metadonnee>
</metadonnees>

Fichier metadonneeRecherche.xml
===============================
Contient la liste des métadonnées qui peuvent être retourné par la recherche de documents

Ex :
<metadonnees>
   <metadonnee>
      <code>ApplicationProductrice</code>
      <libelle>Application Productrice du document</libelle>
      <indexee>Non</indexee>
   </metadonnee>
</metadonnees>

Fichier configurationGenerale.properties
========================================
Contient les chemins vers les fichiers précédents ainsi que la version de l'application


Commande pour générer le code modèle des droits
===============================================
mvn clean generate-sources -PgenererCodeModeleDroit
