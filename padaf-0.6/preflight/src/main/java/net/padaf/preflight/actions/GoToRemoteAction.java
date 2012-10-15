/*******************************************************************************
 * Copyright 2010 Atos Worldline SAS
 * 
 * Licensed by Atos Worldline SAS under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * Atos Worldline SAS licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/**
 * 
 */
package net.padaf.preflight.actions;

import static net.padaf.preflight.ValidationConstants.ACTION_DICTIONARY_KEY_F;
import static net.padaf.preflight.ValidationConstants.ERROR_ACTION_MISING_KEY;

import java.util.List;

import net.padaf.preflight.ValidationResult.ValidationError;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;

/**
 * ActionManager for the GoToRemote action GoToRemoteAction is valid if the F
 * entry is present.
 */
public class GoToRemoteAction extends GoToAction {

  /**
   * 
   * @param amFact
   *          Instance of ActionManagerFactory used to create ActionManager to
   *          check Next actions.
   * @param adict
   *          the COSDictionary of the action wrapped by this class.
   * @param cDoc
   *          the COSDocument from which the action comes from.
   * @param aaKey
   *          The name of the key which identify the action in a additional
   *          action dictionary.
   */
  public GoToRemoteAction(ActionManagerFactory amFact, COSDictionary adict,
      COSDocument doc, String aaKey) {
    super(amFact, adict, doc, aaKey);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.awl.edoc.pdfa.validation.actions.AbstractActionManager#valid(java.util
   * .List)
   */
  @Override
  protected boolean innerValid(List<ValidationError> error) {
    if (super.innerValid(error)) {
      COSBase f = this.actionDictionnary.getItem(COSName
          .getPDFName(ACTION_DICTIONARY_KEY_F));
      if (f == null) {
        error.add(new ValidationError(ERROR_ACTION_MISING_KEY,
            "F entry is mandatory for the GoToRemoteActions"));
        return false;
      }
    }
    return true;
  }
}
