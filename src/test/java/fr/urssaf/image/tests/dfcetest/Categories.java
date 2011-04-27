package fr.urssaf.image.tests.dfcetest;

/**
 * Enumération des 53 catégories de NCOTI
 *
 */
public enum Categories {
   TYPE_DOC("Type de document"),  // 0
   TYPE_DOC_LIBELLE("Libelle type de document"),  // 1
   NUM_CPT("No de compte"),  // 2
   SIRET("No SIRET"),  // 3
   NNI("NNI Salarie"),  // 4
   PERIODE("Periode"),  // 5
   CODE_ORGA_CPT("Code organisme du compte"),  // 6
   CODE_UR("Code URSSAF"),  // 7
   NUM_PERS("No Personne"),  // 8
   NUM_AFFAIRE("No Affaire"),  // 9
   DENOM_CPT("Denomination du compte"),  // 10
   NUM_PIECE("No Piece"),  // 11
   APPLI_SOURCE("Application Source"),  // 12
   NUM_LOT("No Lot"),  // 13
   CODE_COMMUNE("Code Commune"),  // 14
   CATEGORIE("Categorie"),  // 15
   NUM_GROUPE("No Groupe"),  // 16
   DATE_ORIGINE("Date d origine"),  // 17
   DATE_TRAITEMENT("Date Traitement"),  // 18
   DATE_EFFET("Date d Effet"),  // 19
   JOURNEE_COMPTABLE("Journee Comptable"),  // 20
   NUM_STRUCT_CTX("No Structure CNTX"),  // 21
   NUM_PARTENAIRE("No Partenaire"),  // 22
   NUM_INTER_CTX("No Intervention CTRL"),  // 23
   NUM_ALLOCATAIRE("No Allocataire"),  // 24
   CODE_USER_INDEX("Code utilisateur indexation"),  // 25
   CODE_USER_STOCK("Code utilisateur stockage"),  // 26
   NUM_DOSSIER("No Dossier"),  // 27
   DATE_VERIF("Date verification"),  // 28
   AGENT_VERIF("Agent verificateur"),  // 29
   CODE_VERIF("Code verificateur"),  // 30
   DATE_CLASSEMENT_GED("Date de classement GED"),  // 31
   NUM_CPT_INT("No de compte Interne"),  // 32
   NUM_CPT_ANCIEN("Ancien numero de compte"),  // 33
   TYPE_DOC_ARCHIVAGE("Type de document d archivage"),  // 34
   NUM_ID_ARCHIVAGE("No Id Archivage"),  // 35
   CERTIFICAT("Certificat"),  // 36
   CODE_ORGA_MUTUALISE("Code organisme mutualise"),  // 37
   NOM_NAISS_SALARIE("Nom naissance salarie"),  // 38
   NOM_MARITAL_SALARIE("Nom marital salarie"),  // 39
   PRENOM_SALARIE("Prenom salarie"),  // 40
   CODE_INT_SALARIE("Code interne salarie"),  // 41
   NATURE_EMPLOI("Nature de l emploi"),  // 42
   DATE_NAISS_SALARIE("Date naissance salarie"),  // 43
   NUM_COMPOSTAGE("No de Compostage"),  // 44
   DATE_VERSEMENT("Date de versement"),  // 45
   NUM_VOLET_SOCIAL("No Volet Social"),  // 46
   RFP_FONCTION("Fonction RFP"),  // 47
   RFP_ACTIVITE("Activite RFP"),  // 48
   NOM_PERS("Nom de personne"),  // 49
   ID_COMPLEMENTAIRE("Id Complementaire"),  // 50
   NNI_EMPLOYEUR("NNI employeur"),  // 51
   NUM_RECOURS("Numero de Recours");  // 52

   /**
    * Nom de la catégorie
    */
   private String name;

   Categories(String catName) {
      this.name = catName;
   }
   
   public String toString() {
      return this.name;
   }

   /**
    * @return Nom des catégories de la base NCOTI.
    */
   public static String[] getNames() {
      return new String[] { "Type de document", "Libelle type de document",
         "No de compte", "No SIRET", "NNI Salarie", "Periode", "Code organisme du compte",
         "Code URSSAF", "No Personne", "No Affaire", "Denomination du compte", "No Piece",
         "Application Source", "No Lot", "Code Commune", "Categorie", "No Groupe",
         "Date d origine", "Date Traitement", "Date d Effet", "Journee Comptable",
         "No Structure CNTX", "No Partenaire", "No Intervention CTRL", "No Allocataire",
         "Code utilisateur indexation", "Code utilisateur stockage", "No Dossier",
         "Date verification", "Agent verificateur", "Code verificateur", "Date de classement GED",
         "No de compte Interne", "Ancien numero de compte", "Type de document d archivage",
         "No Id Archivage", "Certificat", "Code organisme mutualise", "Nom naissance salarie",
         "Nom marital salarie", "Prenom salarie", "Code interne salarie", "Nature de l emploi",
         "Date naissance salarie", "No de Compostage", "Date de versement", "No Volet Social",
         "Fonction RFP", "Activite RFP", "Nom de personne", "Id Complementaire", "NNI employeur",
         "Numero de Recours" };  } 
}