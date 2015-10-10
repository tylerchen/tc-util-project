/*******************************************************************************
 * Copyright (c) 2014-9-28 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since 2014-9-28
 */
public class PaginationHelper {

	/**
	 * @param pageSize
	 * @param totalCount
	 * @param currentPage start from 1
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since 2015-3-16
	 */
	public static List<Number> middleCurrentPage(long pageSize, long totalCount, long currentPage) {
		List<Number> list = new ArrayList<Number>();
		if (totalCount <= 1) {
			list.add(1);
			list.add(1);
			list.add(1);
			return list;
		}
		pageSize = pageSize < 1 ? 10 : pageSize;
		long totalPage = (pageSize > 0 ? (totalCount + pageSize - 1) / pageSize : 0);
		currentPage = Math.max(currentPage, 1);
		currentPage = currentPage > totalPage ? totalPage : currentPage;
		List<Number> pageLink = new ArrayList<Number>();
		long start = Math.max(1, currentPage - 5), end = Math.min(currentPage + 5, totalPage), pos = 0;
		for (; start <= end; start++) {
			pageLink.add(start);
			pos = start == currentPage ? pageLink.size() - 1 : pos;
		}
		long startOffset = 0, endOffset = 0, len = pageLink.size();
		for (long i = 1; i <= 4; i++) {
			startOffset = pos - i >= 0 ? i : startOffset;
			endOffset = pos + i <= len - 1 ? i : endOffset;
			if (startOffset + endOffset == 4) {
				break;
			}
		}
		pageLink = pageLink.subList((int) (pos - startOffset), (int) (pos + endOffset + 1));
		{
			list.add(1);
			list.addAll(pageLink);
			list.add(totalPage);
		}
		return list;
	}

	public static void main(String[] args) {
		System.out.println(middleCurrentPage(10, 0, 2));
	}
}