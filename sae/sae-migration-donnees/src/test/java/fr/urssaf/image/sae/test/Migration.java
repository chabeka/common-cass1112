package fr.urssaf.image.sae.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBeanCql;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtDao;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDao;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.dao.support.TraceDestinataireSupport;
import fr.urssaf.image.sae.trace.dao.support.TraceJournalEvtSupport;
import fr.urssaf.image.sae.trace.dao.supportcql.TraceJournalEvtCqlSupport;
import fr.urssaf.image.sae.trace.daocql.IGenericType;
import fr.urssaf.image.sae.trace.daocql.ITraceDestinataireCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexCqlDao;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import junit.framework.Assert;

/**
 * TODO (AC75007648) Description du type
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-trace-test.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class Migration {

  private static final Date DATE = new Date();

  private static final int MAX_LIST_SIZE = 100;

  @Autowired
  private ITraceDestinataireCqlDao destinatairedao;

  @Autowired
  private TraceJournalEvtDao dao;

  @Autowired
  private TraceJournalEvtIndexDao indexDao;

  @Autowired
  private TraceJournalEvtSupport supportJThrift;

  @Autowired
  private TraceJournalEvtCqlSupport supportJCql;

  @Autowired
  private ITraceJournalEvtCqlDao tracejdao;

  @Autowired
  private TraceDestinataireSupport supportTDesti;

  @Autowired
  private ITraceJournalEvtIndexCqlDao indexjdao;

  @Autowired
  private TimeUUIDEtTimestampSupport timeUUIDSupport;

  @Autowired
  private IGenericType genericdao;

  @Autowired
  private CassandraServerBeanCql server;

  String[] listcodeevt = {"DFCE_CORBEILLE_DOC|OK", "DFCE_SUPPRESSION_DOC|OK", "DFCE_DEPOT_DOC|OK"};

  String[] listcontexte = {"ModificationDocumentDansDFCE", "CorbeilleDocumentDansDFCE", "SuppressionDocumentDeDFCE"};

  String[] contratservice = {"CS_DEV_TOUTES_ACTIONS", "SAE"};

  static List<String> pagms = new ArrayList<>();

  private static Map<String, String> INFOS;

  static {
    pagms = new ArrayList<>();
    pagms.add("ModificationDocumentDansDFCE");
    pagms.add("CorbeilleDocumentDansDFCE");
    pagms.add("SuppressionDocumentDeDFCE");

    INFOS = new HashMap<String, String>();
    INFOS.put("SuppressionDocumentDeDFCE", "CorbeilleDocumentDansDFCE");
  }

  @Before
  public void before() throws Exception {
    final List<TraceJournalEvtCql> listj = getListTrace();
    for (final TraceJournalEvtCql j : listj) {
      supportJCql.create(j, new Date().getTime());
    }
  }

  @After
  public void after() throws Exception {
    server.resetData();
  }

  @Test
  public void migration() {

    final List<TraceJournalEvtIndexCql> listIndex = Lists.newArrayList(supportJCql.findAllIndex());
    final List<TraceJournalEvtCql> listj = Lists.newArrayList(supportJCql.findAll());

    Assert.assertEquals(listj.size(), listIndex.size());
    Assert.assertEquals(listj.size(), listcodeevt.length);
  }

  // CLASSE UTILITAIRE

  /**
   * Créer une liste de {@link TraceJournalEvtCql} à partir d'une tableau de string
   * representant les codes evèement du journal
   *
   * @return
   */
  public List<TraceJournalEvtCql> getListTrace() {

    final List<TraceJournalEvtCql> list = new ArrayList<>();
    int i = 0;
    for (final String code : listcodeevt) {
      final UUID uuid = timeUUIDSupport.buildUUIDFromDate(DateUtils.addMinutes(DATE, i));
      // System.out.println(uuid);
      final TraceJournalEvtCql tr = new TraceJournalEvtCql(uuid, DATE);
      tr.setCodeEvt(code);
      tr.setContexte("ModificationDocumentDansDFCE");
      tr.setContratService("CS_DEV_TOUTES_ACTIONS");
      tr.setLogin("sae");
      tr.setPagms(pagms);
      tr.setInfos(INFOS);
      list.add(tr);
      i++;
    }
    return list;
  }
}
