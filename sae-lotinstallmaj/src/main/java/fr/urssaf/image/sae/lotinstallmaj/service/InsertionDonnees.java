package fr.urssaf.image.sae.lotinstallmaj.service;

/**
 * Insertion de nouvelles données dans les tables
 */
public interface InsertionDonnees {

   /**
    * Insertion de données de droits
    */
   public void addDroits();

   /**
    * Ajoute les paramètres nécéssaires à la maj du RND
    */
   public void addRndParameters();

   /**
    * Ajoute les paramètres nécessaires à la traçabilité SAE
    */
   public void addTracabiliteParameters();

   /**
    * Ajoute les paramètres nécessaires à la purge de la corbeille
    */
   public void addCorbeilleParameters();

   /**
    * Methode permettant de mettre à jour le referentiel Version 3.
    */
   public void addReferentielEvenementV3();

   /**
    * Methode permettant de mettre à jour le referentiel Version 1.
    */
   public void addReferentielEvenementV1();

   /**
    * Methode permettant de mettre à jour le referentiel Version 2.
    */
   public void addReferentielEvenementV2();

   /**
    * Methode permettant d'ajouter le referentiel des formats.
    */
   public void addReferentielFormat();

   /**
    * Methode permettant d'ajouter le format control profil.
    */
   public void addFormatControleProfil();

   /**
    * Methode permettant de mettre à jour le referentiel Version 4.
    */
   public void addReferentielEvenementV4();

   /**
    * Methode permettant de mettre à jour le referentiel Version 5.
    */
   public void addReferentielEvenementV5();

   /**
    * Methode permettant d'ajouter le referentiel de format V2.
    */
   public void addReferentielFormatV2();

   /**
    * Methode permettant d'ajouter le referentiel d'evenement V6.
    */
   public void addReferentielEvenementV6();

   /**
    * Methode permettant d'ajouter le referentiel d'evenement V7.
    */
   public void addReferentielEvenementV7();

   /**
    * Methode permettant d'ajouter l'action unitaire Note.
    */
   public void addActionUnitaireNote();

   /**
    * Methode permettant d'ajouter l'action unitaire pour la recherche par
    * itérateur.
    */
   public void addActionUnitaireRechercheParIterateur();

   /**
    * Methode permettant d'ajouter l'action unitaire pour l'ajout de note.
    */
   public void modifyActionUnitaireAjoutNote();

   /**
    * Methode permettant de modifier le référentiel pour le format fmt 354.
    */
   public void modifyReferentielFormatFmt354();

   /**
    * Methode permettant d'ajouter une action unitaire pour les Note.
    */
   public void addActionUnitaireNote2();

   /**
    * Methode permettant d'ajouter la version 3 des référentiels de format.
    */
   public void addReferentielFormatV3();

   /**
    * Methode permettant d'ajouter la version 8 des référentiels d'événement.
    */
   public void addReferentielEvenementV8();

   /**
    * Methode permettant d'ajouter une action unitaire pour les documents
    * attachés.
    */
   public void addActionUnitaireAjoutDocAttache();

   /**
    * Methode permettant d'ajouter la version 4 des référentiels de format.
    */
   public void addReferentielFormatV4();

   /**
    * Methode permettant d'ajouter la version 9 des référentiels d'événement.
    */
   public void addReferentielEvenementV9();

   /**
    * Methode permettant d'ajouter une action unitaire pour les traitements de
    * masse.
    */
   public void addActionUnitaireTraitementMasse();

   /**
    * Methode permettant d'ajouter la version 10 des référentiels d'événement.
    */
   public void addReferentielEvenementV10();

   /**
    * Methode permettant d'ajouter la version 11 des référentiels d'événement.
    */
   public void addReferentielEvenementV11();

   /**
    * Methode permettant d'ajouter la version 5 des référentiels des formats.
    */
   public void addReferentielFormatV5();

   /**
    * Methode permettant d'ajouter une action unitaire pour la suppression et la
    * modification.
    */
   public void addActionUnitaireSuppressionModification();

   /**
    * Methode permettant d'ajouter une action unitaire pour la copie.
    */
   public void addActionUnitaireCopie();

   /**
    * Methode permettant d'ajouter la version 6 des référentiels des formats.
    */
   public void addReferentielFormatV6();

   /**
    * Methode permettant de modifier le format fmt/353.
    */
   public void modifyReferentielFormatFmt353();

   /**
    * Methode permettant de modifier le format fmt/44.
    */
   public void modifyReferentielFormatFmt44();

   /**
    * Methode permettant d'ajouter la version 6 bis des référentiels des
    * formats.
    */
   public void addReferentielFormatV6Bis();

   /**
    * Methode permettant de
    */
   public void modifyReferentielFormatCrtl1();

   /**
    * Methode permettant d'ajouter le colonne autorisé en GED pour le
    * référentiel des formats.
    */
   public void addColumnAutoriseGEDReferentielFormat();

   /**
    * Methode permettant d'ajouter les droits GED.
    */
   public void addDroitsGed();

   public void addActionUnitaireTraitementMasse2();

   public void addActionUnitaireRepriseMasse();

   /**
    * Référentiel des événements en V12 Ajout des évenements :
    * <li>
    * WS_MODIFICATION_MASSE|KO</li>
    * <li>MODIFICATION_MASSE|KO</li>
    */
   public void addReferentielEvenementV12();

   /**
    * Methode permettant de
    */
   public void addReferentielFormatV7();

   /**
    * Référentiel des événements en V13 Ajout des évenements :
    * <li>
    * WS_REPRISE_MASSE|KO</li>
    * <li>REPRISE_MASSE|KO</li>
    */
   public void addReferentielEvenementV13();

   /**
    * Référentiel des événements en V14 Ajout des évenements :
    * <li>
    * WS_COPIE_MASSE|KO</li>
    */
   public void addReferentielEvenementV14();

   /**
    * 
    */
   void addReferentielEvenementV15();

   /**
    * 
    */
   void addReferentielFormatV8();

}
