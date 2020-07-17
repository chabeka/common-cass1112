package fr.urssaf.image.sae.lotinstallmaj.service.cql.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.bo.ParameterRowType;
import fr.urssaf.image.sae.commons.bo.ParameterType;
import fr.urssaf.image.sae.commons.bo.cql.ParameterCql;
import fr.urssaf.image.sae.commons.dao.cql.IParametersDaoCql;
import fr.urssaf.image.sae.lotinstallmaj.service.InsertionDonnees;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.DateUtils;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.DroitsServiceUtilsCQL;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.ReferentielServiceUtilsCQL;

@Component
public class InsertionDonneesCQL implements InsertionDonnees {

   private static final Logger LOG = LoggerFactory.getLogger(InsertionDonneesCQL.class);

   @Autowired
   private IParametersDaoCql parametersDaoImpl;

   @Autowired
   private ReferentielServiceUtilsCQL referentielServiceUtilsCQL;

   @Autowired
   private DroitsServiceUtilsCQL droitsServiceUtilsCQL;

   private static final String LIBELLE_DEBUT_TRAITEMENT_REF_EVT = "Mise à jour du référentiel des événements";

   /**
    * Insertion de données de droits
    */
   @Override
   public final void addDroits() {
      droitsServiceUtilsCQL.addDroits();
   }

   @Override
   public final void addRndParameters() {
      LOG.info("Création des paramètres de maj du RND en mode CQL");

      addParameter(ParameterRowType.RND, ParameterType.VERSION_RND_NUMERO, "");
      addParameter(ParameterRowType.RND, ParameterType.VERSION_RND_DATE_MAJ, DateUtils.getRndDate());
   }

   @Override
   public final void addTracabiliteParameters() {

      LOG.info("Création des paramètres de traçabilité CQL");

      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_TECH_DUREE, Integer.valueOf(10));
      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_SECU_DUREE, Integer.valueOf(10));
      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_EXPLOIT_DUREE, Integer.valueOf(10));
      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_EVT_DUREE, Integer.valueOf(10));
      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_TECH_IS_RUNNING, Boolean.FALSE);
      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_SECU_IS_RUNNING, Boolean.FALSE);
      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_EXPLOIT_IS_RUNNING, Boolean.FALSE);
      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_EVT_IS_RUNNING, Boolean.FALSE);
      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_TECH_DATE, DateUtils.getTracabiliteDerniereDateTraitee());
      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_SECU_DATE, DateUtils.getTracabiliteDerniereDateTraitee());
      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_EXPLOIT_DATE, DateUtils.getTracabiliteDerniereDateTraitee());

      addParameter(ParameterRowType.TRACABILITE, ParameterType.PURGE_EVT_DATE, DateUtils.getTracabiliteDerniereDateTraitee());
      addParameter(ParameterRowType.TRACABILITE, ParameterType.JOURNALISATION_EVT_DATE, DateUtils.getTracabiliteDerniereDateTraitee());
      addParameter(ParameterRowType.TRACABILITE, ParameterType.JOURNALISATION_EVT_IS_RUNNING, Boolean.FALSE);

      addParameter(ParameterRowType.TRACABILITE,
                   ParameterType.JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT,
            "00000000-0000-0000-0000-000000000000");
      addParameter(ParameterRowType.TRACABILITE,
                   ParameterType.JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT,
            "0000000000000000000000000000000000000000");
      addParameter(ParameterRowType.TRACABILITE,
                   ParameterType.JOURNALISATION_EVT_META_TITRE,
            "Journal des événements SAE");
      addParameter(ParameterRowType.TRACABILITE,
                   ParameterType.JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE,
            "SAE");
      addParameter(ParameterRowType.TRACABILITE,
                   ParameterType.JOURNALISATION_EVT_META_APPLICATION_TRAITEMENT,
            "SAE");
      addParameter(ParameterRowType.TRACABILITE,
                   ParameterType.JOURNALISATION_EVT_META_CODE_ORGA,
            "UR750");
      addParameter(ParameterRowType.TRACABILITE,
                   ParameterType.JOURNALISATION_EVT_META_CODE_RND,
            "7.7.8.8.1");

   }

   @Override
   public final void addCorbeilleParameters() {

      LOG.info("Création des paramètres de purge de la corbeille CQL");
      addParameter(ParameterRowType.CORBEILLE, ParameterType.PURGE_CORBEILLE_DUREE, Integer.valueOf(20));
      addParameter(ParameterRowType.CORBEILLE, ParameterType.PURGE_CORBEILLE_IS_RUNNING, Boolean.FALSE);
      addParameter(ParameterRowType.CORBEILLE, ParameterType.PURGE_CORBEILLE_DATE_LANCEMENT, DateUtils.getCorbeilleDerniereDateTraitee());
      addParameter(ParameterRowType.CORBEILLE, ParameterType.PURGE_CORBEILLE_DATE_SUCCES, DateUtils.getCorbeilleDerniereDateTraitee());
      addParameter(ParameterRowType.CORBEILLE, ParameterType.PURGE_CORBEILLE_DATE_DEBUT_PURGE, DateUtils.getCorbeilleDerniereDateTraitee());

   }

   /**
    * Ajout d'un nouveau paramètre
    * 
    * @param version
    */
   private void addParameter(final ParameterRowType parameterRowType, final ParameterType parameterType, final Object value) {
      final ParameterCql parameterCql = new ParameterCql();
      parameterCql.setName(parameterType);
      parameterCql.setTypeParameters(parameterRowType);
      parameterCql.setValue(value);
      parametersDaoImpl.saveWithMapper(parameterCql);
   }

   @Override
   public final void addReferentielEvenementV3() {
      referentielServiceUtilsCQL.addReferentielEvenementV3();
   }

   @Override
   public final void addReferentielEvenementV1() {
      referentielServiceUtilsCQL.addReferentielEvenementV1();
   }

   @Override
   public final void addReferentielEvenementV2() {
      referentielServiceUtilsCQL.addReferentielEvenementV2();
   }

   @Override
   public final void addReferentielFormat() {
      referentielServiceUtilsCQL.addReferentielFormat();
   }

   @Override
   public final void addFormatControleProfil() {
      referentielServiceUtilsCQL.addFormatControleProfil();
   }

   @Override
   public final void addReferentielEvenementV4() {
      referentielServiceUtilsCQL.addReferentielEvenementV4();
   }

   @Override
   public final void addReferentielEvenementV5() {
      referentielServiceUtilsCQL.addReferentielEvenementV5();
   }

   /**
    * Methode permettant d'ajouter le referentiel de format V2.
    */
   @Override
   public final void addReferentielFormatV2() {
      referentielServiceUtilsCQL.addReferentielFormatV2();
   }

   @Override
   public final void addReferentielEvenementV6() {
      referentielServiceUtilsCQL.addReferentielEvenementV6();
   }

   @Override
   public final void addReferentielEvenementV7() {
      referentielServiceUtilsCQL.addReferentielEvenementV7();
   }

   @Override
   public final void addActionUnitaireNote() {
      droitsServiceUtilsCQL.addActionUnitaireNote();
   }

   @Override
   public final void addActionUnitaireRechercheParIterateur() {
      droitsServiceUtilsCQL.addActionUnitaireRechercheParIterateur();
   }

   @Override
   public final void modifyActionUnitaireAjoutNote() {
      droitsServiceUtilsCQL.modifyActionUnitaireAjoutNote();
   }

   @Override
   public final void modifyReferentielFormatFmt354() {
      referentielServiceUtilsCQL.modifyReferentielFormatFmt354();
   }

   @Override
   public final void addActionUnitaireNote2() {
      droitsServiceUtilsCQL.addActionUnitaireNote2();
   }

   @Override
   public final void addReferentielFormatV3() {
      referentielServiceUtilsCQL.addReferentielFormatV3();
   }

   @Override
   public final void addReferentielEvenementV8() {
      referentielServiceUtilsCQL.addReferentielEvenementV8();
   }

   @Override
   public final void addActionUnitaireAjoutDocAttache() {
      droitsServiceUtilsCQL.addActionUnitaireAjoutDocAttache();
   }

   @Override
   public final void addReferentielFormatV4() {
      referentielServiceUtilsCQL.addReferentielFormatV4();
   }

   @Override
   public final void addReferentielEvenementV9() {
      referentielServiceUtilsCQL.addReferentielEvenementV9();
   }

   @Override
   public final void addActionUnitaireTraitementMasse() {
      droitsServiceUtilsCQL.addActionUnitaireTraitementMasse();
   }

   @Override
   public final void addReferentielEvenementV10() {
      referentielServiceUtilsCQL.addReferentielEvenementV10();

   }

   @Override
   public final void addReferentielEvenementV11() {
      referentielServiceUtilsCQL.addReferentielEvenementV11();
   }

   @Override
   public final void addReferentielFormatV5() {
      referentielServiceUtilsCQL.addReferentielFormatV5();
   }

   @Override
   public final void addActionUnitaireSuppressionModification() {
      droitsServiceUtilsCQL.addActionUnitaireSuppressionModification();
   }

   @Override
   public final void addActionUnitaireCopie() {
      droitsServiceUtilsCQL.addActionUnitaireCopie();
   }

   @Override
   public final void addReferentielFormatV6() {
      referentielServiceUtilsCQL.addReferentielFormatV6();
   }

   @Override
   public final void modifyReferentielFormatFmt353() {
      referentielServiceUtilsCQL.modifyReferentielFormatFmt353();
   }

   @Override
   public final void modifyReferentielFormatFmt44() {
      referentielServiceUtilsCQL.modifyReferentielFormatFmt44();
   }

   @Override
   public final void addReferentielFormatV6Bis() {
      referentielServiceUtilsCQL.addReferentielFormatV6Bis();
   }

   @Override
   public final void modifyReferentielFormatCrtl1() {
      referentielServiceUtilsCQL.modifyReferentielFormatCrtl1();
   }

   @Override
   public final void addColumnAutoriseGEDReferentielFormat() {
      referentielServiceUtilsCQL.addColumnAutoriseGEDReferentielFormat();
   }

   @Override
   public final void addDroitsGed() {
      droitsServiceUtilsCQL.addDroitsGed();
   }

   @Override
   public void addActionUnitaireTraitementMasse2() {
      LOG.info("Mise à jour des actions unitaires");
      droitsServiceUtilsCQL.addActionUnitaireTraitementMasseBis();
   }

   @Override
   public void addActionUnitaireRepriseMasse() {
      LOG.info("Mise à jour des actions unitaires");
      droitsServiceUtilsCQL.addActionUnitaireRepriseMasse();
   }

   @Override
   public void addReferentielEvenementV12() {
      LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);
      referentielServiceUtilsCQL.addReferentielEvenementV12();
   }

   @Override
   public void addReferentielFormatV7() {
      referentielServiceUtilsCQL.addReferentielFormatV7();
   }

   @Override
   public void addReferentielFormatV8() {
      referentielServiceUtilsCQL.addReferentielFormatV8();
   }

   @Override
   public void addReferentielEvenementV13() {
      LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);
      referentielServiceUtilsCQL.addReferentielEvenementV13();
   }

   @Override
   public void addReferentielEvenementV14() {
      LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);
      referentielServiceUtilsCQL.addReferentielEvenementV14();
   }

   @Override
   public void addReferentielEvenementV15() {
      LOG.info(LIBELLE_DEBUT_TRAITEMENT_REF_EVT);
      referentielServiceUtilsCQL.addReferentielEvenementV15();
   }

   public void majPrmdExpReguliere160600() {
      droitsServiceUtilsCQL.majPrmdExpReguliere160600();
   }

   public void majPagmCsV2AjoutActionReprise170900() {
      droitsServiceUtilsCQL.majPagmCsV2AjoutActionReprise170900();
   }

   public void majPagmCsPourPKINationale180300() {
      droitsServiceUtilsCQL.majPagmCsPourPKINationale180300();
   }

}
