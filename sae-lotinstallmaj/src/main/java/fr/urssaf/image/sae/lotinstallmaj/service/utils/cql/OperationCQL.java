package fr.urssaf.image.sae.lotinstallmaj.service.utils.cql;

import java.util.HashMap;
import java.util.Map;

public enum OperationCQL {

	SAE_MODE_API("SAE_CREATE_MODE_API"),
	SAE_MIG_TRACES("SAE_CREATE_TRACE"),
	SAE_MIG_JOB_SPRING("SAE_CREATE_JOB_SPRING"),
	SAE_MIG_PILE_TRAVAUX("SAE_CREATE_PILE_TRAVAUX"),
	SAE_DELETE_MODE_API("SAE_DELETE_MODE_API"),
	SAE_DELETE_MIG_TRACES("SAE_DELETE_TRACES"),
	SAE_DELETE_MIG_JOB_SPRING("SAE_DELETE_JOB_SPRING"),
	SAE_DELETE_MIG_PILE_TRAVAUX("SAE_DELETE_PILE_TRAVAUX"),
	DFCE_192_TO_200_SCHEMA("DFCE_192_TO_200_SCHEMA"),
	DFCE_200_TO_210_SCHEMA("DFCE_200_TO_210_SCHEMA"),
	DFCE_210_TO_230_SCHEMA("DFCE_210_TO_230_SCHEMA"),
	DFCE_230_TO_192_SCHEMA("DFCE_230_TO_192_SCHEMA");
	
	String nomOp;
	
	// une map qui contient toutes les noms des operations
	private static final Map<String, OperationCQL> lookup = new HashMap<>();
	
	static {
		for (OperationCQL op : OperationCQL.values()) {
			lookup.put(op.getNomOp(), op);
		}
	}

	// constructeur
	private OperationCQL(String nomOperation) {
		this.nomOp = nomOperation;
	}

	public static OperationCQL get(String nomOp) {
		return lookup.get(nomOp);
	}
	
	public String getNomOp() {
		return nomOp;
	}

}
