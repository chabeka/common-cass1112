package fr.urssaf.image.sae.lotinstallmaj.exception;

/**
 * Exception lorsqu'une mise à jour doit être faite en dehors du lot-install
 */
public class MajLotUnknownDFCEVersion extends Exception {

   /**
    * @param arg0
    * @param arg1
    */
   public MajLotUnknownDFCEVersion(final String arg0, final Throwable arg1) {
      super(arg0, arg1);
   }

   /**
    * @param arg0
    */
   public MajLotUnknownDFCEVersion(final String arg0) {
      super(arg0);
   }

   /**
    * @param arg0
    */
   public MajLotUnknownDFCEVersion(final Throwable arg0) {
      super(arg0);
   }

   /**
    * 
    */
   public MajLotUnknownDFCEVersion(final int version) {
      super("La version " + version + " de la DFCE n'existe pas!!! "
            + "Veuillez vérifier qu'elle a bien été créée");
   }


}
