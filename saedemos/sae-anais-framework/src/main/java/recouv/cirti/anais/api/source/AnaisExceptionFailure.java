package recouv.cirti.anais.api.source;

// import




public class AnaisExceptionFailure extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionFailure() { }

    public AnaisExceptionFailure(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "Une erreur non prévue est survenue: "+reason;
	else return "Une erreur non prévue est survenu"; 
    }
}


