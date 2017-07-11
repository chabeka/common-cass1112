#!/bin/bash
# 
# description: Lance ou arrête les outils du SAE.
 
# Source function library
. /etc/rc.d/init.d/functions

GED_CONCERNEE="$2"
 
PROG_TOMCAT="tomcat"
PROG_ZOOKEEPER="zookeeper"
PROG_SAE_ORDONNANCEUR="sae-ordonnanceur"
PROG_GNT_ORDONNANCEUR="gnt-ordonnanceur"
PID_FILE_SAE_ORDONNANCEUR="/var/run/$PROG_SAE_ORDONNANCEUR.pid"
PID_FILE_GNT_ORDONNANCEUR="/var/run/$PROG_GNT_ORDONNANCEUR.pid"
LOCK_FILE_SAE_ORDONNANCEUR="/var/lock/subsys/$PROG_SAE_ORDONNANCEUR"
LOCK_FILE_GNT_ORDONNANCEUR="/var/lock/subsys/$PROG_GNT_ORDONNANCEUR"
 
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
	if [ "GNT" = "$GED_CONCERNEE" ]; then
        echo "Verification de l'etat du service $PROG_GNT_ORDONNANCEUR: "
		if [ -e $PID_FILE_GNT_ORDONNANCEUR ] && [ -e /proc/`cat "$PID_FILE_GNT_ORDONNANCEUR"` ]; then
			echo "Le programme $PROG_GNT_ORDONNANCEUR est deja demarre."
		else
			echo -n "Starting $PROG_GNT_ORDONNANCEUR: "
			service gnt-ordonnanceur start
			echo "$PROG_GNT_ORDONNANCEUR - done."
			sleep 5
			echo "Verification $PROG_GNT_ORDONNANCEUR start "
			if [ -e $PID_FILE_GNT_ORDONNANCEUR ] && [ -e /proc/`cat "$PID_FILE_GNT_ORDONNANCEUR"` ]; then
				#echo -n "$PROG_GNT_ORDONNANCEUR - demarre"
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
		
    elif [ "GNS" = "$GED_CONCERNEE" ]; then
	    echo "Verification de l'etat du service $PROG_SAE_ORDONNANCEUR: "
		if [ -e $PID_FILE_SAE_ORDONNANCEUR ] && [ -e /proc/`cat "$PID_FILE_SAE_ORDONNANCEUR"` ]; then
			echo "Le programme $PROG_SAE_ORDONNANCEUR est deja demarre."
		else
			echo -n "Starting $PROG_SAE_ORDONNANCEUR: "
			service sae-ordonnanceur start
			echo "$PROG_SAE_ORDONNANCEUR - done."
			sleep 5
			echo -n "Verification $PROG_SAE_ORDONNANCEUR start "
			if [ -e $PID_FILE_SAE_ORDONNANCEUR ] && [ -e /proc/`cat "$PID_FILE_SAE_ORDONNANCEUR"` ]; then
				#echo -n "$PROG_SAE_ORDONNANCEUR - demarre"
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
	else
        echo -n "L'ordonnanceur n'a pas pu être arrête car le parametre 2 du script n'est pas reconnu (GNT ou GNS)."
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
	if [ "GNT" = "$GED_CONCERNEE" ]; then
        echo "Verification de l'etat du service $PROG_GNT_ORDONNANCEUR: "
		if [ ! -e $LOCK_FILE_GNT_ORDONNANCEUR ]; then
			echo "L'ordonnanceur GNT n'est pas demarre."
		else
			echo -n "Stopping $PROG_GNT_ORDONNANCEUR: "
			service gnt-ordonnanceur stop
			echo "$PROG_GNT_ORDONNANCEUR - done."
			sleep 5
			echo -n "Verification $PROG_GNT_ORDONNANCEUR stop "
			if [ ! -e $LOCK_FILE_GNT_ORDONNANCEUR ]; then
				#echo -n "$PROG_GNT_ORDONNANCEUR arrête"
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
		
    elif [ "GNS" = "$GED_CONCERNEE" ]; then
	    echo "Verification de l'etat du service $PROG_SAE_ORDONNANCEUR: "
		if [ ! -e $LOCK_FILE_SAE_ORDONNANCEUR ]; then
			echo "L'ordonnanceur SAE n'est pas demarre."
		else
			echo -n "Stopping $PROG_SAE_ORDONNANCEUR: "
			service sae-ordonnanceur stop
			echo "$PROG_SAE_ORDONNANCEUR - done."
			sleep 5
			echo -n "Verification $PROG_SAE_ORDONNANCEUR stop "
			if [ ! -e $LOCK_FILE_SAE_ORDONNANCEUR ]; then
				#echo -n "$PROG_SAE_ORDONNANCEUR arrête"
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
	else
        echo -n "L'ordonnanceur n'a pas pu être arrête car le parametre 2 du script n'est pas reconnu (GNT ou GNS)."
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
	if [ "GNT" = "$GED_CONCERNEE" ]; then
        echo -n "Verification de l'etat du service $PROG_GNT_ORDONNANCEUR: "
		if [ -e $PID_FILE_GNT_ORDONNANCEUR ] && [ -e /proc/`cat "$PID_FILE_GNT_ORDONNANCEUR"` ]; then
			echo "Le programme $PROG_GNT_ORDONNANCEUR est demarre."
		elif [ ! -e $LOCK_FILE_GNT_ORDONNANCEUR ]; then
			echo "L'ordonnanceur GNT n'est pas demarre."
		else
			echo "L'ordonnanceur GNT est dans un état instable. Veuillez détuire le process manuellement svp."
		fi
		
    elif [ "GNS" = "$GED_CONCERNEE" ]; then
	    echo -n "Verification de l'etat du service $PROG_SAE_ORDONNANCEUR: "
		if [ -e $PID_FILE_SAE_ORDONNANCEUR ] && [ -e /proc/`cat "$PID_FILE_SAE_ORDONNANCEUR"` ]; then
			echo "Le programme $PROG_SAE_ORDONNANCEUR est demarre."
		elif [ ! -e $LOCK_FILE_SAE_ORDONNANCEUR ]; then
			echo "L'ordonnanceur SAE n'est pas demarre."
		else
			echo "L'ordonnanceur SAE est dans un état instable. Veuillez détuire le process manuellement svp."
		fi
	else
        echo -n "L'ordonnanceur n'a pas pu être arrête car le parametre 2 du script n'est pas reconnu (GNT ou GNS)."
    fi
}
 
case "$1" in
  start)
	if [ -z "$2" ]; then
	echo "Le parametre 2 est manquant. Saisir GNT ou GNS comme deuxieme parametre svp."
	exit 1
	fi
	case "$2" in "GNT" | "GNS")
		echo "Debut du traitement de demarrage sur le serveur $2"
		;;
	 *)
		echo "Le serveur $2 n'est pas reconnu. Veuillez saisir GNS ou GNT svp."
		exit 1
		;;
	esac
    start
    ;;
  stop)
  	if [ -z "$2" ]; then
	echo "Le parametre 2 est manquant. Saisir GNT ou GNS comme deuxieme parametre svp."
	exit 1
	fi
	case "$2" in "GNT" | "GNS")
		echo "Debut du traitement de demarrage sur le serveur $2"
		;;
	 *)
		echo "Le serveur $2 n'est pas reconnu. Veuillez saisir GNS ou GNT svp."
		exit 1
		;;
	esac
    stop
    ;;
  restart)
  	if [ -z "$2" ]; then
	echo "Le parametre 2 est manquant. Saisir GNT ou GNS comme deuxieme parametre svp."
	exit 1
	fi
	case "$2" in "GNT" | "GNS")
		echo "Debut du traitement de demarrage sur le serveur $2"
		;;
	 *)
		echo "Le serveur $2 n'est pas reconnu. Veuillez saisir GNS ou GNT svp."
		exit 1
		;;
	esac
    restart
    ;;
  status)
  	if [ -z "$2" ]; then
	echo "Le parametre 2 est manquant. Saisir GNT ou GNS comme deuxieme parametre svp."
	exit 1
	fi
	case "$2" in "GNT" | "GNS")
		echo "Debut du traitement de demarrage sur le serveur $2"
		;;
	 *)
		echo "Le serveur $2 n'est pas reconnu. Veuillez saisir GNS ou GNT svp."
		exit 1
		;;
	esac
    status
    ;;
  *)
      echo "Usage : $0 {start GNS ou GNT|stop GNS ou GNT|status  GNS ou GNT|restart GNS ou GNT}"
esac
 
exit 0
