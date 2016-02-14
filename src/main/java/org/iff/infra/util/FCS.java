/*******************************************************************************
 * Copyright (c) 2014-3-14 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

/**
 * <pre>
 * Usage:
 * Example: FCS.get(FCS.get("fuck {0},{1}", "ie6"), "ie7")
 * Expect : fuck ie6,ie7
 * </pre>
 * <pre>
 * Depends: com.foreveross.infra.util.StringHelper
 * </pre>
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-3-14
 */
@SuppressWarnings("serial")
public class FCS extends FormatableCharSequence {
	protected FCS(CharSequence formatString, Object[] parameters) {
		super(formatString, parameters);
	}

}
