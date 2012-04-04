
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

import net.domesdaybook.expression.compiler.sequence.SequenceMatcherCompiler;
import net.domesdaybook.expression.parser.ParseException;
import net.domesdaybook.matcher.sequence.SequenceMatcher;
import net.domesdaybook.reader.ByteReader;


/**
 * Un SideFragment est un fragment d'une subsequences.
 */
public class SideFragment extends SimpleElement {
    
    private static final String FRAGMENT_PARSE_ERROR = "The signature fragment [%s] could not be parsed. "
        + "The error returned was [%s]"; 

    private int myPosition;
    private int myMinOffset;
    private int myMaxOffset;
    private SequenceMatcher matcher;
    /* setters */
    /**
     * @param thePosition the position of the fragment in the
     * list of SideFragments held to the left or right of a
     * subsequence.  
     * 
     * Individual fragments can have the same position as each other -
     * this is how alternatives are represented -
     * as different fragments with the same position.
     */
    public final void setPosition(final int thePosition) {
        this.myPosition = thePosition;
    }

    /**
     * A minimum offset is the amount of bytes to skip before
     * looking for this fragment.
     *   
     * @param theMinOffset The minimum offset to begin looking for this fragment.
     */
    public final void setMinOffset(final int theMinOffset) {
        this.myMinOffset = theMinOffset;
        // ensure the maximum is never less than then minimum.
        if (this.myMaxOffset < this.myMinOffset) {
            this.myMaxOffset = theMinOffset;
        }
    }

    /**
     * A maximum offset is the largest amount of bytes to look
     * in for this fragment.  If the maximum offset is greater
     * than the minimum offset, then a range of bytes will be
     * searched for this fragment.
     * 
     * @param theMaxOffset The maximum offset to begin lookiing for this fragment.
     */
    public final void setMaxOffset(final int theMaxOffset) {
        this.myMaxOffset = theMaxOffset;
        // ensure the minimum is never greater than the maximum.
        if (this.myMinOffset > this.myMaxOffset) {
            this.myMinOffset = theMaxOffset;
        }
    }

    /**
     * 
     * @param expression The regular expression defining the fragment.
     */
    public final void setFragment(final String expression) {
        try {
            SequenceMatcherCompiler compiler = new SequenceMatcherCompiler();
            final String transformed = FragmentRewriter.rewriteFragment(expression);
            compiler.compile(transformed);
        } catch (ParseException ex) {
            final String warning = String.format(FRAGMENT_PARSE_ERROR, expression, ex.getMessage());
            getLog().warn(warning);            
            //throw new IllegalArgumentException(expression, ex);
        }
    }
    
    /**
     * 
     * @return Whether the fragment managed to be assembled correctly.
     */
    public final boolean isInvalidFragment() {
        //return isInvalidFragment;
       return false;
    }

    @Override
    public final void setAttributeValue(final String name, final String value) {
        if ("Position".equals(name)) {
            setPosition(Integer.parseInt(value));
        } else if ("MinOffset".equals(name)) {
            setMinOffset(Integer.parseInt(value));
        } else if ("MaxOffset".equals(name)) {
            setMaxOffset(Integer.parseInt(value));
        } else {
            unknownAttributeWarning(name, this.getElementName());
        }
    }

    /* getters */
    /**
     * 
     * @return the position of this fragment.
     */
    public final int getPosition() {
        return myPosition;
    }

    /**
     * A minimum offset is the amount of bytes to skip before
     * looking for this fragment.
     * 
     * @return The minimum offset to begin looking for this fragment.
     */
    public final int getMinOffset() {
        return myMinOffset;
    }

    /**
     * A maximum offset is the largest amount of bytes to look
     * in for this fragment.  If the maximum offset is greater
     * than the minimum offset, then a range of bytes will be
     * searched for this fragment.
     * 
     * @return The maximum offset to look for this fragment.
     */
    public final int getMaxOffset() {
        return myMaxOffset;
    }

    /**
     * 
     * @return The number of bytes matched by this fragment.
     */
    public final int getNumBytes() {
        return matcher == null ? 0 : matcher.length();
    }

    /**
     * Set the sideFragment sequence.
     * This will have been stored in the text attribute by the setText method.
     * Then transforms the input string into a list of matching objects.
     */
    @Override
    public final void completeElementContent() {
        setFragment(this.getText());
    }

    /**
     * Matches the fragment against the position in the ByteReader given.
     * 
     * @param bytes The byte reader to match the bytes with.
     * @param matchFrom The position to match from.
     * @return Whether the fragment matches at the position given.
     */
    public final boolean matchesBytes(final ByteReader bytes, final long matchFrom) {
        return matcher.matches(bytes, matchFrom);
    }


    /**
     * Returns a regular expression representation of this fragment.
     * 
     * @param prettyPrint whether to pretty print the regular expression.
     * @return a regular expression defining this fragment,
     * but minus any offsets defined here (handled by the parent subsequence).
     */
    public final String toRegularExpression(final boolean prettyPrint) {
        return matcher == null ? "" : matcher.toRegularExpression(prettyPrint);
    }
}
