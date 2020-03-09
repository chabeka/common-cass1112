package fr.urssaf.image.sae.trace.dao.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.trace.dao.model.Chainage;
import fr.urssaf.image.sae.trace.dao.model.Journal;
import fr.urssaf.image.sae.trace.exception.TraceRuntimeException;
import fr.urssaf.image.sae.trace.model.DfceTraceDoc;
import fr.urssaf.image.sae.trace.model.JournalType;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.recordmanager.RMLogArchiveReport;
import net.docubase.toolkit.model.search.SearchResult;
import net.docubase.toolkit.model.search.SortedSearchQuery;

/**
 * Service permettant de réaliser des opérations sur les journaux DFCE
 * (historique des événements ou du cycle de vie des archives)
 *
 *
 */
@Component
public class JournalDfceSupport {

  private static final int CENT = 100;

  /**
   * Classe d'accès à DFCE
   */
  private final DFCEServices dfceServices;

  /**
   * Constructeur
   *
   * @param serviceProvider
   *           Classe d'accès à DFCE
   */
  @Autowired
  public JournalDfceSupport(final DFCEServices dfceServices) {
    super();
    this.dfceServices = dfceServices;
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
  public final List<Journal> findByDocumentUuid(final UUID docUuid) {

    final CycleVieSupport cycleVieSupport = new CycleVieSupport(dfceServices);

    final List<DfceTraceDoc> listeTraces = cycleVieSupport.findByDocUuid(docUuid);

    final List<UUID> listeUuidJournal = new ArrayList<>();
    for (final DfceTraceDoc dfceTraceDoc : listeTraces) {
      final UUID idJournal = dfceTraceDoc.getIdJournal();
      listeUuidJournal.add(idJournal);
    }

    final Set<UUID> set = new HashSet<>();
    set.addAll(listeUuidJournal);
    final ArrayList<UUID> listeUuidJournalUnique = new ArrayList<>(set);

    final List<Journal> listeJournal = new ArrayList<>();

    for (final UUID uuid : listeUuidJournalUnique) {
      if (uuid != null) {
        final Base base = dfceServices.getLogsArchiveBase();
        final Document doc = dfceServices.getDocumentByUUID(base, uuid);
        final String nomFichier = doc.getFilename() + "." + doc.getExtension();

        final Date dateDebutEvt = (Date) doc.getSingleCriterion(
            "LOG_ARCHIVE_BEGIN_DATE").getWord();
        final Date dateFinEvt = (Date) doc.getSingleCriterion(
            "LOG_ARCHIVE_END_DATE").getWord();

        final Journal journal = new Journal(doc.getArchivageDate(), uuid,
                                            nomFichier, dateDebutEvt, dateFinEvt, doc.getSize());
        listeJournal.add(journal);
      }
    }

    return listeJournal;
  }

  /**
   * Retourne la liste des journaux des événements DFCE compris dans
   * l'intervalle de dates donné
   *
   * @param dateDebut
   *           Date de début de l'intervalle
   * @param dateFin
   *           Date de fin de l'intervalle
   * @param journalType
   *           Type du journal
   * @return Liste des journaux des événements compris dans l'intervalle de
   *         dates donné
   */
  public final List<Journal> findByDates(final String dateDebut, final String dateFin,
                                         final JournalType journalType) {

    try {

      // Construction de la requête LUCENE

      final StringBuffer sBuffer = new StringBuffer(CENT);
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

      final Base base = dfceServices.getLogsArchiveBase();

      // Lancement de la recherche
      final String requete = sBuffer.toString();
      final int nbMaxElements = Integer.MAX_VALUE;

      final SortedSearchQuery paramSearchQuery = ToolkitFactory.getInstance().createMonobaseSortedQuery(requete, base);
      paramSearchQuery.setPageSize(nbMaxElements);
      paramSearchQuery.setOffset(0);

      final SearchResult resultat = dfceServices.search(paramSearchQuery);

      final List<Journal> listeJournal = new ArrayList<>();

      final List<Document> listeDoc = resultat.getDocuments();
      for (final Document document : listeDoc) {
        final String nomFichier = document.getFilename() + "."
            + document.getExtension();

        final Date dateDebutEvt = (Date) document.getSingleCriterion(
            "LOG_ARCHIVE_BEGIN_DATE").getWord();
        final Date dateFinEvt = (Date) document.getSingleCriterion(
            "LOG_ARCHIVE_END_DATE").getWord();

        final Journal journal = new Journal(document.getCreationDate(), document
                                            .getUuid(), nomFichier, dateDebutEvt, dateFinEvt, document
                                            .getSize());

        listeJournal.add(journal);

      }
      return listeJournal;

    } catch (final ExceededSearchLimitException e) {
      throw new TraceRuntimeException(e);
    } catch (final SearchQueryParseException e) {
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
  public final List<Chainage> checkChaining(final Date dateDebut, final Date dateFin,
                                            final JournalType journalType) {

    List<RMLogArchiveReport> liste;
    if (journalType.equals(JournalType.JOURNAL_CYCLE_VIE)) {
      liste = dfceServices.createDocumentLogArchiveChainingReport(dateDebut, dateFin);
    } else {
      liste = dfceServices.createSystemLogArchiveChainingReport(dateDebut, dateFin);
    }

    final List<Chainage> listeChainage = new ArrayList<>();
    for (final RMLogArchiveReport rmLogArchiveReport : liste) {
      final Chainage chainage = new Chainage();
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
   * @return Contenu du journal ou null si le document n'existe pas
   */
  public final byte[] getContent(final UUID idJournal) {
    final Base base = dfceServices.getLogsArchiveBase();
    final Document doc = dfceServices.getDocumentByUUID(base, idJournal);
    if (doc != null) {
      final InputStream inStream = dfceServices.getDocumentFile(doc);
      try {
        return IOUtils.toByteArray(inStream);
      } catch (final IOException e) {
        throw new TraceRuntimeException(e);
      }
    } else {
      return null;
    }

  }

  /**
   * Retourne le nom du journal dont l'identifiant est passé en paramètre
   *
   * @param idJournal
   *           Identifiant du journal
   * @return le nom du journal
   */
  public final String getNomJournal(final UUID idJournal) {
    final Base base = dfceServices.getLogsArchiveBase();
    final Document doc = dfceServices.getDocumentByUUID(base, idJournal);

    return doc.getFilename() + "." + doc.getExtension();

  }

  /**
   * Retourne le journal dont l'identifiant est passé en paramètre
   *
   * @param idJournal
   *           Identifiant du journal
   * @return le journal
   */
  public final Journal getJournal(final UUID idJournal) {
    final Base base = dfceServices.getLogsArchiveBase();
    final Document document = dfceServices.getDocumentByUUID(base, idJournal);
    // Contrôle sur les dates pour éviter exception
    if (document != null && document.getSingleCriterion(
        "LOG_ARCHIVE_BEGIN_DATE") != null
        && document.getSingleCriterion(
            "LOG_ARCHIVE_END_DATE") != null) {
      final String nomFichier = document.getFilename() + "."
          + document.getExtension();

      final Date dateDebutEvt = (Date) document.getSingleCriterion(
          "LOG_ARCHIVE_BEGIN_DATE").getWord();
      final Date dateFinEvt = (Date) document.getSingleCriterion(
          "LOG_ARCHIVE_END_DATE").getWord();

      final Journal journal = new Journal(document.getCreationDate(), document
                                          .getUuid(), nomFichier, dateDebutEvt, dateFinEvt, document
                                          .getSize());

      return journal;
    } else {
      return null;
    }

  }

}
