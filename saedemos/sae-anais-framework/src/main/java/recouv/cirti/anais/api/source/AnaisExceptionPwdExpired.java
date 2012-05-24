package recouv.cirti.anais.api.source;

// import





public class AnaisExceptionPwdExpired extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionPwdExpired() { }

    public AnaisExceptionPwdExpired(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "Le mot de passe de l'utilisateur a expiré et doit être changé :"+reason;
	else return "Le mot de passe de l'utilisateur a expiré et doit être changé "; 
    }
}


