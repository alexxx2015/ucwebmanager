package com.example.mysampleapp;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class Subwindow  extends Window {

	public String value;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Subwindow() 
	{
		 
	}

}
