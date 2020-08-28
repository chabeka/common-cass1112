#!/bin/sh
#
# description: Utilitaire de mise à jour de la base SAE

PARAM1="$1"
PARAM2="$2"

exec="java -Dlogback.configurationFile=@SAE_HOME@/sae-lotinstallmaj/logback-sae-lotinstallmaj.xml -Dsae.base.rnd=@SAE_HOME@/sae-dfce-admin-exploit/LifeCycleRule.xml -Dsae.base.meta=@SAE_HOME@/sae-dfce-admin-exploit/saeBase.xml -jar @SAE_HOME@/sae-lotinstallmaj/sae-lotinstallmaj.jar @SAE_HOME@/sae-config.properties $PARAM1 $PARAM2"

start() {
    $exec 
}

#########################
# The command line help #
#########################
display_help() {
    echo "Utilitaire de mise à jour de la base SAE"
    echo "   --info                         Affiche la version de la bdd inscrite en bdd, et la dernière version disponible"
    echo "   --details NUM_VERSION          Donne la version du lot et le détail de ce qui est fait par la version"
    echo "   --update                       Enchaine les mises à jour, jusqu'à la dernière version disponible"
    echo "   --updateTo NUM_VERSION         Enchaine les mise à jour jusqu'à la version passée en paramètre"
    echo "   --verify NUM_VERSION           Vérifie que les actions ont bien été faites pour la version indiquée"
    echo "   --redo NUM_VERSION             Réexécute la mise à jour. Ne change pas le numéro de version en bdd"
    echo "   --changeVersionTo NUM_VERSION  Force le numéro de version inscrit en bdd"
    echo "   --create                       Création de la base SAE avec les tables Thrift jusqu'à la version 30"
    echo "   --createcql                    Création de tables cql"
    echo "   --rattrapagecql                Mise à jour des tables cql jusqu'à la version 33 de la base Thrift"
    echo "   --help, -h                     Aide"
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
