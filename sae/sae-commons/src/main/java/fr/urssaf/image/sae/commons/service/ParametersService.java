/**
 * 
 */
package fr.urssaf.image.sae.commons.service;

import java.util.Date;

import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;

/**
 * Service de gestion des parametres
 * 
 */
public interface ParametersService {

   /**
    * @param duree
    *           duree de retention pour la purge du registre technique
    */
   void setPurgeTechDuree(Integer duree);

   /**
    * @return duree de retention pour la purge technique
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Integer getPurgeTechDuree() throws ParameterNotFoundException;

   /**
    * @param duree
    *           duree de retention pour la purge du registre d'exploitation
    */
   void setPurgeExploitDuree(Integer duree);

   /**
    * @return duree de retention pour la purge du registre d'exploitation
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Integer getPurgeExploitDuree() throws ParameterNotFoundException;

   /**
    * @param duree
    *           duree de retention pour la purge du registre de sécurité
    */
   void setPurgeSecuDuree(Integer duree);

   /**
    * @return duree de retention pour la purge du registre de sécurité
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Integer getPurgeSecuDuree() throws ParameterNotFoundException;

   /**
    * @param date
    *           dernière date minimale utilisée pour la purge du registre
    *           technique
    */
   void setPurgeTechDate(Date date);

   /**
    * @return date dernière date minimale utilisée pour la purge du registre
    *         technique
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getPurgeTechDate() throws ParameterNotFoundException;

   /**
    * @param date
    *           dernière date minimale utilisée pour la purge du registre
    *           d'exploitation
    */
   void setPurgeExploitDate(Date date);

   /**
    * @return date dernière date minimale utilisée pour la purge du registre
    *         d'exploitation
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getPurgeExploitDate() throws ParameterNotFoundException;

   /**
    * @param date
    *           dernière date minimale utilisée pour la purge du registre de
    *           sécurité
    */
   void setPurgeSecuDate(Date date);

   /**
    * @return date dernière date minimale utilisée pour la purge du registre de
    *         sécurité
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getPurgeSecuDate() throws ParameterNotFoundException;

   /**
    * @param isRunning
    *           indicateur permettant de savoir si la purge des registres
    *           techniques est en cours
    */
   void setPurgeTechIsRunning(Boolean isRunning);

   /**
    * @return indicateur permettant de savoir si la purge des registres
    *         techniques est en cours
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Boolean isPurgeTechIsRunning() throws ParameterNotFoundException;

   /**
    * @param isRunning
    *           indicateur permettant de savoir si la purge des registres
    *           d'exploitation est en cours
    */
   void setPurgeExploitIsRunning(Boolean isRunning);

   /**
    * @return indicateur permettant de savoir si la purge des registres
    *         d'exploitation est en cours
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Boolean isPurgeExploitIsRunning() throws ParameterNotFoundException;

   /**
    * @param isRunning
    *           indicateur permettant de savoir si la purge des registres de
    *           sécurité est en cours
    */
   void setPurgeSecuIsRunning(Boolean isRunning);

   /**
    * @return indicateur permettant de savoir si la purge des registres de
    *         sécurité est en cours
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Boolean isPurgeSecuIsRunning() throws ParameterNotFoundException;

   /**
    * @param date
    *           dernière date minimale utilisée pour la journalisation des
    *           événements SAE
    */
   void setJournalisationEvtDate(Date date);

   /**
    * @return date dernière date minimale utilisée pour journalisation des
    *         événements SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getJournalisationEvtDate() throws ParameterNotFoundException;

   /**
    * @param isRunning
    *           indicateur permettant de savoir si la journalisation des
    *           événements SAE est en cours
    */
   void setJournalisationEvtIsRunning(Boolean isRunning);

   /**
    * @return indicateur permettant de savoir si la journalisation des
    *         événements SAE est en cours
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Boolean isJournalisationEvtIsRunning() throws ParameterNotFoundException;

   /**
    * @param duree
    *           duree de retention pour la purge du journal des événements
    */
   void setPurgeEvtDuree(Integer duree);

   /**
    * @return duree de retention pour la purge du journal des événements
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Integer getPurgeEvtDuree() throws ParameterNotFoundException;

   /**
    * @param date
    *           dernière date minimale utilisée pour la purge des événements SAE
    */
   void setPurgeEvtDate(Date date);

   /**
    * @return date dernière date minimale utilisée pour purge des événements SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getPurgeEvtDate() throws ParameterNotFoundException;

   /**
    * @param isRunning
    *           indicateur permettant de savoir si la purge du journal des
    *           événements est en cours
    */
   void setPurgeEvtIsRunning(Boolean isRunning);

   /**
    * @return indicateur permettant de savoir si la purge du journal des
    *         événements est en cours
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Boolean isPurgeEvtIsRunning() throws ParameterNotFoundException;

   /**
    * @param hash
    *           le hash du journal précédent réalisé lors de la journalisation
    */
   void setJournalisationEvtHashJournPrec(String hash);

   /**
    * @return le hash du journal précédent réalisé lors de la journalisation
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtHashJournPrec() throws ParameterNotFoundException;

   /**
    * @param identifiant
    *           l'identifiant unique du journal précédent réalisé lors de la
    *           journalisation
    */
   void setJournalisationEvtIdJournPrec(String identifiant);

   /**
    * @return l'identifiant unique du journal précédent réalisé lors de la
    *         journalisation
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtIdJournPrec() throws ParameterNotFoundException;

   /**
    * @param applProd
    *           le code de l'application productrice rattachée à la
    *           journalisation
    */
   void setJournalisationEvtMetaApplProd(String applProd);

   /**
    * @return le code de l'application productrice rattachée à la journalisation
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtMetaApplProd() throws ParameterNotFoundException;

   /**
    * @param applTrait
    *           le code de l'application de traitement rattachée à la
    *           journalisation
    */
   void setJournalisationEvtMetaApplTrait(String applTrait);

   /**
    * @return le code de l'application de traitement rattachée à la
    *         journalisation
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtMetaApplTrait() throws ParameterNotFoundException;

   /**
    * @param codeOrga
    *           le code de l'organisme propriétaire et gestionnaire rattachée à
    *           la journalisation
    */
   void setJournalisationEvtMetaCodeOrga(String codeOrga);

   /**
    * @return le code de l'organisme propriétaire et gestionnaire rattachée à la
    *         journalisation
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtMetaCodeOrga() throws ParameterNotFoundException;

   /**
    * @param codeRnd
    *           le code RND rattachée à la journalisation
    */
   void setJournalisationEvtMetaCodeRnd(String codeRnd);

   /**
    * @return le code RND rattachée à la journalisation
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtMetaCodeRnd() throws ParameterNotFoundException;

   /**
    * @param titre
    *           le titre rattachée à la journalisation
    */
   void setJournalisationEvtMetaTitre(String titre);

   /**
    * @return le titre rattachée à la journalisation
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtMetaTitre() throws ParameterNotFoundException;

   /**
    * @return Le numéro de la version du RND en cours dans le SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getVersionRndNumero() throws ParameterNotFoundException;

   /**
    * @param numVersion
    *           le numéro de la version
    */
   void setVersionRndNumero(String numVersion);

   /**
    * @return La date de la dernière mise à jour du RND dans le SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getVersionRndDateMaj() throws ParameterNotFoundException;

   /**
    * @param dateMajRnd
    *           La date de mise à jour du RND
    */
   void setVersionRndDateMaj(Date dateMajRnd);

}
