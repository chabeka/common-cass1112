package fr.urssaf.image.sae.commons.service;

import java.util.Date;

import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;

/**
 * Service de gestion des parametres
 * 
 */
public interface ParametersService {

   /**
    * Durée de rétention des traces dans le registre de surveillance technique
    * avant qu'elles soient purgées
    * 
    * @param duree
    *           Durée de rétention des traces dans le registre de surveillance
    *           technique avant purge (en nombre de jours)
    */
   void setPurgeTechDuree(Integer duree);

   /**
    * Durée de rétention des traces dans le registre de surveillance technique
    * avant qu'elles soient purgées
    * 
    * @return Durée de rétention des traces dans le registre de surveillance
    *         technique avant purge (en nombre de jours)
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Integer getPurgeTechDuree() throws ParameterNotFoundException;

   /**
    * Durée de rétention des traces dans le registre d'exploitation avant
    * qu'elles soient purgées
    * 
    * @param duree
    *           Durée de rétention des traces dans le registre d'exploitation
    *           avant qu'elles soient purgées (en nombre de jours)
    */
   void setPurgeExploitDuree(Integer duree);

   /**
    * Durée de rétention des traces dans le registre d'exploitation avant
    * qu'elles soient purgées
    * 
    * @return Durée de rétention des traces dans le registre d'exploitation
    *         avant qu'elles soient purgées (en nombre de jours)
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Integer getPurgeExploitDuree() throws ParameterNotFoundException;

   /**
    * Durée de rétention des traces dans le registre de sécurité avant qu'elles
    * soient purgées
    * 
    * @param duree
    *           Durée de rétention des traces dans le registre de sécurité avant
    *           qu'elles soient purgées (en nombre de jours)
    */
   void setPurgeSecuDuree(Integer duree);

   /**
    * Durée de rétention des traces dans le registre de sécurité avant qu'elles
    * soient purgées
    * 
    * @return Durée de rétention des traces dans le registre de sécurité avant
    *         qu'elles soient purgées (en nombre de jours)
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Integer getPurgeSecuDuree() throws ParameterNotFoundException;

   /**
    * Toutes traces antérieures ou datant de cette journée ont été purgées dans
    * le registre de surveillance technique.<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @param date
    *           Toutes traces antérieures ou datant de cette journée ont été
    *           purgées dans le registre de surveillance technique.
    */
   void setPurgeTechDate(Date date);

   /**
    * Toutes traces antérieures ou datant de cette journée ont été purgées dans
    * le registre de surveillance technique.<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @return date Toutes traces antérieures ou datant de cette journée ont été
    *         purgées dans le registre de surveillance technique.
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getPurgeTechDate() throws ParameterNotFoundException;

   /**
    * Toutes traces antérieures ou datant de cette journée ont été purgées dans
    * le registre d'exploitation.<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @param date
    *           Toutes traces antérieures ou datant de cette journée ont été
    *           purgées dans le registre d'exploitation.
    */
   void setPurgeExploitDate(Date date);

   /**
    * Toutes traces antérieures ou datant de cette journée ont été purgées dans
    * le registre d'exploitation.<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @return date Toutes traces antérieures ou datant de cette journée ont été
    *         purgées dans le registre d'exploitation.<br>
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getPurgeExploitDate() throws ParameterNotFoundException;

   /**
    * Toutes traces antérieures ou datant de cette journée ont été purgées dans
    * le registre de sécurité.<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @param date
    *           Toutes traces antérieures ou datant de cette journée ont été
    *           purgées dans le registre de sécurité.<br>
    */
   void setPurgeSecuDate(Date date);

   /**
    * Toutes traces antérieures ou datant de cette journée ont été purgées dans
    * le registre de sécurité.<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @return date Toutes traces antérieures ou datant de cette journée ont été
    *         purgées dans le registre de sécurité.<br>
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getPurgeSecuDate() throws ParameterNotFoundException;

   /**
    * Flag indiquant si la purge du registre de surveillance technique est en
    * cours
    * 
    * @param isRunning
    *           Flag indiquant si la purge du registre de surveillance technique
    *           est en cours
    */
   void setPurgeTechIsRunning(Boolean isRunning);

   /**
    * Flag indiquant si la purge du registre de surveillance technique est en
    * cours
    * 
    * @return Flag indiquant si la purge du registre de surveillance technique
    *         est en cours
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Boolean isPurgeTechIsRunning() throws ParameterNotFoundException;

   /**
    * Flag indiquant si la purge du registre d'exploitation est en cours
    * 
    * @param isRunning
    *           Flag indiquant si la purge du registre d'exploitation est en
    *           cours
    */
   void setPurgeExploitIsRunning(Boolean isRunning);

   /**
    * Flag indiquant si la purge du registre d'exploitation est en cours
    * 
    * @return Flag indiquant si la purge du registre d'exploitation est en cours
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Boolean isPurgeExploitIsRunning() throws ParameterNotFoundException;

   /**
    * Flag indiquant si la purge du registre de sécurité est en cours
    * 
    * @param isRunning
    *           Flag indiquant si la purge du registre de sécurité est en cours
    */
   void setPurgeSecuIsRunning(Boolean isRunning);

   /**
    * Flag indiquant si la purge du registre de sécurité est en cours
    * 
    * @return Flag indiquant si la purge du registre de sécurité est en cours
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Boolean isPurgeSecuIsRunning() throws ParameterNotFoundException;

   /**
    * Toutes traces du journal des événements antérieures ou datant de cette
    * journée ont été journalisées dans un produit documentaire. (ces traces
    * sont donc éligibles à une purge)<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @param date
    *           Toutes traces du journal des événements antérieures ou datant de
    *           cette journée ont été journalisées dans un produit documentaire.
    */
   void setJournalisationEvtDate(Date date);

   /**
    * Toutes traces du journal des événements antérieures ou datant de cette
    * journée ont été journalisées dans un produit documentaire. (ces traces
    * sont donc éligibles à une purge)<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @return Toutes traces du journal des événements antérieures ou datant de
    *         cette journée ont été journalisées dans un produit documentaire.
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getJournalisationEvtDate() throws ParameterNotFoundException;

   /**
    * Flag indiquant si la journalisation des événements SAE est en cours
    * 
    * @param isRunning
    *           Flag indiquant si la journalisation des événements SAE est en
    *           cours
    */
   void setJournalisationEvtIsRunning(Boolean isRunning);

   /**
    * Flag indiquant si la journalisation des événements SAE est en cours
    * 
    * @return Flag indiquant si la journalisation des événements SAE est en
    *         cours
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Boolean isJournalisationEvtIsRunning() throws ParameterNotFoundException;

   /**
    * Durée de rétention des traces dans le journal des événements SAE avant
    * qu'elles soient purgées
    * 
    * @param duree
    *           Durée de rétention des traces dans le journal des événements SAE
    *           avant qu'elles soient purgées (en nombre de jours)
    */
   void setPurgeEvtDuree(Integer duree);

   /**
    * Durée de rétention des traces dans le journal des événements SAE avant
    * qu'elles soient purgées
    * 
    * @return Durée de rétention des traces dans le journal des événements SAE
    *         avant qu'elles soient purgées (en nombre de jours)
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Integer getPurgeEvtDuree() throws ParameterNotFoundException;

   /**
    * Toutes traces antérieures ou datant de cette journée ont été purgées dans
    * le journal des événements SAE.<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @param date
    *           Toutes traces antérieures ou datant de cette journée ont été
    *           purgées dans le journal des événements SAE
    */
   void setPurgeEvtDate(Date date);

   /**
    * Toutes traces antérieures ou datant de cette journée ont été purgées dans
    * le journal des événements SAE.<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @return Toutes traces antérieures ou datant de cette journée ont été
    *         purgées dans le journal des événements SAE.
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getPurgeEvtDate() throws ParameterNotFoundException;

   /**
    * Flag indiquant si la purge du journal des événements SAE est en cours
    * 
    * @param isRunning
    *           Flag indiquant si la purge du journal des événements SAE est en
    *           cours
    */
   void setPurgeEvtIsRunning(Boolean isRunning);

   /**
    * Flag indiquant si la purge du journal des événements SAE est en cours
    * 
    * @return Flag indiquant si la purge du journal des événements SAE est en
    *         cours
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Boolean isPurgeEvtIsRunning() throws ParameterNotFoundException;

   /**
    * Hash du journal précédent produit lors de la précédente journalisation des
    * événements SAE
    * 
    * @param hash
    *           Hash du journal précédent produit lors de la précédente
    *           journalisation des événements SAE
    */
   void setJournalisationEvtHashJournPrec(String hash);

   /**
    * Hash du dernier journal produit lors de la journalisation des événements
    * SAE
    * 
    * @return Hash du dernier journal produit lors de la journalisation des
    *         événements SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtHashJournPrec() throws ParameterNotFoundException;

   /**
    * Identifiant unique du dernier journal produit lors de la journalisation
    * des événements SAE
    * 
    * @param identifiant
    *           Identifiant unique du dernier journal produit lors de la
    *           journalisation des événements SAE
    */
   void setJournalisationEvtIdJournPrec(String identifiant);

   /**
    * Identifiant unique du dernier journal produit lors de la journalisation
    * des événements SAE
    * 
    * @return Identifiant unique du dernier journal produit lors de la
    *         journalisation des événements SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtIdJournPrec() throws ParameterNotFoundException;

   /**
    * Valeur de la métadonnée "ApplicationProductrice" à associer aux journaux
    * des événements SAE
    * 
    * @param applProd
    *           Valeur de la métadonnée "ApplicationProductrice" à associer aux
    *           journaux des événements SAE
    */
   void setJournalisationEvtMetaApplProd(String applProd);

   /**
    * Valeur de la métadonnée "ApplicationProductrice" à associer aux journaux
    * des événements SAE
    * 
    * @return Valeur de la métadonnée "ApplicationProductrice" à associer aux
    *         journaux des événements SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtMetaApplProd() throws ParameterNotFoundException;

   /**
    * Valeur de la métadonnée "ApplicationTraitement" à associer aux journaux
    * des événements SAE
    * 
    * @param applTrait
    *           Valeur de la métadonnée "ApplicationTraitement" à associer aux
    *           journaux des événements SAE
    */
   void setJournalisationEvtMetaApplTrait(String applTrait);

   /**
    * Valeur de la métadonnée "ApplicationTraitement" à associer aux journaux
    * des événements SAE
    * 
    * @return Valeur de la métadonnée "ApplicationTraitement" à associer aux
    *         journaux des événements SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtMetaApplTrait() throws ParameterNotFoundException;

   /**
    * Valeur des métadonnées "CodeOrganismeProprietaire" et
    * "CodeOrganismeGestionnaire" à associer aux journaux des événements SAE
    * 
    * @param codeOrga
    *           Valeur des métadonnées "CodeOrganismeProprietaire" et
    *           "CodeOrganismeGestionnaire" à associer aux journaux des
    *           événements SAE
    */
   void setJournalisationEvtMetaCodeOrga(String codeOrga);

   /**
    * Valeur des métadonnées "CodeOrganismeProprietaire" et
    * "CodeOrganismeGestionnaire" à associer aux journaux des événements SAE
    * 
    * @return Valeur des métadonnées "CodeOrganismeProprietaire" et
    *         "CodeOrganismeGestionnaire" à associer aux journaux des événements
    *         SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtMetaCodeOrga() throws ParameterNotFoundException;

   /**
    * Valeur de la métadonnée "CodeRND" à associer aux journaux des événements
    * SAE
    * 
    * @param codeRnd
    *           Valeur de la métadonnée "CodeRND" à associer aux journaux des
    *           événements SAE
    */
   void setJournalisationEvtMetaCodeRnd(String codeRnd);

   /**
    * Valeur de la métadonnée "CodeRND" à associer aux journaux des événements
    * SAE
    * 
    * @return Valeur de la métadonnée "CodeRND" à associer aux journaux des
    *         événements SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtMetaCodeRnd() throws ParameterNotFoundException;

   /**
    * Valeur de la métadonnée "Titre" à associer aux journaux des événements SAE
    * 
    * @param titre
    *           Valeur de la métadonnée "Titre" à associer aux journaux des
    *           événements SAE
    */
   void setJournalisationEvtMetaTitre(String titre);

   /**
    * Valeur de la métadonnée "Titre" à associer aux journaux des événements SAE
    * 
    * @return Valeur de la métadonnée "Titre" à associer aux journaux des
    *         événements SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getJournalisationEvtMetaTitre() throws ParameterNotFoundException;

   /**
    * Le numéro de la version du RND en cours dans le SAE
    * 
    * @return Le numéro de la version du RND en cours dans le SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   String getVersionRndNumero() throws ParameterNotFoundException;

   /**
    * Le numéro de la version du RND en cours dans le SAE
    * 
    * @param numVersion
    *           Le numéro de la version du RND en cours dans le SAE
    */
   void setVersionRndNumero(String numVersion);

   /**
    * La date de la dernière mise à jour du RND dans le SAE
    * 
    * @return La date de la dernière mise à jour du RND dans le SAE
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getVersionRndDateMaj() throws ParameterNotFoundException;

   /**
    * La date de la dernière mise à jour du RND dans le SAE
    * 
    * @param dateMajRnd
    *           La date de la dernière mise à jour du RND dans le SAE
    */
   void setVersionRndDateMaj(Date dateMajRnd);

   /**
    * Durée de rétention des documents dans la corbeille avant qu'ils soient
    * purgés
    * 
    * @param duree
    *           Durée de rétention des documents dans la corbeille avant qu'ils
    *           soient purgés (en nombre de jours)
    */
   void setPurgeCorbeilleDuree(Integer duree);

   /**
    * Dernière date de succès de la purge de la corbeille
    * 
    * @return Durée de rétention des traces dans le registre de surveillance
    *         technique avant purge (en nombre de jours)
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Integer getPurgeCorbeilleDuree() throws ParameterNotFoundException;

   /**
    * Dernière date de succès de la purge de la corbeille
    * 
    * @param date
    *           Tous documents antérieurs ou datant de cette journée ont été
    *           purgées dans la corbeille
    */
   void setPurgeCorbeilleDateSucces(Date date);

   /**
    * Tous documents antérieurs ou datant de cette journée ont été purgées dans
    * la corbeille<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @return Tous documents antérieurs ou datant de cette journée ont été
    *         purgées dans la corbeille
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getPurgeCorbeilleDateSucces() throws ParameterNotFoundException;

   /**
    * Dernière date de lancement de la purge de la corbeille A noter que la date
    * est tronquée à la journée (année/mois/jour), l'heure n'est pas prise en
    * compte. Tous documents antérieurs ou datant de cette journée ont été
    * purgées dans la corbeille<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @param date
    *           Dernière date de lancement
    */
   void setPurgeCorbeilleDateDebutPurge(Date date);

   /**
    * Date à utiliser pour la borne minimale de l’intervalle de document à
    * purger Tous documents antérieurs ou datant de cette journée ont été
    * purgées dans la corbeille<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte. Tous documents antérieurs ou datant de cette
    * journée ont été purgées dans la corbeille<br>
    * A noter que la date est tronquée à la journée (année/mois/jour), l'heure
    * n'est pas prise en compte.
    * 
    * @return date Dernière date de lancement
    * 
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getPurgeCorbeilleDateDebutPurge() throws ParameterNotFoundException;

   /**
    * Sate à utiliser pour la borne minimale de l’intervalle de document à
    * purger
    * 
    * @param date
    *           Dernière date de lancement
    */
   void setPurgeCorbeilleDateLancement(Date date);

   /**
    * Dernière date de lancement de la purge de la corbeille A noter que la date
    * est tronquée à la journée (année/mois/jour), l'heure n'est pas prise en
    * compte.
    * 
    * @return date Dernière date de lancement
    * 
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Date getPurgeCorbeilleDateLancement() throws ParameterNotFoundException;

   /**
    * Flag indiquant si la purge de la corbeille est en cours
    * 
    * @return Flag indiquant si la purge de la corbeille est en cours
    * @throws ParameterNotFoundException
    *            exception levée si le paramètre n'est pas trouvé
    */
   Boolean isPurgeCorbeilleIsRunning() throws ParameterNotFoundException;

   /**
    * Flag indiquant si la purge de la corbeille est en cours
    * 
    * @param isRunning
    *           Flag indiquant si la purge de la corbeille est en cours
    */
   void setPurgeCorbeilleIsRunning(Boolean isRunning);

}
