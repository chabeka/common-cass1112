package fr.urssaf.image.sae.hawai.validate;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;


public class ParameterValidator {

    public void validate(Object obj) throws MojoExecutionException {
        try {
            if (obj != null) {
                // on boucle sur tous les field de la classe
                for (Field f : obj.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    Allowed property = f.getAnnotation(Allowed.class);
                    if ((property != null) && (f.getType() == String.class)) {
                        String value = f.get(obj).toString().toLowerCase();
                        List<String> allowedValues = Arrays.asList(property.values());
                        if (!allowedValues.contains(value)) {
                            throw new MojoExecutionException("la valeur '" + value + "' du paramètre '" + f.getName()
                                    + "' est incorrecte (attendue : " + formatAllowedValues(allowedValues) + ")");
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Erreur interne au plugin : " + e.getMessage(), e);
        }
    }

    private String formatAllowedValues(List<String> allowedValues) {
        StringBuffer buffer = new StringBuffer();
        for (String value : allowedValues) {
            if (buffer.length() > 0) {
                buffer.append("|");
            }
            buffer.append(value);
        }
        return buffer.toString();

    }

}