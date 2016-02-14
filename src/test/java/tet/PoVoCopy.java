/*******************************************************************************
 * Copyright (c) Jan 27, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package tet;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jan 27, 2016
 */
public interface PoVoCopy {

	<T> T copy(Object dest, Object src);
}
