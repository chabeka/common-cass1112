package recouv.cirti.anais.api.source;

// import





public class AnaisExceptionAuthAccountLocked extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionAuthAccountLocked() { }

    public AnaisExceptionAuthAccountLocked(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "Les drois d'accès de l'utilisateur sont insuffisants ou le compte est bloqué :"+reason;
	else return "Les drois d'accès de l'utilisateur sont insuffisants ou le compte est bloqué."; 
    }
}


