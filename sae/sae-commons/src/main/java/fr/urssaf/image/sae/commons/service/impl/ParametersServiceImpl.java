package fr.urssaf.image.sae.commons.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.commons.zookeeper.ZookeeperMutex;
import fr.urssaf.image.sae.commons.bo.Parameter;
import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.exception.ParameterRuntimeException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.commons.support.ParametersSupport;
import fr.urssaf.image.sae.commons.utils.ZookeeperUtils;

/**
 * Classe d'implémentation du service {@link ParametersService}. Cette classe
 * est un singleton qui peut être accessible via le mecanisme d'injection IOC
 * avec l'annotation @Autowired
 * 
 */
@Service
public class ParametersServiceImpl implements ParametersService {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(ParametersServiceImpl.class);
   private static final String PREFIXE_PARAMS = "/Parameters/";

   private final ParametersSupport parametersSupport;
   private final JobClockSupport clockSupport;
   private final CuratorFramework curator;

   /**
    * Constructeur
    * 
    * @param parametersSupport
    *           support pour la column family Parameters
    * @param clockSupport
    *           l'horloge
    * @param curator
    *           le client zookeeper
    */
   @Autowired
   public ParametersServiceImpl(ParametersSupport parametersSupport,
         JobClockSupport clockSupport, CuratorFramework curator) {
      this.parametersSupport = parametersSupport;
      this.clockSupport = clockSupport;
      this.curator = curator;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Date getJournalisationEvtDate()
         throws ParameterNotFoundException {

      return (Date) parametersSupport
            .find(ParameterType.JOURNALISATION_EVT_DATE,
                  ParameterRowType.TRACABILITE).getValue();

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getJournalisationEvtHashJournPrec()
         throws ParameterNotFoundException {
      return (String) parametersSupport.find(
            ParameterType.JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getJournalisationEvtIdJournPrec()
         throws ParameterNotFoundException {
      return (String) parametersSupport.find(
            ParameterType.JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getJournalisationEvtMetaApplProd()
         throws ParameterNotFoundException {
      return (String) parametersSupport.find(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getJournalisationEvtMetaApplTrait()
         throws ParameterNotFoundException {
      return (String) parametersSupport.find(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_TRAITEMENT,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getJournalisationEvtMetaCodeOrga()
         throws ParameterNotFoundException {
      return (String) parametersSupport.find(
            ParameterType.JOURNALISATION_EVT_META_CODE_ORGA,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getJournalisationEvtMetaCodeRnd()
         throws ParameterNotFoundException {
      return (String) parametersSupport.find(
            ParameterType.JOURNALISATION_EVT_META_CODE_RND,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getJournalisationEvtMetaTitre()
         throws ParameterNotFoundException {
      return (String) parametersSupport.find(
            ParameterType.JOURNALISATION_EVT_META_TITRE,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Date getPurgeEvtDate() throws ParameterNotFoundException {
      return (Date) parametersSupport.find(ParameterType.PURGE_EVT_DATE,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Integer getPurgeEvtDuree() throws ParameterNotFoundException {
      return (Integer) parametersSupport.find(ParameterType.PURGE_EVT_DUREE,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Date getPurgeExploitDate() throws ParameterNotFoundException {
      return (Date) parametersSupport.find(ParameterType.PURGE_EXPLOIT_DATE,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Integer getPurgeExploitDuree()
         throws ParameterNotFoundException {
      return (Integer) parametersSupport.find(
            ParameterType.PURGE_EXPLOIT_DUREE, ParameterRowType.TRACABILITE)
            .getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Date getPurgeSecuDate() throws ParameterNotFoundException {
      return (Date) parametersSupport.find(ParameterType.PURGE_SECU_DATE,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Integer getPurgeSecuDuree() throws ParameterNotFoundException {
      return (Integer) parametersSupport.find(ParameterType.PURGE_SECU_DUREE,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Date getPurgeTechDate() throws ParameterNotFoundException {
      return (Date) parametersSupport.find(ParameterType.PURGE_TECH_DATE,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Integer getPurgeTechDuree() throws ParameterNotFoundException {
      return (Integer) parametersSupport.find(ParameterType.PURGE_TECH_DUREE,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Boolean isJournalisationEvtIsRunning()
         throws ParameterNotFoundException {
      return (Boolean) parametersSupport.find(
            ParameterType.JOURNALISATION_EVT_IS_RUNNING,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Boolean isPurgeEvtIsRunning() throws ParameterNotFoundException {
      return (Boolean) parametersSupport.find(
            ParameterType.PURGE_EVT_IS_RUNNING, ParameterRowType.TRACABILITE)
            .getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Boolean isPurgeExploitIsRunning()
         throws ParameterNotFoundException {
      return (Boolean) parametersSupport.find(
            ParameterType.PURGE_EXPLOIT_IS_RUNNING,
            ParameterRowType.TRACABILITE).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Boolean isPurgeSecuIsRunning()
         throws ParameterNotFoundException {
      return (Boolean) parametersSupport.find(
            ParameterType.PURGE_SECU_IS_RUNNING, ParameterRowType.TRACABILITE)
            .getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Boolean isPurgeTechIsRunning()
         throws ParameterNotFoundException {
      return (Boolean) parametersSupport.find(
            ParameterType.PURGE_TECH_IS_RUNNING, ParameterRowType.TRACABILITE)
            .getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setJournalisationEvtDate(Date date) {
      Date dateOk = DateUtils.truncate(date, Calendar.DATE);
      Parameter parameter = new Parameter(
            ParameterType.JOURNALISATION_EVT_DATE, dateOk);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setJournalisationEvtHashJournPrec(String hash) {
      Parameter parameter = new Parameter(
            ParameterType.JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT, hash);
      insertParameter(parameter, ParameterRowType.TRACABILITE);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setJournalisationEvtIdJournPrec(String identifiant) {
      Parameter parameter = new Parameter(
            ParameterType.JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT, identifiant);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setJournalisationEvtIsRunning(Boolean isRunning) {
      Parameter parameter = new Parameter(
            ParameterType.JOURNALISATION_EVT_IS_RUNNING, isRunning);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setJournalisationEvtMetaApplProd(String applProd) {
      Parameter parameter = new Parameter(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE,
            applProd);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setJournalisationEvtMetaApplTrait(String applTrait) {
      Parameter parameter = new Parameter(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_TRAITEMENT,
            applTrait);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setJournalisationEvtMetaCodeOrga(String codeOrga) {
      Parameter parameter = new Parameter(
            ParameterType.JOURNALISATION_EVT_META_CODE_ORGA, codeOrga);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setJournalisationEvtMetaCodeRnd(String codeRnd) {
      Parameter parameter = new Parameter(
            ParameterType.JOURNALISATION_EVT_META_CODE_RND, codeRnd);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setJournalisationEvtMetaTitre(String titre) {
      Parameter parameter = new Parameter(
            ParameterType.JOURNALISATION_EVT_META_TITRE, titre);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeEvtDate(Date date) {
      Date dateOk = DateUtils.truncate(date, Calendar.DATE);
      Parameter parameter = new Parameter(ParameterType.PURGE_EVT_DATE, dateOk);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeEvtDuree(Integer duree) {
      Parameter parameter = new Parameter(ParameterType.PURGE_EVT_DUREE, duree);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeEvtIsRunning(Boolean isRunning) {
      Parameter parameter = new Parameter(ParameterType.PURGE_EVT_IS_RUNNING,
            isRunning);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeExploitDate(Date date) {
      Date dateOk = DateUtils.truncate(date, Calendar.DATE);
      Parameter parameter = new Parameter(ParameterType.PURGE_EXPLOIT_DATE,
            dateOk);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeExploitDuree(Integer duree) {
      Parameter parameter = new Parameter(ParameterType.PURGE_EXPLOIT_DUREE,
            duree);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeExploitIsRunning(Boolean isRunning) {
      Parameter parameter = new Parameter(
            ParameterType.PURGE_EXPLOIT_IS_RUNNING, isRunning);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeSecuDate(Date date) {
      Date dateOk = DateUtils.truncate(date, Calendar.DATE);
      Parameter parameter = new Parameter(ParameterType.PURGE_SECU_DATE, dateOk);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeSecuDuree(Integer duree) {
      Parameter parameter = new Parameter(ParameterType.PURGE_SECU_DUREE, duree);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeSecuIsRunning(Boolean isRunning) {
      Parameter parameter = new Parameter(ParameterType.PURGE_SECU_IS_RUNNING,
            isRunning);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeTechDate(Date date) {
      Date dateOk = DateUtils.truncate(date, Calendar.DATE);
      Parameter parameter = new Parameter(ParameterType.PURGE_TECH_DATE, dateOk);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeTechDuree(Integer duree) {
      Parameter parameter = new Parameter(ParameterType.PURGE_TECH_DUREE, duree);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setPurgeTechIsRunning(Boolean isRunning) {
      Parameter parameter = new Parameter(ParameterType.PURGE_TECH_IS_RUNNING,
            isRunning);
      insertParameter(parameter, ParameterRowType.TRACABILITE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Date getVersionRndDateMaj() throws ParameterNotFoundException {
      return (Date) parametersSupport.find(ParameterType.VERSION_RND_DATE_MAJ,
            ParameterRowType.RND).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getVersionRndNumero() throws ParameterNotFoundException {
      return (String) parametersSupport.find(ParameterType.VERSION_RND_NUMERO,
            ParameterRowType.RND).getValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setVersionRndDateMaj(Date dateMajRnd) {
      Parameter parameter = new Parameter(ParameterType.VERSION_RND_DATE_MAJ,
            dateMajRnd);
      insertParameter(parameter, ParameterRowType.RND);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setVersionRndNumero(String numVersion) {
      Parameter parameter = new Parameter(ParameterType.VERSION_RND_NUMERO,
            numVersion);
      insertParameter(parameter, ParameterRowType.RND);
   }

   private void insertParameter(Parameter parameter, ParameterRowType rowType) {
      String trcPrefix = "insertParameter()";

      LOGGER.debug("{} - Début de la création du parametre {}", trcPrefix,
            parameter.getName().toString());

      if (parameter.getValue() == null) {
         LOGGER.debug("{} - Valeur à écrire : {}", trcPrefix, "null");
      } else {
         LOGGER.debug("{} - Valeur à écrire : {}", trcPrefix, parameter
               .getValue().toString());
      }

      String resourceName = PREFIXE_PARAMS + rowType.toString() + "/"
            + parameter.getName().toString();

      ZookeeperMutex mutex = ZookeeperUtils.createMutex(curator, resourceName);
      try {
         ZookeeperUtils.acquire(mutex, resourceName);

         parametersSupport.create(parameter, rowType, clockSupport
               .currentCLock());

         checkLock(mutex, parameter, rowType);

         LOGGER.debug("{} - Fin de la création du parametre {}", trcPrefix,
               parameter.getName().toString());

      } finally {
         mutex.release();
      }
   }

   private void checkLock(ZookeeperMutex mutex, Parameter parameter,
         ParameterRowType rowType) {
      if (!ZookeeperUtils.isLock(mutex)) {

         ParameterType code = parameter.getName();

         Parameter storedParam;
         try {
            storedParam = parametersSupport.find(code, rowType);
            if (!storedParam.getValue().equals(parameter.getValue())) {
               throw new ParameterRuntimeException("le parametre "
                     + code.toString() + " a déjà été créé");
            }
         } catch (Exception e) {
            throw new ParameterRuntimeException("le parametre "
                  + code.toString() + "n'a pas été créé", e);
         }

      }

   }

}
