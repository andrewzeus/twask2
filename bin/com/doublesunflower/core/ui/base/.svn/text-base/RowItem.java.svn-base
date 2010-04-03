package com.doublesunflower.core.ui.base;

import java.util.HashMap;

import android.graphics.drawable.Drawable;

public class RowItem extends HashMap<String,Object> {
	public static final String ROW_TEXT_1 = "rowText1";
	public static final String ROW_TEXT_2 = "rowText2";
	public static final String ICON = "icon";
	public static final String TAG = "tag";
	public static final String ID = "id";
	
	boolean selectable = true;
    
    public RowItem(
    		String rowText1,
    		String rowText2,
    		Drawable icon){
    	this.put(ROW_TEXT_1, rowText1);
    	this.put(ROW_TEXT_2, rowText2);
    	this.put(ICON, icon);
    }
    
    public RowItem(
    		String rowText1,
    		String rowText2,
    		Drawable icon,
    		Object tag,
    		Long id){
    	this.put(ROW_TEXT_1, rowText1);
    	this.put(ROW_TEXT_2, rowText2);
    	this.put(ICON, icon);
    	this.put(TAG, tag);
    	this.put(ID, id);
    }

	public Drawable getIcon() {
		if((this.get(ICON) != null)&&
       		 (this.get(ICON).getClass().equals(Drawable.class)))
			return (Drawable)this.get(ICON);
		return null;
	}

	public void setIcon(Drawable icon) {
		this.put(ICON, icon);
	}

	public Long getId() {
		if((this.get(ID) != null)&&
       		 (this.get(ID).getClass().equals(Long.class)))
			return (Long)this.get(ID);
		return null;
	}

	public void setId(Long id) {
		this.put(ID, id);
	}

	public String getRowText1() {
		if(this.get(ROW_TEXT_1) != null)
			return this.get(ROW_TEXT_1).toString();
		return null;
	}

	public void setRowText1(String rowText1) {
		this.put(ROW_TEXT_1, rowText1);
	}

	public String getRowText2() {
		if(this.get(ROW_TEXT_2) != null)
			return this.get(ROW_TEXT_2).toString();
		return null;
	}

	public void setRowText2(String rowText2) {
		this.put(ROW_TEXT_2, rowText2);
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public Object getTag() {
		if(this.get(TAG) != null)
			return this.get(TAG);
		return null;
	}

	public void setTag(Object tag) {
		this.put(TAG, tag);
	}

}
