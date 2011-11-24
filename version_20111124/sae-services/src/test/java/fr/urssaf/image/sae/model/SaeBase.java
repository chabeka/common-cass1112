package fr.urssaf.image.sae.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe permettant de désérialiser les données de la base documentaire.<BR />
 * Elle contient les attributs :
 * <ul>
 * <li>
 * baseId : Le nom de la base</li>
 * <li>docId : l'id du document</li>
 * <li>typeDoc : Le type de document</li>
 * <li>
 * filePath : Le chemin du fichier xml</li>
 * <li>saeCategories : Les catégories de la base.</li>
 * </ul>
 * 
 * @author akenore, rhofir.
 * 
 */
@XStreamAlias("base")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class SaeBase {
   private String baseId;
   private String docId;
   private String typeDoc;
   private String filePath;
   @XStreamAlias("categories")
   private SaeCategories saeCategories;

   /**
    * @return Les categories
    */
   public final SaeCategories getSaeCategories() {
      return saeCategories;
   }

   /**
    * @param seaCategories
    *           : Les categories
    */
   public final void setSaeCategories(final SaeCategories seaCategories) {
      this.saeCategories = seaCategories;
   }

   /**
    * @param baseId
    *           : Le libellé de la base
    */
   public final void setBaseId(final String baseId) {
      this.baseId = baseId;
   }

   /**
    * @return Le libellé de la base
    */
   public final String getBaseId() {
      return baseId;
   }

   /** {@inheritDoc} */
   @Override
   public final String toString() {
      final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
      toStringBuilder.append("BaseId", baseId);
      toStringBuilder.append("docId", docId);
      if (saeCategories != null) {
         toStringBuilder.append("saeCategories", saeCategories.toString());
      }
      return toStringBuilder.toString();
   }

   /**
    * @param docId
    *           : L'identifiant du document
    */
   public final void setDocId(final String docId) {
      this.docId = docId;
   }

   /**
    * @return L'identifiant du document
    */
   public final String getDocId() {
      return docId;
   }

   /**
    * @param typeDoc
    *           : Le type de document
    */
   public final void setTypeDoc(final String typeDoc) {
      this.typeDoc = typeDoc;
   }

   /**
    * @return Le type de document
    */
   public final String getTypeDoc() {
      return typeDoc;
   }

   /**
    * @param filePath
    *           : Le chemin du fichier
    */
   public final void setFilePath(final String filePath) {
      this.filePath = filePath;
   }

   /**
    * @return Le chemin du fichier
    */
   public final String getFilePath() {
      return filePath;
   }

}
