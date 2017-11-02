/*******************************************************************************
 * Copyright (c) Nov 30, 2015 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.infra.util.database;

import java.io.Serializable;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since Nov 30, 2015
 */
public class Field implements Serializable {
	private String name;
	private String type;
	private Integer length;
	private Integer scale;
	private String isNullable;
	private String comment;
	private String defaultValue;
	private String isPrimaryKey;
	private String isIndex;
	private String isUnique;
	private String isAutoIncrement;
	private String foreignTable;
	private String foreignField;
	private int sort;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getIsPrimaryKey() {
		return isPrimaryKey;
	}

	public void setIsPrimaryKey(String isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public String getIsIndex() {
		return isIndex;
	}

	public void setIsIndex(String isIndex) {
		this.isIndex = isIndex;
	}

	public String getIsUnique() {
		return isUnique;
	}

	public void setIsUnique(String isUnique) {
		this.isUnique = isUnique;
	}

	public String getIsAutoIncrement() {
		return isAutoIncrement;
	}

	public void setIsAutoIncrement(String isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	public String getForeignTable() {
		return foreignTable;
	}

	public void setForeignTable(String foreignTable) {
		this.foreignTable = foreignTable;
	}

	public String getForeignField() {
		return foreignField;
	}

	public void setForeignField(String foreignField) {
		this.foreignField = foreignField;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "Field [name=" + name + ", type=" + type + ", length=" + length + ", scale=" + scale + ", isNullable="
				+ isNullable + ", comment=" + comment + ", defaultValue=" + defaultValue + ", isPrimaryKey="
				+ isPrimaryKey + ", isIndex=" + isIndex + ", isUnique=" + isUnique + ", isAutoIncrement="
				+ isAutoIncrement + ", foreignTable=" + foreignTable + ", foreignField=" + foreignField + ", sort="
				+ sort + "]";
	}

}
