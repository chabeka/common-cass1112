#!/bin/sh

source ~/.bash_profile

CONFIG_REP="/home/support/sae-rsmed-test"
CONFIG_FILE="$CONFIG_REP/sae-rsmed.conf"

source $CONFIG_FILE
current_dir=`pwd`

JAR_ARGS=" --logging.level.fr.urssaf.image=$JAR_LOG_PACKAGE_LEVEL --logging.file=$JAR_LOG_LOCATION --spring.main.banner-mode=off"

log(){
   
	LEVEL=$1
	MESSAGE=$2
	
	date=`date '+%Y-%m-%d %H:%M:%S:%3N'`
	echo "$date  $LEVEL $$ --- ["${0##*/}"] $MESSAGE" >> $RSMED_SCRIPT_LOG 2>&1
}

empty_workdir(){
    log "INFO" "Vider le contenu du repertoire $WORKDIR"
	cd $WORKDIR
	rm -rf *
	cd $current_dir
}

generate_saturne_archive(){
	
	zip_name=$1
	file_to_saturne=${zip_name##*/}
	
	date=`date '+%Y%m%d%H%M%S%3N'`
	prefix_file='UR000_'$date'_AMGNS_RSMED_'


	log "INFO" "Génération de l'archive et le déplacer dans le repertoire $OUT_DIR"
	
	cd $WORKDIR
	mv sommaire.xml $prefix_file"sommaire.xml" >> $RSMED_SCRIPT_LOG 2>&1

	# Création du tar des pdf
	tar -cvf $prefix_file"documents.tar" --remove-files *.pdf
	
	# suppression du zip origine et création du nouveau zip contenant le sommaire
	rm -f $WORKDIR/$zip_name >> $RSMED_SCRIPT_LOG 2>&1
	zip $file_to_saturne $prefix_file"documents.tar" $prefix_file"sommaire.xml"

	mv $file_to_saturne $OUT_DIR >> $RSMED_SCRIPT_LOG 2>&1
	cd $current_dir
}
	
clean_rep_test(){
	cd $ZIP_FILES_DIR
	rm -rf *
	cd $current_dir

	cd $WORKDIR
	rm -rf *
	cd $current_dir

	cd $REJECT_DIR
	rm -rf *
	cd $current_dir

	cd $OUT_DIR
	rm -rf *
	cd $current_dir

}
copy_files_test(){
	cp /home/support/sae-rsmed-test/save_file_in/$1/*.zip $ZIP_FILES_DIR
}

[[ $1 == 'p' ]] && clean_rep_test && copy_files_test "cas_passant" && exit 0; 
[[ $1 == 'n' ]] && clean_rep_test && copy_files_test "cas_non_passant" && exit 0;

for zip_file in `find $ZIP_FILES_DIR -type f -name *.zip`; do

	zip_name=$(basename "$zip_file")
	log "INFO" "Traitement du fichier: $zip_name"

	empty_workdir

	log "décompréssion du fichier $zip_name"
	mv $zip_file $WORKDIR
	unzip -j $WORKDIR/$zip_name -d $WORKDIR/ 1>/dev/null 2>> $RSMED_SCRIPT_LOG

	log "INFO" "Execution du jar avec la commande: java -jar $SAE_RSMED_JAR $JAR_ARGS"

	java -jar $SAE_RSMED_JAR $JAR_ARGS >> $RSMED_SCRIPT_LOG 2>&1
	sommaire_gen_result=$? 

  
	if [[ $sommaire_gen_result -eq 0 ]]; then
		log "INFO" "Fin de l'execution du jar. génération du sommaire OK"
		generate_saturne_archive $zip_name 1>/dev/null 2>> $RSMED_SCRIPT_LOG
		
    else
		log "INFO" "Fin de l'execution du jar. echec de la génération du sommaire"
		log "ERROR" "Déplacement de $zip_name dans le repertoire de rejet: $REJECT_DIR"
		mv $WORKDIR/$zip_name $REJECT_DIR >> $RSMED_SCRIPT_LOG 2>&1;
   fi

done




