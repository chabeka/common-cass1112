package recouv.cirti.anais.api.source;

// import





public class AnaisExceptionPwdExpiring extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionPwdExpiring() { }

    public AnaisExceptionPwdExpiring(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "Le mot de passe expirera bientôt "+reason;
	else return "Le mot de passe expirera bientôt "; 
    }
}

