
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;


public final class FragmentRewriter {

    /**
     * Inverted sets use ! in the droid syntax.
     */
    private static final char INVERTED_OLD = '!';
    /**
     * Inverted sets use ^ in the net.domesdaybook syntax.
     */
    private static final char INVERTED_NEW = '^';
    /**
     * Set ranges use : in the droid syntax.
     */
    private static final char RANGE_OLD = ':';
    /**
     * Set ranges use - in the net.domesbook syntax.
     */
    private static final char RANGE_NEW = '-';
    /**
     * Defines a case sensitive string delimiter.
     */
    private static final char QUOTE = '\'';
    /**
     * Defines a case insensitive string delimiter.
     */
    private static final char BACKTICK = '`';
    /**
     * Square brackets open a set definition.
     */
    private static final char OPENSET = '[';
    /**
     * Square brackets close a set definition.
     */
    private static final char CLOSESET = ']';

    /**
     * Private constructor - this is a static utility class.
     */
    private FragmentRewriter() {
    }

    /**
     * 
     * @param fragment The DROID 4 syntax fragment to rewrite.
     * @return A fragment compatible with net.domesdaybook regular expressions.
     */
    //CHECKSTYLE:OFF - cyclomatic complexity is too high.
    public static String rewriteFragment(final String fragment) {
    //CHECKSTYLE:ON
        StringBuilder builder = new StringBuilder();
        final int length = fragment.length();
        boolean inCaseSensitiveString = false;
        boolean inCaseInsensitiveString = false;
        int inSet = 0;
        for (int charIndex = 0; charIndex < length; charIndex++) {
            char theChar = fragment.charAt(charIndex);

            // substitute characters if needed, or just add them:
            if (inSet > 0 && !inCaseSensitiveString && !inCaseInsensitiveString) {
                if (theChar == INVERTED_OLD) {
                    builder.append(INVERTED_NEW);
                } else if (theChar == RANGE_OLD) {
                    builder.append(RANGE_NEW);
                } else {
                    builder.append(theChar);
                }
            } else {
                builder.append(theChar);
            }

            // Determine if we are in sets or strings
            if (theChar == QUOTE && !inCaseInsensitiveString) {
                inCaseSensitiveString = !inCaseSensitiveString;
            } else if (theChar == BACKTICK && !inCaseSensitiveString) {
                inCaseInsensitiveString = !inCaseInsensitiveString;
            } else if (!inCaseSensitiveString && !inCaseInsensitiveString) {
                if (theChar == OPENSET) {
                    inSet++;
                } else if (theChar == CLOSESET) {
                    inSet--;
                }
            }
        }
        
        return builder.toString();
    }

}
