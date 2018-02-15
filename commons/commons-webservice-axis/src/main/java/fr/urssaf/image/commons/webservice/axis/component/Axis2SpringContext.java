/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.urssaf.image.commons.webservice.axis.component;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.engine.ServiceLifeCycle;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Cette classe permet d'initialiser le contexte Spring à l'intérieur d'une
 * archive .AAR du framework Axis<br>
 * <br>
 * Il est n&eacute;cessaire de configurer le fichier <code>services.xml</code>
 * 
 */
public class Axis2SpringContext implements ServiceLifeCycle {

   private static final Logger LOG = Logger.getLogger(Axis2SpringContext.class);

   /**
    * initialisation du du fichier <code>applicationContext.xml</code>
    * {@inheritDoc}
    */
   @Override
   public final void startUp(ConfigurationContext context, AxisService service) {
      ClassLoader classLoader = service.getClassLoader();
      ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext(
            new String[] { "applicationContext.xml" }, false);
      appCtx.setClassLoader(classLoader);
      appCtx.refresh();

      LOG.debug("initialisation du context spring");

   }

   @Override
   public final void shutDown(ConfigurationContext ignore, AxisService service) {

      // not implemented
   }
}