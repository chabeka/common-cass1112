package fr.urssaf.image.sae.dfce.admin.messages;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Fournit des services récupération et de formattage de messages.
 * 
 * @author akenore
 * 
 */

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class MessageHandler {
	// Récupération du message.
	private static final MessageSource MESSAGE_SOURCES = (MessageSource) new ClassPathXmlApplicationContext(
			"applicationContext-sae-dfce-admin.xml").getBean("messageSource");
	// Le local par défaut
	public static final Locale LOCAL = Locale.FRENCH;
	// Message par défaut
	@SuppressWarnings("PMD.LongVariable")
	public static final String NO_MESSAGE_FOR_THIS_KEY = "Pas de message correspondant à cette clé";

	/**
	 * 
	 * @param messageKey
	 *            : La clé du message.
	 * @param impactKey
	 *            : La clé de l'impact.
	 * @param actionKey
	 *            : La clé de l'action.
	 * @return Le message en faisant la concaténation de [message |impact|
	 *         action].
	 */
	public static String getMessage(final String messageKey,
			final String impactKey, final String actionKey) {
		final StringBuilder strBuilder = new StringBuilder();
		strBuilder.setLength(0);

		strBuilder.append(MESSAGE_SOURCES.getMessage(messageKey, null,
				NO_MESSAGE_FOR_THIS_KEY, LOCAL));
		strBuilder
				.append(" | ")
				.append(MESSAGE_SOURCES.getMessage(impactKey, null,
						NO_MESSAGE_FOR_THIS_KEY, LOCAL))
				.append(" | ")
				.append(MESSAGE_SOURCES.getMessage(actionKey, null,
						NO_MESSAGE_FOR_THIS_KEY, LOCAL));
		return strBuilder.toString();
	}

	/**
	 * 
	 * @param messageKey
	 *            : La clé du message.
	 * @param impactKey
	 *            : La clé de l'impact.
	 * @param actionKey
	 *            : La clé de l'action
	 * @return Le message en faisant la concaténation du message |impact|
	 *         action.
	 */
	public static String getMessageFormatted(final String messageKey,
			final String impactKey, final String actionKey) {
		final StringBuilder strBuilder = new StringBuilder();
		strBuilder.setLength(0);
		strBuilder.append(messageKey);
		strBuilder.append(" | ").append(impactKey).append(" | ")
				.append(actionKey);
		return strBuilder.toString();
	}

	/**
	 * 
	 * @param messageKey
	 *            : La clé du message.
	 * @param valueToPrint
	 *            : La valeur de substitutions.
	 * 
	 * @return Le message contenant les valeurs substituées.
	 */
	public static String getMessage(final String messageKey, final String valueToPrint) {

		return MESSAGE_SOURCES.getMessage(messageKey, new Object[] {valueToPrint}, LOCAL);
	}

	/**
	 * @param messageKey
	 *            : La clé du message.
	 * @return Le message
	 */
	@SuppressWarnings("PMD.LongVariable")
	public static String getMessage(final String messageKey) {
		return MESSAGE_SOURCES.getMessage(messageKey, null,
				NO_MESSAGE_FOR_THIS_KEY, LOCAL);
	}

	// Cette classe ne doit pas avoir de constructeur.
	private MessageHandler() {
		assert false;

	}
}
