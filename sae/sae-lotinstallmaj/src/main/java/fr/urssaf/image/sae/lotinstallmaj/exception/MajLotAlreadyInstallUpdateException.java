package fr.urssaf.image.sae.lotinstallmaj.exception;

/**
 * Exception lorsqu'une mise à jour doit être faite en dehors du lot-install
 */
public class MajLotAlreadyInstallUpdateException extends Exception {

   /**
    * @param arg0
    * @param arg1
    */
   public MajLotAlreadyInstallUpdateException(final String arg0, final Throwable arg1) {
      super(arg0, arg1);
   }

   /**
    * @param arg0
    */
   public MajLotAlreadyInstallUpdateException(final String arg0) {
      super(arg0);
   }

   /**
    * @param arg0
    */
   public MajLotAlreadyInstallUpdateException(final Throwable arg0) {
      super(arg0);
   }

   /**
    * 
    */
   public MajLotAlreadyInstallUpdateException(final int version) {
      super("La base de données est déja en version : " + version);
   }


}
