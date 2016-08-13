/*******************************************************************************
 * Copyright (c) Jul 12, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.mybatis.plugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.MapHelper;

/**
 * Page query
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Jul 12, 2016
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@XmlRootElement(name = "Page")
public class Page implements Serializable, Cloneable {
	/* 显示数目 */
	private static int PAGE_SIZE_DEFAULT = 10;
	/* 当页显示数目 */
	private int pageSize = PAGE_SIZE_DEFAULT;
	/* 总记录数 */
	private int totalCount;
	/* 当前页 */
	private int currentPage;
	/* 记录偏移量 */
	private int offset;
	private boolean offsetPage = false;
	/* 分页结果 */
	private List rows = new ArrayList();
	/*order by*/
	private List<Map> orderBy = new ArrayList<Map>();

	public Page() {
	}

	/**
	 * create a pageable query, this page query will get the total count.
	 * @param pageSize
	 * @param currentPage
	 * @param totalCount
	 * @param rows
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 12, 2016
	 */
	public static Page pageable(int pageSize, int currentPage, int totalCount, List rows) {
		Page page = new Page();
		{
			pageSize = pageSize < 1 ? PAGE_SIZE_DEFAULT : pageSize;
			page.setPageSize(pageSize);
			currentPage = currentPage < 1 ? 1 : currentPage;
			page.setCurrentPage(currentPage);
			totalCount = totalCount < -1 ? 0 : totalCount;
			page.setTotalCount(totalCount);
			rows = rows == null ? page.getRows() : rows;
			page.setRows(rows);
			page.setTotalCount(Math.max(rows.size(), totalCount));
		}
		return page;
	}

	/**
	 * create a offset query, this page query will NOT get the total count.
	 * @param offset
	 * @param pageSize
	 * @param rows
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Jul 12, 2016
	 */
	public static Page offsetPage(int offset, int pageSize, List rows) {
		Page page = new Page();
		{
			offset = offset < 1 ? 0 : offset;
			page.setOffset(offset);
			pageSize = pageSize < 1 ? PAGE_SIZE_DEFAULT : pageSize;
			page.setPageSize(pageSize);
			rows = rows == null ? page.getRows() : rows;
			page.setRows(rows);
			page.setTotalCount(rows.size());
			page.offsetPage = true;
		}
		return page;
	}

	public boolean isOffsetPage() {
		return offsetPage;
	}

	public int getOffset() {
		if (isOffsetPage()) {
			return offset;
		} else {
			return Math.max((currentPage - 1) * pageSize, 0);
		}
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	// @Override org.apache.ibatis.session.RowBounds.getLimit()
	public int getLimit() {
		return pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getCurrentPage() {
		return currentPage < 1 ? 1 : currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		if (getTotalCount() == 0) {
			return 0;
		}
		int totalPage = (this.getTotalCount() + pageSize - 1) / pageSize;
		return totalPage;
	}

	public <T> List<T> getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public List<Map> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(List<Map> orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * add asc order by.
	 * @param name
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 12, 2016
	 */
	public Page addAscOrderBy(String name) {
		orderBy.add(MapHelper.toMap("name", name, "order", "asc"));
		return this;
	}

	/**
	 * add desc order by.
	 * @param name
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 12, 2016
	 */
	public Page addDescOrderBy(String name) {
		orderBy.add(MapHelper.toMap("name", name, "order", "desc"));
		return this;
	}

	/**
	 * convert row list object to other object.
	 * @param voClass
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 12, 2016
	 */
	public Page toPage(Class<?> voClass) {
		if (this.rows == null || this.rows.isEmpty()) {
			return this;
		}
		List list = new ArrayList(this.rows.size());
		for (Object o : this.rows) {
			try {
				list.add(BeanHelper.copyProperties(voClass.getConstructor().newInstance(), o));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (isOffsetPage()) {
			return Page.offsetPage(offset, pageSize, list);
		}
		return Page.pageable(pageSize, currentPage, totalCount, list);
	}

	public Page clone() {
		if (isOffsetPage()) {
			return Page.offsetPage(offset, pageSize, rows);
		}
		return Page.pageable(pageSize, currentPage, totalCount, rows);
	}

	public String toString() {
		return "Pages [currentPage=" + currentPage + ", pageSize=" + pageSize + ", rows=" + rows + ", totalCount="
				+ totalCount + "]";
	}

	/**
	 * return a not null page. if the page is null then return a default one.
	 * @param page
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 12, 2016
	 */
	public static Page notNullPage(Page page) {
		if (page == null) {
			return Page.offsetPage(0, 10, null);
		}
		return page;
	}
}
