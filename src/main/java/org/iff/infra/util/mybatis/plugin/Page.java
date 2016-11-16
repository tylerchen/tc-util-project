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
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
public class Page<T> implements Serializable, Cloneable {
	/* 显示数目 */
	public static final int PAGE_SIZE_DEFAULT = 10;
	/* 当页显示数目 */
	protected int pageSize = PAGE_SIZE_DEFAULT;
	/* 总记录数 */
	protected int totalCount;
	/* 当前页 */
	protected int currentPage;
	/* 记录偏移量 */
	protected int offset;
	protected boolean offsetPage = false;
	/* 分页结果 */
	@XmlElementWrapper(name = "rows")
	@XmlElement(name = "row")
	protected List<T> rows = new ArrayList<T>();
	/*order by*/
	protected List<LinkedHashMap> orderBy = new ArrayList<LinkedHashMap>();

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

	/*@Override org.apache.ibatis.session.RowBounds.getLimit()*/
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

	public List<T> getRows() {
		return (List<T>) rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public List<LinkedHashMap> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(List<LinkedHashMap> orderBy) {
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
		orderBy.add((LinkedHashMap) MapHelper.toMap("name", name, "order", "asc"));
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
		orderBy.add((LinkedHashMap) MapHelper.toMap("name", name, "order", "desc"));
		return this;
	}

	/**
	 * convert row list object to other object.
	 * @param voClass
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 12, 2016
	 */
	public <E> Page<E> toPage(Class<E> voClass) {
		if (this.rows == null) {
			setRows(new ArrayList<T>());
		}
		List<E> list = new ArrayList<E>(this.rows.size());
		for (Object o : this.rows) {
			try {
				list.add((E) BeanHelper.copyProperties(voClass.getConstructor().newInstance(), o));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Page<E> page = null;
		if (isOffsetPage()) {
			page = Page.offsetPage(offset, pageSize, list);
		} else {
			page = Page.pageable(pageSize, currentPage, totalCount, list);
		}
		page.getOrderBy().addAll(this.getOrderBy());
		return page;
	}

	public Page<T> clone() {
		Page<T> page = null;
		if (isOffsetPage()) {
			page = Page.offsetPage(offset, pageSize, rows);
		} else {
			page = Page.pageable(pageSize, currentPage, totalCount, rows);
		}
		page.getOrderBy().addAll(this.getOrderBy());
		return page;
	}

	public String toString() {
		return "Page [pageSize=" + pageSize + ", totalCount=" + totalCount + ", currentPage=" + currentPage
				+ ", offset=" + offset + ", offsetPage=" + offsetPage + ", rows=" + rows + ", orderBy=" + orderBy + "]";
	}

	/**
	 * return a not null page. if the page is null then return a default one.
	 * @param page
	 * @return
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since Aug 12, 2016
	 */
	public static <E> Page<E> notNullPage(Page<E> page) {
		if (page == null) {
			return Page.offsetPage(0, 10, new ArrayList<E>());
		}
		return page;
	}
}
