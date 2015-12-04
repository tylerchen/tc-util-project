

import java.io.Serializable;

import org.iff.infra.util.mybatis.service.Dao;

@SuppressWarnings("serial")
public class ModelProperty implements Serializable {

	private String id;
	private java.util.Date createTime;
	private int sort;
	private String isNull;
	private String showName;
	private String dbName;
	private String isIndex;
	private String name;
	private String modelid;
	private String defaultValue;
	private String type;
	private String size;

	public ModelProperty() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getIsNull() {
		return isNull;
	}

	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getIsIndex() {
		return isIndex;
	}

	public void setIsIndex(String isIndex) {
		this.isIndex = isIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModelid() {
		return modelid;
	}

	public void setModelid(String modelid) {
		this.modelid = modelid;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public static ModelProperty get(ModelProperty one) {
		return Dao.queryOne("ModelProperty.getModelProperty", one);
	}

	public static void remove(ModelProperty one) {
		Dao.remove("ModelProperty.deleteModelProperty", one);
	}

	public void add() {
		Dao.save("ModelProperty.insertModelProperty", this);
	}

	public void update() {
		Dao.save("ModelProperty.updateModelProperty", this);
	}

	public void remove() {
		Dao.remove("ModelProperty.deleteModelProperty", this);
	}
}
