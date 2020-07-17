package fr.urssaf.image.sae.lotinstallmaj.modele;

/**
 * Représente les informations d'un Lot :
 * nom du lot
 * la version du lot
 * le descriptif du lot
 */
public class InfoLot {

   private int version;

   private String nomLot;

   private String descriptif;

   private boolean isManuel;

   /**
    * 
    */
   public InfoLot() {
      super();
   }

   /**
    * @param version
    * @param nomLot
    */
   public InfoLot(final int version, final String nomLot) {
      this();
      this.version = version;
      this.nomLot = nomLot;
   }

   /**
    * @param version
    * @param nomLot
    * @param descriptif
    */
   public InfoLot(final int version, final String nomLot, final String descriptif) {
      this(version, nomLot);
      this.descriptif = descriptif;
   }

   /**
    * @return the version
    */
   public int getVersion() {
      return version;
   }

   /**
    * @param version
    *           the version to set
    */
   public void setVersion(final int version) {
      this.version = version;
   }

   /**
    * @return the nomLot
    */
   public String getNomLot() {
      return nomLot;
   }

   /**
    * @param nomLot
    *           the nomLot to set
    */
   public void setNomLot(final String nomLot) {
      this.nomLot = nomLot;
   }

   /**
    * @return the descriptif
    */
   public String getDescriptif() {
      return descriptif;
   }

   /**
    * @param descriptif
    *           the descriptif to set
    */
   public void setDescriptif(final String descriptif) {
      this.descriptif = descriptif;
   }

   /**
    * @return the isManuel
    */
   public boolean isManuel() {
      return isManuel;
   }

   /**
    * @param isManuel the isManuel to set
    */
   public void setManuel(boolean isManuel) {
      this.isManuel = isManuel;
   }

}
