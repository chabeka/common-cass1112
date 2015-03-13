package fr.urssaf.image.sae.trace.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.trace.dao.model.Chainage;
import fr.urssaf.image.sae.trace.dao.model.Journal;
import fr.urssaf.image.sae.trace.dao.support.JournalDfceSupport;
import fr.urssaf.image.sae.trace.dao.support.JournalSaeSupport;
import fr.urssaf.image.sae.trace.model.JournalType;
import fr.urssaf.image.sae.trace.service.JournalService;

/**
 * Classe d'implémentation du service JournalService. Cette classe est un
 * singleton et peut être accessible par le mécanisme d'injection IOC avec
 * l'annotation @Autowired
 * 
 * 
 */
@Service
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class JournalServiceImpl implements JournalService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(JournalServiceImpl.class);

   private final JournalDfceSupport journalDfceSupport;

   private final JournalSaeSupport journalSaeSupport;

   private static final String LOG_DEBUT = "{} - début";
   private static final String LOG_FIN = "{} - fin";

   /**
    * @param journalDfceSupport
    *           Service permettant de réaliser des opérations sur les journaux
    *           DFCE
    * @param journalSaeSupport
    *           Service permettant de réaliser des opérations sur les journaux
    *           SAE
    */
   @Autowired
   public JournalServiceImpl(JournalDfceSupport journalDfceSupport,
         JournalSaeSupport journalSaeSupport) {
      super();
      this.journalDfceSupport = journalDfceSupport;
      this.journalSaeSupport = journalSaeSupport;
   }

   @Override
   public final List<Journal> rechercherJournauxDocument(UUID docUuid) {
      String trcPrefix = "rechercherJournauxDocument";
      LOGGER.debug(LOG_DEBUT, trcPrefix);
      LOGGER.debug("{} - UUID Doc : {}", new String[] { trcPrefix,
            docUuid.toString() });

      LOGGER.debug(LOG_FIN, trcPrefix);
      return journalDfceSupport.findByDocumentUuid(docUuid);

   }

   @Override
   public final List<Journal> rechercherJournauxEvenementDfce(Date dateDebut,
         Date dateFin) {
      String trcPrefix = "rechercherJournauxEvenement";
      LOGGER.debug("{} - début", trcPrefix);
      LOGGER.debug("{} - Date début : {}", new String[] { trcPrefix,
            dateDebut.toString() });
      LOGGER.debug("{} - Date fin : {}", new String[] { trcPrefix,
            dateFin.toString() });

      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.FRENCH);
      String date1 = sdf.format(dateDebut);

      String date2 = sdf.format(dateFin);
      date2 = date2.concat("9999");

      LOGGER.debug(LOG_FIN, trcPrefix);
      return journalDfceSupport.findByDates(date1, date2,
            JournalType.JOURNAL_EVENEMENT_DFCE);
   }

   @Override
   public final List<Journal> rechercherJournauxCycleVie(Date dateDebut,
         Date dateFin) {

      String trcPrefix = "rechercherJournauxCycleVie";
      LOGGER.debug("{} - début", trcPrefix);
      LOGGER.debug("{} - Date début : {}", new String[] { trcPrefix,
            dateDebut.toString() });
      LOGGER.debug("{} - Date fin : {}", new String[] { trcPrefix,
            dateFin.toString() });
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.FRENCH);
      String date1 = sdf.format(dateDebut);
      date1 = date1.concat("000000000");
      String date2 = sdf.format(dateFin);
      date2 = date2.concat("999999999");

      LOGGER.debug("{} - fin", trcPrefix);
      return journalDfceSupport.findByDates(date1, date2,
            JournalType.JOURNAL_CYCLE_VIE);
   }

   @Override
   public final byte[] recupererContenuJournalDfce(UUID uuidJournal) {
      String trcPrefix = "recupererContenuJournal";
      LOGGER.debug(LOG_DEBUT, trcPrefix);
      LOGGER.debug("{} - UUID journal : {}", new String[] { trcPrefix,
            uuidJournal.toString() });

      LOGGER.debug(LOG_FIN, trcPrefix);

      return journalDfceSupport.getContent(uuidJournal);

   }

   @Override
   public final List<Chainage> verifierChainage(Date dateDebut, Date dateFin,
         JournalType journalType) {
      String trcPrefix = "verifierChainage";
      LOGGER.debug(LOG_DEBUT, trcPrefix);
      LOGGER.debug("{} - Date début : {}", new String[] { trcPrefix,
            dateDebut.toString() });
      LOGGER.debug("{} - Date fin : {}", new String[] { trcPrefix,
            dateFin.toString() });
      LOGGER.debug("{} - Type journal : {}", new String[] { trcPrefix,
            journalType.toString() });

      LOGGER.debug(LOG_FIN, trcPrefix);

      return journalDfceSupport.checkChaining(dateDebut, dateFin, journalType);

   }

   @Override
   public final List<Journal> rechercherJournauxEvenementSae(Date dateDebut,
         Date dateFin, String nomBase) {

      String trcPrefix = "rechercherJournauxEvenementSae";
      LOGGER.debug("{} - début", trcPrefix);
      LOGGER.debug("{} - Date début : {}", new String[] { trcPrefix,
            dateDebut.toString() });
      LOGGER.debug("{} - Date fin : {}", new String[] { trcPrefix,
            dateFin.toString() });
      LOGGER.debug("{} - Nom de la base : {}", new String[] { trcPrefix,
            nomBase });

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH);
      String date1 = sdf.format(dateDebut);

      String date2 = sdf.format(dateFin);

      LOGGER.debug(LOG_FIN, trcPrefix);
      return journalSaeSupport.findByDates(date1, date2, nomBase);

   }

   @Override
   public final byte[] recupererContenuJournalSae(UUID uuidJournal,
         String nomBase) {
      String trcPrefix = "recupererContenuJournalSae";
      LOGGER.debug("{} - début", trcPrefix);
      LOGGER.debug("{} - UUID journal : {}", new String[] { trcPrefix,
            uuidJournal.toString() });
      LOGGER.debug("{} - Nom de la base : {}", new String[] { trcPrefix,
            nomBase });

      LOGGER.debug("{} - fin", trcPrefix);
      return journalSaeSupport.getContent(uuidJournal, nomBase);
   }

   @Override
   public final String getNomJournalDfce(UUID uuidJournal) {
      String trcPrefix = "getNomJournalDfce";
      LOGGER.debug("{} - début", trcPrefix);
      LOGGER.debug("{} - UUID journal : {}", new String[] { trcPrefix,
            uuidJournal.toString() });

      LOGGER.debug("{} - fin", trcPrefix);
      return journalDfceSupport.getNomJournal(uuidJournal);
   }

   @Override
   public Journal rechercherJournauxDfce(UUID uuidJournal) {
      String trcPrefix = "rechercherJournauxEvenementDfce";
      LOGGER.debug("{} - début", trcPrefix);
      LOGGER.debug("{} - UUID journal : {}", new String[] { trcPrefix,
            uuidJournal.toString() });

      LOGGER.debug("{} - fin", trcPrefix);
      return journalDfceSupport.getJournal(uuidJournal);
   }

   @Override
   public Journal rechercherJournauxEvenementSae(UUID uuidJournal,
         String nomBase) {
      String trcPrefix = "rechercherJournauxEvenementSae";
      LOGGER.debug("{} - début", trcPrefix);
      LOGGER.debug("{} - UUID journal : {}", new String[] { trcPrefix,
            uuidJournal.toString() });

      LOGGER.debug("{} - fin", trcPrefix);
      return journalSaeSupport.findByUUID(uuidJournal, nomBase);
   }



}
