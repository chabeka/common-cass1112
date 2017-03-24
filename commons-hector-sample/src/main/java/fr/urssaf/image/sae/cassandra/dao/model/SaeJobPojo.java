package fr.urssaf.image.sae.cassandra.dao.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.Column;
import me.prettyprint.hom.annotations.Id;

@Entity
@Table(name = "SaeJobs")
public class SaeJobPojo {
   @Id
   private String clef;
   @Column(name="idJob")
   private UUID clefColonne;
   @Column(name="values")
   private String values;

   /**
    * @return the clef
    */
   public String getClef() {
      return clef;
   }

   /**
    * @param clef
    *           the clef to set
    */
   public void setClef(String clef) {
      this.clef = clef;
   }

   /**
    * @return the clefColonne
    */
   public UUID getClefColonne() {
      return clefColonne;
   }

   /**
    * @param clefColonne
    *           the clefColonne to set
    */
   public void setClefColonne(UUID clefColonne) {
      this.clefColonne = clefColonne;
   }

   /**
    * @return the values
    */
   public String getValues() {
      return values;
   }

   /**
    * @param values
    *           the values to set
    */
   public void setValues(String values) {
      this.values = values;
   }

}
