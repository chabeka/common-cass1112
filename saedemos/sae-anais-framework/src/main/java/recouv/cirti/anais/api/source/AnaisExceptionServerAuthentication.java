package recouv.cirti.anais.api.source;

public class AnaisExceptionServerAuthentication extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionServerAuthentication() { }

    public AnaisExceptionServerAuthentication(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
    	if (reason!=null) 
    		return "Les paramètres d'authentification au serveur sont invalides. Veuillez vérifier le code application, le code environnement et le mot de passe du compte : "+reason;
    	else 
    		return "Les paramètres d'authentification au serveur sont invalides. Veuillez vérifier le code application, le code environnement et le mot de passe du compte."; 
       }
}

