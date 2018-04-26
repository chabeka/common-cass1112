test d'intégration

pour les postes de développeur (sur windows)

1 - création des repertoires
			-  c:\sae\certificats\ACRacine
			-  c:\sae\certificats\CRL
2 - exécution : java -jar target/sae-igcmaj-exe.jar config/windows/igcConfig_success.xml
3 - vérification : les crls du serveur http://cer69idxpkival1.cer69.recouv/ sont téléchargés dans c:\sae\certificats\CRL

pour le serveur d'intégration (sur linux)

1 - création des repertoires
			-   /appl/sae/certificats/ACRacineDeConfiance
			-   /appl/sae/certificats/CRL
2 - exécution : java -jar target/sae-igcmaj-exe.jar config/linux/igcConfig_success.xml
3 - vérification : les crls du serveur http://cer69idxpkival1.cer69.recouv/ sont téléchargés dans /appl/sae/certificats/CRL

les tests pour le serveur d'intégration sont dans conf/linux et ceux du poste de développeur sont dans conf/windows

1 - igcConfig_success.xml : fichier de configuration correct
2 - igcConfig_failure_ac_racines_notexist.xml : le répertoire ACRacine n'existe pas
3 - igcConfig_failure_ac_racines_required.xml : le fichier ne comporte pas la balise <repertoireACRacines>
4 - igcConfig_failure_crls_notexist.xml : le répertoire de depôt des CRL n'existe pas
5 - igcConfig_failure_crls_required.xml :  le fichier ne comporte pas la balise <repertoireCRL>
6 - igcConfig_failure_urls_crl_required.xml : le fichier ne comporte pas la balise <URLTelechargementCRL>
7 - igcConfig_failure.properties : le fichier n'est au format XML
8 - igcConfig_failure_download.xml : l'url de téléchargement n'est pas atteignable à cause du proxy