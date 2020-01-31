package fr.urssaf.astyanaxtest.dao;

import java.util.UUID;





import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.mapping.MappingUtil;
import com.netflix.astyanax.serializers.UUIDSerializer;

import fr.urssaf.astyanaxtest.helper.ConvertHelper;

public class BasesReferenceDao {

	/**
	 * Renvoie l'UUID d'une base DFCE dont le nom est donné
	 * 
	 * @param baseName : Nom de la base (ex : 'SAE-PROD')
	 * @return UUID de la base
	 */
	public static UUID getBaseUUID(MappingUtil mapper, String baseName) throws Exception {
		BasesReferenceEntity base = mapper.get(BasesReferenceCF.get(), baseName, BasesReferenceEntity.class);
		if (base == null) return null;
		byte[] bytes = base.getBaseUUID();
		return UUIDSerializer.get().fromBytes(bytes);
	}

	
	/**
	 * Met à jour la colonne "uuid" pour changer l'uuid de la base
	 */
	public static void setBaseUUID(Keyspace keyspace, String baseName, UUID baseUUID) throws Exception {
		MutationBatch batch = keyspace.prepareMutationBatch();
		byte[] value = UUIDSerializer.get().toBytes(baseUUID);
		batch.withRow(BasesReferenceCF.get(), baseName).putColumn("uuid", value);
		OperationResult<Void> result = batch.execute();
	}

}
