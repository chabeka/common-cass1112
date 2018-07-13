package recouv.cirti.anais.api.source;

// import





public class AnaisExceptionNoAttributeValue extends AnaisException { 
	static final long serialVersionUID = 1;
    public String   reason=null;

    public AnaisExceptionNoAttributeValue() { }

    public AnaisExceptionNoAttributeValue(String _reason) {
	reason=_reason;
    }

    public String getReason() { return reason; }

    public String toString() { 
	if (reason!=null) return "L'objet ne possède pas une valeur d'attribut retiré par l'opération :"+reason;
	else return "L'objet ne possède pas une valeur d'attribut retiré par l'opération"; 
    }
}


