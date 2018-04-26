package recouv.cirti.anais.api.source;

// import





public class AnaisExceptionAlreadyExist extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionAlreadyExist() { }

    public AnaisExceptionAlreadyExist(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "L'objet existe déjà dans l'annuaire :"+reason;
	else return "L'objet existe déjà dans l'annuaire "; 
    }
}


