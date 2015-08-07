package com.example.mysampleapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.sqlite.JDBC;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

@SuppressWarnings("serial")
@Theme("mysampleapp")
public class NextPage extends VerticalLayout implements View , Receiver, SucceededListener
{  MysampleappUI mainObj;
FileInfo fileinfoObj;
Connection conn=null;
  


	
	public   NextPage(MysampleappUI objmain) 
	{ 
		mainObj=objmain;
		//setRows(18);
		//setColumns(8);
		setSizeFull();setMargin(true);
		addStyleName("backColorGrey");
		//Label lblwelcome=new Label("Welcome ");
		Button btnnext=new Button("Next");
		Button btnprev=new Button("Prev");
		Button btnupload=new Button("upload");
		
		btnnext.setStyleName("v-button");
		btnprev.setStyleName("v-button");
		 List<List<String>> Files = new ArrayList<List<String>>();
		 
		 
		try
		{   Properties prop = new Properties();
			
			String propFileName = "Config.properties";
			 
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			prop.load(inputStream);
			String dbpath = prop.getProperty("databasepath");
			new File("").getAbsolutePath();
			System.out.println("database path is "+dbpath);
			// set the properties value
			prop.setProperty("database", "localhost");
			prop.setProperty("dbuser", "mkyong");
			prop.setProperty("dbpassword", "password");

			// save properties to project root folder
			
		DatabaseConnection db=new DatabaseConnection();
		String connectionstring="jdbc:sqlite:"+dbpath;
		// conn=db.Createconnection("jdbc:sqlite:"+System.getProperty("databasepath")+prop.getProperty("databasename"));
		 conn=db.Createconnection(connectionstring);
		 ResultSet rs=null;
		 if(conn!=null)
		{
		 Statement statement = conn.createStatement();
		 statement.setQueryTimeout(30);  // set timeout to 30 sec.	
		 
		 //statement.executeUpdate("insert into Staticanalysis values('program5',DateTime('now'), 'Not Yet Started')");		
		  rs = statement.executeQuery("select * from Staticanalysis");	
		}
		else
			System.out.println("could not create connection to database "+connectionstring);
 		// Have a container of some type to contain the data			
 		 
		 
		System.out.println("file read start"); 
		 BeanItemContainer<FileInfo> filedata =	 new BeanItemContainer<>(FileInfo.class);
		 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
         Document doc = docBuilder.parse (new File("C:/Users/Student/Samplefiledata.xml"));
         doc.getDocumentElement ().normalize ();
         System.out.println ("Root element of the doc is " +   doc.getDocumentElement().getNodeName());
         NodeList listOffile = doc.getElementsByTagName("File");
         int totalFile = listOffile.getLength();
         System.out.println("Total no of File : " + totalFile);
         for(int s=0; s<listOffile.getLength() ; s++)
         {


             Node FileNode = listOffile.item(s);
             if(FileNode.getNodeType() == Node.ELEMENT_NODE){

                 
                 Element FileElement = (Element)FileNode;

                 //-------
                 NodeList NameList = FileElement.getElementsByTagName("Name");
                 Element NameElement = (Element)NameList.item(0);
                 NodeList textNameList = NameElement.getChildNodes();
                 System.out.println("First Name : " + 
                        ((Node)textNameList.item(0)).getNodeValue().trim());
                                 //-------
                 NodeList StatusList = FileElement.getElementsByTagName("Status");
                 Element StatusElement = (Element)StatusList.item(0);

                 NodeList textStatusList = StatusElement.getChildNodes();
                 System.out.println("Last Name : " + 
                        ((Node)textStatusList.item(0)).getNodeValue().trim());

                 //----
                 NodeList TimeList = FileElement.getElementsByTagName("Time");
                 Element TimeElement = (Element)TimeList.item(0);

                 NodeList textTimeList = TimeElement.getChildNodes();
                 System.out.println("Age : " + 
                        ((Node)textTimeList.item(0)).getNodeValue().trim());
                 List<String> listtoadd=new ArrayList<String>();
                 listtoadd.add(((Node)textNameList.item(0)).getNodeValue().trim());
                 listtoadd.add(((Node)textStatusList.item(0)).getNodeValue().trim());	 
                 listtoadd.add(((Node)textTimeList.item(0)).getNodeValue().trim());
                 Files.add(listtoadd);   		

                 //------


             }//end of if clause


         }//end of for loop with s var
         while(rs.next())
		 {
		  
		  rs.getString("Name");
		  rs.getString("status");
		  rs.getString("time");
		  Files.add(Arrays.asList(rs.getString("Name"), rs.getString("status"),rs.getString("time")));
		 }


		 }
		 catch(IOException | SAXException | ParserConfigurationException  ex)
		 {
			 System.out.println(ex.getMessage());
		 } catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//ends
 		//BeanItemContainer<FileInfo> containerFiles =  new BeanItemContainer<FileInfo>(FileInfo.class, Files);
 		 IndexedContainer containerFiles =  new IndexedContainer(Files);
 		

		Grid gridmain=new Grid();
		//gridmain.setContainerDataSource(containerFiles);
		System.out.println("set datasource");
		List<Grid.Column> listcolumns=gridmain.getColumns();
		
		gridmain.setLocale(Locale.GERMANY);
		gridmain.setEditorEnabled(true);
		gridmain.setSizeFull();
		
		List<String> listaction=new ArrayList<String>();
		listaction.add("Static Analysis");
		listaction.add("Instrumentation");
		listaction.add("Runtime Analysis");
			
		gridmain.addColumn("Name", String.class);
		gridmain.addColumn("Status", String.class);
		gridmain.addColumn("Action", String.class);
		gridmain.addColumn("Proceed", String.class);
		gridmain.addColumn("Time", String.class);
		//FileUploader receiver = new ImageUploader(); 
		Upload uploadFile = new Upload("Upload",this);
		uploadFile.addSucceededListener(this);	
		Table tble=new Table();
		//upload.addSucceededListener(receiver);
		gridmain.getColumn("Name").setEditable(false);
		
		gridmain.getColumn("Time").setEditable(false);
		gridmain.getColumn("Status").setEditable(false);
		gridmain.getColumn("Proceed").setEditable(false);
		
		gridmain.getColumn("Action").setEditorField(
				getComboBox("Selection  is required!",listaction)
				);
//		Grid.Column bornColumn = gridmain.getColumn("Time");
//		bornColumn.setRenderer(new NumberRenderer("born in %d AD"));
		
		gridmain.getColumn("Proceed").setRenderer(new ButtonRenderer());
		//gridmain.getColumn("Action").setRenderer(new CustomRenderer());
		//gridmain.getColumn("Go").setRenderer(new ButtonRenderer(e->gridmain.getContainerDataSource().removeItem(e.getItemId())));
		gridmain.getColumn("Proceed").setRenderer(new ButtonRenderer(e->{
			//gridmain.getContainerDataSource().removeItem(e.getItemId());
			
			try {
				Object rowId = e.getItemId(); // get the selected rows id
				
						
				String value=(String)gridmain.getContainerDataSource().getContainerProperty(rowId,"Action").getValue();
				String filename=(String)gridmain.getContainerDataSource().getContainerProperty(rowId,"Name").getValue();
				//filename="C:\\Users\\subash\\Documents\\HiwiApp\\9th june\\StaticAnalysis\\9th june09-06-2015 12-17-228533325276181607600.txt";
				
				switch (value) {
				  case "Static Analysis":
					  System.out.println(value+ "Yeah");
					  
					  mainObj.navigator.navigateTo("/"+filename); 
				        break;
				  case "Instrumentation": 
					  System.out.println(value+ "Yeah");
					  
					  mainObj.navigator.navigateTo("Instrumentation/"+filename); 
					  
				        break;
				  case "Runtime Analysis":
					  System.out.println(value+ "Yeah");
					  mainObj.navigator.navigateTo("Runtime/"+filename);//not working this navigation
					  //mainObj.navigator.navigateTo("NextPage");
				  /*default:
					  mainObj.navigator.navigateTo(""); 
				        break;*/
				}
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println(e1.getMessage());
			}
			}));
		
		for(int i=0;i<Files.size();i++)
		{
			gridmain.addRow(Files.get(i).get(0), Files.get(i).get(1),"empty","Go",Files.get(i).get(2));
		}	
		/*gridmain.addRow("Nicolaus Copernicus", "Not Yet started","empty","Go",new Date());
		gridmain.addRow("Galileo Galilei", "Static analysis done","empty","Go",new Date());
		gridmain.addRow("Johannes Kepler","Instrumentation done","empty","Go",new Date());	
		*/
		//read data from xml file and display in grid
				
		
		//addComponent(lblwelcome,1,0,2,0);
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
		//addComponent(btnnext);
		//addComponent(btnprev);
		addComponent(gridmain);
		setComponentAlignment(gridmain, Alignment.MIDDLE_CENTER);
		addComponent(uploadFile);
		setComponentAlignment(uploadFile, Alignment.BOTTOM_LEFT);
		//addComponent(btnupload,0,7,2,7);
		
		btnupload.addClickListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{ 
				
	            
				selectdatafromdb();
				
		       	}
		});
		
		btnprev.addClickListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{ 
				
	            mainObj.navigator.navigateTo("");

				
		       	}
		});
				
		
	
		
		
		
	}
   public Field<?> getComboBox(String requiredErrorMsg, List<String> items) {

		ComboBox comboBox = new ComboBox();

		comboBox.setNullSelectionAllowed(false);

		IndexedContainer container = new IndexedContainer(items);

		comboBox.setContainerDataSource(container);

		comboBox.setRequired(true);

		comboBox.setRequiredError(requiredErrorMsg);

		return comboBox;

	}
	@Override
	public void enter(ViewChangeEvent event)
	{
		// TODO Auto-generated method stub
		
	}
	private void selectdatafromdb()
	{
	     
		 Connection connection = null;
		   try
		   {
			   System.out.println("1reached");
			   //DriverManager.registerDriver(new org.sqlite.JDBC());
			   System.out.println("2reached");
			   System.out.println("3reached");
		   Class.forName("org.sqlite.JDBC");
		// create a database connection
		   System.out.println("4reached");
		connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
		Statement statement = connection.createStatement();
		statement.setQueryTimeout(30);  // set timeout to 30 sec.			
		ResultSet rs = statement.executeQuery("select * from person");
		while(rs.next())
		{
		 // read the result set
		 System.out.println("name = " + rs.getString("name"));
		 System.out.println("id = " + rs.getInt("id"));
		}

		   }
		catch(SQLException | ClassNotFoundException ex){System.out.println(ex.getMessage());}
	



}    @Override
	 public OutputStream receiveUpload(String filename, String MIMEType) {
	FileOutputStream fos = null; // Output stream to write to
File file = new File("UploadFilePath" + filename);
try {
// Open the file for writing.
fos = new FileOutputStream(file);
} 
catch (final java.io.FileNotFoundException e) 
{
// Error while opening the file. Not reported here.
e.printStackTrace();
return null;
}

return fos; // Return the output stream to write to
}    @Override
	 public void uploadSucceeded(Upload.SucceededEvent event)
	 {
	 Statement statement;
	try {
		statement = conn.createStatement();
		statement.executeUpdate("insert into Staticanalysis values('"+event.getFilename()+"',DateTime('now'), 'Not Yet Started')");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
	 
	        
	    }

	    // This is called if the upload fails.
	    public void uploadFailed(Upload.FailedEvent event) 
	    {
	        // Log the failure on screen.
	       
	    }
}