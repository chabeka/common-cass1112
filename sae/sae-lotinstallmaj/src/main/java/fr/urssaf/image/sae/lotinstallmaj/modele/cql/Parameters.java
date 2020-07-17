package fr.urssaf.image.sae.lotinstallmaj.modele.cql;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Classe de mapping de la Table parameterscql
 */
@Table(name = "parameterscql")
public class Parameters {

   @PartitionKey
   @Column(name = "typeparameters")
   private String typeParameter;

   private String name;

   private Object value;
}
