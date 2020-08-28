#!/bin/sh
#
# description: Lance ou arrête l'ordonnanceur du SAE.

. /etc/rc.d/init.d/functions

PARAM1="$1"
PARAM2="$2"

PROG_NAME=@PROJECT_NAME@-lotinstallmaj

# Les sorties sont redirigées dans ce fichier. 
# Ce n'est pas un fichier de log à proprement parlé
# car les logs applicatifs sont gérés via logback.

OUT_FILE="@LOGS_PATH@/$PROG_NAME.out"

PID_FILE="/var/run/$PROG_NAME.pid"
LOCK_FILE="/var/lock/subsys/$PROG_NAME"

exec="java -Dlogback.configurationFile=@SAE_HOME@/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -Dsae.base.rnd=@SAE_HOME@/sae-dfce-admin-exploit/LifeCycleRule.xml -Dsae.base.meta=@SAE_HOME@/sae-dfce-admin-exploit/saeBase.xml -jar @SAE_HOME@/sae-lotinstallmaj/sae-lotinstallmaj.jar @SAE_HOME@/sae-config.properties $PARAM1 $PARAM2"

start() {

    echo "Vérifier si le programme n'est pas déjà lancé "
	
    if [ -e $PID_FILE ] && [ -e /proc/`cat "$PID_FILE"` ]; then
        echo "Le programme est déja démarré."
        return 1
    fi
	
	echo "Lancement du programme "
	
    $exec &
    RETVAL=$?
    sleep 1
    p_pid=`pgrep -f -x "$exec"`

    if [ "x$p_pid" != x ]; then
        echo "$p_pid" > $PID_FILE
        success "OK"
    else
        failure "KO"
    fi

    echo
    [ $RETVAL -eq 0 ] && touch $LOCK_FILE;
    return $RETVAL

}
#########################
# The command line help #
#########################
display_help() {
    echo
    echo "   --info                         Affiche la version de la bdd inscrite en bdd, et la dernière version disponible"
    echo "   --details NUM_VERSION          Donne la version du lot et le détail de ce qui est fait par la version"
    echo "   --update                       Enchaine les mises à jour, jusqu'à la dernière version disponible"
	echo "   --updateTo NUM_VERSION         Enchaine les mise à jour jusqu'à la version passée en paramètre"
	echo "   --verify NUM_VERSION           Vérifie que les actions ont bien été faites pour la version indiquée"
    echo "   --redo NUM_VERSION             Réexécute la mise à jour. Ne change pas le numéro de version en bdd"
	echo "   --changeVersionTo NUM_VERSION  Force le numéro de version inscrit en bdd"
	echo "   --create                      	Création de la base SAE avec les tables Thrift jusqu'à la version 30"
	echo "   --createcql                	Création de tables cql"
	echo "   --rattrapagecql                Mise à jour des tables cql jusqu'à la version 33 de la base Thrift"
    echo "   --help, -h                 	Aide"
    echo
    exit 1
}


case "$1" in
	--info)
		start
		;;
	--detail)
		if [ -z "$2" ]; then
			echo "Le parametre 2 est manquant. Saisir le numéro de la version"
			exit 1
		fi
		start
		;;
	--update)
		start
		;;
	--updateToVersion)
		if [ -z "$2" ]; then
			echo "Le parametre 2 est manquant. Saisir le numéro de la version"
			exit 1
		fi
		start
		;;
	--verify)
		if [ -z "$2" ]; then
			echo "Le parametre 2 est manquant. Saisir le numéro de la version"
			exit 1
		fi
		start
		;;
	--redo)
		if [ -z "$2" ]; then
			echo "Le parametre 2 est manquant. Saisir le numéro de la version"
			exit 1
		fi
		start
		;;
	--changeToVersion)
		if [ -z "$2" ]; then
			echo "Le parametre 2 est manquant. Saisir le numéro de la version"
			exit 1
		fi
   		start
   		;;	
	-h)
   		display_help
   		;;
	--help)
   		display_help
   		;;		
   *)
    echo $"Usage: $0 {--changeToVersion --redo --verify --updateToVersion --detail NUM_VERSION|--info|--update|--create|--createcql|--rattrapagecql|-h,--help}"
    exit 1
esac

exit $?
