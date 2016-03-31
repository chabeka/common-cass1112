package fr.urssaf.image.sae.trace.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.trace.dao.model.Chainage;
import fr.urssaf.image.sae.trace.dao.model.Journal;
import fr.urssaf.image.sae.trace.model.JournalType;

/**
 * Service de manipulation des journaux
 * 
 * 
 */
public interface JournalService {

   /**
    * Renvoie la liste des journaux des évènements DFCE pour l'intervalle de
    * dates donné
    * 
    * @param dateDebut
    *           Date de début de l'intervalle
    * @param dateFin
    *           Date de fin de l'intervalle
    * @return Liste des journaux compris dans l'intervalle de dates
    */
   List<Journal> rechercherJournauxEvenementDfce(Date dateDebut, Date dateFin);

   /**
    * Renvoie la liste des journaux du cycle de vie pour l'intervalle de dates
    * donné
    * 
    * @param dateDebut
    *           Date de début de l'intervalle
    * @param dateFin
    *           Date de fin de l'intervalle
    * @return Liste des journaux compris dans l'intervalle de dates
    */
   List<Journal> rechercherJournauxCycleVie(Date dateDebut, Date dateFin);

   /**
    * Renvoie la liste des journaux du cycle de vie des archives pour le
    * document donc l'identifiant est passé en paramètre
    * 
    * @param docUuid
    *           Identifiant unique du document
    * @return Liste des journaux concernant le document
    */
   List<Journal> rechercherJournauxDocument(UUID docUuid);

   /**
    * Renvoie le chainage des journaux transmis directement par DFCE pour un
    * intervalle de dates et un type de journal donnés
    * 
    * @param dateDebut
    *           Date de début de l'intervalle
    * @param dateFin
    *           Date de fin de l'intervalle
    * @param journalType
    *           Type de journal dont il faut vérifier le chainage
    * @return Liste des chaînage
    */
   List<Chainage> verifierChainage(Date dateDebut, Date dateFin,
         JournalType journalType);

   /**
    * Renvoie la liste des journaux des évènements SAE pour l'intervalle de
    * dates donné
    * 
    * @param dateDebut
    *           Date de début de l'intervalle
    * @param dateFin
    *           Date de fin de l'intervalle
    * @param nomBase
    *           Nom de la base SAE dans DFCE
    * @return Liste des journaux compris dans l'intervalle de dates
    */
   List<Journal> rechercherJournauxEvenementSae(Date dateDebut, Date dateFin,
         String nomBase);

   /**
    * Renvoie le journal des évènements SAE correspondant à un UUID
    * 
    * @param uuidJournal
    *           l'UUID du journal
    * @param nomBase
    *           Nom de la base SAE dans DFCE
    * @return Liste des journaux compris dans l'intervalle de dates
    */
   Journal rechercherJournauxEvenementSae(UUID uuidJournal, String nomBase);

   /**
    * Renvoie le contenu du journal DFCE avec l'identifiant passé en paramètre
    * 
    * @param uuidJournal
    *           Identifiant unique du journal
    * @return Contenu du journal ou null si le document n'existe pas
    */
   byte[] recupererContenuJournalDfce(UUID uuidJournal);

   /**
    * Renvoie le contenu du journal SAE avec l'identifiant passé en paramètre
    * 
    * @param uuidJournal
    *           Identifiant unique du journal
    * @param nomBase
    *           Nom de la base
    * @return Contenu du journal
    */
   byte[] recupererContenuJournalSae(UUID uuidJournal, String nomBase);

   /**
    * Récupère le nom du journal dont l'id est passé en paramètre
    * 
    * @param uuidJournal
    *           Identifiant unique du journal
    * @return Nom du journal
    */
   String getNomJournalDfce(UUID uuidJournal);

   /**
    * Renvoie le journal des évènements DFCE correspondant à un UUID
    * 
    * @param uuidJournal
    *           UUID du journal
    * @return Le journal correspondant à l'UUID
    */
   Journal rechercherJournauxDfce(UUID uuidJournal);

}
