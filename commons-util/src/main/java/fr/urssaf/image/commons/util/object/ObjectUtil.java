package fr.urssaf.image.commons.util.object;


/**
 * Méthodes utilitaires pour le type Java <code>{@link Object}</code>
 */
public final class ObjectUtil {

	private ObjectUtil() {

	}

	private static int intDefault;
	private static long longDefault;
	private static double doubleDefault;
	private static float floatDefault;
	private static char charDefault;
	private static boolean booleanDefault;
	private static byte byteDefault;

	
	/**
	 * Renvoie la valeur par défaut d'un type
	 * 
	 * @param classe le type dont on veut la valeur par défaut
	 * @return la valeur par défaut
	 */
	@SuppressWarnings({"unchecked","PMD.AvoidUsingShortType"})
	public static Object getDefaultValue(Class classe) {

		Object value = null;

		if (int.class.isAssignableFrom(classe)) {
			value = intDefault;
		} else if (boolean.class.isAssignableFrom(classe)) {
			value = booleanDefault;
		} else if (float.class.isAssignableFrom(classe)) {
			value = floatDefault;
		} else if (double.class.isAssignableFrom(classe)) {
			value = doubleDefault;
		} else if (byte.class.isAssignableFrom(classe)) {
			value = byteDefault;
		} else if (long.class.isAssignableFrom(classe)) {
			value = longDefault;
		} else if (char.class.isAssignableFrom(classe)) {
			value = charDefault;
		} else if (short.class.isAssignableFrom(classe)) {
			value = intDefault;
		}

		return value;
	}
}
