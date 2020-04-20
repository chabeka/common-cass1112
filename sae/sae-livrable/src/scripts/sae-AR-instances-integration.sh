#!/bin/bash
# 
# description: Lance ou arrete les outils du SAE.
 
# Source function library
. /etc/rc.d/init.d/functions
 
PROG_TOMCAT="tomcat"
PROG_ZOOKEEPER="zookeeper"
PROG_GED_ORDONNANCEUR="ged_ordonnanceur"
PID_GED_ORDONNANCEUR_NAME="GED-ordonnanceur"
PID_FILE_GED_ORDONNANCEUR="/var/run/$PID_GED_ORDONNANCEUR_NAME.pid"
LOCK_FILE_GED_ORDONNANCEUR="/var/lock/subsys/$PID_GED_ORDONNANCEUR_NAME"
 
start(){
	echo -n "Verification de l'etat du service $PROG_ZOOKEEPER: "
	RETVAL=$(service $PROG_ZOOKEEPER status)
	echo "$RETVAL"
	if [ "$RETVAL" = "[HAWAI] - Status du service ZOOKEEPER : start" ]; then
        echo "Le service $PROG_ZOOKEEPER est deja demarre."
	else
		echo -n "Starting $PROG_ZOOKEEPER: "
		service $PROG_ZOOKEEPER start
		echo "$PROG_ZOOKEEPER - done."
		sleep 5
		echo "Verification $PROG_ZOOKEEPER start: "
		RETVAL=$(service $PROG_ZOOKEEPER status)
		echo -n "$RETVAL"
		if [ "$RETVAL" = "[HAWAI] - Status du service ZOOKEEPER : start" ]; then
			#echo -n "$PROG_ZOOKEEPER - demarre"
			success "OK"
			echo -e "\n"
			sleep 5
		else
			failure "KO"
			echo -e "\n"
			sleep 10
			start
		fi
    fi	
	echo -n "Verification de l'etat du service $PROG_TOMCAT: "
	RETVAL=$(service $PROG_TOMCAT status)
	echo "$RETVAL"
	if [ "$RETVAL" = "[HAWAI] - Status du service TOMCAT : start" ]; then
        echo "Le service $PROG_TOMCAT est deja demarre."
	else
		echo -n "Starting $PROG_TOMCAT: "
		service $PROG_TOMCAT start
		echo "done."
		sleep 5
		echo "Verification $PROG_TOMCAT start : "
		RETVAL=$(service $PROG_TOMCAT status)
		echo -n "$RETVAL"
		if [ "$RETVAL" = "[HAWAI] - Status du service TOMCAT : start" ]; then
			#echo -n "$PROG_TOMCAT - demarre"
			success "OK"
			echo -e "\n"
			sleep 5
		else
			failure "KO"
			sleep 10
			start
		fi
    fi	
        echo "Verification de l'etat du service $PROG_GED_ORDONNANCEUR: "
		if [ -e $PID_FILE_GED_ORDONNANCEUR ] && [ -e /proc/`cat "$PID_FILE_GED_ORDONNANCEUR"` ]; then
			echo "Le programme $PROG_GED_ORDONNANCEUR est deja demarre."
		else
			echo -n "Starting $PROG_GED_ORDONNANCEUR: "
			service ged_ordonnanceur start
			echo "$PROG_GED_ORDONNANCEUR - done."
			sleep 5
			echo "Verification $PROG_GED_ORDONNANCEUR start "
			if [ -e $PID_FILE_GED_ORDONNANCEUR ] && [ -e /proc/`cat "$PID_FILE_GED_ORDONNANCEUR"` ]; then
				#echo -n "$PROG_GED_ORDONNANCEUR - demarre"
				success "OK"
				echo -e "\n"
				sleep 5
			else
				failure "KO"
				echo -e "\n"
				sleep 10
				start
			fi
		fi
}
 
stop(){
	echo -n "Verification de l'etat du service $PROG_TOMCAT: "	
	RETVAL=$(service $PROG_TOMCAT status)
	echo "$RETVAL"
	if [ "$RETVAL" = "[HAWAI] - Status du service TOMCAT : stop" ]; then
        echo "Le service $PROG_TOMCAT n'est pas demarre."
	else
		echo -n "Shutting down $PROG_TOMCAT: "
		service $PROG_TOMCAT stop
		echo "$PROG_TOMCAT - done."
		sleep 5
		echo -n "Verification $PROG_TOMCAT stop: "
		RETVAL=$(service $PROG_TOMCAT status)
		echo -n "$RETVAL"
		if [ "$RETVAL" = "[HAWAI] - Status du service TOMCAT : stop" ]; then
			#echo -n "$PROG_TOMCAT arrête"
			success "OK"
			echo -e "\n"
			sleep 5
		else
			failure "KO"
			echo -e "\n"
			sleep 10
			stop
		fi
    fi	

	echo -n "Verification de l'etat du service $PROG_ZOOKEEPER: "	
	RETVAL=$(service $PROG_ZOOKEEPER status)
	echo "$RETVAL"
	if [ "$RETVAL" = "[HAWAI] - Status du service ZOOKEEPER : stop" ]; then
        echo "Le service $PROG_ZOOKEEPER n'est pas demarre."
	else
		echo -n "Starting $PROG_ZOOKEEPER: "
		service $PROG_ZOOKEEPER stop
		echo "$PROG_ZOOKEEPER - done."
		sleep 5
		echo "Verification $PROG_ZOOKEEPER stop: "
		RETVAL=$(service $PROG_ZOOKEEPER status)
		echo -n "$RETVAL"
		if [ $"$RETVAL" = "[HAWAI] - Status du service ZOOKEEPER : stop" ]; then
			#echo -n "$PROG_ZOOKEEPER arrête"
			success "OK"
			echo -e "\n"
			sleep 5
		else
			failure "KO"
			echo -e "\n"
			sleep 10
			stop
		fi
    fi	
    echo "Verification de l'etat du service $PROG_GED_ORDONNANCEUR: "
	if [ ! -e $LOCK_FILE_GED_ORDONNANCEUR ]; then
		echo "L'ordonnanceur GED n'est pas demarre."
	else
		echo -n "Stopping $PROG_GED_ORDONNANCEUR: "
		service ged_ordonnanceur stop
		echo "$PROG_GED_ORDONNANCEUR - done."
		sleep 5
		echo -n "Verification $PROG_GED_ORDONNANCEUR stop "
		if [ ! -e $LOCK_FILE_GED_ORDONNANCEUR ]; then
			success "OK"
			echo -e "\n"
			sleep 5
		else
			failure "KO"
			echo -e "\n"
			sleep 10
			stop
		fi
	fi
}
 
restart(){
   stop
   sleep 10
   start
}
 
status(){
    echo -n "Verification de l'etat du service $PROG_TOMCAT: "	
	RETVAL=$(service $PROG_TOMCAT status)
	echo "$RETVAL"
	echo -n "Verification de l'etat du service $PROG_ZOOKEEPER: "	
	RETVAL=$(service $PROG_ZOOKEEPER status)
	echo "$RETVAL"
    echo -n "Verification de l'etat du service $PROG_GED_ORDONNANCEUR: "
	if [ -e $PID_FILE_GED_ORDONNANCEUR ] && [ -e /proc/`cat "$PID_FILE_GED_ORDONNANCEUR"` ]; then
		echo "Le programme $PROG_GED_ORDONNANCEUR est demarre."
	elif [ ! -e $LOCK_FILE_GED_ORDONNANCEUR ]; then
		echo "L'ordonnanceur GED n'est pas demarre."
	else
		echo "L'ordonnanceur GED est dans un etat instable. Veuillez detuire le process manuellement svp."
	fi
}
 
case "$1" in
  start)
	echo "Debut du traitement de demarrage sur le serveur GED"
    start
        ;;
  stop)
	echo "Debut du traitement de l'arret sur le serveur GED"
    stop
        ;;
  restart)
	echo "Debut du traitement de redemarrage sur le serveur GED"
    restart
        ;;
  status)
	echo "Debut du traitement du status sur le serveur GED"
    status
    ;;
  *)
    echo "Usage : $0 {start|stop|status|restart}"
esac
 
exit 0
