pour générer le code source du web service et le tester lancer la commande
mvn install
avec les options
	-Djavax.net.ssl.keyStore=src/test/resources/client.jks (optionnel avec certficat)
	-Djavax.net.ssl.keyStorePassword=password (optionnel avec certificat)
	-Djavax.net.ssl.trustStore=src/test/resources/client.jks 
	-Djavax.net.ssl.trustStorePassword=password


commande avec -e
mvn install -e -Djavax.net.ssl.keyStore=src/test/resources/client.jks -Djavax.net.ssl.keyStorePassword=password -Djavax.net.ssl.trustStore=src/test/resources/client.jks -Djavax.net.ssl.trustStorePassword=password

