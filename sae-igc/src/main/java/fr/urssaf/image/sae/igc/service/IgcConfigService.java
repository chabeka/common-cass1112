package fr.urssaf.image.sae.igc.service;

import org.springframework.core.io.Resource;

import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;

/**
 * Manipulation de la configuration des éléments de l'IGC
 * 
 * 
 */
public interface IgcConfigService {

   @SuppressWarnings("PMD.LongVariable")
   String AC_RACINES_REQUIRED = "L'AC racine n’est pas spécifiée dans le fichier de configuration ${0}";

   @SuppressWarnings("PMD.LongVariable")
   String AC_RACINES_NOTEXIST = "Le répertoire des certificats des AC racines (${0}) spécifié dans le fichier de configuration (${1}) est introuvable";

   String CRLS_REQUIRED = "Le répertoire de téléchargement des CRL n’est pas spécifié dans le fichier de configuration ${0}";

   String CRLS_NOTEXIST = "Le répertoire de téléchargement des CRL (${0}) spécifié dans le fichier de configuration (${1}) est introuvable";

   String URLS_CRL_REQUIRED = "Il faut spécifier au moins une URL de téléchargement des CRL dans le fichier de configuration ${0} ";

   String ID_PKI_REQUIRED = "L'identifiant de la PKI est à renseigner dans le fichier de configuration ${0}";

   /**
    * Renvoie la configuration des éléments de l'IGC
    * 
    * @param pathConfigFile
    *           Chemin complet du fichier de configuration de l'IGC
    * @return Configuration des éléments de l'IGC
    * @throws IgcConfigException
    *            Une erreur s'est produite lors de la lecture ou de la
    *            vérification de la configuration de l'IGC
    */
   IgcConfigs loadConfig(String pathConfigFile) throws IgcConfigException;

   /**
    * Renvoie la configuration des éléments de l'IGC
    * 
    * @param configFile
    *           La ressource représentant le fichier de configuration IGC
    * @return Configuration des éléments de l'IGC
    * @throws IgcConfigException
    *            Une erreur s'est produite lors de la lecture ou de la
    *            vérification de la configuration de l'IGC
    */
   IgcConfigs loadConfig(Resource configFile) throws IgcConfigException;

}
