#!/bin/sh

# verifie que le nombre d'arguments est correct
if [ "$#" -ne 2 ]; then
  echo "Usage: $0 REPERTOIRE_IMAGE NOM_FICHIER_PDF" >&2
  exit 1
fi

CHEMIN_IMAGES=$1
if [ ! -d ${CHEMIN_IMAGES} ]
then
  echo "${CHEMIN_IMAGES} n'est pas un répertoire"
  exit 2
fi
NOM_PDF=$2

DIR_CMD=`dirname $0`

cd ${CHEMIN_IMAGES}

# genere le fichier output jbig2
${DIR_CMD}/jbig2 -s -p *.jpg 

# genere le nouveau fichier pdf
python ${DIR_CMD}/pdf.py output > ${NOM_PDF}

# supprime les fichiers temporaire
rm output.*
