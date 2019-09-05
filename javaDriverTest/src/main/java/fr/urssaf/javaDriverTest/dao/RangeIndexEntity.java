package fr.urssaf.javaDriverTest.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RangeIndexEntity implements Comparable<RangeIndexEntity> {

   private int id;

   private String LOWER_BOUND;

   private String UPPER_BOUND;

   private long COUNT;

   private String STATE;

   public RangeIndexEntity() {
   }

   public RangeIndexEntity(final int id, final String lower, final String upper, final long count, final String state) {
      this.id = id;
      LOWER_BOUND = lower;
      UPPER_BOUND = upper;
      COUNT = count;
      STATE = state;
   }

   @JsonProperty("ID")
   public int getId() {
      return id;
   }

   @JsonProperty("ID")
   public void setId(final int id) {
      this.id = id;
   }

   @JsonProperty("LOWER_BOUND")
   public String getLOWER_BOUND() {
      return LOWER_BOUND;
   }

   @JsonProperty("LOWER_BOUND")
   public void setLOWER_BOUND(final String LOWER_BOUND) {
      this.LOWER_BOUND = LOWER_BOUND;
   }

   @JsonProperty("UPPER_BOUND")
   public String getUPPER_BOUND() {
      return UPPER_BOUND;
   }

   @JsonProperty("UPPER_BOUND")
   public void setUPPER_BOUND(final String uPPER_BOUND) {
      UPPER_BOUND = uPPER_BOUND;
   }

   @JsonProperty("COUNT")
   public long getCOUNT() {
      return COUNT;
   }

   @JsonProperty("COUNT")
   public void setCOUNT(final int count) {
      COUNT = count;
   }

   @JsonProperty("STATE")
   public String getSTATE() {
      return STATE;
   }

   @JsonProperty("STATE")
   public void setSTATE(final String state) {
      STATE = state;
   }

   @Override
   public RangeIndexEntity clone() {
      final RangeIndexEntity clone = new RangeIndexEntity(id, LOWER_BOUND, UPPER_BOUND, COUNT, STATE);
      return clone;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int compareTo(final RangeIndexEntity other) {
      if ("min_lower_bound".equals(LOWER_BOUND)) {
         return -1;
      }
      if ("min_lower_bound".equals(other.LOWER_BOUND)) {
         return 1;
      }
      return LOWER_BOUND.compareTo(other.LOWER_BOUND);
   }
}
