package recouv.cirti.anais.api.source;

// import





public class AnaisExceptionNoObject extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionNoObject() { }

    public AnaisExceptionNoObject(String _reason) 
    {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "L'objet n'a pas été trouvé dans l'annuaire :"+reason;
	else return "L'objet n'a pas été trouvé dans l'annuaire "; 
    }
}


