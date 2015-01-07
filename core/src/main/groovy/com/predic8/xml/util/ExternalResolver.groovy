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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.predic8.schema.Import as SchemaImport
import com.predic8.schema.Include as SchemaInclude
import com.predic8.wsdl.Import as WsdlImport
import com.predic8.wadl.Include as WadlInclude

class ExternalResolver extends ResourceResolver {

	private static final Logger log = LoggerFactory.getLogger(ExternalResolver.class)

	String proxyHost
	int proxyPort
	int timeout = 10000

	def resolve(input, baseDir) {
		
		if ( input instanceof SchemaImport || input instanceof SchemaInclude) {
			if ( !input.schemaLocation ) return
				input = input.schemaLocation
		}
		
		if(input instanceof WsdlImport) {
			if ( !input.location ) return
			input = input.location
		}

		if (input instanceof InputStream )  {
			log.debug("resolving from reader, baseDir: $baseDir")
			return fixUtf(input);
		}

		log.debug("resolving: $input, baseDir: $baseDir")

		if(input instanceof File){
			return fixUtf(new FileInputStream(input))
		}

		if (! input instanceof String)
			throw new RuntimeException("Do not know how to resolve $input")

		if(input.startsWith('file')) {
			return fixUtf(new FileInputStream(new URL(input).getPath()))
		}

		if(input.startsWith('http:') || input.startsWith('https:')) {
			return resolveViaHttp(input)
		}
		
		if(input.matches(/^([A-Z]|[a-z]):\/.*$/) || input.startsWith('/') || input.startsWith('\\') ){
			return resolveAsFile(input, null)
		}

		if(baseDir && (baseDir.startsWith('http') || baseDir.startsWith('https'))) {
			return resolveViaHttp(baseDir + input)
		}
		resolveAsFile(input, baseDir)
	}

	public InputStream resolveAsFile(java.lang.String filename, java.lang.String baseDir) {
		if(baseDir) {
			return fixUtf(new FileInputStream(new File(baseDir,filename)))
		}
		fixUtf(new FileInputStream(new File(filename)))
	}

	protected resolveViaHttp(url) {
		URI uri = new URI(url)
		uri = uri.normalize()
		new StringReader(resolveAsString(uri.toString()))
	}

	private resolveAsString(url) {
		try{
			HttpResponse con = request(url)
			EntityUtils.toString(con.entity)
		} catch (ResourceDownloadException e) {
			throw e
		} catch (Exception e) {
			throw new ResourceDownloadException(rootCause : e, url : url)
		}
	}

	private request(url) {
		HttpClient client = new DefaultHttpClient();
		if ( proxyHost ) {
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);

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
}
