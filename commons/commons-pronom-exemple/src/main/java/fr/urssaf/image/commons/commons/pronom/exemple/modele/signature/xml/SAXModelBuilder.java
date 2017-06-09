
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

import java.lang.reflect.Method;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



/**
 * Lis et parses des données depuis un fichier XML.
 *
 * @version 4.0.0
 */
public class SAXModelBuilder extends DefaultHandler {

    private static final String ADD = "add";
    private static final String SET = "set";

    private Log log = LogFactory.getLog(this.getClass());
    
    private Stack<Object> stack = new Stack<Object>();
    private SimpleElement element;

    private String mySignaturePackage = FFSignatureFile.class.getPackage().getName();
    private String myFormatPackage = FileFormat.class.getPackage().getName();
    
    private String namespace = "";
    private boolean useNamespace;
    private boolean allowGlobalNamespace = true;


    /**
     * 
     * @param theSignaturePackage The signature package to use.
     */
    public final void setSignaturePackage(String theSignaturePackage) {
        mySignaturePackage = theSignaturePackage;
    }

    /**
     * Set up XML namespace handling.
     * <p/>
     * <p>If <code>allowGlobalNamespace</code> is set to <code>true</code>, elements
     * that do not have a namespace specified are parsed; attributes that don't
     * have a namespace specified are parsed.  If it is <code>false</code>, for
     * it to be parsed, an element must have a namespace specifed (by default or
     * with a prefix); an attribute must have a namespace specified with a prefix.
     *
     * @param nspace            the XML namespace to use
     * @param globalNamespace allow the parser to recognise elements/ attributes that aren't in any namespace
     */
    public final void setupNamespace(String nspace, boolean globalNamespace) {
        if (nspace == null) {
            throw new IllegalArgumentException("Namespace cannot be null");
        }

        this.namespace = nspace;
        this.useNamespace = true;
        this.allowGlobalNamespace = globalNamespace;

    }

    /**
     * Handle names in a namespace-aware fashion.
     * <p/>
     * <p>If an element/ attribute is in a namespace, qname is not required to be set.
     * We must, therefore, use the localname if the namespace is set, and qname if it isn't.
     *
     * @param nspace the namespace uri
     * @param localname the local part of the name
     * @param qname     a qualified name
     * @return the local part or the qualified name, as appropriate
     */
    private String handleNameNS(String nspace, String localname, String qname) {
        String result = null;
        if (this.useNamespace && this.namespace.equals(nspace)) {
            // Name is in the specified namespace
            result = localname;
        } else if (this.allowGlobalNamespace && "".equals(nspace)) {
            // Name is in the global namespace
            result = qname;
        }
        return result;
    }

    /**
     * @param nspace the namespace uri
     * @param localname the local part of the name
     * @param qname     a qualified name
     * @param atts The attributes of the element.
     */
    @Override
   public final void startElement(String nspace, String localname, String qname, Attributes atts) {
        String elementName = handleNameNS(nspace, localname, qname);
        if (elementName == null) {
            return;
        }
        SimpleElement elem = null;
        String packName;
        if ("FileFormat".equals(elementName) 
                || "FileFormatHit".equals(elementName)
                || "FileFormatCollection".equals(elementName)) {
            packName = myFormatPackage;
        } else {
            packName = mySignaturePackage;
        }
        String fullName = packName + "." + elementName; 
        try {
            elem = (SimpleElement) Class.forName(fullName).newInstance();
        //CHECKSTYLE:OFF
        } catch (Exception e) {
        	log.debug("No class exists for element name:" + elementName);
        }
        //CHECKSTYLE:ON
        if (elem == null) {
            elem = new SimpleElement();
        }

        for (int i = 0; i < atts.getLength(); i++) {
            String attributeName = handleNameNS(atts.getURI(i), atts.getLocalName(i), atts.getQName(i));
            if (attributeName == null) {
                continue;
            }
            elem.setAttributeValue(attributeName, atts.getValue(i));
        }
        stack.push(elem);
    }

    /**
     * @param nspace the namespace uri
     * @param localname the local part of the name
     * @param qname     a qualified name 
     * @throws SAXException if a problem occurs.
     */
    @Override
   public final void endElement(String nspace, String localname, String qname)
        throws SAXException {
        String elementName = handleNameNS(nspace, localname, qname);
        if (elementName == null) {
            return;
        }
        element = (SimpleElement) stack.pop();
        element.completeElementContent();
        if (!stack.empty()) {
            try {
                setProperty(elementName, stack.peek(), element);
            } catch (SAXException e) {
                throw new SAXException(e); // do not understand this logic!
            }
        }
    }

    @Override
   public final void characters(char[] ch, int start, int len) {
        if (!stack.empty()) { // Ignore character data if we don't have an element to put it in.
            String text = new String(ch, start, len);
            ((SimpleElement) (stack.peek())).setText(text);
        }
    }

    /**
     * 
     * @param name The name of the method.
     * @param target The target class.
     * @param value The value to set.
     * @throws SAXException exception if a problem occurs
     */
    final void setProperty(String name, Object target, Object value) throws SAXException {
        Method method = null;
        Object val = value;
        try {
            method = target.getClass().getMethod(
                    ADD + name, new Class[]{val.getClass()});
        //CHECKSTYLE:OFF
        } catch (NoSuchMethodException e) {
        }
        //CHECKSTYLE:ON
        if (method == null) {
            try {
                method = target.getClass().getMethod(
                        SET + name, new Class[]{val.getClass()});
              //CHECKSTYLE:OFF
            } catch (NoSuchMethodException e) {
            }
            //CHECKSTYLE:ON
        }
        if (method == null) {
            try {
                val = ((SimpleElement) val).getText().trim();
                method = target.getClass().getMethod(
                        ADD + name, new Class[]{String.class});
              //CHECKSTYLE:OFF
            } catch (NoSuchMethodException e) {
            }
            //CHECKSTYLE:ON
        }
        try {
            if (method == null) {
                method = target.getClass().getMethod(
                        SET + name, new Class[]{String.class});
            }
            method.invoke(target, val);
        } catch (NoSuchMethodException e) {
            unknownElementWarning(name, ((SimpleElement) target).getElementName());
          //CHECKSTYLE:OFF
        } catch (Exception e) {
            throw new SAXException(e);
        }
        //CHECKSTYLE:ON
    }

    /**
     * 
     * @return The element.
     */
    public final SimpleElement getModel() {
        return element;
    }
    
    /**
     * Displays a special warning for unknown XML elements when reading
     * XML files.
     *
     * @param unknownElement   The name of the element which was not recognised
     * @param containerElement The name of the element which contains the unrecognised element
     */
    public final void unknownElementWarning(String unknownElement, String containerElement) {
        String warning = "WARNING: Unknown XML element " + unknownElement + " found under " + containerElement + " ";
        log.debug(warning);
    }    

}