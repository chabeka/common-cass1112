tomcat permet de générer :
	- des certificats server-public.cer & client-public.cer
	- des magasins de certificats client.jks et server.jks


dans server.xml de notre tomcat on ajoute le connecteur pour le port 8443

<Connector SSLEnabled="true" SSLEngine="on"
	SSLVerifyClient="require" SSLVerifyDepth="2" acceptCount="100"
	clientAuth="true" disableUploadTimeout="true" enableLookups="true"
	keystoreFile="server.jks" keystorePass="password" keystoreType="JKS"
	maxSpareThreads="75" maxThreads="200" minSpareThreads="5" port="8443"
	scheme="https" secure="true" sslProtocol="TLS" truststoreFile="server.jks"
	truststorePass="password" truststoreType="JKS" />
	
on installe à la racine de notre tomcat server.jks
