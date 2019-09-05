# Script permettant de générer un répertoire sur l'ecde pour un nouveau job
# le 1er paramètre du script est le répertoire racine de l'ecde
#

ecdeRoot=$1

appliPath=$ecdeRoot/sae-integration-test
today="$( date +"%Y%m%d" )"
todayPath=$appliPath/$today

if [ ! -e $todayPath ]; then
   mkdir -p $todayPath
fi

number=1
jobDir=$todayPath/$number

while [ -e "$jobDir" ]; do
    jobDir=$todayPath/"$(( ++number ))"
done
mkdir $jobDir

echo $jobDir