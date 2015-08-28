/*******************************************************************************
 * Copyright (c) Aug 9, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.moduler;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iff.infra.util.FCS;
import org.iff.infra.util.SocketHelper;
import org.iff.infra.util.moduler.TCModule.TCActionInvoker;
import org.iff.infra.util.moduler.TCRenderManager.TCRender;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Aug 9, 2015
 */
public class TCActionHandler {

	public static class TCChain {
		protected TCChain chain;

		public Object process(Map params) {
			return null;
		}
	}

	public static class TCBeforeActionHandler extends TCChain {
		public Object process(Map params) {
			return chain == null ? true : chain.process(params);
		}
	}

	public static class TCProcessActionHandler extends TCChain {
		public Object process(Map params) {
			HttpServletRequest request = (HttpServletRequest) params.get("request");
			HttpServletResponse response = (HttpServletResponse) params.get("response");
			String moduleName = (String) params.get("moduleName");
			{
				String target = (String) params.get("target");
				TCActionInvoker actionInvoker = TCModuleManager.me().get(moduleName).getAction(target, params);
				if (actionInvoker != null) {
					PrintWriter writer = null;
					try {
						writer = response.getWriter();
						Object value = actionInvoker.invoke(new Object[0]);
						if (value != null && value instanceof TCRender) {
							((TCRender) value).render();
						}
						if (response.getContentType() == null || response.getContentType().length() < 1) {
							response.setContentType("text/html; charset=UTF-8");
						}
					} catch (Exception e) {
						e.printStackTrace(writer);
					} finally {
						SocketHelper.closeWithoutError(writer);
					}
				} else {
					PrintWriter writer = null;
					try {
						writer = response.getWriter();
						writer.write(FCS.get("<html><body><h1>Target not found</h1><div>{0}</div></body></html>",
								target).toString());
					} catch (Exception e) {
						e.printStackTrace(writer);
					} finally {
						SocketHelper.closeWithoutError(writer);
					}
				}
			}
			return chain == null ? true : chain.process(params);
		}
	}

	public static class TCAfterActionHandler extends TCChain {
		public Object process(Map params) {
			return chain == null ? true : chain.process(params);
		}

	}
}
