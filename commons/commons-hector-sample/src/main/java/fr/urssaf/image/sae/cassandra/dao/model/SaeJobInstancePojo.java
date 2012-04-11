package fr.urssaf.image.sae.cassandra.dao.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.Table;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hom.annotations.AnonymousPropertyHandling;
import me.prettyprint.hom.annotations.Id;

@Entity
@Table(name = "JobInstance")
@AnonymousPropertyHandling(serializer = StringSerializer.class, type = String.class, adder = "addAnonymousProp", getter = "getAnonymousProps")
public class SaeJobInstancePojo {
   @Id
   private String clef;

   private Map<String, String> anonymousProps = new HashMap<String, String>();

   public void addAnonymousProp(String name, String value) {
      anonymousProps.put(name, value);
   }

   public Collection<Entry<String, String>> getAnonymousProps() {
      return anonymousProps.entrySet();
   }

   public String getAnonymousProp(UUID name) {
      return anonymousProps.get(name);
   }

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

}
