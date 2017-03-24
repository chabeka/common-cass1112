keytool -genkey -alias myservicekey -keyalg RSA -sigalg SHA1withRSA -keypass skpass -storepass sspass -keystore serviceKeystore.jks -dname "cn=localhost"
keytool -genkey -alias myclientkey -keyalg RSA -sigalg SHA1withRSA -keypass ckpass -storepass cspass -keystore clientKeystore.jks -dname "cn=clientuser"


keytool -export -rfc -keystore clientKeystore.jks -storepass cspass -alias myclientkey -file MyClient.cer
keytool -import -trustcacerts -keystore serviceKeystore.jks -storepass sspass -alias myclientkey -file MyClient.cer -noprompt

keytool -export -rfc -keystore serviceKeystore.jks -storepass sspass -alias myservicekey -file MyService.cer
keytool -import -trustcacerts -keystore clientKeystore.jks -storepass cspass -alias myservicekey -file MyService.cer -noprompt
