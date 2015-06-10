
package com.example.mysampleapp;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextArea;

@SuppressWarnings("serial")
@Theme("RuntimeAnalysis")
public class RuntimeAnalysis extends GridLayout implements View
{
	  MysampleappUI mainObj;
	public   RuntimeAnalysis(MysampleappUI objmain) 
	{   
		mainObj=objmain;
		
		setRows(12);
		setColumns(8);
		setSizeFull();
		setMargin(true);
	
	Button btnrun = new Button("Run");	
	TextArea textstaticanalysis=new TextArea("");
	textstaticanalysis.setSizeFull();
	
	addComponent(textstaticanalysis,1,1,6,10);
	addComponent(btnrun,1,11,3,11);

	}
		@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
