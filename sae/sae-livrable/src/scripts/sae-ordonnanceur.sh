#!/bin/sh


start() {
	echo "#################################################"
	echo "# Demarrage de sae-ordonnanceur pour HAWAI      #"
	echo "#################################################"

	echo "start" > /tmp/sae-ordonnanceur-status
   sleep 1	
   echo 
}

stop() {
	echo
	echo "SERVEUR sae-ordonnanceur pour HAWAI"
	echo
	echo -ne $"Stopping sae-ordonnanceur en cours...\n"
	echo "stop" > /tmp/sae-ordonnanceur-status
   sleep 1
	echo
}

status() {
	echo -ne $"[HAWAI] - Status du service sae-ordonnanceur : "
	cat /tmp/sae-ordonnanceur-status 2>/dev/null || :
	echo
}

case $1 in
start)
	start
    ;;
stop)
	stop
    ;;
restart)
    shift
    "$0" stop ${@}
    sleep 3
    "$0" start ${@}
    ;;
status)
	status
    ;;
*)
    echo "Usage: $0 {start|stop|restart|status}" 
	exit 1
esac

exit 0