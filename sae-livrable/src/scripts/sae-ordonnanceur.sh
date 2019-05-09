#!/bin/bash
#
# Startup script for the @PROJECT_NAME@-ordonnanceur
#
# chkconfig: - 85 15
# description: Service @PROJECT_NAME@_ordonnanceur
# processname: sae
# pidfile: @PROJECT_NAME@-ordonnanceur.pid

# source function library
. /etc/rc.d/init.d/functions

RETVAL=0
exec="java -Dlogback.configurationFile=@SAE_HOME@/sae-ordonnanceur/logback-sae-ordonnanceur.xml -jar @SAE_HOME@/sae-ordonnanceur/sae-ordonnanceur.jar @SAE_HOME@/sae-config.properties"
prog="@PROJECT_NAME@-ordonnanceur"
user="root"
pidfile=${PIDFILE-/var/run/@PROJECT_NAME@-ordonnanceur.pid}
lockfile=${LOCKFILE-/var/lock/subsys/@PROJECT_NAME@-ordonnanceur.pid}
logfile="/hawai/logs/ged/@PROJECT_NAME@.log"

runlevel=$(set -- $(runlevel); eval "echo \$$#" )

start() {
    #[ -x $exec ] || exit 5

    umask 022

    touch $logfile $pidfile
    chown $user:$user $logfile $pidfile

    echo -n $"Starting $prog: "
    ## holy shell shenanigans, batman!
    ## daemon can't be backgrounded.  We need the pid of the spawned process,
    ## which is actually done via runuser thanks to --user.  you can't do "cmd
    ## &; action" but you can do "{cmd &}; action".
    daemon \
        --pidfile=$pidfile \
        --user=$user \
        " { nohup $exec &>> $logfile & } ; echo \$! >| $pidfile " &>/dev/null
    sleep 2
    rh_status_q
    RETVAL=$?
    
    [ $RETVAL -eq 0 ] && { touch $lockfile; success; } || failure
    echo

    return $RETVAL
}

stop() {
    echo -n $"Shutting down $prog: "
    killproc -p $pidfile -TERM
    RETVAL=$?
    echo
    [ $RETVAL -eq 0 ] && rm -f $lockfile $pidfile
    return $RETVAL
}

restart() {
    stop
    start
}

rh_status() {
    status -p $pidfile $exec
}

rh_status_q() {
    rh_status >/dev/null 2>&1
}

case "$1" in
    start)
        rh_status_q && exit 0
        start
        ;;
    stop)
        if ! rh_status_q; then
            rm -f $lockfile $pidfile
            exit 0
        fi
        stop
        ;;
    restart)
        restart
        ;;
    condrestart|try-restart)
        rh_status_q || exit 0
        if [ -f $lockfile ] ; then
            restart
        fi
        ;;
    status)
        rh_status
        RETVAL=$?
        if [ $RETVAL -eq 3 -a -f $lockfile ] ; then
            RETVAL=2
        fi
        ;;
    *)
        echo $"Usage: $0 {start|stop|restart|condrestart|try-restart|status}"
        RETVAL=2
esac
exit $RETVAL
