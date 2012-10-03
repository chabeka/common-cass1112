Pour truster le serveur d'intégration de la signature, pour le SSL :

"%JAVA_HOME%/bin/keytool" -import -v -trustcacerts -alias dictaoint -file hierarchie_toutes_authent.pem -keystore "%JAVA_HOME%/jre/lib/security/cacerts"



Pour lancer le jar exécutable :
java -jar -Djavax.net.ssl.trustStore="%JAVA_HOME%/jre/lib/security/cacerts" -Djavax.net.ssl.trustStorePassword="changeit" client-dictao-0.0.1-SNAPSHOT.jar "C:/fichier_signature_a_verifier.xml" 1 "Vd0T5ShUx4er0PJ8Z7WU2XDmzPozvQAdmN6C7lHthyI=" false

