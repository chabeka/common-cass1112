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

sudo java -Dlogback.configurationFile=/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar /hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar /hawai/data/sae/sae-config.properties META_150100

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

Met la base Cassandra du SAE en version 12 (Mise à jour référentiel des évenements, Ajout de l'action unitaire pour ajouter une note, Ajout de la meta Note) 
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties CASSANDRA_151000

Met la base Cassandra du SAE en version 13 (Ajout de la metadonnee ApplicationMetier)
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties CASSANDRA_DFCE_151001

// ------------------------------------------
// 11) Lot 151200SAE
// ------------------------------------------

11-1) Lot 151200SAE
Met la base Cassandra du SAE en version 14 (Ajout de nouvelles métadonnées scribe et DUE / Ajout action unitaire recherche_iterateur / Ajout du convertisseur pour le format fmt/354)
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties CASSANDRA_DFCE_151200

11-2) Lot 151201SAE
Met la base Cassandra du SAE en version 15 (Création de l'action unitaire ajout_note suite bug lot 151200, méta Note passée en non transférable, Plus de trim gauche et droite sur méta IdGed, Taille max de IdTraitementMasse et IdTraitementMasseInterne à 36, Taille max NumeroPiece � 12)
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties CASSANDRA_DFCE_151201

// ------------------------------------------
// 12) Lot 160300SAE
// ------------------------------------------

Lot 160300SAE
Met la base Cassandra du SAE en version 16 :

- Ajout des index composite :
DomaineCotisant-CodeOrganismeProprietaire-DateJourneeComptable
DomaineCotisant-CodeAgent-DateCreation
DomaineCotisant-ApplicationProductrice-ApplicationTraitement-ApplicationMetier-DateArchivage
DomaineCotisant-ApplicationProductrice-ApplicationTraitement-ApplicationMetier-CodeRND-DateArchivage
=>Pour info, ces index composite ont été créés manuellement sur la prod en avance de phase.

- Ajout de l'action unitaire ajout_doc_attache

- MIse à jour référentiel des événements (WS_GET_DOC_FORMAT_ORIGINE|KO)

sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties CASSANDRA_DFCE_160300


// ------------------------------------------
// 13) Lot 160400SAE
// ------------------------------------------

Lot 160400SAE
Met la base Cassandra du SAE en version 17 :

- Ajout du format pdf pour DEA

- Ajout des métadonnées pour WATT

- Ajout des métadonnées qui seront utilisées par la suppression/restore de masse (service livré dans le lot 160600)

- Ajout des index composite + indexation NumeroIdArchivage utilisé par la recherche documentaire suite à mise en prod des LAD2GED

- Indexation vide par défaut pour les 4 index composite créés dans le 160300

- Ajout de l'événement :
	DFCE_DEPOT_ATTACH|OK

sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties CASSANDRA_DFCE_160400


// ------------------------------------------
// 13) Lot 160600SAE
// ------------------------------------------

13-1) Lot 160600SAE
Met la base Cassandra du SAE en version 18 :

- Ajout des actions unitaire suppression_masse et restore_masse

- Ajout des événements :
	WS_SUPPRESSION_MASSE|KO,
	WS_RESTORE_MASSE|KO, 
	SUPPRESSION_MASSE|KO, 
	RESTORE_MASSE_KO,
	DFCE_CORBEILLE_DOC|OK,
	DFCE_RESTORE_DOC|OK
	
- Ajout des index composite pour WATT :
	DomaineCotisant-CodeOrganismeProprietaire-StatutWATT-CodeProduitV2-CodeTraitementV2-DateArchivage
	DomaineCotisant-CodeOrganismeProprietaire-StatutWATT-DateArchivage
	
- On échappe tous les . des valeurs des métadonnées des PRMD suite passage aux expressions régulières
	
Attention le nom de l'opération est différent entre GNT et GNS pour différencier la création des index composite.	
POUR LA GNS :
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties GNS_CASSANDRA_DFCE_160600

POUR LA GNT :
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties GNT_CASSANDRA_DFCE_160600

13-2) Lot 160601SAE
Met la base Cassandra du SAE en version 19 :

- Ajout de la nouvelle metadonnee ATransfererScribe pour les besoins de SCRIBE

- Ajout de l'index composite ATransfererScribe-ApplicationProductrice-DateArchivage pour les besoins de SCRIBE

Attention le nom de l'opération est différent entre GNT et GNS pour différencier la création des index composite.	
POUR LA GNS :
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties GNS_CASSANDRA_DFCE_160601

POUR LA GNT :
sudo java -Dlogback.configurationFile=c:/hawai/data/sae/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -jar c:/hawai/data/sae/sae-lotinstallmaj/sae-lotinstallmaj.jar c:/hawai/data/sae/sae-config.properties GNT_CASSANDRA_DFCE_160601
