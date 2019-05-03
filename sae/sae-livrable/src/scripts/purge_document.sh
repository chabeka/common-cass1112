#!/bin/bash

source /etc/profile

REQ_LUCENE="$2"

purge_corbeille() {
	echo -n "Demarrage de la purge des documents de la corbeille"
	#Purge de la corbeille
	/usr/bin/java -Dlogback.configurationFile=@GED_HOME@/ged_batch_docs_executable/logback-sae-batch-docs-executable.xml -Doperation=PURGE_CORBEILLE -jar @GED_HOME@/ged_batch_docs_executable/sae-batch-docs-executable.jar DELETE_DOCUMENTS_CORBEILLE @GED_ID_INSTANCE@ "$REQ_LUCENE"
}

purge() {
	echo -n "Demarrage de la purge des documents"
	#Purge de la corbeille
	/usr/bin/java -Dlogback.configurationFile=@GED_HOME@/ged_batch_docs_executable/logback-sae-batch-docs-executable.xml -Doperation=PURGE -jar @GED_HOME@/ged_batch_docs_executable/sae-batch-docs-executable.jar DELETE_DOCUMENTS @GED_ID_INSTANCE@ "$REQ_LUCENE"
}
	
#########################
# The command line help #
#########################
display_help() {
    echo "Usage: $0 [option...] {requete_lucene}" >&2
    echo
	echo "   --help, -h                 Aide"
    echo
    exit 1
}

case "$1" in
	purge_corbeille)
        if [ -z "$2" ]; then
			echo "Le parametre 2 est manquant. Saisir la requete lucene necesssaire a la suppression des documents de la corbeille svp."
			exit 1
		fi
		purge_corbeille
        ;;
    purge)
        if [ -z "$2" ]; then
			echo "Le parametre 2 est manquant. Saisir la requete lucene necesssaire a la suppression des documents svp."
			exit 1
		fi
		purge
        ;;
	-h)
   		display_help
   		;;
   --help)
   		display_help
   		;;
   *)
    echo $"Usage: $0 {purge_corbeille requete_lucene|purge requete_lucene|-h,--help}"
    exit 1
esac

exit $?