#!/bin/bash

# Scripts de vérification des logs des agents DFCE du SAE.
#
# Le script vérifie que tous les agents DFCE sont terminés.
# Exit code : Nombre de traitements non terminés. 

# Répertoire des logs
LOG_DIR="/hawai/logs/sae"

# Fichier de log des agents DFCE
LOG_FILE="$LOG_DIR/sae_dfce_admin_exploit-debug.log"

# Historique des logs.
#
# Permet de tracer les précédentes exécutions. 
# Utile pour une vérification humaine des logs.
# Doit être géré via logrotate.
HISTORY_FILE="$LOG_DIR/sae_dfce_admin_exploit.history.log"

# Fichier temporaire pour faire le pseudo log rotate
HISTORY_FILE_TMP="$HISTORY_FILE.tailed"

# Cron qui lance les agents DFCE sur un seul serveur d'un seul CNP
CRON_AGENT_FILE="/etc/cron.d/agents_sae"

# Exit code
ERRNO=0
declare -i ERRNO

error () 
{
    d=`date`
    echo "[$d] ERROR: $1" >&2 
}

if [ ! -f $CRON_AGENT_FILE ]; then
    error "Cron $CRON_AGENT_FILE inexistant"
    exit 1
else
    if grep -q "^#" $CRON_AGENT_FILE; then
        echo "Agent désactivé"
        exit 0 # Ce cas n'est pas une erreur
    fi
fi

if ! grep -q "Fin de la purge des événements de type documents" $LOG_FILE; then
    error "La purge des évènements de type documents n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "Fin de la purge des événements de type système" $LOG_FILE; then
    error "La purge des évènements de type système n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "Journalisation de type système est terminée" $LOG_FILE; then
    error "La journalisation de type système n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "Journalisation de type documents est terminée" $LOG_FILE; then
    error "La journalisation de type documents n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "Réindexation DFCE terminée" $LOG_FILE; then
    error "La réindexation de la base DFCE n'est pas terminée"
    ERRNO+=1
fi

# Sauvegarde des logs
cat $LOG_FILE >> $HISTORY_FILE

# Pseudo logrotate : on garde les 1000 dernières lignes.
tail -1000 $HISTORY_FILE > $HISTORY_FILE_TMP
mv -f $HISTORY_FILE_TMP $HISTORY_FILE

# RAZ des logs pour la prochaine exécution
echo "" > $LOG_FILE

exit $ERRNO
