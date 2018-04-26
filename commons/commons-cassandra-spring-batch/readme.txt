Ce projet fournit une implémentation des classes DAO de spring-batch qui utilise cassandra et zookeeper.

Pour faire fonctionner spring-batch avec ces classes, il faut au préalable avoir créé
les familles de colonnes nécessaires dans cassandra. Pour ça, se reporter au fichier
src/config/modele_schema_cassandra.txt


Pour instancier les différentes DAO avec Spring, nous vous conseillons d'importer le fichier
de contexte spring suivant :
src/main/resources/applicationContext-commons-cassandra-spring-batch.xml

Les place-holders utilisés sont les suivants :
cassandra.startlocal				: mettre "true" pour utiliser un serveur cassandra local lancé à la volée (utile pour les tests unitaires)
cassandra.hosts						: listes des serveurs cassandra, avec le port(9160), séparés par des virgules
cassandra.username					: username pour la connexion à cassandra (peut être vide dans la cas d'un serveur démarré localement)
cassandra.password					: mot de passe pour la connexion à cassandra
cassandra.keyspace					: nom du keyspace cassandra à utiliser (normalement "SAE", sauf si vous voulez vous créer un keyspace personnel)
cassandra.dataset					: dans le cas du mode "local", il s'agit du chemin du fichier dataset qui sera utilisé pour charger les données
zookeeper.startlocal				: mettre "true" pour utiliser un serveur zookeeper local lancé à la volée (utile pour les tests unitaires)
zookeeper.hosts						: nom des serveurs zookeeper, avec le port, séparés par des virgules (peut être vide dans la cas d'un serveur démarré localement)
zookeeper.namespace					: espace de nom utilisé par zookeeper (normalement : "SAE", sauf si vous voulez vous isoler)

Exemple 1 : serveur cassandra et zookeeper distants

cassandra.startlocal=false
cassandra.hosts=cer69imageint9.cer69.recouv:9160
cassandra.username=root
cassandra.password=regina4932
cassandra.keyspace=SAE
cassandra.dataset=
zookeeper.startlocal=false
zookeeper.hosts=cer69-ds4int.cer69.recouv:2181
zookeeper.namespace=SAE

Exemple 2 : serveurs cassandra et zookeeper locaux et temporaires, pour tests unitaires

cassandra.startlocal=true
cassandra.hosts=
cassandra.username=
cassandra.password=
cassandra.keyspace=Batch
cassandra.dataset=dataSet-commons-cassandra-spring-batch.xml
zookeeper.startlocal=true
zookeeper.hosts=
zookeeper.namespace=Batch

 