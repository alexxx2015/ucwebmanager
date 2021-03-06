/*This class is the home page of our application. It contains the summary table which contains the list of different applications and 
 * the status of analysis, instrumentation and run. We can navigate to other views from this view on the basis of the action we choose
 * 
 * */
package com.example.mysampleapp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.james.mime4j.field.datetime.DateTime;
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
import com.vaadin.server.Page;
import com.vaadin.server.VaadinServlet;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

@SuppressWarnings("serial")
@Theme("tests-valo-dark")
public class NextPage extends VerticalLayout implements View, Receiver, SucceededListener {
	MysampleappUI mainObj;
	FileInfo fileinfoObj;
	Connection conn = null;
	Properties prop = new Properties();
	String propFileName = "Config.properties";//
	String Staticanalysispath;
	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	String dbpath;
	Grid gridmain = new Grid();
	String databasedriver ;
	//
	int count;
	String relativepath;

	public NextPage(MysampleappUI objmain) {
		mainObj = objmain;
		//relativepath = new File("").getAbsolutePath();
		relativepath=VaadinServlet.getCurrent().getServletContext().getRealPath("/");
		//relativepath=VaadinServlet.getCurrent().getServletContext().getRealPath("/");
		//String relativepathtemp=VaadinServlet.getCurrent().getServletContext().getContextPath();
		
		//String basepath = getApplication().getContext().getBaseDirectory().getAbsolutePath();
		
		try {
			prop.load(inputStream);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		//Staticanalysispath =new File("").getAbsolutePath();
		Staticanalysispath=VaadinServlet.getCurrent().getServletContext().getRealPath("/");
		databasedriver=prop.getProperty("databasedriver");
		
		addStyleName("backColorGrey");
		
		Button btnnext = new Button("Next");
		Button btnprev = new Button("Prev");
		Button btnupload = new Button("upload");
		Panel pnlgrid = new Panel();

		Panel pnlupload = new Panel();

		btnnext.setStyleName("v-button");
		btnprev.setStyleName("v-button");
		List<List<String>> Files = new ArrayList<List<String>>();
		HorizontalLayout horiprevnext = new HorizontalLayout();
		/*horiprevnext.addComponent(btnprev);
		horiprevnext.addComponent(btnnext);*/
		HorizontalLayout horicore = new HorizontalLayout();
		VerticalLayout verticalbuttons = new VerticalLayout();
		Button btnruntime = new Button("RunTime Analysis");
		btnruntime.setSizeFull();
		Button btnstatic = new Button("Static Analysis");
		btnstatic.setSizeFull();

		Button btninstrument = new Button("Instrumentation");
		btninstrument.setSizeFull();
		btnstatic.addStyleName("big");
		btnruntime.addStyleName("big");
		// btnruntime.setStyleName("borderless");
		btninstrument.addStyleName("big");

		// setMargin(true);
		/*verticalbuttons.addComponent(btnstatic);
		verticalbuttons.addComponent(btninstrument);
		verticalbuttons.addComponent(btnruntime);*/
		verticalbuttons.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
		verticalbuttons.setSpacing(false);
		verticalbuttons.setSizeFull();
		verticalbuttons.setMargin(true);

		VerticalLayout verticalcore = new VerticalLayout();
		Upload uploadFile = new Upload("Upload", this);

		try {
			Properties prop = new Properties();

			String propFileName = "Config.properties";

			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			prop.load(inputStream);
			dbpath = relativepath + prop.getProperty("databasepath");

			
			
			// set the properties value
			prop.setProperty("database", "localhost");
			prop.setProperty("dbuser", "mkyong");
			prop.setProperty("dbpassword", "password");

			// save properties to project root folder

			DatabaseConnection db = new DatabaseConnection();
			String connectionstring = databasedriver + dbpath;
			// conn=db.Createconnection("jdbc:sqlite:"+System.getProperty("databasepath")+prop.getProperty("databasename"));
			conn = db.Createconnection(connectionstring);
			ResultSet rs = null;
			if (conn != null) {
				Statement statement = conn.createStatement();
				statement.setQueryTimeout(30); // set timeout to 30 sec.

				// statement.executeUpdate("insert into Staticanalysis
				// values('program5',DateTime('now'), 'Not Yet Started')");
				rs = statement.executeQuery("select * from Staticanalysis ");
			} else
				System.out.println("could not create connection to database " + connectionstring);
			// Have a container of some type to contain the data

			
			BeanItemContainer<FileInfo> filedata = new BeanItemContainer<>(FileInfo.class);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
						while (rs.next()) {

				rs.getString("Name");
				rs.getString("status");
				rs.getString("time");
				Files.add(Arrays.asList(rs.getString("Name"), rs.getString("status"), rs.getString("time")));
			}

		} catch (IOException | ParserConfigurationException ex) {
			System.out.println(ex.getMessage());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// ends
		// BeanItemContainer<FileInfo> containerFiles = new
		// BeanItemContainer<FileInfo>(FileInfo.class, Files);
		IndexedContainer containerFiles = new IndexedContainer(Files);

		// Grid gridmain=new Grid();
		// gridmain.setContainerDataSource(containerFiles);
		System.out.println("set datasource");
		List<Grid.Column> listcolumns = gridmain.getColumns();

		gridmain.setLocale(Locale.GERMANY);
		gridmain.setEditorEnabled(true);
		gridmain.setSizeFull();
		gridmain.setWidth(50.0f, ComboBox.UNITS_EM);

		List<String> listaction = new ArrayList<String>();
		listaction.add("Static Analysis");
		listaction.add("Instrumentation");
		listaction.add("Runtime Analysis");

		gridmain.addColumn("Name", String.class);
		gridmain.addColumn("Status", String.class);
		gridmain.addColumn("Action", String.class);
		gridmain.addColumn("Proceed", String.class);

		gridmain.addColumn("Time", String.class);
		// FileUploader receiver = new ImageUploader();
		gridmain.addColumn("Stop", String.class);
		uploadFile.addSucceededListener(this);
		// Table tble=new Table();
		// upload.addSucceededListener(receiver);
		gridmain.getColumn("Name").setEditable(false);

		gridmain.getColumn("Time").setEditable(false);
		gridmain.getColumn("Status").setEditable(false);
		gridmain.getColumn("Proceed").setEditable(false);
		gridmain.getColumn("Stop").setEditable(false);

		gridmain.getColumn("Action").setEditorField(getComboBox("Selection  is required!", listaction));
		// Grid.Column bornColumn = gridmain.getColumn("Time");
		// bornColumn.setRenderer(new NumberRenderer("born in %d AD"));

		gridmain.getColumn("Proceed").setRenderer(new ButtonRenderer());
		gridmain.getColumn("Stop").setRenderer(new ButtonRenderer());
		// gridmain.getColumn("Action").setRenderer(new CustomRenderer());
		// gridmain.getColumn("Go").setRenderer(new
		// ButtonRenderer(e->gridmain.getContainerDataSource().removeItem(e.getItemId())));
		gridmain.getColumn("Proceed").setRenderer(new ButtonRenderer(e -> {
			// gridmain.getContainerDataSource().removeItem(e.getItemId());

			try {
				Object rowId = e.getItemId(); // get the selected rows id

				String value = (String) gridmain.getContainerDataSource().getContainerProperty(rowId, "Action")
						.getValue();
				String filename = (String) gridmain.getContainerDataSource().getContainerProperty(rowId, "Name")
						.getValue();
				// filename="C:\\Users\\subash\\Documents\\HiwiApp\\9th
				// june\\StaticAnalysis\\9th june09-06-2015
				// 12-17-228533325276181607600.txt";

				switch (value) {
				case "Static Analysis":
					System.out.println(value + "Yeah");
					mainObj.navigator.navigateTo("Main/" + filename);
					break;
				case "Instrumentation":
					mainObj.navigator.navigateTo("Instrumentation/" + filename);
					break;
				case "Runtime Analysis":
					mainObj.navigator.navigateTo("Runtime/" + filename);// not working this navigation
					break;
				}

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}));
		gridmain.getColumn("Stop").setRenderer(new ButtonRenderer(e -> {

			try {
				Object rowId = e.getItemId(); // get the selected rows id

				String value = (String) gridmain.getContainerDataSource().getContainerProperty(rowId, "Action")
						.getValue();
				String filename = (String) gridmain.getContainerDataSource().getContainerProperty(rowId, "Name")
						.getValue();

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}));

		for (int i = 0; i < Files.size(); i++) {
			gridmain.addRow(Files.get(i).get(0), Files.get(i).get(1), "empty", "Go", Files.get(i).get(2), "Stop");
		}
		
		
		  MenuBar barmenu = new MenuBar();
		 /* MenuBar.MenuItem menuhome = barmenu.addItem("Home", null,		  null);
		  @SuppressWarnings("deprecation") MenuBar.MenuItem menusa =
		  barmenu.addItem("Static Analysis", null, null);
		
		  MenuBar.MenuItem menuinstr = barmenu.addItem("Instrumentation", null,		  null); 
		  MenuBar.MenuItem		  menura = barmenu.addItem("runtime Analysis", null, null); 
		*/	barmenu.addItem("Home", new MenuBar.Command() 
			{
			    public void menuSelected(MenuBar.MenuItem selectedItem) 
			    {
			    	mainObj.navigator.navigateTo("");
			    }
			});
			barmenu.addItem("Static Analysis", new MenuBar.Command() 
			{
			    public void menuSelected(MenuBar.MenuItem selectedItem) 
			    {
			    	mainObj.navigator.navigateTo("Main");
			    }
			});
			barmenu.addItem("Instrumentation", new MenuBar.Command() 
			{
			    public void menuSelected(MenuBar.MenuItem selectedItem) 
			    {
			    	mainObj.navigator.navigateTo("Instrumentation");
			    }
			});
			barmenu.addItem("runtime Analysis", new MenuBar.Command() 
			{
			    public void menuSelected(MenuBar.MenuItem selectedItem) 
			    {
			    	mainObj.navigator.navigateTo("Runtime");
			    }
			});

		// setMargin(true);
		//
		
		  HorizontalLayout hlayoutmenu=new HorizontalLayout();
		  hlayoutmenu.setSpacing(true);
		 
		  hlayoutmenu.addComponent(barmenu);
		  
		
		  hlayoutmenu.setComponentAlignment(barmenu, Alignment.TOP_CENTER);
		 
		 
		  addComponent(hlayoutmenu);
		  this.setComponentAlignment(hlayoutmenu,Alignment.TOP_CENTER );
		// addComponent(btnnext);
		// addComponent(btnprev);
		// pnlgrid.setContent(gridmain);
		// pnlupload.setContent(uploadFile);
		// pnlgrid.getContent().setSizeUndefined();
		// pnlupload.getContent().setSizeUndefined();
		// pnlgrid.setScrollLeft(6);
		// pnlupload.setScrollLeft(6);
		verticalcore.addComponent(uploadFile);

		verticalcore.addComponent(gridmain);

		verticalcore.setSizeUndefined();

		verticalcore.setSizeFull();
		horicore.addComponent(verticalbuttons);
		horicore.addComponent(verticalcore);
		addComponent(horiprevnext);
		// setComponentAlignment(gridmain, Alignment.MIDDLE_CENTER);
		addComponent(horicore);
		this.setSpacing(true);

		// setComponentAlignment(uploadFile, Alignment.BOTTOM_LEFT);

		btnupload.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				//selectdatafromdb();

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
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		updatesummarytable();
		if (event.getParameters() != null) {
			// split at "/", add each part as a label
			String[] msgs = event.getParameters().split("/");
			for (String msg : msgs) {
				//refresh = msg;

			}
			// count++;
			

		}

	}

	/*private void selectdatafromdb() {

		Connection connection = null;
		try {
			System.out.println("1reached");
			// DriverManager.registerDriver(new org.sqlite.JDBC());
			System.out.println("2reached");
			System.out.println("3reached");
			Class.forName("org.sqlite.JDBC");
			// create a database connection
			System.out.println("4reached");
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("select * from person");
			while (rs.next()) {
				// read the result set
				System.out.println("name = " + rs.getString("name"));
				System.out.println("id = " + rs.getInt("id"));
			}

		} catch (SQLException | ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}

	}*/

	@Override
	public OutputStream receiveUpload(String filename, String MIMEType) {
		File directory = new File(relativepath + "/apps/ws-flowAnaluser" + "/" + filename + "/Source");
		
		if (!directory.exists()) {
			if (directory.mkdirs()) {
				System.out.println("Directory is created!");

			}
		}
		OutputStream fos = null; // Output stream to write to
		File file = new File(relativepath + "/apps/ws-flowAnaluser" + "/" + filename + "/Source/" + filename);// strUploadFilePathCG+

		try {
			// Open the file for writing.
			fos = new FileOutputStream(file);

		} catch (final java.io.FileNotFoundException e) {
			// Error while opening the file. Not reported here.
			e.printStackTrace();
			return null;
		}

		return fos; // Return the output stream to write to
	}

	@Override
	public void uploadSucceeded(Upload.SucceededEvent event) {
		// unzip(Staticanalysispath+"/"+event.getFilename()+"/"+"Applicationfiles/"+event.getFilename(),Staticanalysispath+"/"+event.getFilename()+"/"+"Applicationfiles");
		unZipIt(Staticanalysispath + "/apps/ws-flowAnaluser" + "/" + event.getFilename() + "/Source/" + event.getFilename(),
				Staticanalysispath + "/apps/ws-flowAnaluser" + "/" + event.getFilename() + "/Source");
		try {
			//
			//String sql = "update people set firstname=? , lastname=? where id=?";
			String sql = "SELECT count(*) AS rowcount1 FROM Staticanalysis WHERE Name=?";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1,event.getFilename());	
			ResultSet rs = preparedStatement.executeQuery();
			int count = rs.getInt("rowcount1");
			//
			
			//removed sqlstatement
			//Statement statement;
			//statement = conn.createStatement();
			 
			 //rs = statement.executeQuery(
				//	"SELECT count(*) AS rowcount1 FROM Staticanalysis WHERE Name='" + event.getFilename() + "'");
			//count = rs.getInt("rowcount1");
			//removed sqlstatemnet
			if (count == 0)
			{
				
			//	statement.executeUpdate("insert into Staticanalysis values('" + event.getFilename()	+ "',DateTime('now'), 'Not Yet Started')");
				
				sql="insert into Staticanalysis values(?,?,'Not Yet Started')";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1,event.getFilename());	
				preparedStatement.setDate(2, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
				
				
				 count = preparedStatement.executeUpdate();
			}
			else if (count == 1)
			{
				sql="UPDATE Staticanalysis SET status ='Not Yet Started' WHERE Name=?";
				//statement.executeUpdate("UPDATE Staticanalysis SET status ='Not Yet Started' WHERE Name='" + event.getFilename() + "'");
				preparedStatement.close();
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1,event.getFilename());					
				 count = preparedStatement.executeUpdate();
				
			}

			//statement.close();

			Page.getCurrent().reload();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// This is called if the upload fails.
	public void uploadFailed(Upload.FailedEvent event) {
		// Log the failure on screen.

	}

	public void updatesummarytable() {
		DatabaseConnection db = new DatabaseConnection();
		List<List<String>> files = new ArrayList<List<String>>();
		
		String connectionstring = "jdbc:sqlite:" + dbpath;

		conn = db.Createconnection(connectionstring);
		ResultSet rs = null;
		try {
			if (conn != null) {
//				Statement statement;
//				statement = conn.createStatement();
//				statement.setQueryTimeout(30); // set timeout to 30 sec.				
//				rs = statement.executeQuery("select * from Staticanalysis ");
				
				String sql="select * from Staticanalysis ";
				PreparedStatement preparedStatement  = conn.prepareStatement(sql);			
				
				
				rs = preparedStatement.executeQuery();
			} else
				System.out.println("could not create connection to database " + connectionstring);
			BeanItemContainer<FileInfo> filedata = new BeanItemContainer<>(FileInfo.class);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			//Document doc = docBuilder.parse(new File("C:/Users/Student/Samplefiledata.xml"));
			//doc.getDocumentElement().normalize();
			//System.out.println("Root element of the doc is " + doc.getDocumentElement().getNodeName());
			//NodeList listOffile = doc.getElementsByTagName("File");
			//int totalFile = listOffile.getLength();
			//System.out.println("Total no of File : " + totalFile);
			/*//for (int s = 0; s < listOffile.getLength(); s++) {

				Node FileNode = listOffile.item(s);
				if (FileNode.getNodeType() == Node.ELEMENT_NODE) {

					Element FileElement = (Element) FileNode;

					// -------
					NodeList NameList = FileElement.getElementsByTagName("Name");
					Element NameElement = (Element) NameList.item(0);
					NodeList textNameList = NameElement.getChildNodes();
					System.out.println("First Name : " + ((Node) textNameList.item(0)).getNodeValue().trim());
					// -------
					NodeList StatusList = FileElement.getElementsByTagName("Status");
					Element StatusElement = (Element) StatusList.item(0);

					NodeList textStatusList = StatusElement.getChildNodes();
					System.out.println("Last Name : " + ((Node) textStatusList.item(0)).getNodeValue().trim());

					// ----
					NodeList TimeList = FileElement.getElementsByTagName("Time");
					Element TimeElement = (Element) TimeList.item(0);

					NodeList textTimeList = TimeElement.getChildNodes();
					System.out.println("Age : " + ((Node) textTimeList.item(0)).getNodeValue().trim());
					List<String> listtoadd = new ArrayList<String>();
					listtoadd.add(((Node) textNameList.item(0)).getNodeValue().trim());
					listtoadd.add(((Node) textStatusList.item(0)).getNodeValue().trim());
					listtoadd.add(((Node) textTimeList.item(0)).getNodeValue().trim());
					files.add(listtoadd);

					// ------

				} // end of if clause

			} // end of for loop with s var
*/			while (rs.next()) {

				rs.getString("Name");
				rs.getString("status");
				rs.getString("time");
				files.add(Arrays.asList(rs.getString("Name"), rs.getString("status"), rs.getString("time")));
			}
			gridmain.getContainerDataSource().removeAllItems();
			for (int i = 0; i < files.size(); i++) {
				gridmain.addRow(files.get(i).get(0), files.get(i).get(1), "empty", "Go", files.get(i).get(2), "Stop");
			}
			// Have a container of some type to contain the data
		} catch (SQLException | ParserConfigurationException   e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * if(refresh.equals("Refresh") &&(count<2)) {
		 * Page.getCurrent().reload(); }
		 */
	}

	public void unzip(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {

		File file = new File(filePath);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[4096];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	public void unZipIt(String zipFile, String outputFolder) {

		byte[] buffer = new byte[1024];

		try {

			// create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				System.out.println("file unzip : " + newFile.getAbsoluteFile());
				if (ze.isDirectory()) {

					File dir = new File(outputFolder + File.separator + fileName);
					dir.mkdir();
					new File(newFile.getParent()).mkdirs();
					ze = zis.getNextEntry();
					continue;
				}

				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

			System.out.println("Done");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}