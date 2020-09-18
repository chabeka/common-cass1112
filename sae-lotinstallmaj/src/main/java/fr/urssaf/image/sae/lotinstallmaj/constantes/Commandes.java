package fr.urssaf.image.sae.lotinstallmaj.constantes;

import java.util.ArrayList;
import java.util.List;

public enum Commandes {
   INFO("--info"),
   CREATE("--create"),
   CREATE_CQL("--createcql"),
   UPDATE("--update"),
   REDO("--redo", true),
   DETAILS("--details", true),
   UPDATE_TO("--updateToVersion", true),
   VERIFY("--verify", true),
   CHANGE_VERSION_TO("--changeToVersion", true),
   RATTRAPAGE_CQL("--rattrapagecql");


   private final String name;

   private boolean hasSecondParam;

   private static List<String> listeCommandes = new ArrayList<>();

   private static List<String> listeCommandesWithParam = new ArrayList<>();

   private Commandes(final String cmdName) {
      name = cmdName;
      hasSecondParam = false;
   }

   private Commandes(final String cmdName, final boolean hasSecondParam) {
      this(cmdName);
      this.hasSecondParam = hasSecondParam;
   }

   public String getName() {
      return name;
   }

   public boolean hasSecondParam() {
      return hasSecondParam;
   }

   static {
      for (final Commandes commande : Commandes.values()) {
         final String cmdName = commande.getName();
         listeCommandes.add(cmdName);
         if (commande.hasSecondParam()) {
            listeCommandesWithParam.add(cmdName);
         }
      }
   }

   /**
    * Retourne la liste des commandes possible
    * 
    * @return
    */
   public static List<String> getAllCommandes() {

      return listeCommandes;
   }

   /**
    * Retourne la liste des commandes qui attendent un paramètre possible
    * 
    * @return
    */
   public static List<String> getAllCommandesWithParam() {

      return listeCommandesWithParam;
   }

}