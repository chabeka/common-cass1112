package recouv.cirti.anais.api.source;

// import





public class AnaisExceptionSchema extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionSchema() { }

    public AnaisExceptionSchema(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "Une violation de schéma s'est produite :"+reason;
	else return "Une violation de schéma s'est produite "; 
    }
}


