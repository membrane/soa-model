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
import org.apache.commons.logging.*
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.predic8.schema.*
import com.predic8.soamodel.Consts;
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
    HttpClient client = new DefaultHttpClient();
    if ( proxyHost ) {
	  HttpHost proxy = new HttpHost(proxyHost, proxyPort);
	  client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }
	HttpParams params = client.getParams();
	HttpConnectionParams.setConnectionTimeout(params, 10000);
    
    HttpGet method = new HttpGet(url);
    method.setHeader("User-Agent", "SOA Model (see http://membrane-soa.org)")
	HttpResponse response = client.execute(method)
    if(response.statusLine.statusCode != 200) {
      def rde = new ResourceDownloadException("could not get resource $url by HTTP")
      rde.status = status
      rde.url = url
      method.releaseConnection()
      throw rde
    }
    response
  }
  
  protected resolveViaHttp(url) {
  	URI uri = new URI(url)
  	uri.normalize()
    new StringReader(resolveAsString(uri.toString()))
  }
  
  public resolveAsString(url) {
    try{
      HttpResponse con = request(url)
      EntityUtils.toString(con.entity)
    } catch (ResourceDownloadException e) {
      throw e
    } catch (Exception e) {
      throw new ResourceDownloadException(rootCause : e, url : url)
    }
  }

}
