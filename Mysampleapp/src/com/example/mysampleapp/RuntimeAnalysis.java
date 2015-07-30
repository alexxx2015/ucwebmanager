
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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
//@Theme("mysampleaeeepp")
public class RuntimeAnalysis extends VerticalLayout implements View
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
		
		/*setRows(12);
		setColumns(8);*/
		setSizeFull();
		setMargin(true);
		addStyleName("backColorGrey");
	Button btnrun = new Button("RunTime Analyse");	
	Button btnprev = new Button("Prev");
	Button btnnext = new Button("Next");
	TextArea textstaticanalysis=new TextArea("");
	Label lblWelcome =new Label("Run Time Analysis");
	textstaticanalysis.setSizeFull();
//
	MenuBar barmenu = new MenuBar();
	
	@SuppressWarnings("deprecation")
	MenuBar.MenuItem menusa = barmenu.addItem("Static Analysis", null, null);
	MenuBar.MenuItem menura = barmenu.addItem("runtime Analysis", null, null);
	MenuBar.MenuItem menuinstr = barmenu.addItem("Instrumentation", null, null);
	MenuBar.MenuItem menuhome = barmenu.addItem("Home", null, null);

	
	
	setMargin(true);
	//
	HorizontalLayout hlayoutmenu=new  HorizontalLayout();
	hlayoutmenu.setSpacing(true);
	hlayoutmenu.addComponent(btnprev);		
	hlayoutmenu.addComponent(barmenu);
	
	hlayoutmenu.addComponent(btnnext);
	hlayoutmenu.setComponentAlignment(barmenu, Alignment.TOP_CENTER);
	hlayoutmenu.setComponentAlignment(btnprev, Alignment.TOP_LEFT);
	hlayoutmenu.setComponentAlignment(btnnext, Alignment.TOP_RIGHT);
	addComponent(hlayoutmenu);

	//
	//addComponent(lblWelcome);
	//addComponent(btnprev);
	addComponent(textstaticanalysis);
	addComponent(btnrun);
	hlayoutmenu.setComponentAlignment(btnrun, Alignment.BOTTOM_LEFT);
	hlayoutmenu.setComponentAlignment(textstaticanalysis, Alignment.MIDDLE_CENTER);
	btnprev.addClickListener(new Button.ClickListener() 
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
