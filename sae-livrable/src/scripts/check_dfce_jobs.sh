#!/bin/bash

# Scripts de vérification des logs de l'agent SAE.
#
# Le script vérifie que tous les jobs sont terminés avec succès.
# Exit code : Nombre de traitements non terminés. 

# Répertoire des logs SAE
LOG_DIR="/hawai/logs/@@PROJECT_NAME@@"

# Cron qui lance l'agent SAE sur un seul serveur d'un seul CNP
CRON_AGENT_FILE="/etc/cron.d/agents_@@PROJECT_NAME@@"


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

#
# ============ Analyses des logs sae-dfce-admin-exploit.jar ============ 
#

if ! grep -q "Fin de la purge des événements de type documents" "$LOG_DIR/sae_dfce_admin_exploit-clearDocEvent.log"; then
    error "La purge des évènements de type documents n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "Fin de la purge des événements de type système" "$LOG_DIR/sae_dfce_admin_exploit-clearSystemEvent.log"; then
    error "La purge des évènements de type système n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "Journalisation de type système est terminée" "$LOG_DIR/sae_dfce_admin_exploit-creatSystemEvent.log"; then
    error "La journalisation de type système n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "Journalisation de type documents est terminée" "$LOG_DIR/sae_dfce_admin_exploit-createDocEvent.log"; then
    error "La journalisation de type documents n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "Réindexation DFCE terminée" "$LOG_DIR/sae_dfce_admin_exploit-reindex.log"; then
    error "La réindexation de la base DFCE n'est pas terminée"
    ERRNO+=1
fi

#
# ============ Analyses des logs sae-trace-executable.jar ============ 
#

if ! grep -q "fin du traitement de la journalisation pour le type JOURNALISATION_EVT" "$LOG_DIR/sae_trace_executable-JOURNALISATION_EVT.log"; then
    error "La journalisation JOURNALISATION_EVT n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "fin du traitement de la purge pour le type PURGE_EVT" "$LOG_DIR/sae_trace_executable-PURGE_EVT.log"; then
    error "La purge de type PURGE_EVT n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "fin du traitement de la purge pour le type PURGE_TECHNIQUE" "$LOG_DIR/sae_trace_executable-PURGE_TECHNIQUE.log"; then
    error "La purge de type PURGE_TECHNIQUE n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "fin du traitement de la purge pour le type PURGE_EXPLOITATION" "$LOG_DIR/sae_trace_executable-PURGE_EXPLOITATION.log"; then
    error "La purge de type PURGE_EXPLOITATION n'est pas terminée"
    ERRNO+=1
fi

if ! grep -q "fin du traitement de la purge pour le type PURGE_SECURITE" "$LOG_DIR/sae_trace_executable-PURGE_SECURITE.log"; then
    error "La purge de type PURGE_SECURITE n'est pas terminée"
    ERRNO+=1
fi


#
# ============ Analyses des logs sae-rnd-executable.jar ============ 
#

if ! grep -q "Fin de la synchronisation avec l'ADRN" "$LOG_DIR/sae_rnd_executable-MAJ_RND.log"; then
    error "Erreur dans la mise à jour du RND"
    ERRNO+=1
fi

exit $ERRNO
