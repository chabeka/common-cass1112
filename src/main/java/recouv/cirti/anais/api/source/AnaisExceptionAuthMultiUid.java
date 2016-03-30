package recouv.cirti.anais.api.source;

// import





public class AnaisExceptionAuthMultiUid extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionAuthMultiUid() { }

    public AnaisExceptionAuthMultiUid(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "Plusieurs utilisateurs ayant le même login sont présents dans l'annuaire :"+reason;
	else return "Plusieurs utilisateurs ayant le même login sont présents dans l'annuaire "; 
    }
}


