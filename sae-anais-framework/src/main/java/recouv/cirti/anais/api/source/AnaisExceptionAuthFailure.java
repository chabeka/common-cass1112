package recouv.cirti.anais.api.source;


public class AnaisExceptionAuthFailure extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionAuthFailure() { }

    public AnaisExceptionAuthFailure(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) 
		return "Les paramètres d'authentification de l'utilisateur sont invalides. Veuillez vérifier le mot de passe du compte : "+reason;
	else 
		return "Les paramètres d'authentification de l'utilisateur sont invalides. Veuillez vérifier le mot de passe du compte."; 
    }
}
