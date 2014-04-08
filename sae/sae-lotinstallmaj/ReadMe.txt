// ----------------------------------------------------------------------------
// UTILISATION DE sae-lotinstallmaj DANS L'INSTALLATION DES LOTS SAE
// ----------------------------------------------------------------------------


// ------------------------------------------
// 1) Lot 120510SAE
// ------------------------------------------


1.1) CODEACTIVITENONOBLIGATOIRE

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties CODEACTIVITENONOBLIGATOIRE

Passe la métadonnées CodeActivite en non obligatoire dans DFCE


1.2) CASSANDRA_120510

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties CASSANDRA_120510

Met la base Cassandra du SAE en version 1

Création du Keyspace SAE

Création des CF pour SpringBatch :
   - JobInstance
   - JobInstancesByName
   - JobInstanceToJobExecution
   - JobExecution
   - JobExecutions
   - JobExecutionsRunning
   - JobExecutionToJobStep
   - JobStep
   - JobSteps
   - Sequences

Création des CF pour la pile des travaux :
   - JobRequest
   - JobsQueue

Création de la CF des paramètres :
   - Parameters

Positionne le paramètre versionBDD de la CF Parameters à la valeur "1"



1.3) CASSANDRA_120512

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties CASSANDRA_120512

Met la base Cassandra du SAE en version 2 

Création d'1 CF supplémentaire pour la pile des travaux :
   - JobHistory

Positionne le paramètre versionBDD de la CF Parameters à la valeur "2"



// ------------------------------------------
// 2) Lot 121110SAE
// ------------------------------------------


2.1) CASSANDRA_121110

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties CASSANDRA_121110

Met la base Cassandra du SAE en version 3

Création des CF pour les droits :
   - DroitContratService
   - DroitPagm
   - DroitPagma
   - DroitPagmp
   - DroitActionUnitaire
   - DroitPrmd

Insertion des données de base pour les droits :
   - Les actions unitaires dans DroitActionUnitaire :
     consultation, recherche, archivage_masse, archivage_unitaire
   - Le PRMD "ACCES_FULL_PRMD" dans DroitPrmd
   - Le contrat de service CS_ANCIEN_SYSTEME et ses éléments constitutifs

Positionne le paramètre versionBDD de la CF Parameters à la valeur "3"



2.2) META_SEPA

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties META_SEPA

Création des métadonnées pour SEPA dans DFCE :
   - JetonDePreuve (jdp)
   - RUM (rum)
   - DateSignature (dsi)


// ------------------------------------------
// 3) Lot 130400SAE
// ------------------------------------------

3.1) CASSANDRA_130400 

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties CASSANDRA_130400

Met la base Cassandra du SAE en version 4

Création des CF pour la traçabilité :
   - TraceDestinataire
   - TraceRegSecurite
   - TraceRegSecuriteIndex
   - TraceRegExploitation
   - TraceRegExploitationIndex
   - TraceRegTechnique
   - TraceRegTechniqueIndex

Insertion des paramètres pour la purge des registres dans la CF Parameters :
   - PURGE_EXPLOIT_DUREE = 10
   - PURGE_SECU_DUREE = 10
   - PURGE_TECH_DUREE = 10

Insertion des événéments à tracer dans la CF TraceDestinataire :
   - WS_RECHERCHE|KO dans le registre de surveillance technique avec all_infos 
   - WS_CAPTURE_MASSE|KO dans le registre de surveillance technique avec all_infos
   - WS_CAPTURE_UNITAIRE|KO dans le registre de surveillance technique avec all_infos
   - WS_CONSULTATION|KO dans le registre de surveillance technique avec all_infos
   - WS_PING_SECURE|KO dans le registre de surveillance technique avec all_infos
   
Positionne le paramètre versionBDD de la CF Parameters à la valeur "4"



3.2) META_130400

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties META_130400

Création des nouvelles métadonnées :
   - ReferenceDocumentaire (rdo)

// ------------------------------------------
// 4) Lot 130700SAE
// ------------------------------------------

4.1) CASSANDRA_130700 

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties CASSANDRA_130700

Met la base Cassandra du SAE en version 5

Création des CF pour les métadonnées :
   - Metadata
   - Dictionary
   
Création des CF pour l'automatisation de la mise à jour du RND   
   - Rnd
   - CorrespondancesRnd

Insertion des événéments à tracer dans la CF TraceDestinataire :
   - MAJ_VERSION_RND|OK dans le registre de journal des événements du SAE avec all_infos

Insertion des métadonnées du référentiel 1.8 (moins les nouvelles métadonnées) dans la CF Metadata
   
Positionne le paramètre versionBDD de la CF Parameters à la valeur "5"

Positionne à vide le paramètre VERSION_RND_NUMERO de la CF Rnd.

// ------------------------------------------
// 5) Lot 131100SAE
// ------------------------------------------

5.1) CASSANDRA_131100 

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties CASSANDRA_131100

Met la base Cassandra du SAE en version 6

Mise à jour de la CF Metadata (les options modifiables n'avaient pas été paramétrées dans le lot précédent)
(référentiel 1.8 moins les nouvelles métadonnées)
   - Metadata

Positionne le paramètre versionBDD de la CF Parameters à la valeur "6"

// ------------------------------------------
// 6) Lot 140400SAE
// ------------------------------------------

6.1) CASSANDRA_140400

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties CASSANDRA_140400

Met la base Cassandra du SAE en version 7

Mise à jour des référentiels des évenements

Ajout de la colonne dispo dans la CF Metadata (disponible client)
Ajout des colonnes trim gauche et trim droite dans la CF Metadata

Positionne le paramètre versionBDD de la CF Parameters à la valeur "7"
