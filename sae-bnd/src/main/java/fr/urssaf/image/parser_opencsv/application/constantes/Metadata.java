package fr.urssaf.image.parser_opencsv.application.constantes;

public class Metadata {

   public static final String CODE_ORGA_GESTIONNAIRE = "CodeOrganismeGestionnaire";

   public static final String FORM_FICHIER = "FormatFichier";

   public static final String CODE_ORGA_PROPRIETAIRE = "CodeOrganismeProprietaire";

   public static final String[] CSV_HEADERS = new String[] {"nir", "nom", "nomMarital", "prenom", "dateNaissance", "numTi",
                                                            "raisonSociale", "enseigne", "matriculeRet", "riba", "siren", 
                                                            "situation", "caisse", "UID", "codeObjet", "nomObjet", "codeClasse", 
                                                            "codeDomaine", "nature", "numeroLot", "name", "dateCreation",
                                                            "URL", "source", "mime", "hash"
   };

   private Metadata() {
      throw new RuntimeException("Cette ne peut être instanciée");
   }
}
