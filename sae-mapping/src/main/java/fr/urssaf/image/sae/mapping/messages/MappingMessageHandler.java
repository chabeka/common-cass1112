package fr.urssaf.image.sae.mapping.messages;

import org.springframework.context.MessageSource;

import fr.urssaf.image.sae.mapping.context.MappingApplicationContext;
import fr.urssaf.image.sae.metadata.constants.Constants;

/**
 * Fournit des services qui retournent un message à partir de sa clés .
 */
public final class MappingMessageHandler {
	private static final MessageSource MESSAGE_SOURCES;

	static {
		// Récupération du contexte pour les fichiers properties
		MESSAGE_SOURCES = MappingApplicationContext.getApplicationContext().getBean("messageSource_sae_mapping", MessageSource.class);
	}

	/**
	 * Récupéré un message d'erreur.
	 * 
	 * @param messageKey
	 *            : La clé du message
	 * @return Le message avec les valeurs substituées.
	 */
	@SuppressWarnings("PMD.AvoidDuplicateLiterals")
	public static String getMessage(final String messageKey) {

		return MESSAGE_SOURCES.getMessage(messageKey, null,
				Constants.DEFAULT_LOCAL);
	}

	/**
	 * Récupéré un message d'erreur.
	 * 
	 * @param messageKey
	 *            : La clé du message
	 * @param valueKey
	 *            : La valeur de substitution
	 * @return Le message avec les valeurs substituées.
	 */
	@SuppressWarnings("PMD.AvoidDuplicateLiterals")
	public static String getMessage(final String messageKey,
			final String valueKey) {
		return MESSAGE_SOURCES.getMessage(messageKey,
				new Object[] { valueKey }, Constants.DEFAULT_LOCAL);
	}

	/**
	 * Récupéré un message d'erreur.
	 * 
	 * @param messageKey
	 *            : La clé du message
	 * @param firstValueKey
	 *            : La valeur de substitution.
	 * @param secondValueKey
	 *            : La valeur de substitution
	 * @return Le message avec les valeurs substituées.
	 */
	@SuppressWarnings("PMD.AvoidDuplicateLiterals")
	public static String getMessage(final String messageKey,
			final String firstValueKey, final Object secondValueKey) {
		return MESSAGE_SOURCES.getMessage(messageKey, new Object[] {
				firstValueKey, secondValueKey }, Constants.DEFAULT_LOCAL);
	}

	/** Cette classe n'est pas faite pour être instanciée. */
	private MappingMessageHandler() {
		assert false;
	}
}
