package recouv.cirti.anais.api.source;

// import





public class AnaisExceptionAttributeValueAlreadyExist extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionAttributeValueAlreadyExist() { }

    public AnaisExceptionAttributeValueAlreadyExist(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "L'objet possède déjà une valeur d'attribut ajoutée par l'opération :"+reason;
	else return "L'objet possède déjà une valeur d'attribut ajoutée par l'opération "; 
    }
}


