#!/bin/sh
#
# description: Lance ou arr�te l'ordonnanceur du SAE.

. /etc/rc.d/init.d/functions

SCHEDULER_CMD_LINE="java -Dlogback.configurationFile=@@SAE_HOME@@/sae-ordonnanceur/logback-sae-ordonnanceur.xml -jar @@SAE_HOME@@/sae-ordonnanceur/sae-ordonnanceur.jar @@SAE_HOME@@/sae-config.properties"

PROG_NAME=@@PROJECT_NAME@@-ordonnanceur

# Les sorties sont redirig�es dans ce fichier. 
# Ce n'est pas un fichier de log � proprement parl� 
# car les logs applicatifs sont g�r�s via logback.
OUT_FILE="@@LOGS_PATH@@/$PROG_NAME.out"

PID_FILE="/var/run/$PROG_NAME.pid"
LOCK_FILE="/var/lock/subsys/$PROG_NAME"

start() {
    echo -n "D�marrage de l'ordonnanceur du SAE... "

    if [ -e $PID_FILE ] && [ -e /proc/`cat "$PID_FILE"` ]; then
        echo "L'ordonnanceur du SAE est d�ja d�marr�."
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
    echo -n "Arr�t de l'ordonnanceur du SAE... "

    if [ ! -e $LOCK_FILE ]; then
        echo "L'ordonnanceur n'est pas d�marr�."
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
        start
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
        echo $"Usage: $0 {start|stop|status|restart}"
        exit 1
esac

exit $?
