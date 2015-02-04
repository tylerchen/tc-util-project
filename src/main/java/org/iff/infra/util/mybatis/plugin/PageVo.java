package org.iff.infra.util.mybatis.plugin;

import java.util.ArrayList;
import java.util.List;

public class PageVo {

	private int pageSize = 0; // 当页显示数目
	private int totalCount; // 总记录数
	private int currentPage; // 当前页
	private int offset; // 记录偏移量

	/** 分页结果 */
	private List Rows = new ArrayList();

	public PageVo(int pageSize, int totalCount, int currentPage, List Rows){
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.currentPage = currentPage;
		this.Rows = Rows;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public List getRows() {
		return Rows;
	}

	public void setRows(List rows) {
		Rows = rows;
	}

	
}
