package fr.urssaf.hectotest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.persistence.Entity;
import javax.persistence.Table;

import me.prettyprint.hom.annotations.*;

@Entity
@Table(name = "DocInfo")
@AnonymousPropertyHandling(adder = "addAnonymousProp", getter = "getAnonymousProps")
public class MyPojo {
   @Id
   private UUID id;

   @Column(name = "lp1")
   private long longProp1;

   private Map<String, String> anonymousProps = new HashMap<String, String>();

   public void addAnonymousProp(String name, String value) {
      anonymousProps.put(name, value);
   }

   public Collection<Entry<String, String>> getAnonymousProps() {
      return anonymousProps.entrySet();
   }

   public String getAnonymousProp(String name) {
      return anonymousProps.get(name);
   }

   public UUID getId() {
      return id;
   }

   public void setId(UUID id) {
      this.id = id;
   }

   public long getLongProp1() {
      return longProp1;
   }

   public void setLongProp1(long longProp1) {
      this.longProp1 = longProp1;
   }

}