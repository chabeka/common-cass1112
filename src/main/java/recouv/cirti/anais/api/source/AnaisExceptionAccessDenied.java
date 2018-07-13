package recouv.cirti.anais.api.source;


public class AnaisExceptionAccessDenied extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionAccessDenied() { }

    public AnaisExceptionAccessDenied(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "Les drois d'accès de l'utilisateur sont insuffisants :"+reason;
	else return "Les drois d'accès de l'utilisateur sont insuffisants "; 
    }
}


