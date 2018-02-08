Ce projet fournit des services de gestion de file d'attente de travaux.
L'implémentation utiliser cassandra et zookeeper. 

Ces classes peuvent fonctionner en mode "tests unitaires", avec des serveurs cassandra et zookeeper
qui sont lancés localement à la volée.
Ces classes peuvent également fonctionner en mode "réel", et nécessitent alors
la création préalable des familles de colonnes dans cassandra. Pour ça, se reporter au fichier
src/config/modele_schema_cassandra.txt


Les place-holders utilisés par les fichiers spring sont les suivants :

cassandra.startlocal				: mettre "true" pour utiliser un serveur cassandra local lancé à la volée (utile pour les tests unitaires)
cassandra.hosts						: listes des serveurs cassandra, avec le port(9160), séparés par des virgules (peut être vide dans la cas d'un serveur démarré localement)
cassandra.username					: username pour la connexion à cassandra 
cassandra.password					: mot de passe pour la connexion à cassandra
cassandra.timeout           : timeout de la connexion à CASSANDRA
cassandra.keyspace					: nom du keyspace cassandra à utiliser (normalement "SAE", sauf si vous voulez vous créer un keyspace personnel)
cassandra.dataset					: dans le cas du mode "local", il s'agit du chemin du fichier dataset qui sera utilisé pour charger les données
zookeeper.startlocal				: mettre "true" pour utiliser un serveur zookeeper local lancé à la volée (utile pour les tests unitaires)
zookeeper.hosts						: nom des serveurs zookeeper, avec le port, séparés par des virgules (peut être vide dans le cas d'un serveur lancé localement)
zookeeper.namespace					: espace de nom utilisé par zookeeper (normalement : "SAE", sauf si vous voulez vous isoler)

Exemple 1 : serveur cassandra et zookeeper distants

cassandra.startlocal=false
cassandra.hosts=cer69imageint9.cer69.recouv:9160
cassandra.username=root
cassandra.password=regina4932
cassandra.timeout=60000
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
cassandra.timeout=60000
cassandra.keyspace=Batch
cassandra.dataset=dataSet-sae-pile-travaux.xml
zookeeper.startlocal=true
zookeeper.hosts=
zookeeper.namespace=Batch

 