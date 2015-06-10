package com.example.mysampleapp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextArea;

@SuppressWarnings("serial")
@Theme("Instrumentation")
public class Instrumentation extends GridLayout implements View
{    
	
	  MysampleappUI mainObj;
	  static String filename;
	public   Instrumentation(MysampleappUI objmain) 
	{   
		mainObj=objmain;
		System.out.println("instrumentation reached");	
		//GridLayout glayout = new GridLayout(8,12);
		setRows(12);
		setColumns(8);
		setSizeFull();
		setMargin(true);
		TextArea textstaticanalysis=new TextArea();
		textstaticanalysis.setSizeFull();
			
		
	try {
		File analysedfile=new File("C:\\Users\\subash\\Documents\\HiwiApp\\StaticAnalysis\\18-05-2015 21-14-586665426477859404576.txt");
		
		FileOutputStream io=new FileOutputStream(analysedfile);
		System.out.println(io.toString());
		//textstaticanalysis.setValue(io.toString());
	 FileReader reader;
	 
		reader = new FileReader("C:\\Users\\subash\\Documents\\HiwiApp\\StaticAnalysis\\18-05-2015 21-14-586665426477859404576.txt");
		System.out.println(reader.toString());
		textstaticanalysis.setValue("The file is analysed");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	
	//Button btninstrument=new Button("Instrument");
	Button btninstrument = new Button("Instrument", new Button.ClickListener()
	{

        @Override
        public void buttonClick(ClickEvent event) {
        	createDirectory("samplename");
        }
    });
	
	
	addComponent(textstaticanalysis,1,1,6,10);
	addComponent(btninstrument,1,11,3,11);
	

	}
	protected void createDirectory(String Name)
    {
    	File directory = new File("C:\\Users\\subash\\Documents\\HiwiApp\\"+Name+"\\Instrumentation");
    	//File directory = new File("C:\\Users\\subash\\Documents\\HiwiApp\\App1");
    	try {
    		if (!directory.exists()) {
        		if (directory.mkdirs()) {
        			System.out.println("Directory is created!");
        			
        		} else {
        			System.out.println("Failed to create directory! which is ::"+"C:\\Users\\subash\\Documents\\HiwiApp\\"+Name+"\\Instrumentation");
        		}
        	}


            String date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date());
            System.out.println(date);
			File.createTempFile(Name+date, ".txt", directory) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
    }
	@Override
	public void enter(ViewChangeEvent event) 
	{
	if(event.getParameters() != null)
	{
        // split at "/", add each part as a label
        String[] msgs = event.getParameters().split("/");
        for (String msg : msgs)
        {
        	filename= msg;
        	System.out.println("enter view changeevent "+filename);
        }
	}
        
		// TODO Auto-generated method stub
		
	}
}
