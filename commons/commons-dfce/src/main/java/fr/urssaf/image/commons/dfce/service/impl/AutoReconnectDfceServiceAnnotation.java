package fr.urssaf.image.commons.dfce.service.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indiquant qu'il s'agit d'un service DFCE qui provoquera potentiellement la reconnexion
 * automatique à DFCE. La reconnexion est gérée en Aspect par {@link DFCEReconnectionAspect}
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface AutoReconnectDfceServiceAnnotation {

}
