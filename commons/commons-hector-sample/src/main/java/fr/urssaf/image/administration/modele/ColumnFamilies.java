package fr.urssaf.image.administration.modele;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import fr.urssaf.image.administration.modele.ColumnFamily;

@XStreamAlias("columnFamilies")
public class ColumnFamilies {
   @XStreamImplicit(itemFieldName = "columnFamily")
   private List<ColumnFamily> columnFamily;

   /**
    * @return the columnFamily
    */
   public List<ColumnFamily> getColumnFamily() {
      return columnFamily;
   }

   /**
    * @param columnFamily
    *           the columnFamily to set
    */
   public void setColumnFamily(List<ColumnFamily> columnFamily) {
      this.columnFamily = columnFamily;
   }

}
