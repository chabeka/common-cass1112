package fr.urssaf.image.sae.lotinstallmaj.service.utils.cql;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.datastax.driver.core.Statement;

/**
 * Classe permettant de lire un fichier et d'en extraire le contenu 
 * pour en faire des requete {@link Statement} qui sont executable 
 * par le driver datastax
 */
public class CQLDataFileSet {

	private static final Logger LOG = LoggerFactory.getLogger(CQLDataFileSet.class);
    public static final String END_OF_STATEMENT_DELIMITER = ";";
    public static final String START_OF_MULTI_COMMENT_DELIMITER = "/*";
    public static final String END_OF_MULTI_COMMENT_DELIMITER = "*/";

    public static final String SINGLE_LINE_COMMENT_DELIMITER = "//";
    public static final String SINGLE_LINE_COMMENT_DELIMITER_1 = "--";
    
    private String dataSetLocation = null;
    private String keyspaceName = null;

    public CQLDataFileSet(String dataSetLocation) {
        this.dataSetLocation = dataSetLocation;
    }

    public List<String> getCQLStatements() {
        List<String> lines = getLines();
        return linesToCQLStatements(lines);
    }

    /**
     * Transforme les lignes en requetes cql;
     * @param lines
     * @return
     */
    private List<String> linesToCQLStatements(List<String> lines) {
    	List<String> statements = new ArrayList<>();
    	StringBuffer sbf = new StringBuffer();
		String spaceString = " ";
    	for(String line : lines) {
    		line = line.trim();
    		sbf.append(line + spaceString);
    		// ajout d'un nouveau statement lorsqu'on a un ";" qui indique la fin de la requete
    		if(line.indexOf(END_OF_STATEMENT_DELIMITER) != -1 && END_OF_STATEMENT_DELIMITER.charAt(0)==line.charAt(line.length() -1)) {
    			statements.add(sbf.toString());
    			sbf.setLength(0);
    		}
    	}	
    	return statements;
    }

    /**
     * Lecture des lignes du fichier cql
     * @return
     */
    private List<String> getLines() {
    	
        String line;
        List<String> cqlQueries = new ArrayList<>();
        String lineState = StringUtils.EMPTY;
        try {
        	
        	ClassPathResource updateFile = new ClassPathResource(dataSetLocation);
            InputStream inputStream = updateFile.getInputStream();
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);

        	// on ajoute pas les lignes de commentaires
            while ((line = br.readLine()) != null) {
            	
            	if(lineState.equals("simpleCommentLine") || lineState.equals("multiCommentEnd")) {
            		lineState = StringUtils.EMPTY;
            	}
                if (StringUtils.isNotBlank(line)) {
                	//ligne commencant par "//"
                	if(line.trim().indexOf(SINGLE_LINE_COMMENT_DELIMITER) > -1) {
                		lineState = "simpleCommentLine";
                	}
                	// ligne commencant par "--"
                	if(line.trim().indexOf(SINGLE_LINE_COMMENT_DELIMITER_1) > -1) {
                		lineState = "simpleCommentLine";
                	}
                	// ligne commencant par "/*"
                	if(line.trim().indexOf(START_OF_MULTI_COMMENT_DELIMITER) > -1) {
                		lineState = "multiCommentStart";
                	} 
                	
                	if(lineState.equals("multiCommentStart") && line.trim().indexOf(END_OF_MULTI_COMMENT_DELIMITER) > -1) {
                		lineState = "multiCommentEnd";
                	}
                	if(lineState.isEmpty()) {
                		cqlQueries.add(line.trim());
                	}
                }
            }
            br.close();
        } catch (IOException e) {
        	LOG.error("Problème de chargement ou de lecture dans le fichier cql : " + dataSetLocation );
        }
        return cqlQueries;
    }

    
    public String getKeyspaceName() {
        return keyspaceName;
    }

}
