package fr.urssaf.image.sae.trace.dao.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.recordmanager.RMLogArchiveReport;
import net.docubase.toolkit.model.search.SearchResult;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.sae.trace.dao.model.Chainage;
import fr.urssaf.image.sae.trace.dao.model.Journal;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.DfceTraceDoc;
import fr.urssaf.image.sae.trace.model.JournalType;

/**
 * Service permettant de réaliser des opérations sur les journaux DFCE
 * (historique des évènements ou du cycle de vie des archives)
 * 
 * 
 */
@Component
public class JournalDfceSupport {

   private static final int CENT = 100;

   /**
    * Classe d'accès à DFCE
    */
   private final ServiceProviderSupport serviceProvider;

   /**
    * Constructeur
    * 
    * @param serviceProvider
    *           Classe d'accès à DFCE
    */
   @Autowired
   public JournalDfceSupport(ServiceProviderSupport serviceProvider) {
      super();
      this.serviceProvider = serviceProvider;
   }

   /**
    * Retourne la liste des journaux concernant le document dont l'identifiant
    * est passé en paramètre
    * 
    * @param docUuid
    *           Identifiant unique du document
    * @return Liste des journaux concernant le document dont l'uuid est passé en
    *         paramètre
    */
   public final List<Journal> findByDocumentUuid(UUID docUuid) {

      CycleVieSupport cycleVieSupport = new CycleVieSupport(serviceProvider);

      List<DfceTraceDoc> listeTraces = cycleVieSupport.findByDocUuid(docUuid);

      List<UUID> listeUuidJournal = new ArrayList<UUID>();
      for (DfceTraceDoc dfceTraceDoc : listeTraces) {
         UUID idJournal = dfceTraceDoc.getIdJournal();
         listeUuidJournal.add(idJournal);
      }

      Set<UUID> set = new HashSet<UUID>();
      set.addAll(listeUuidJournal);
      ArrayList<UUID> listeUuidJournalUnique = new ArrayList<UUID>(set);

      List<Journal> listeJournal = new ArrayList<Journal>();

      for (UUID uuid : listeUuidJournalUnique) {
         if (uuid != null) {
            Base base = serviceProvider.getArchiveService()
                  .getLogsArchiveBase();
            Document doc = serviceProvider.getSearchService()
                  .getDocumentByUUID(base, uuid);
            String nomFichier = doc.getFilename() + "." + doc.getExtension();

            Date dateDebutEvt = (Date) doc.getSingleCriterion(
                  "LOG_ARCHIVE_BEGIN_DATE").getWord();
            Date dateFinEvt = (Date) doc.getSingleCriterion(
                  "LOG_ARCHIVE_END_DATE").getWord();

            Journal journal = new Journal(doc.getArchivageDate(), uuid,
                  nomFichier, dateDebutEvt, dateFinEvt);
            listeJournal.add(journal);
         }
      }

      return listeJournal;
   }

   /**
    * Retourne la liste des journaux des évènements DFCE compris dans
    * l'intervalle de dates donné
    * 
    * @param dateDebut
    *           Date de début de l'intervalle
    * @param dateFin
    *           Date de fin de l'intervalle
    * @param journalType
    *           Type du journal
    * @return Liste des journaux des évènements compris dans l'intervalle de
    *         dates donné
    */
   public final List<Journal> findByDates(String dateDebut, String dateFin,
         JournalType journalType) {

      try {

         // Construction de la requête LUCENE

         StringBuffer sBuffer = new StringBuffer(CENT);
         sBuffer.append("LOG_ARCHIVE_BEGIN_DATE:[");
         sBuffer.append(dateDebut);
         sBuffer.append(" TO ");
         sBuffer.append(dateFin);
         sBuffer.append("] AND LOG_ARCHIVE_END_DATE:[");
         sBuffer.append(dateDebut);
         sBuffer.append(" TO ");
         sBuffer.append(dateFin);
         sBuffer.append("] AND LOG_ARCHIVE_TYPE:");

         if (journalType.equals(JournalType.JOURNAL_EVENEMENT_DFCE)) {
            sBuffer.append("SYSTEM");
         } else if (journalType.equals(JournalType.JOURNAL_CYCLE_VIE)) {
            sBuffer.append("DOCUMENT");
         }

         Base base = serviceProvider.getArchiveService().getLogsArchiveBase();

         // Lancement de la recherche
         String requete = sBuffer.toString();
         int nbMaxElements = Integer.MAX_VALUE;

         SearchResult resultat = serviceProvider.getSearchService().search(
               requete, nbMaxElements, base);

         List<Journal> listeJournal = new ArrayList<Journal>();

         List<Document> listeDoc = resultat.getDocuments();
         for (Document document : listeDoc) {
            String nomFichier = document.getFilename() + "."
                  + document.getExtension();

            Date dateDebutEvt = (Date) document.getSingleCriterion(
                  "LOG_ARCHIVE_BEGIN_DATE").getWord();
            Date dateFinEvt = (Date) document.getSingleCriterion(
                  "LOG_ARCHIVE_END_DATE").getWord();

            Journal journal = new Journal(document.getCreationDate(), document
                  .getUuid(), nomFichier, dateDebutEvt, dateFinEvt);

            listeJournal.add(journal);

         }
         return listeJournal;

      } catch (ExceededSearchLimitException e) {
         throw new TraceRuntimeException(e);
      } catch (SearchQueryParseException e) {
         throw new TraceRuntimeException(e);
      }
   }

   /**
    * Vérifie le chaînage des journaux
    * 
    * @param dateDebut
    *           Date de début de l'intervalle
    * @param dateFin
    *           Date de fin de l'intervalle
    * @param journalType
    *           Type de journal dont il faut vérifier le chaînage
    * @return La liste des chaînages
    */
   public final List<Chainage> checkChaining(Date dateDebut, Date dateFin,
         JournalType journalType) {

      List<RMLogArchiveReport> liste;
      if (journalType.equals(JournalType.JOURNAL_CYCLE_VIE)) {
         liste = serviceProvider.getArchiveService()
               .createDocumentLogArchiveChainingReport(dateDebut, dateFin);
      } else {
         liste = serviceProvider.getArchiveService()
               .createSystemLogArchiveChainingReport(dateDebut, dateFin);
      }

      List<Chainage> listeChainage = new ArrayList<Chainage>();
      for (RMLogArchiveReport rmLogArchiveReport : liste) {
         Chainage chainage = new Chainage();
         chainage.setAlgoHash(rmLogArchiveReport.getDigestAlgorithm());
         chainage.setDateFin(rmLogArchiveReport.getEndDate());
         chainage.setHash(rmLogArchiveReport.getDigest());
         chainage.setHashRecalcule(rmLogArchiveReport.getReComputedDigest());
         chainage.setUuidPrecedentJournal(rmLogArchiveReport
               .getPreviousArchiveUUID());
         listeChainage.add(chainage);
      }
      return listeChainage;
   }

   /**
    * Récupère le contenu du journal
    * 
    * @param idJournal
    *           L'identifiant unique du journal
    * @return Contenu du journal
    */
   public final byte[] getContent(UUID idJournal) {
      Base base = serviceProvider.getArchiveService().getLogsArchiveBase();
      Document doc = serviceProvider.getSearchService().getDocumentByUUID(base,
            idJournal);
      InputStream inStream = serviceProvider.getStoreService().getDocumentFile(
            doc);
      try {
         return IOUtils.toByteArray(inStream);
      } catch (IOException e) {
         throw new TraceRuntimeException(e);
      }
   }

   /**
    * Retourne le nom du journal dont l'identifiant est passé en paramètre
    * 
    * @param idJournal
    *           Identifiant du journal
    * @return le nom du journal
    */
   public final String getNomJournal(UUID idJournal) {
      Base base = serviceProvider.getArchiveService().getLogsArchiveBase();
      Document doc = serviceProvider.getSearchService().getDocumentByUUID(base,
            idJournal);

      return doc.getFilename() + "." + doc.getExtension();

   }

}
