# I) Classe de test sur la pile de travaux (PileTravauxTest)

## Purge de la pile des travaux

Dans la classe de Junit **PileTravauxTest**, le junit **deleteOldJobs** permet de vérifier et purger les anciens jobs de plus de 30 jours.
Ce junit a été développé parce qu'il y avait un problème avec le jar de purge de la pile des travaux. En effet, ce job n'itère pas sur l'ensemble des jobs
mais en lisait initialement 200 jobs (ça a évolué, on en lit 20000, mais ce n'est pas suffisant).

Dans ce junit, il y a un flag **dryRun** qui permet de lancer le junit en mode vérification (s'il est à true). Cela permet donc de parcourir l'ensemble 
des jobs et de compter le nombre qui devrait être supprimé.

Pour les supprimer réellement, passer le flag **dryRun** à false, et relancer le junit.

Pour info, cette classe de test pointe sur la production, mais on peut facilement switché sur un autre environnement, en changeant l'annotation **@ContextConfiguration** en haute de la classe

## Vérifier si un job est en cours sur chacun des serveurs d'applications

Il s'agit du Junit **getRunningJobs** de la classe **PileTravauxTest**.

## Vérifier si un job est bloqué sur un des serveurs d'application

Il s'agit du Junit **getBlockingJobs** de la classe **PileTravauxTest**.

## Débloquer un job de la pile des travaux

Il s'agit du Junit **unblocOrdonnanceur** de la classe **PileTravauxTest**.

# II) Classe de test sur l'état des jobs (JobInstanceTest)

## Vérifier l'état des jobs DFCE

Il s'agit du Junit **getStateDailyJobsDfce** de la classe **JobInstanceTest**.

Ce junit trace des logs en ERROR si un job est en retard (c'est à dire qu'il ne s'est pas exécuté correctement lors du/des dernier(s) appel(s)). 
Si tout est en info, cela signifie que les jobs DFCE se sont exécutés correctement.

TODO : Si un jour ont doit lancé un nouveau job DFCE (ex: cycle de vie), il faudra rajouter le code pour calculer le libellé, et pour calculer la fréquence d'appel (tous les jours...).

## Vérifier l'état des jobs SAE

Il s'agit du Junit **getStateDailyJobsSae** de la classe **JobInstanceTest**.

Ce junit trace en ERROR les jobs en retard (c'est à dire les jobs qui ne se sont pas exécutés à la bonne fréquence ou en erreur lors de la dernière exécution).
Si tout est en info, cela signifie que les jobs SAE se sont exécutés correctement.

Attention, ce junit se base uniquement sur la table Parameter du schéma SAE. On ne vérifie nul par si le job s'est correctement exécuté (on ne peut pas avoir l'information, ce n'est pas des jobs spring-batch).
Pour le job de purge, on parcourt l'ensemble de la pile des travaux pour vérifier s'il n'y a pas des vieux jobs.

# III) Autres classes de test

## Création d'index composite 

Il s'agit de la classe ** CompositeIndexTest** qui contient plusieurs junit de création d'index composite :

* createCompositeIndexSicomor
* createCompositeIndexGroom

## Suppression de documents

Il s'agit des classe de test **DeleteDocsGroomTest** et **DeleteDocsSicomorTest**.
Ces classes contiennent plusieurs méthodes de comptage, de recherche, et surtout d'extraction des uuid, et de suppression à partir de l'extraction

## Désactivation d'un index composite

Il s'agit de la classe de test **DisableCompositeIndexTest**. 
Cette classe contient plusieurs junit de suppression d'index composite : 

* disableCompositeIndexNff
* disableCompositeIndexGroom
* disableCompositeIndexMontants
* disableCompositeIndexNomFournisseur 
* disableCompositeIndexWatt

Le junit **disableCompositeIndexWatt** est celui qui supprime tous ce qui a attrait à l'index composite.
C'est celui qui a permis de faire la fonctionnalité de désindexation d'un index composite de sae-lotinstallmaj

## Changement du flag d'indexation

Il s'agit de la classe de test **DisableIndexTest**

## Geler/Dégeler/Tester si un document est gelé

Il s'agit de la classe de test **GelDfceTest**

## Indexation d'une métadonnée

Il s'agit de la classe de test **IndexCategoryTest**

## Purge des événements système de DFCE 

Il s'agit de la classe de test **PurgeSystemEventJobTest**

## Consultation de la liste des ranges DFCE

Il s'agit de la classe de test **RangeManagerTest**

## Recherche de documents

Il s'agit de la classe de test **RechercheDocsTest**. Elle contient beaucoup de junit de recherche.

## Recherche dans la corbeille 

Il s'agit de la classe de test **RechercheDocsInRecycleBeanTest**

## Recherche d'évenements système ou sur les documents DFCE

Il s'agit de la classe de test **RecordManagerTest**

## Changement d'un type de métadonnée

Il s'agit de la classe de test **UpdateTypeMetadonneeTest**. Il vaut mieux ne pas utiliser ce junit.
Cela fonctionnerait sur une métadonnée non indexée, mais il vaut mieux éviter. 
