#!/bin/bash
# 
# description: Lance ou arrete les outils du SAE.
 
# Source function library
. /etc/rc.d/init.d/functions
 
PROG_CASSANDRA="cassandra"
PID_FILE_CASSANDRA="/var/run/$PROG_CASSANDRA/$PROG_CASSANDRA.pid"

# System directory
_LOG_DIR=/var/log/cassandra
_DATA_DIR=/var/lib/cassandra/donnees/data/
_COMMITLOG_DIR=/var/lib/cassandra/commitlog

# Keyspaces directory
KEYSPACE_SAE_NAME=SAE
KEYSPACE_DOCUBASE_NAME=Docubase
KEYSPACE_SYST_NAME=system
KEYSPACE_SYST_AUTH_NAME=system_auth
KEYSPACE_SYST_TRC_NAME=system_traces
_DATA_KP_SAE_DIR=$_DATA_DIR$KEYSPACE_SAE_NAME
_DATA_KP_DOCUBASE_DIR=$_DATA_DIR$KEYSPACE_DOCUBASE_NAME
_DATA_KP_SYST_DIR=$_DATA_DIR$KEYSPACE_SYST_NAME
_DATA_KP_SYST_AUTH_DIR=$_DATA_DIR$KEYSPACE_SYST_AUTH_NAME
_DATA_KP_SYST_TRC_DIR=$_DATA_DIR$KEYSPACE_SYST_TRC_NAME
 
start(){
    echo "Verification de l'etat du service $PROG_CASSANDRA: "
	if [ -e $PID_FILE_CASSANDRA ] && [ -e /proc/`cat "$PID_FILE_CASSANDRA"` ]; then
		echo "Le programme $PROG_CASSANDRA est deja demarre."
	else
		echo -n "Starting $PROG_CASSANDRA: "
		service $PROG_CASSANDRA start
		echo "$PROG_CASSANDRA - done."
		sleep 5
		echo -n "Verification $PROG_CASSANDRA start "
		if [ -e $PID_FILE_CASSANDRA ] && [ -e /proc/`cat "$PID_FILE_CASSANDRA"` ]; then
			success "OK"
			echo -e "\n"
			sleep 5
		else
			failure "KO"
			echo -e "\n"
			sleep 5
			start
		fi
	fi
}

stop(){
	echo -n "Verification de l'etat du service $PROG_CASSANDRA: "	
	RETVAL=$(service $PROG_CASSANDRA status)
	echo "$RETVAL"
	case "$RETVAL" in
  		"$PROG_CASSANDRA is stopped" | "$PROG_CASSANDRA est arrêté")
		    echo "Le service $PROG_CASSANDRA n'est pas demarre."
		;;
	*)
      	echo -n "Shutting down $PROG_CASSANDRA: "
		cassandraSafeStop.sh
		echo "$PROG_CASSANDRA - done."
		sleep 5
		echo -n "Verification $PROG_CASSANDRA stop: "
		RETVAL=$(service $PROG_CASSANDRA status)
		echo -n "$RETVAL"
		case "$RETVAL" in
  		"$PROG_CASSANDRA is stopped" | "$PROG_CASSANDRA est arrêté")
		    #echo -n "$PROG_CASSANDRA arrêté"
			success "OK"
			echo -e "\n"
			sleep 5
		;;
		*)
			failure "KO"
			echo -e "\n"
			sleep 5
			stop
		esac
  	esac
}
 
restart(){
   stop
   sleep 10
   start
}
 
status(){
   echo -n "Verification de l'etat du service $PROG_CASSANDRA: "	
	RETVAL=$(service $PROG_CASSANDRA status)
	echo "$RETVAL"
}

delete(){
   echo -n "Verification de l'etat du service $PROG_CASSANDRA: "	
	RETVAL=$(service $PROG_CASSANDRA status)
	echo "$RETVAL"
	case "$RETVAL" in
  		"$PROG_CASSANDRA is stopped" | "$PROG_CASSANDRA est arrêté")
		    echo "$PROG_CASSANDRA est arrete"
			sleep 5
		;;
	*)
      	echo "$PROG_CASSANDRA est demarre, arret en cours..."
		sleep 10
		stop
  	esac
	sleep 10
	echo "Demarrage du processus de suppression de la base GED..."
	echo "Demarrage du processus de suppression du keyspace SAE"
	ls -d $_DATA_KP_SAE_DIR/* |awk '{gsub("'$_DATA_KP_SAE_DIR/'", "");print}' > sae_dir_list
	for SAE_TABLE_DIR in `cat sae_dir_list`
	do
		find $_DATA_KP_SAE_DIR/$SAE_TABLE_DIR/  \( -not -regex ".*snapshots.*" \) -a \( -not -regex ".*backups.*" \) -type f -delete
		if [ $? -eq 0 ]; then
			echo "Le contenu du dossier $_DATA_KP_SAE_DIR/$SAE_TABLE_DIR a ete supprime"
		else
			echo "La supression des fichiers du dossier $_DATA_KP_SAE_DIR/$SAE_TABLE_DIR a echoue"
			exit 1
		fi
	done
	#Suppression sae_dir_list
	rm -rf sae_dir_list
	
	echo "Demarrage du processus de suppression du keyspace Docubase"
	ls -d $_DATA_KP_DOCUBASE_DIR/* |awk '{gsub("'$_DATA_KP_DOCUBASE_DIR/'", "");print}' > docubase_dir_list
	for DOCUBASE_TABLE_DIR in `cat docubase_dir_list`
	do
		find $_DATA_KP_DOCUBASE_DIR/$DOCUBASE_TABLE_DIR/ \( -not -regex ".*snapshots.*" \) -a \( -not -regex ".*backups.*" \) -type f -delete
		if [ $? -eq 0 ]; then
			echo "Le contenu du dossier $_DATA_KP_DOCUBASE_DIR/$DOCUBASE_TABLE_DIR a ete supprime"
		else
			echo "La supression des fichiers du dossier $_DATA_KP_DOCUBASE_DIR/$DOCUBASE_TABLE_DIR a echoue"
			exit 1
		fi
	done
	#Suppression docubase_dir_list
	rm -rf docubase_dir_list
	
	echo "Demarrage du processus de suppression du keyspace system_auth"
	ls -d $_DATA_KP_SYST_DIR/* |awk '{gsub("'$_DATA_KP_SYST_DIR/'", "");print}' > syst_dir_list
	for SYST_TABLE_DIR in `cat syst_dir_list`
	do
		find $_DATA_KP_SYST_DIR/$SYST_TABLE_DIR/ \( -not -regex ".*snapshots.*" \) -a \( -not -regex ".*backups.*" \) -type f -delete
		if [ $? -eq 0 ]; then
			echo "Le contenu du dossier $_DATA_KP_SYST_DIR/$SYST_TABLE_DIR a ete supprime"
		else
			echo "La supression des fichiers du dossier $_DATA_KP_SYST_DIR/$SYST_TABLE_DIR a echoue"
			exit 1
		fi
	done
	#Suppression syst_dir_list
	rm -rf syst_dir_list
	
	echo "Demarrage du processus de suppression du keyspace system_auth"
	ls -d $_DATA_KP_SYST_AUTH_DIR/* |awk '{gsub("'$_DATA_KP_SYST_AUTH_DIR/'", "");print}' > syst_auth_dir_list
	for SYST_AUTH_TABLE_DIR in `cat syst_auth_dir_list`
	do
		find $_DATA_KP_SYST_AUTH_DIR/$SYST_AUTH_TABLE_DIR/ \( -not -regex ".*snapshots.*" \) -a \( -not -regex ".*backups.*" \) -type f -delete
		if [ $? -eq 0 ]; then
			echo "Le contenu du dossier $_DATA_KP_SYST_AUTH_DIR/$SYST_AUTH_TABLE_DIR a ete supprime"
		else
			echo "La supression des fichiers du dossier $_DATA_KP_SYST_AUTH_DIR/$SYST_AUTH_TABLE_DIR a echoue"
			exit 1
		fi
	done
	#Suppression syst_auth_dir_list
	rm -rf syst_auth_dir_list
	
	echo "Demarrage du processus de suppression du keyspace system_traces"
	ls -d $_DATA_KP_SYST_TRC_DIR/* |awk '{gsub("'$_DATA_KP_SYST_TRC_DIR/'", "");print}' > syst_traces_dir_list
	for SYST_TRC_TABLE_DIR in `cat syst_traces_dir_list`
	do
		find $_DATA_KP_SYST_TRC_DIR/$SYST_TRC_TABLE_DIR/ \( -not -regex ".*snapshots.*" \) -a \( -not -regex ".*backups.*" \) -type f -delete
		if [ $? -eq 0 ]; then
			echo "Le contenu du dossier $_DATA_KP_SYST_TRC_DIR/$SYST_TRC_TABLE_DIR a ete supprime"
		else
			echo "La supression des fichiers du dossier $_DATA_KP_SYST_TRC_DIR/$SYST_TRC_TABLE_DIR a echoue"
			exit 1
		fi
	done
	#Suppression syst_traces_dir_list
	rm -rf syst_traces_dir_list
	
	echo "Demarrage du processus de suppression des fichiers de log Cassandra"
	rm -f $_LOG_DIR/*
	RETVAL=$(ls -lR $_LOG_DIR | grep ^- | wc -l)
	if [ $RETVAL -eq 0 ]; then
		echo "Le contenu du dossier $_LOG_DIR a ete supprime"
	else
		echo "La supression des fichiers du dossier $_LOG_DIR a echoue"
		exit 1
	fi
	
	echo "Demarrage du processus de suppression des archives logs Cassandra"
	rm -f $_COMMITLOG_DIR/*
	RETVAL=$(ls -lR $_COMMITLOG_DIR | grep ^- | wc -l)
	if [ $RETVAL -eq 0 ]; then
		echo "Le contenu du dossier $_COMMITLOG_DIR a ete supprime"
	else
		echo "La supression des fichiers du dossier $_COMMITLOG_DIR a echoue"
		exit 1
	fi
}
 
 install(){
	echo -n "Verification de l'etat du service $PROG_CASSANDRA: "	
	if [ -e $PID_FILE_CASSANDRA ] && [ -e /proc/`cat "$PID_FILE_CASSANDRA"` ]; then
		echo "$PROG_CASSANDRA est demarre"
		sleep 5
	else
		echo "$PROG_CASSANDRA est arrete, demarrage en cours..."
		sleep 10
		start
	fi
	sleep 10
	echo "Demarrage du processus d'installation de la base SAE..."
	cassandra-cli -h $(hostname) -u cassandra -pw cassandra -f /etc/cassandra/schema/upgrade-replic-authentification.cli
	if [ $? -eq 0 ]; then
		echo "Creation des authentifications OK"
	else
		echo "Erreur sur la cr�ation des authentifications"
		return $?
	fi
	nodetool -h $(hostname) -u root -pw regina4932 repair system_auth
	if [ $? -eq 0 ]; then
		echo "Commande de repair 1 OK"
	else
		echo "Erreur sur la commande de repair 1"
		return $?
	fi
	cqlsh $(hostname) -u cassandra -p cassandra -f /etc/cassandra/schema/config-authentification.cql
	nodetool -h $(hostname) -u root -pw regina4932 repair system_auth
	if [ $? -eq 0 ]; then
		echo "Commande de repair 2 OK"
	else
		echo "Erreur sur la commande de repair 2"
		return $?
	fi
	cassandra-cli -h $(hostname)  -u root -pw regina4932 -f /etc/cassandra/schema/dfce-1.0.1-schema.txt
	if [ $? -eq 0 ]; then
		echo "La cr�ation de la base de donn�e Cassandra pour DFCE OK"
	else
		echo "Erreur lors de cr�ation de la base de donn�e Cassandra pour DFCE. Lancement du processus de reprise..."
		sleep 10
		gerererreurdfceschema
	fi
	sleep 10
	repart
}

repart() {
	echo "Lancement du processus de repartition entre les 2 serveurs Cassandra: "	
	#R�cup�ration des IP et Token des serveurs du cluster.
	IPSERVEUR1=$(ifconfig | awk '{ if (/inet ad/) { print substr($2,5,15); exit; } }' | sed 's/:/ /g'  | sed 's/ //g')
	echo "IP serveur 1 = $IPSERVEUR1"
	IPSERVEUR2=$(nodetool -h $(hostname) -u root -pw regina4932 ring | awk -v var=$IPSERVEUR1 '{ if ($1 != var && /Up/) { print $1 } }')
	echo "IP serveur 2 = $IPSERVEUR2"
	TOKENSERVEUR1=$(token-generator 2 | awk '{ if (/Node #1:/) { print $3 } }')
	echo "Token serveur 1 = $TOKENSERVEUR1"
	TOKENSERVEUR2=$(token-generator 2 | awk '{ if (/Node #2:/) { print $3 } }')
	echo "Token serveur 2 = $TOKENSERVEUR2"
	echo "tokens generation"
	if [ -n $IPSERVEUR1 ] && [ -n $IPSERVEUR2 ] && [ -n $TOKENSERVEUR1 ] && [ -n $TOKENSERVEUR2 ]; then
		success "OK"
		echo -e "\n"
		sleep 5
	else
		failure "KO"
		echo -e "\n"
		sleep 5
		return 1
	fi

	#token-generator 2
	#if [ $? -eq 0 ]; then

		#echo "Serveur 1 : IP:$IPSERVEUR1 TOKEN:$TOKENSERVEUR1"
		#echo "Serveur 2 : IP:$IPSERVEUR2 TOKEN:$TOKENSERVEUR2"
	#else
		#echo "Erreur lors de la generation des tokens"
		#return $?
	#fi
	echo "La repartition sur le serveur $IPSERVEUR1"
	nodetool -h $(hostname) -u root -pw regina4932 move "$TOKENSERVEUR1"
	if [ $? -eq 0 ]; then
		success "OK"
		echo -e "\n"
		sleep 5
	else
		failure "KO"
		echo -e "\n"
		sleep 5
		echo "Erreur lors de la repartition sur le serveur $IPSERVEUR1. Veuillez contacter l'administrateur reseau svp?"
		return $?
	fi
	echo "La repartition sur le serveur $IPSERVEUR2"
	nodetool -h $IPSERVEUR2 -u root -pw regina4932 move "$TOKENSERVEUR2"
	if [ $? -eq 0 ]; then
		success "OK"
		echo -e "\n"
		sleep 5
	else
		failure "KO"
		echo -e "\n"
		sleep 5
		echo "Erreur lors de la repartition sur le serveur $IPSERVEUR2. Veuillez contacter l'administrateur reseau svp?"
		return $?
	fi	
	nodetool -h $(hostname) -u root -pw regina4932 ring
	REPARTSERVEUR1=$(nodetool -h $(hostname) -u root -pw regina4932 ring | awk -v var=$IPSERVEUR1 '{ if ($1 == var && /Up/) { print $7 } }')
	REPARTSERVEUR2=$(nodetool -h $(hostname) -u root -pw regina4932 ring | awk -v var=$IPSERVEUR1 '{ if ($1 != var && /Up/) { print $7 } }')
	echo "La repartition entre les serveurs $IPSERVEUR1 et $IPSERVEUR2"
	if [ $REPARTSERVEUR1 = $REPARTSERVEUR2 ]; then
		success "OK"
		echo -e "\n"
		sleep 5
	else
		failure "KO"
		echo -e "\n"
		sleep 5
		echo "Erreur lors de la repartition entre les serveurs $IPSERVEUR1 et $IPSERVEUR2. Veuillez contacter l'administrateur reseau svp ?"
		return 1
	fi
}
 
gerererreurdfceschema() {
	echo "Processus de reprise de l'installation de la base de donn�es Cassandra pour DFCE :"
	SRC_DIRECTORY="/etc/cassandra/schema/"
	FILESRCNAME="dfce-1.0.1-schema.txt"
	SRC="$SRC_DIRECTORY$FILESRCNAME"
	FILETRVNAME="dfce-1.0.1-schema-trv.txt"
	SRC_TRV="$SRC_DIRECTORY$FILETRVNAME"
	#echo "SRC=$SRC -  SRCBAK=$SRCBAK - SRC_TRV=$SRC_TRV"
		
	cd $SRC_DIRECTORY
	
	touch $FILETRVNAME
	#Suppression de toutes les lignes pr�cedent la ligne contenant "ADMIN group creation.".
	sed -n -e '/.*ADMIN group creation.*/,$ w dfce-1.0.1-schema-trv.txt' $SRC
	#Ajout de la ligne "use Docubase;" au d�but du fichier.
	sed -i '1 i\ use Docubase;' $SRC_TRV

	echo "La cr�ation de la base de donn�e Cassandra pour DFCE"
	cassandra-cli -h $(hostname)  -u root -pw regina4932 -f $SRC_TRV
	if [ $? -eq 0 ]; then
		rm -rf $SRC_TRV
		echo "La creation de la base de donnee Cassandra pour DFCE terminee"
		success "OK"
		echo -e "\n"
		sleep 5
	else
		failure "KO"
		echo -e "\n"
		sleep 5
		echo "Erreur lors de cr�ation de la base de donn�e Cassandra pour DFCE. Modifier le fichier dfce-1.0.1-schema.txt (cf. Document install)"
		return $?
	fi
}

#########################
# The command line help #
#########################
display_help() {
    echo "Usage: $0 [option...] {start|stop|status|restart|delete|install|repart|gerererreurdfceschema}" >&2
    echo
    echo "   start                      Demarrage de la base de donnees Cassandra"
    echo "   stop                       Arret de la base de donnees  Cassandra"
	echo "   restart                    stop&start"
	echo "   status                     Retourne l'etat de la base de donnees Cassandra"
	echo "   delete                     Arret de la base de donnees  Cassandra si necessaire et "
	echo "                              Suppression des fichiers de donnees GNS ou GNT (Schema et archive log compris)"
	echo "   install                    Demarrage et creation de la base de donnees GNS ou GNT (Schema de la base de donnees et Authentification)"
	echo "   repart                     Gestiond de la repartition entre les differents serveurs du Cluster Cassandra"
	echo "   gerererreurdfceschema      Gestion de l'erreur de creation de la base de donnees pour DFCE"
	echo "   --help, -h                 Aide"
    echo
    exit 1
}
 
################################
# Check if parameters options  #
# are given on the commandline #
################################
case "$1" in
  start)
    start
    ;;
  stop)
    stop
    ;;
  restart)
    restart
    ;;
  status)
    status
    ;;
  delete)
    delete
    ;;
  -d)
    delete
    ;;
  install)
	#if [ -z "$2" ]; then
	#	echo "Le parametre 2 est manquant. Saisir 'cas1' ou 'cas2' comme deuxieme parametre svp."
	#	exit 1
	#fi
	#case "$2" in "cas1" | "cas2")
	#	echo "Debut du traitement de demarrage sur le serveur $2"
	#	;;
	# *)
	#	echo "Le serveur $2 n'est pas reconnu. Veuillez saisir 'cas1' ou 'cas2' svp."
	#	exit 1
	#	;;
	#esac
    install
    ;;
  -i)
    install
    ;;
  repart)
	repart
	;;
  -r)
	repart
	;;
  gerererreurdfceschema)
   gerererreurdfceschema
   ;;
  -g)
   gerererreurdfceschema
   ;;
   -h)
   display_help
   ;;
   --help)
   display_help
   ;;
  *)
      echo "Usage : $0 {start|stop|status|restart|delete,-d|install,-i|repart,-r|gerererreurdfceschema,-g|--help,-h}"
esac
 
exit 0
