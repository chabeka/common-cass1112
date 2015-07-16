﻿// ----------------------------------------------------------------------------
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
// 6) Lot 140700SAE
// ------------------------------------------

6.1) CASSANDRA_140700

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties CASSANDRA_140700

Met la base Cassandra du SAE en version 7

Mise à jour des référentiels des évenements

Ajout de la CF ReferentielFormat
Mise à jour du référentiel des formats (fmt/354 et crtl/1)

Ajout de la CF DroitPagmf
Ajout de la CF DroitFormatControlProfil (et mise à jour concernant le format fmt/354)
Ajout des données pour le controle du format FMT/354

Ajout de la colonne dispo dans la CF Metadata (disponible client)
Ajout des colonnes trim gauche et trim droite dans la CF Metadata

Positionne le paramètre versionBDD de la CF Parameters à la valeur "7"


// ------------------------------------------
// 7) Lot 150100SAE
// ------------------------------------------

7.1) CASSANDRA_150100

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties CASSANDRA_150100

Met la base Cassandra du SAE en version 8

Ajout action unitaire transfert
Ajout évènements transfert DFCE_TRANSFERT_DOC|OK WS_TRANSFERT|KO
Ajout de la CF TraceJournalEvtIndexDoc

Positionne le paramètre versionBDD de la CF Parameters à la valeur "8"

7.2) META_150100

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties META_SEPA

Création des métadonnées suivantes :
   - CodePartenaire
   - DateArchivageGNT


// ----------------------------------
// Generation code modele xml meta
// ----------------------------------
mvn generate-sources -P genererCodeModeleMetas



// ------------------------------------------
// 8) Lot 150400SAE
// ------------------------------------------

Met la base Cassandra du SAE en version 9 et CREATION DES INDEXES COMPOSITES:
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties CASSANDRA_DFCE_150400

Création de la CF NotesIndex dans Docubase
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties DFCE_150400

Re-création des CF TermInfoRangeDouble et TermInfoRangeFloat dans Docubase
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties DFCE_150400_P5

Suppression des index composite (se base sur le fichier xml des index composites, et supprimer ceux qui existent et qui sont marqués à créer : non)
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties DISABLE_COMPOSITE_INDEX


// ------------------------------------------
// 9) Lot 150600SAE
// ------------------------------------------

Met la base Cassandra du SAE en version 10 + Mise à jour DFCE (Création des métas Groom) :
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties CASSANDRA_DFCE_150600

Met la base Cassandra du SAE en version 11 (Mise à jour référentiel des évenements et création des index composite Groom) :
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties CASSANDRA_DFCE_150601

// ------------------------------------------
// 10) Lot 151000SAE
// ------------------------------------------

Création des nouvelles CF de la version 1.7.0
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties DFCE_151000
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties CASSANDRA_151000
