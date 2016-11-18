#!/bin/sh
#
# description: Lance ou arrête l'ordonnanceur du SAE.

. /etc/rc.d/init.d/functions

GED_CONCERNEE="$2"

PROG_NAME=@@PROJECT_NAME@@-integrationinstall

# Les sorties sont redirigées dans ce fichier. 
# Ce n'est pas un fichier de log à proprement parlé
# car les logs applicatifs sont gérés via logback.
OUT_FILE="@@LOGS_PATH@@/$PROG_NAME.out"

PID_FILE="/var/run/$PROG_NAME.pid"
LOCK_FILE="/var/lock/subsys/$PROG_NAME"

start() {
	SCHEDULER_CMD_LINE="java -Dlogback.configurationFile=@@SAE_HOME@@/sae-integrationinstall/logback-sae-integrationinstall.xml -jar @@SAE_HOME@@/sae-integrationinstall/sae-integrationinstall.jar @@SAE_HOME@@/sae-config.properties @@SAE_HOME@@/sae-dfce-admin-exploit/saeBase.xml @@SAE_HOME@@/sae-dfce-admin-exploit/LifeCycleRule.xml $GED_CONCERNEE"
	
    echo -n "Démarrage de l'installation de la base SAE $GED_CONCERNEE... "

    if [ -e $PID_FILE ] && [ -e /proc/`cat "$PID_FILE"` ]; then
        echo "Le programme est déja démarré."
        return 1
    fi

    $SCHEDULER_CMD_LINE &> $OUT_FILE &
    RETVAL=$?
    sleep 1
    sched_pid=`pgrep -f -x "$SCHEDULER_CMD_LINE"`
    
    if [ "x$sched_pid" != x ]; then
        echo "$sched_pid" > $PID_FILE
        success "OK"
    else
        failure "KO"
    fi
    
    echo
    [ $RETVAL -eq 0 ] && touch $LOCK_FILE;
    return $RETVAL
}

stop() {
    echo -n "Arrêt de l'installation de la base SAE... "

    if [ ! -e $LOCK_FILE ]; then
        echo "L'outil d' installation de la base SAE n'est pas démarré."
        return 1
    fi

    killproc $PROG_NAME
    RETVAL=$?
    echo
    [ $RETVAL -eq 0 ] && rm -f $LOCK_FILE;
    return $RETVAL
}


case "$1" in
    start)
        if [ -z "$2" ]; then
		echo "Le parametre 2 est manquant. Saisir GNT ou GNS comme deuxieme parametre svp."
		exit 1
		fi
		start
		echo "Veuillez suivre les traitements sur le fichier de sortie $OUT_FILE"
        ;;
    stop)
        stop
        ;;
    status)
        status $PROG_NAME
        ;;
    restart)
        stop
        sleep 3
        start
        ;;
    *)
        echo $"Usage: $0 {start GNS ou GNT|stop|status|restart GNS ou GNT}"
        exit 1
esac

exit $?