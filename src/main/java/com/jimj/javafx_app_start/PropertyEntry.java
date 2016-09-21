package com.jimj.javafx_app_start;

import org.apache.commons.lang3.StringUtils;

public class PropertyEntry implements Comparable<PropertyEntry>
{
	private String name;
	private String catName;
	private String fieldName;
	private int index;
	private String value;

	public PropertyEntry(String catName, String fieldName, int index, String name, String value)
	{
		super();
		this.catName = catName;
		this.fieldName = fieldName;
		this.index = index;
		this.name = name;
		this.value = value;
	}

	public String getCatName()
	{
		return catName;
	}

	public void setCatName(String catName)
	{
		this.catName = catName;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "PropertyEntry [name=" + name + ", catName=" + catName + ", fieldName=" + fieldName + ", index=" + index
		        + ", value=" + value + "]";
	}

	public String sortKey()
	{
		StringBuilder result = new StringBuilder();
		result.append(StringUtils.leftPad(catName, 10));
		result.append(String.format("%010d", index));
		return result.toString();
	}

	@Override
	public int compareTo(PropertyEntry o)
	{
		int result = sortKey().compareTo(o.sortKey());
		return result;
	}
}
