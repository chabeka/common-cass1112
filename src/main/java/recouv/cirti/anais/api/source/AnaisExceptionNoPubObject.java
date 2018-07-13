package recouv.cirti.anais.api.source;

// import





public class AnaisExceptionNoPubObject extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionNoPubObject() { }

    public AnaisExceptionNoPubObject(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return " Un objet de publication nécessaire n'a pas été créé dans le conteneur de publication :"+reason;
	else return " Un objet de publication nécessaire n'a pas été créé dans le conteneur de publication "; 
    }
}


