
package com.example.mysampleapp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
@Theme("mysampleapp")
public class RuntimeAnalysis extends GridLayout implements View
{
	  MysampleappUI mainObj;
	  static String filename,runtimeoutputpath;
	  
	public   RuntimeAnalysis(MysampleappUI objmain)  
	{   
		mainObj=objmain;
		Properties prop = new Properties();
		
		String propFileName = "Config.properties";
		
		try
		{
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		prop.load(inputStream);
		 runtimeoutputpath = prop.getProperty("Runtimeoutputpath");
		Panel pnlparent =new Panel();
		
		setRows(12);
		setColumns(8);
		setSizeFull();
		setMargin(true);
		addStyleName("backColorGrey");
	Button btnrun = new Button("RunTime Analyse");	
	Button btnmain = new Button("Main");
	TextArea textstaticanalysis=new TextArea("");
	Label lblWelcome =new Label("Run Time Analysis");
	textstaticanalysis.setSizeFull();
	addComponent(lblWelcome,1,0,4,0);
	addComponent(btnmain,1,1,3,1);
	addComponent(textstaticanalysis,1,2,6,10);
	addComponent(btnrun,1,11,3,11);
	
	btnmain.addClickListener(new Button.ClickListener() 
	{
		public void buttonClick(ClickEvent event) 
		{ 
			mainObj.navigator.navigateTo("NextPage");
	       	}
	});
	btnrun.addClickListener(new Button.ClickListener() 
	{
		public void buttonClick(ClickEvent event) 
		{ 
			createDirectory(filename);
						       	
		}
	});
		}
		catch(Exception e){}

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
	}
	protected void createDirectory(String Name)
    {
    	File directory = new File(runtimeoutputpath);
    	//File directory = new File("C:\\Users\\subash\\Documents\\HiwiApp\\App1");
    	try {
    		if (!directory.exists()) {
        		if (directory.mkdirs()) {
        			System.out.println("Directory is created!");
        			
        		} else {
        			System.out.println("Failed to create directory! which is ::"+runtimeoutputpath+Name);
        		}
        	}


            String date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date());
            Files.write(Paths.get(runtimeoutputpath+"/"+Name+date+".txt"),"sample".getBytes());
			//File.createTempFile(Name+date, ".txt", directory) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
    }
}
