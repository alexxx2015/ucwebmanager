package com.example.mysampleapp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;

@SuppressWarnings("serial")
@Theme("mysampleapp")
public class Instrumentation extends GridLayout implements View
{    
	
	  MysampleappUI mainObj;
	  static String filename;
	  String Instrumentationpath;
	public   Instrumentation(MysampleappUI objmain) 
	{   ServletContext serv= VaadinServlet.getCurrent().getServletContext();
	    String PropertFilePath=serv.getInitParameter("propertyfilePath");
	    
		mainObj=objmain;
		System.out.println("instrumentation reached");	
		//GridLayout glayout = new GridLayout(8,12);
		setRows(12);
		setColumns(8);
		setSizeFull();
		setMargin(true);
		Label lblwelcome=new Label("Instrumentation");
		lblwelcome.setStyleName("labelwelcome");
		addStyleName("backColorGrey");
		TextArea textstaticanalysis=new TextArea();
		textstaticanalysis.setSizeFull();
		Button btnMain=new Button("Main");
		this.setCaption("Instrumentation Page");
		
			
		
	try {
		Properties prop = new Properties();
		
		String propFileName = "Config.properties";
		 
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		prop.load(inputStream);
		Instrumentationpath = prop.getProperty("InstrumentationPath");
		File analysedfile=new File("C:\\Users\\subash\\Documents\\HiwiApp\\StaticAnalysis\\18-05-2015 21-14-586665426477859404576.txt");
		
		FileOutputStream io=new FileOutputStream(analysedfile);
		System.out.println(io.toString());
		//textstaticanalysis.setValue(io.toString());
	 FileReader reader;
	 
		reader = new FileReader("C:\\Users\\subash\\Documents\\HiwiApp\\StaticAnalysis\\18-05-2015 21-14-586665426477859404576.txt");
		System.out.println(reader.toString());
		textstaticanalysis.setValue("The file is analysed");
	} catch ( IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	
	//Button btninstrument=new Button("Instrument");
	Button btninstrument = new Button("Instrument", new Button.ClickListener()
	{

        @Override
        public void buttonClick(ClickEvent event) {
        	createDirectory(filename);
        }
    });
	btnMain.addClickListener(new Button.ClickListener() 
	{
		public void buttonClick(ClickEvent event) 
		{ 
			mainObj.navigator.navigateTo("NextPage");
	       	}
	});
	addComponent(lblwelcome,1,0,3,0);
	addComponent(btnMain,1,1,3,1);
	addComponent(textstaticanalysis,1,2,6,10);
	addComponent(btninstrument,1,11,3,11);
	

	}
	protected void createDirectory(String Name)
    {
    	File directory = new File(Instrumentationpath);
    	//File directory = new File("C:\\Users\\subash\\Documents\\HiwiApp\\App1");
    	try {
    		if (!directory.exists()) {
        		if (directory.mkdirs()) {
        			System.out.println("Directory is created!");
        			
        		} else {
        			System.out.println("Failed to create directory! which is ::"+Instrumentationpath+Name);
        		}
        	}


            String date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date());
            Files.write(Paths.get(Instrumentationpath+"/"+Name+date+".txt"),"sample".getBytes());
			//File.createTempFile(Name+date, ".txt", directory) ;
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
