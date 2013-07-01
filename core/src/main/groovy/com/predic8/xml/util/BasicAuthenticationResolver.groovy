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

import org.apache.commons.httpclient.params.*
import org.apache.commons.httpclient.methods.*
import org.apache.commons.httpclient.*
import org.apache.commons.httpclient.auth.*
import org.apache.commons.logging.*
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.predic8.schema.Import as SchemaImport
import com.predic8.wsdl.Import as WsdlImport
import com.predic8.io.*

class BasicAuthenticationResolver extends ResourceResolver {
  
  private Log log = LogFactory.getLog(this.class)
  
  def baseDir = ''
  def username = ''
  def password = ''
  def proxyHost
  def proxyPort
  
  def resolve(input) {
    if ( input instanceof SchemaImport ) {
      if ( !input.schemaLocation ) return 
      input = input.schemaLocation
    }
		
    if ( input instanceof WsdlImport ) {
    	if ( !input.location ) return 
    			input = input.location
    }

		if ( input instanceof InputStream )  {
      log.debug("resolving from reader, baseDir: $baseDir")
      return input;
		}
		
		if(input instanceof Reader) {
			throw new RuntimeException("Please use an InputStream instead of Reader!")
		}
    
    log.debug("resolving: $input, baseDir: $baseDir")
    
    if(input instanceof File){
      return new FileInputStream(input)
    } 
    if (! input instanceof String) 
      throw new RuntimeException("Do not know how to resolve $input")
    
    if(input.startsWith('file')){
    	def url = new URL(input)
      return new FileInputStream(url.getPath())
    } else if(input.startsWith('http') || input.startsWith('https')) {
      return resolveViaHttp(input)
    } else {
      if(baseDir && (baseDir.startsWith('http') || baseDir.startsWith('https'))){
        return resolveViaHttp(baseDir + input)
      } else if(baseDir) {
        return new FileInputStream(baseDir+input)
      }
      else {
        def file = new File(input)
        return new FileInputStream(file.getAbsolutePath())
      }
    }
  }
	
  private request(url) {
    HttpClient client = new DefaultHttpClient();
    if ( username ) {
      client.params.authenticationPreemptive=true
      Credentials defaultcreds = new UsernamePasswordCredentials(username, password)
      client.state.setCredentials(AuthScope.ANY, defaultcreds)
    }
    if ( proxyHost )
      client.getHostConfiguration().setProxy(proxyHost, proxyPort)
    
    HttpGet method = new HttpGet(url);
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
    /*def con = request(url)
    def res = con.getResponseBodyAsStream()
    //con.releaseConnection()
    res*/
    new StringReader(resolveAsString(url))
  }
  
  public resolveAsString(url) {
    def con = request(url)
    def res = con.getResponseBodyAsString()
    con.releaseConnection()
    res    
  }
  
}
