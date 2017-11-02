/*******************************************************************************
 * Copyright (c) Nov 30, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 30, 2015
 */
public class Table implements Serializable, Comparable<Table> {
	private String name;
	private String comment;
	private String engine;
	private String charset;
	private List<Field> fields = new ArrayList<Field>();
	private int sort;
	private String orginScript;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getOrginScript() {
		return orginScript;
	}

	public void setOrginScript(String orginScript) {
		this.orginScript = orginScript;
	}

	@Override
	public String toString() {
		return "Table [name=" + name + ", comment=" + comment + ", engine=" + engine + ", charset=" + charset
				+ ", fields=" + fields + ", sort=" + sort + ", orginScript=" + orginScript + "]";
	}

	@Override
	public int compareTo(Table other) {
		if (other == null) {
			return -1;
		}
		return StringUtils.defaultString(getName(), "").compareToIgnoreCase(StringUtils.defaultString(other.getName()));
	}
}
