package fr.urssaf.image.helper;

import me.prettyprint.hector.api.ddl.ComparatorType;

/**
 * @author rhofir.
 */
public enum ComparatorTypes {
   // Code RND
   ASCIITYPE("AsciiType", ComparatorType.ASCIITYPE),
   // Code fonction
   BYTESTYPE("BytesType", ComparatorType.BYTESTYPE),
   // CodeActivite
   INTEGERTYPE("IntegerType", ComparatorType.INTEGERTYPE),
   // DateArchivage
   LEXICALUUIDTYPE("LexicalUUIDType", ComparatorType.LEXICALUUIDTYPE),
   // DateDebutConservation
   LOCALBYPARTITIONERTYPE("LocalByPartionerType",
         ComparatorType.LOCALBYPARTITIONERTYPE),
   // DateFinConservation
   LONGTYPE("LongType", ComparatorType.LONGTYPE),
   // ContratDeService
   TIMEUUIDTYPE("TimeUUIDType", ComparatorType.TIMEUUIDTYPE),
   // pas de valeur
   NOVALUE("", ComparatorType.UTF8TYPE),
   // Version RND
   UTF8TYPE("UTF8Type", ComparatorType.UTF8TYPE),
   // Hash
   COMPOSITETYPE("CompositeType", ComparatorType.COMPOSITETYPE),
   // TypeHash
   DYNAMICCOMPOSITETYPE("DynamicCompositeType",
         ComparatorType.DYNAMICCOMPOSITETYPE),
   // APPLICATIONPRODUCTRICE
   UUIDTYPE("UUIDType", ComparatorType.UUIDTYPE),
   // NBPAGES
   COUNTERTYPE("CounterColumnType", ComparatorType.COUNTERTYPE);

   // Le code long de la métadonnée.
   private String CodeComparatorType;
   private ComparatorType comparatorType;

   /**
    * 
    * @param shortCode
    *           . Le code court
    */
   ComparatorTypes(final String codeComparatorType,
         final ComparatorType comparatorType) {
      this.CodeComparatorType = codeComparatorType;
      this.comparatorType = comparatorType;
   }

   /**
    * @param codeComparatorType
    *           : Le code long de la métadonnée.
    */
   public void setCodeComparatorType(final String codeComparatorType) {
      this.CodeComparatorType = codeComparatorType;
   }

   /**
    * @return : Le code long de la métadonnée.
    */
   public String getCodeComparatorType() {
      return CodeComparatorType;
   }

   /**
    * @return the comparatorType
    */
   public ComparatorType getComparatorType() {
      return comparatorType;
   }

   /**
    * @param comparatorType
    *           the comparatorType to set
    */
   public void setComparatorType(ComparatorType comparatorType) {
      this.comparatorType = comparatorType;
   }

}
