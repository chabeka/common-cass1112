# I) Split DFCE

Il y a plusieurs classe de tests concernées par le split : 

- **SplitBoundaryCalculator** qui permet de calculer les futurs nouveaux ranges d'un index, en spécifiant en combien de bout on veut splitter. 
- **SplitGeneratorTest** qui permet de calculer les ranges sur les UUID uniquement
- **CountIndexRangeSplitTest** qui contient pas mal de junit pour vérifier que les splits ont fonctionné

# II) Métadonnée SM_FINAL_DATE renseignée à tord

Il y a deux classes de tests permettant de vérifier s'il reste des index à supprimer dans TermInfo et TermInfoRangeDatetime : 

 - **FinalDateIndexationTest**
 - **FinalDateIndexationAstyanaxTest**

# III) Autres classes de test

## Avoir des informations sur les métadonnées présentes dans une base et des stats par Base DFCE

Il s'agit de la classe de test **BasesAdminTest** : 
 
 - Permet d'avoir la liste des métadonnées par base DFCE
 - Permet d'avoir le nombre de documents par base
 - Permet d'avoir le nombre de documents pour un code rnd demandé

## Verifier la conf cassandra des différentes CF

Il s'agit de la classe de test **CassandraTest** : 
 
  - permet de verifier les paramètres suivants (CompactionStrategy, CompressionOptions, CompactionStrategyOptions)
  
## Compter des documents sicomor

Il s'agit de la classe de test **CountDocsSicomorTest**

## Avoir l'état de certains jobs DFCE

Il s'agit de la classe de test **JobAdminTest**

## Récupérer une note sur un document

Il s'agit de la classe de test **NotesTest**

## Recherche des documents avec une valeur d'un index de type range

Il s'agit de la classe de test **TermInfoRangeStringTest**

## Recherche de l'indexation d'un document par son UUID 

Il s'agit des classes de test **TermInfoTest** et **ViewTimestampDocTest**