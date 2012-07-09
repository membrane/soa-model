/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.xml.util

import java.net.ConnectException;
import groovyjarjarantlr.RecognitionException;
import org.apache.commons.httpclient.params.*
import org.apache.commons.httpclient.methods.*
import org.apache.commons.httpclient.*
import org.apache.commons.logging.*
import com.predic8.schema.*
import com.predic8.io.*

class ExternalResolver extends ResourceResolver {
  
  private Log log = LogFactory.getLog(this.class)
  
  def proxyHost
  def proxyPort
  int timeout = 10000
  
  def resolve(input, baseDir) {
    if ( input instanceof Import ) {
      if ( !input.schemaLocation ) return
      input = input.schemaLocation
    }

    if ( input instanceof Reader || input instanceof InputStream )  {
      log.debug("resolving from reader, baseDir: $baseDir")
      return input;
    }

    log.debug("resolving: $input, baseDir: $baseDir")

    if(input instanceof File){
      return new FileInputStream(input)
    }

    if (! input instanceof String)
      throw new RuntimeException("Do not know how to resolve $input")

    if(input.startsWith('file')) {
        return new FileInputStream(new URL(input).getPath())
    }

    if(input.startsWith('http') || input.startsWith('https')) {
        return resolveViaHttp(input)
    } 
    
    if(baseDir && (baseDir.startsWith('http') || baseDir.startsWith('https'))) {
        return resolveViaHttp(baseDir + input)
    }

	resolveAsFile(input, baseDir)
  }
  
  public InputStream resolveAsFile(java.lang.String filename, java.lang.String baseDir) {
	  if(baseDir) {
		  return new FileInputStream(new File(baseDir,filename))
	  }
  
	  new FileInputStream(new File(filename))
  }

  private request(url) {
    HttpClient client = new HttpClient();
    if ( proxyHost ) {
      client.getHostConfiguration().setProxy(proxyHost, proxyPort)
    }
    client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
      
    
    HttpMethod method = new GetMethod(url);
    method.params.setParameter(HttpMethodParams.USER_AGENT,"SOA Model (see http://membrane-soa.org)")
    int status = client.executeMethod(method);
    if(status != 200) {
      def rde = new ResourceDownloadException("could not get resource $url by HTTP")
      rde.status = status
      rde.url = url
      method.releaseConnection()
      throw rde
    }
    method
  }
  
  protected resolveViaHttp(url) {
    new StringReader(resolveAsString(url))
  }
  
  public resolveAsString(url) {
    try{
      def con = request(url)
      def res = con.getResponseBodyAsString()
      con.releaseConnection()
      res 
    } catch (ResourceDownloadException e) {
      throw e
    } catch (Exception e) {
      throw new ResourceDownloadException(rootCause : e, url : url)
    }
  }
  
}
