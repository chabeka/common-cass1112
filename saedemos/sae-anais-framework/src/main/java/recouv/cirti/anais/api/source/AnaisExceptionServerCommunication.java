package recouv.cirti.anais.api.source;

// import





public class AnaisExceptionServerCommunication extends AnaisException { 
	
	static final long serialVersionUID = 1;
	
    public String   reason=null;

    public AnaisExceptionServerCommunication() { }

    public AnaisExceptionServerCommunication(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "Le serveur ANAIS est indisponible :"+reason;
	else return "Le serveur ANAIS est indisponible "; 
    }
}


