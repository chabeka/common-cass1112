package fr.urssaf.astyanaxtest.helper;

import java.util.ArrayList;

import com.google.common.base.Splitter;

public class MetaHelper {

	/**
	 * Convertit un index (composite ou non) en liste de metas
	 * @param index  : le nom de l'index (ex: "nce" ou "cot&cag&SM_CREATION_DATE&")
	 * @return Liste de métas
	 */
	public static ArrayList<String> indexToMetas(String index) {
		ArrayList<String> realMetas = new ArrayList<String>();
		if (index.contains("&")) {
			for (String m : Splitter.on("&").split(index)) {
				if (! m.isEmpty()) realMetas.add(m);
			}
		}
		else {
			realMetas.add(index);
		}
		return realMetas;
	}
}
