package com.example.mysampleapp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("tests-valo-dark")
public class MysampleappUI extends UI
{
	public Navigator navigator= new Navigator(this,this);
	
	Properties prop = new Properties();
	String propFileName = "Config.properties";//
	 
	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	
	
	

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MysampleappUI.class)
	public static class Servlet extends VaadinServlet
	{
	}
	

	@Override
	protected void init(VaadinRequest request) 
	{
		ServletContext serv= VaadinServlet.getCurrent().getServletContext();
		String PropertFilePath=serv.getInitParameter(Main.CONFIG_PROP);
		
		//navigator.addView("",new Main(this));
		navigator.addView("",new NextPage(this));
			//navigator.addView("NextPage", new NextPage(this));
		navigator.addView("Main",new Main(this));
		navigator.addView("Instrumentation", new Instrumentation(this));
		navigator.addView("Runtime", new RuntimeAnalysis(this));
		
	}
	protected void createDirectory(String Name)
    {
    	File directory = new File(prop.getProperty("BaseFolderPath")+Name+prop.getProperty("StaticAnalysisFoldername"));
    	//File directory = new File("C:\\Users\\subash\\Documents\\HiwiApp\\App1");
    	try {
    		if (!directory.exists()) {
        		if (directory.mkdirs()) {
        			System.out.println("Directory is created!");
        			
        		} else {
        			System.out.println("Failed to create directory! which is ::"+prop.getProperty("BaseFolderPath")+Name+prop.getProperty("StaticAnalysisFoldername"));
        		}
        	}


            String date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date());
            System.out.println(date);
			File.createTempFile(Name+date, ".txt", directory) ;
			String filepath = "D:\\HIWI\\Samplefiledata.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);
			Node company = doc.getFirstChild();
			Node staff = doc.getElementsByTagName("Name").item(0);
			
		} catch (IOException | ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
    }
	
}
