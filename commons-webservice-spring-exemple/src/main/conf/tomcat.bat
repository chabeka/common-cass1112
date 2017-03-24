keytool -genkeypair -alias serverkey -keyalg RSA -dname "CN=localhost,OU=webservice.exemple,O=fr.urssaf.image,L=nc,S=nc,C=fr" -keypass password -keystore server.jks -storepass password
keytool -genkeypair -alias clientkey -keyalg RSA -dname "CN=localhost,OU=webservice.exemple,O=fr.urssaf.image,L=nc,S=nc,C=fr" -keypass password -storepass password -keystore client.jks

keytool -exportcert -alias clientkey -file client-public.cer -keystore client.jks -storepass password
keytool -importcert -keystore server.jks -alias clientcert -file client-public.cer -storepass password -noprompt
 

keytool -list -keystore server.jks -storepass password

keytool -exportcert -alias serverkey -file server-public.cer -keystore server.jks -storepass password
keytool -importcert -keystore client.jks -alias servercert -file server-public.cer -storepass password -noprompt
 
keytool -list -keystore client.jks -storepass password