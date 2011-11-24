Mot de passe du PKCS#12 : mdpKeyStore


Génération du p12 :
keytool -genkey -keyalg RSA -alias aliasClePrivee -keystore keystore.p12 -storepass mdpKeyStore -storetype pkcs12 -validity 10000 -keysize 2048
