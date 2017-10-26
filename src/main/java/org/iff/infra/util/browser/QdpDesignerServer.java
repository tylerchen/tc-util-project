/*******************************************************************************
 * Copyright (c) Jun 26, 2017 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.browser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;
import org.apache.http.util.EntityUtils;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.ResourceHelper;
import org.iff.infra.util.StreamHelper;
import org.iff.infra.util.StringHelper;
import org.iff.infra.util.database.DbMetaHelper;
import org.iff.infra.util.database.DescTableModel;

import com.google.common.io.Resources;

/** 
 * Basic, yet fully functional and spec compliant, HTTP/1.1 file server. 
 * { projectBaseDir:
 *   projectConfig: {projectPath, jdbcUrl, userName, password}
 *   data: [
 *        {id, name, packageName, type, children:[
 *           {id, name, packageName, tableName, type, children:[
 *              {id, name, type, remove, fileName, formType, tableName, actions:{
 *              	{actionName, eventName, actionType, shortCut, actionIcon}
 *              }, fields:[
 *                  {label, field, dbType, javaType, length, scale, defValue, isNull, isPk, isIndex, isUnique, isAutoIncrease, isTableColumn, seqNo, description,
 *                   addable, editable, listable, infoable, searchable, sortable, width, wordbreak, dataUrl, jsonData, 
 *                   type:{add, edit, grid, info},  refTable, refField, refLabelField, 
 *                   rule:{type, required, requiredMsg, regex, regexMsg, rangeMin, rangeMax, rangeMsg, length, lengthMsg, enums, enumsMsg, noBlank, noBlankMsg},
 *                  }
 *              ]}
 *           ]}
 *        ]}
 *   ],
 *   tables:{
 *   	tableName: {
 *   		catalog, name, type, remarks, columns: [
 *   			name, type, sqlType, size, remarks, isAutoIncrement, isNullable, isPrimaryKey
 *   		]
 *   	}
 *   }
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jun 26, 2017
 */
public class QdpDesignerServer {

	private static boolean IS_DEV = true;

	public static void main(String[] args) throws Exception {
		///Users/zhaochen/dev/workspace/cocoa/tc-util-project/src/main/resources/qdp-designer
		String docRoot = new File("").getAbsolutePath();
		if (docRoot != null) {
			if (!docRoot.endsWith("tc-util-project")) {
				IS_DEV = false;
			}
		}
		int port = 2020;
		if (args.length >= 2) {
			port = Integer.parseInt(args[1]);
		}

		// Set up the HTTP protocol processor  
		HttpProcessor httpproc = HttpProcessorBuilder.create().add(new ResponseDate())
				.add(new ResponseServer("Test/1.1")).add(new ResponseContent()).add(new ResponseConnControl()).build();

		// Set up request handlers  
		UriHttpRequestHandlerMapper reqistry = new UriHttpRequestHandlerMapper();
		reqistry.register("*", new HttpFileHandler(docRoot));

		// Set up the HTTP service  
		HttpService httpService = new HttpService(httpproc, reqistry);

		SSLServerSocketFactory sf = null;
		if (port == 2443) {
			// Initialize SSL context  
			ClassLoader cl = QdpDesignerServer.class.getClassLoader();
			URL url = cl.getResource("my.keystore");
			if (url == null) {
				System.out.println("Keystore not found");
				System.exit(1);
			}
			KeyStore keystore = KeyStore.getInstance("jks");
			keystore.load(url.openStream(), "secret".toCharArray());
			KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmfactory.init(keystore, "secret".toCharArray());
			KeyManager[] keymanagers = kmfactory.getKeyManagers();
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(keymanagers, null, null);
			sf = sslcontext.getServerSocketFactory();
		}

		Thread t = new RequestListenerThread(port, httpService, sf);
		t.setDaemon(false);
		t.start();
	}

	static class HttpFileHandler implements HttpRequestHandler {

		private final String docRoot;

		public HttpFileHandler(final String docRoot) {
			super();
			this.docRoot = docRoot;
		}

		public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context)
				throws HttpException, IOException {
			String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
			if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
				throw new MethodNotSupportedException(method + " method not supported");
			}
			String target = request.getRequestLine().getUri();
			byte[] entityContent = new byte[0];
			if (request instanceof HttpEntityEnclosingRequest) {
				HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
				entityContent = EntityUtils.toByteArray(entity);
				//System.out.println("Incoming entity content (bytes): " + entityContent.length);
			}
			if (method.equals("POST")) {
				post(response, entityContent);
			} else if (method.equals("GET")) {
				get(response, target);
			}
		}

		private Object result(String status, Object body) {
			return MapHelper.toMap("header", MapHelper.toMap("status", status), "body", body);
		}

		private void out(final HttpResponse response, String status, Object body) {
			response.setStatusCode(HttpStatus.SC_OK);
			StringEntity entity = new StringEntity(GsonHelper.toJsonString(result(status, body)), "UTF-8");
			response.setEntity(entity);
		}

		private void post(final HttpResponse response, byte[] entityContent) throws UnsupportedEncodingException {
			String jsonString = new String(entityContent, "UTF-8");
			System.out.println(jsonString);
			Map<String, Object> json = GsonHelper.toJson(jsonString);
			if (json.get("type").equals("gen-code")) {
				genCode(response, json);
				return;
			}
			if (json.get("type").equals("save-data")) {
				saveData(response, json);
				return;
			}
			if (json.get("type").equals("project-config")) {
				projectConfig(response, json);
				return;
			}
			if (json.get("type").equals("database-config")) {
				databaseConfig(response, json);
				return;
			}
		}

		private void genCode(final HttpResponse response, Map<String, Object> json) {
			Map<String, Object> data = (Map<String, Object>) json.get("data");
			Map<String, Object> pc = (Map<String, Object>) data.get("projectConfig");
			File file = new File((String) pc.get("projectPath"));
			if (!(file.exists() && file.isDirectory())) {
				out(response, "error", "project-config: file not found.");
				return;
			}
			File pom = new File(file, "pom.xml");
			if (!(pom.exists() && pom.isFile())) {
				out(response, "error", "project-config: file not found.");
				return;
			}
			File configFile = new File(file, ".qdp_config");
			if (!configFile.exists()) {
				out(response, "error", "project-config: file not found.");
				return;
			}
			Map qdpConfig = null;
			try {
				qdpConfig = GsonHelper.toJson(StreamHelper.getContent(new FileInputStream(configFile), false));
				String projectPath = StringUtils.defaultIfBlank((String) pc.get("projectPath"), "temppath");
				String artifactId = StringUtils.defaultIfBlank((String) pc.get("artifactId"),
						"demo-project-" + StringHelper.uuid());
				String groupId = StringUtils.defaultIfBlank((String) pc.get("groupId"), "org.iff.demo");
				String version = StringUtils.defaultIfBlank((String) pc.get("version"), "1.0.0");
				String templateVersion = StringUtils.defaultIfBlank((String) pc.get("templateVersion"), "1.0.0");
				{
					if (StringUtils.contains(projectPath, "tc-util-project")) {
						projectPath = "/Users/zhaochen/Desktop/temppath";
					}
					if (projectPath.endsWith(artifactId)) {
						projectPath = projectPath.substring(0, projectPath.length() - artifactId.length());
					}
				}
				qdpConfig.put("projectBaseDir", projectPath);
				qdpConfig.put("projectName", artifactId);
				qdpConfig.put("groupId", groupId);
				qdpConfig.put("artifactId", artifactId);
				qdpConfig.put("version", version);
				QdpCodeGenerator gen = QdpCodeGenerator.create();
				if (IS_DEV) {
					String docRoot = new File("").getAbsolutePath();
					gen.loadResource(
							"file://" + docRoot + "/src/main/resources/qdp-designer/template/" + templateVersion);
				} else {
					gen.loadResource("classpath://qdp-designer/template/" + templateVersion);
				}
				String msg = gen.process(qdpConfig);
				System.out.println(msg);
				out(response, "ok", msg);
				return;
			} catch (Exception e) {
				e.printStackTrace();
				out(response, "error", "read file error: .qdp_config!");
				return;
			}
		}

		private void saveData(final HttpResponse response, Map<String, Object> json) {
			Map<String, Object> data = (Map<String, Object>) json.get("data");
			//projectPath : getItem('projectPath', ''), jdbcUrl : getItem('jdbcUrl', ''), userName : getItem('userName', ''),
			//password : getItem('password', ''), forms : getItem('forms', {}), tables : getItem('tables', {})
			Map<String, Object> pc = (Map<String, Object>) data.get("projectConfig");
			File file = new File((String) pc.get("projectPath"));
			if (file.exists() && file.isDirectory()) {
				File pom = new File(file, "pom.xml");
				if (pom.exists() && pom.isFile()) {
					File configFile = new File(file, ".qdp_config");
					if (!configFile.exists()) {
						try {
							configFile.createNewFile();
						} catch (IOException e) {
							out(response, "error", "create file error: .qdp_config!");
							return;
						}
					}
					{
						String content = GsonHelper.toJsonString(MapHelper.toMap("data", data));
						OutputStreamWriter osw = null;
						FileOutputStream out = null;
						try {
							out = new FileOutputStream(configFile);
							osw = new OutputStreamWriter(out, "UTF-8");
							osw.write(content);
							osw.flush();
						} catch (IOException e) {
							out(response, "error", "write file error: .qdp_config!");
							return;
						} finally {
							StreamHelper.closeWithoutError(osw);
							StreamHelper.closeWithoutError(out);
						}
					}
					try {
						Map<String, Object> root = GsonHelper
								.toJson(StreamHelper.getContent(new FileInputStream(configFile), false));
						out(response, "ok", root);
						return;
					} catch (FileNotFoundException e) {
						out(response, "error", "read file error: .qdp_config!");
						return;
					}
				}
			}
			out(response, "error", "project-config: file not found.");
			return;
		}

		private void databaseConfig(final HttpResponse response, Map<String, Object> json) {
			Map<String, Object> data = (Map<String, Object>) json.get("data");
			Map<String, Object> pc = (Map<String, Object>) data.get("projectConfig");
			String jdbcUrl = (String) pc.get("jdbcUrl");
			String userName = (String) pc.get("userName");
			String password = (String) pc.get("password");
			DataSource dataSource = null;
			try {
				dataSource = DbMetaHelper.create(userName, password, jdbcUrl, "com.mysql.jdbc.Driver", "select 1", 3,
						3);
				Map<String, DescTableModel> allTables = DbMetaHelper.findAllTableDesc(dataSource);
				out(response, "ok", allTables);
				return;
			} catch (Exception e) {
				StringWriter sw = new StringWriter(1024);
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				out(response, "error", "database-config: can't access database: " + sw.toString());
				return;
			} finally {
				try {
					if (dataSource != null && dataSource instanceof BasicDataSource) {
						((BasicDataSource) dataSource).close();
					}
				} catch (Exception e) {
				}
			}
		}

		private void projectConfig(final HttpResponse response, Map<String, Object> json) {
			Map<String, Object> data = (Map<String, Object>) json.get("data");
			Map<String, Object> pc = (Map<String, Object>) data.get("projectConfig");
			File file = new File((String) pc.get("projectPath"));
			if (file.exists() && file.isDirectory()) {
				File pom = new File(file, "pom.xml");
				if (pom.exists() && pom.isFile()) {
					File configFile = new File(file, ".qdp_config");
					if (!configFile.exists()) {
						try {
							configFile.createNewFile();
							String content = GsonHelper.toJsonString(MapHelper.toMap("data", data));
							OutputStreamWriter osw = null;
							FileOutputStream out = null;
							try {
								out = new FileOutputStream(configFile);
								osw = new OutputStreamWriter(out, "UTF-8");
								osw.write(content);
								osw.flush();
							} catch (IOException e) {
								out(response, "error", "write file error: .qdp_config!");
								return;
							} finally {
								StreamHelper.closeWithoutError(osw);
								StreamHelper.closeWithoutError(out);
							}
						} catch (IOException e) {
							out(response, "error", "create file error: .qdp_config!");
							return;
						}
					}
					try {
						Map<String, Object> loadData = GsonHelper
								.toJson(StreamHelper.getContent(new FileInputStream(configFile), false));
						try {
							String pomContent = StreamHelper.getContent(new FileInputStream(pom), false);
							pomContent = StringUtils.contains(pomContent, "</parent>")
									? StringUtils.substringAfter(pomContent, "</parent>") : pomContent;
							//<groupId>org.iff</groupId>
							String groupId = StringUtils.substringAfter(pomContent, "<groupId>");
							groupId = StringUtils.substringBefore(groupId, "</groupId>").trim();
							//<artifactId>tc-util</artifactId>
							String artifactId = StringUtils.substringAfter(pomContent, "<artifactId>");
							artifactId = StringUtils.substringBefore(artifactId, "</artifactId>").trim();
							//<version>1.0.18</version>
							String version = StringUtils.substringAfter(pomContent, "<version>");
							version = StringUtils.substringBefore(version, "</version>").trim();
							if (StringUtils
									.isBlank((String) MapHelper.getByPath(loadData, "data/projectConfig/groupId"))) {
								MapHelper.setByPath(loadData, "data/projectConfig/groupId", groupId);
							}
							if (StringUtils
									.isBlank((String) MapHelper.getByPath(loadData, "data/projectConfig/artifactId"))) {
								MapHelper.setByPath(loadData, "data/projectConfig/artifactId", artifactId);
							}
							if (StringUtils
									.isBlank((String) MapHelper.getByPath(loadData, "data/projectConfig/version"))) {
								MapHelper.setByPath(loadData, "data/projectConfig/version", version);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						out(response, "ok", loadData);
						return;
					} catch (FileNotFoundException e) {
						out(response, "error", "read file error: .qdp_config!");
						return;
					}
				}
			}
			out(response, "error", "project-config: file not found.");
			return;
		}

		private void get(final HttpResponse response, String target) throws UnsupportedEncodingException {
			String fileName = StringUtils.substringBeforeLast(target, "?");
			String fileUrl = null;
			if (IS_DEV) {
				fileUrl = "file://" + new File("").getAbsolutePath() + "/src/main/resources/qdp-designer"
						+ URLDecoder.decode(fileName, "UTF-8");
			} else {
				List<String> loadResources = ResourceHelper.loadResources(
						"classpath://qdp-designer" + URLDecoder.decode(fileName, "UTF-8"), "*", "*", null);
				fileUrl = loadResources.size() > 0 ? loadResources.get(0) : null;
			}
			URL url = null;
			if (fileUrl != null) {
				try {
					url = ResourceHelper.url(fileUrl);
				} catch (Exception e) {
					Exceptions.runtime("resource to url error.", e);
				}
			} else {
				try {
					url = getClass().getResource("/qdp-designer" + URLDecoder.decode(fileName, "UTF-8"));
				} catch (Exception e) {
					Exceptions.runtime("resource to url error.", e);
				}
			}
			if (url == null) {
				response.setStatusCode(HttpStatus.SC_NOT_FOUND);
				StringEntity entity = new StringEntity(
						"<html><body><h1>File " + fileName + " not found</h1></body></html>",
						ContentType.create("text/html", "UTF-8"));
				response.setEntity(entity);
				System.out.println("File " + fileName + " not found");

			} else {
				InputStream inputStream = null;
				try {
					inputStream = url.openStream();
					response.setStatusCode(HttpStatus.SC_OK);
					String contentType = org.iff.infra.util.ContentType.getContentType(fileName, "text/html");
					InputStreamEntity body = new InputStreamEntity(inputStream,
							ContentType.create(contentType, (Charset) Charset.forName("UTF-8")));
					response.addHeader("Pragma", "no-cache");
					response.setEntity(body);
				} catch (Exception e) {
					response.setStatusCode(HttpStatus.SC_FORBIDDEN);
					StringEntity entity = new StringEntity("<html><body><h1>Access denied</h1></body></html>",
							ContentType.create("text/html", "UTF-8"));
					response.setEntity(entity);
					System.out.println("Cannot read file " + fileName);
				}
			}
		}

	}

	static class RequestListenerThread extends Thread {

		private final HttpConnectionFactory<DefaultBHttpServerConnection> connFactory;
		private final ServerSocket serversocket;
		private final HttpService httpService;

		public RequestListenerThread(final int port, final HttpService httpService, final SSLServerSocketFactory sf)
				throws IOException {
			this.connFactory = DefaultBHttpServerConnectionFactory.INSTANCE;
			this.serversocket = sf != null ? sf.createServerSocket(port) : new ServerSocket(port);
			this.httpService = httpService;
		}

		@Override
		public void run() {
			System.out.println("Listening on port " + this.serversocket.getLocalPort());
			while (!Thread.interrupted()) {
				try {
					// Set up HTTP connection  
					Socket socket = this.serversocket.accept();
					//System.out.println("Incoming connection from " + socket.getInetAddress());
					HttpServerConnection conn = this.connFactory.createConnection(socket);

					// Start worker thread  
					Thread t = new WorkerThread(this.httpService, conn);
					t.setDaemon(true);
					t.start();
				} catch (InterruptedIOException ex) {
					break;
				} catch (IOException e) {
					System.err.println("I/O error initialising connection thread: " + e.getMessage());
					break;
				}
			}
		}
	}

	static class WorkerThread extends Thread {

		private final HttpService httpservice;
		private final HttpServerConnection conn;

		public WorkerThread(final HttpService httpservice, final HttpServerConnection conn) {
			super();
			this.httpservice = httpservice;
			this.conn = conn;
		}

		@Override
		public void run() {
			//System.out.println("New connection thread");
			HttpContext context = new BasicHttpContext(null);
			try {
				while (!Thread.interrupted() && this.conn.isOpen()) {
					this.httpservice.handleRequest(this.conn, context);
				}
			} catch (ConnectionClosedException ex) {
				//System.err.println("Client closed connection");
			} catch (IOException ex) {
				System.err.println("I/O error: " + ex.getMessage());
			} catch (HttpException ex) {
				System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
			} finally {
				try {
					this.conn.shutdown();
				} catch (IOException ignore) {
				}
			}
		}

	}

}