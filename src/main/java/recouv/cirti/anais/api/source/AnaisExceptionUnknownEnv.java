package recouv.cirti.anais.api.source;

// import




public class AnaisExceptionUnknownEnv extends AnaisException { 
	
	static final long serialVersionUID = 1;
    public String   reason=null;

    public  AnaisExceptionUnknownEnv() { }

    public AnaisExceptionUnknownEnv(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "Environnement inconnu: "+reason;
	else return "Environnement inconnu"; 
    }
}


