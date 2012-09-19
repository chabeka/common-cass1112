package recouv.cirti.anais.api.source;

// import




public class AnaisExceptionUnknownApp extends AnaisException { 

	static final long serialVersionUID = 1;
	
    public String   reason=null;

    public AnaisExceptionUnknownApp() { }

    public AnaisExceptionUnknownApp(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "Application inconnue: "+reason;
	else return "Application inconnue"; 
    }
}


