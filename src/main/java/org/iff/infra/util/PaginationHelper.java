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
 * <pre>
 * totalPage, totalCount, pageSize, currentPage: [cp, +1, +2, +3, +4] [-2, -1, cp, +1, +2] [-4, -3, -2, -1, cp]
 * var cp=currentPage, pageLink=[cp-2, cp-1, cp, cp+1, cp+2];//先把5页准备好
 * for(var i=0;i<pageLink.length;i++){ (pageLink[i]<1||pageLink[i]>totalPage) && (pageLink.splice(i,1) && (i=i-1)); }//移除不超范围的分页
 * if( pageLink.length<5 && pageLink[0]>(cp-2) ){ (cp+3)<=totalPage && pageLink.push(cp+3); }//如果移除了前面的，则添加后面分页，最多两页
 * if( pageLink.length<5 && pageLink[0]>(cp-2) ){ (cp+4)<=totalPage && pageLink.push(cp+4); }//如果移除了前面的，则添加后面分页，最多两页
 * if( pageLink.length<5 ){ (cp-3)>0 && pageLink.splice(0,0,cp-3);}//如果移除了后面的，则添加前面分页，最多两页
 * if( pageLink.length<5 ){ (cp-4)>0 && pageLink.splice(0,0,cp-4); }//如果移除了后面的，则添加前面分页，最多两页
 * 总记录: {{totalCount}}, 总页数: {{totalPage}}
 * for(var i=0; i<pageLink.length;i++){
 * if(i==0){
 *   <a href="?pn={{1}}" class="page_up" title="首页"><<</a>
 * }
 * if(pageLink[i]==currentPage){
 *   <span class="page_cur">{{currentPage}}
 * } else {
 *   <a href="?pn={{pageLink[i]}}">{{pageLink[i]}}</a>
 * }
 * if(i==pageLink.length-1){
 *   <a href="?pn={{totalPage}}" class="page_down" title="末页">>></a>
 * }
 * }
 * </pre>
 */
public class PaginationHelper {

	/**
	 * return 7 page numbers, and set currentPage on the midle.
	 * <pre>
	 * middleCurrentPage(10, 1000, 20) => [1, 18, 19, 20, 21, 22, 100]
	 * middleCurrentPage(10, 0, 20) => [1, 1, 1]
	 * </pre>
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
		System.out.println(middleCurrentPage(10, 1000, 20));
	}
}