package fr.urssaf.astyanaxtest.dao;

import org.codehaus.jackson.annotate.JsonProperty;




public class RangeIndexEntity {

	private int id;
	private String LOWER_BOUND;
	private String UPPER_BOUND;
	private int COUNT;
	private String STATE;
	
	@JsonProperty("ID")	
	public int getId() {
		return id;
	}
	@JsonProperty("ID")	
	public void setId(int id) {
		this.id = id;
	}
	@JsonProperty("LOWER_BOUND")	
	public String getLOWER_BOUND() {
		return LOWER_BOUND;
	}
	@JsonProperty("LOWER_BOUND")	
	public void setLOWER_BOUND(String LOWER_BOUND) {
		this.LOWER_BOUND = LOWER_BOUND;
	}
	@JsonProperty("UPPER_BOUND")	
	public String getUPPER_BOUND() {
		return UPPER_BOUND;
	}
	@JsonProperty("UPPER_BOUND")	
	public void setUPPER_BOUND(String uPPER_BOUND) {
		UPPER_BOUND = uPPER_BOUND;
	}
	@JsonProperty("COUNT")	
	public int getCOUNT() {
		return COUNT;
	}
	@JsonProperty("COUNT")	
	public void setCOUNT(int count) {
		COUNT = count;
	}
	@JsonProperty("STATE")	
	public String getSTATE() {
		return STATE;
	}
	@JsonProperty("STATE")	
	public void setSTATE(String state) {
		STATE = state;
	}
	
	@Override
	public RangeIndexEntity clone() {
		RangeIndexEntity clone = new RangeIndexEntity();
		clone.id = id;
		clone.LOWER_BOUND = LOWER_BOUND;
		clone.UPPER_BOUND = UPPER_BOUND;
		clone.COUNT = COUNT;
		clone.STATE = STATE;
		return clone;
	}
}
