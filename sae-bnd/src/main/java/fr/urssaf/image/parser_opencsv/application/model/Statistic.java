package fr.urssaf.image.parser_opencsv.application.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Statistiques sur le parsing du fichier CSV en Sommaire.xml, ces statistiques contiennent
 * Nombre de documents initial
 * Nombre de documents corectement ajoutés au sommaire.xml
 * Nombre de documents non ajoutés au sommaire.xml
 */
@XmlRootElement
public class Statistic {

   private int initialDocumentsCount;

   private int addedDocumentsCount;

   private int nonAddedDocumentsCount;

   public Statistic() {
      super();
   }

   /**
    * @param initialDocumentsCount
    * @param addedDocumentsCount
    * @param nonAddedDocumentsCount
    */
   public Statistic(final int initialDocumentsCount, final int addedDocumentsCount, final int nonAddedDocumentsCount) {
      this();
      this.initialDocumentsCount = initialDocumentsCount;
      this.addedDocumentsCount = addedDocumentsCount;
      this.nonAddedDocumentsCount = nonAddedDocumentsCount;
   }

   /**
    * @return the initialDocumentsCount
    */
   public int getInitialDocumentsCount() {
      return initialDocumentsCount;
   }

   /**
    * @param initialDocumentsCount
    *           the initialDocumentsCount to set
    */
   @XmlAttribute
   public void setInitialDocumentsCount(final int initialDocumentsCount) {
      this.initialDocumentsCount = initialDocumentsCount;
   }

   /**
    * @return the addedDocumentsCount
    */
   public int getAddedDocumentsCount() {
      return addedDocumentsCount;
   }

   /**
    * @param addedDocumentsCount
    *           the addedDocumentsCount to set
    */
   @XmlAttribute
   public void setAddedDocumentsCount(final int addedDocumentsCount) {
      this.addedDocumentsCount = addedDocumentsCount;
   }

   /**
    * @return the nonAddedDocumentsCount
    */
   public int getNonAddedDocumentsCount() {
      return nonAddedDocumentsCount;
   }

   /**
    * @param nonAddedDocumentsCount
    *           the nonAddedDocumentsCount to set
    */
   @XmlAttribute
   public void setNonAddedDocumentsCount(final int nonAddedDocumentsCount) {
      this.nonAddedDocumentsCount = nonAddedDocumentsCount;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return "Statistic [initialDocumentsCount=" + initialDocumentsCount + ", addedDocumentsCount=" + addedDocumentsCount + ", nonAddedDocumentsCount="
            + nonAddedDocumentsCount + "]";
   }

}
