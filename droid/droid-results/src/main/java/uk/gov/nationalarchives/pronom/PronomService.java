/**
 * Copyright (c) 2012, The National Archives <pronom@nationalarchives.gsi.gov.uk>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following
 * conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of the The National Archives nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package uk.gov.nationalarchives.pronom;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import uk.gov.nationalarchives.pronom.signaturefile.XmlFragment;

/**
 * This class was generated by Apache CXF 2.2.5
 * Wed Dec 09 09:40:08 GMT 2009
 * Generated source version: 2.2.5
 * 
 */
 
@WebService(targetNamespace = "http://pronom.nationalarchives.gov.uk", name = "PronomService")
@XmlSeeAlso({uk.gov.nationalarchives.pronom.signaturefile.ObjectFactory.class,ObjectFactory.class})
public interface PronomService {

    @WebResult(name = "SignatureFile", targetNamespace = "http://pronom.nationalarchives.gov.uk")
    @RequestWrapper(localName = "getSignatureFileV1", targetNamespace = "http://pronom.nationalarchives.gov.uk", className = "uk.gov.nationalarchives.pronom.GetSignatureFileV1")
    @ResponseWrapper(localName = "getSignatureFileV1Response", targetNamespace = "http://pronom.nationalarchives.gov.uk", className = "uk.gov.nationalarchives.pronom.GetSignatureFileV1Response")
    @WebMethod(action = "http://pronom.nationalarchives.gov.uk:getSignatureFileV1In")
    public XmlFragment getSignatureFileV1();

    @RequestWrapper(localName = "getSignatureFileVersionV1", targetNamespace = "http://pronom.nationalarchives.gov.uk", className = "uk.gov.nationalarchives.pronom.GetSignatureFileVersionV1")
    @ResponseWrapper(localName = "getSignatureFileVersionV1Response", targetNamespace = "http://pronom.nationalarchives.gov.uk", className = "uk.gov.nationalarchives.pronom.GetSignatureFileVersionV1Response")
    @WebMethod(action = "http://pronom.nationalarchives.gov.uk:getSignatureFileVersionV1In")
    public void getSignatureFileVersionV1(
        @WebParam(mode = WebParam.Mode.OUT, name = "Version", targetNamespace = "http://pronom.nationalarchives.gov.uk")
        javax.xml.ws.Holder<Version> version,
        @WebParam(mode = WebParam.Mode.OUT, name = "Deprecated", targetNamespace = "http://pronom.nationalarchives.gov.uk")
        javax.xml.ws.Holder<java.lang.Boolean> deprecated
    );
}
