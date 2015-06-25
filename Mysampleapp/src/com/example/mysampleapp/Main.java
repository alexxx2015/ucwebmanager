package com.example.mysampleapp;

import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.vaadin.annotations.Theme;
import com.vaadin.client.ui.VContextMenu;
import com.vaadin.client.ui.menubar.MenuItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("mysampleapp")
public class Main extends VerticalLayout implements View
{
	MysampleappUI mainObj;
	static String filename,Staticanalysispath;

	/*@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = NextPage.class)
	public static class Servlet extends VaadinServlet
	{
	}*/
	
	@SuppressWarnings("deprecation")
	public   Main(MysampleappUI o)
	{		//final VerticalLayout layout = new VerticalLayout();
//		this.getSession().getConfiguration().getInitParameters();
		//Panel panel = new Panel("Static analysis");
		Properties prop = new Properties();
	this.mainObj= o;
		 OutputStream output = null;
	 
		
	 
			try {
				output = new FileOutputStream("config.properties");
 
				// set the properties value
				
				
				String propFileName = "Config.properties";
				 
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
				prop.load(inputStream);
				Staticanalysispath = prop.getProperty("StaticAnalysisoutputpath");
				prop.setProperty("database", "localhost");
				prop.setProperty("dbuser", "mkyong");
				prop.setProperty("dbpassword", "password");
 
				// save properties to project root folder
				prop.store(output, null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
        Label lblwelcome =new Label("Static Analysis");
        lblwelcome.setStyleName("labelwelcome");
        ComboBox cmbselectpr=new ComboBox();
        cmbselectpr.setCaption("Select Program");
		CheckBox chkmultithreaded=new CheckBox();
		CheckBox chkcomputechops=new CheckBox();
		CheckBox chksensitiveness=new CheckBox();
		CheckBox chkindirectflows=new CheckBox();
		Label lblmultithreaded=new Label("Multithreaded");		
		Label lblcomputechops=new Label("Compute Chops");		
		Label lblsensitiveness=new Label("Object Sensitiveness");
		Label lblindirectflows=new Label("Indirect Flows");
		//Label lblname=new Label("Analysis Name");		
		//Label lblmode=new Label("Mode");		
		//Label lblpath=new Label("ClassPath");
		//Label lblsource=new Label("Source and Sinks");
		Label lblthirdpartylib=new Label("ThirdParty Library");
		//Label lblSDGFile=new Label("SDGFile");
		//Label lblCGFile=new Label("CGFile");
		//Label lblReportFile=new Label("Report File");
		TextField txtSDGFile=new TextField("SDGFile");
		TextField txtCGFile=new TextField("CGFile");
		TextField txtReportFile=new TextField("Report File");
		Upload uploadSDGFile = new Upload();
		//uploadSDGFile.setButtonCaption("Browse");
		Upload uploadCGFile = new Upload();
		//uploadCGFile.setButtonCaption("Browse");
		Upload uploadReportFile = new Upload();
		//uploadReportFile.setButtonCaption("Browse");
		TextField txtfldname=new TextField("Analysis Name");
		TextField txtfldmode=new TextField("Mode");
		TextField txtfldpath=new TextField("Class Path");
		TextField txtfldthirdpartylib=new TextField("Third Party Library");
		
	    Button btnnext = new Button("Next");
		Button btnprev = new Button("Prev");
		//Button btnload = new Button("Instrument");
		Button btnsave = new Button("Save saveconfiguration");
		Button btnrun = new Button("Run Analysis");
		Button btnselectsnsFile = new Button("Select sns File");
		MenuBar barmenu = new MenuBar();
		
		@SuppressWarnings("deprecation")
		MenuBar.MenuItem menusa = barmenu.addItem( "Home",null, null);
		MenuBar.MenuItem menura = barmenu.addItem( "Home",null, null);
		MenuBar.MenuItem menuinstr = barmenu.addItem( "Home",null, null);
		MenuBar.MenuItem menuhome = barmenu.addItem("Home", null, null);
		

		//Button btnruntime =new Button();
		Button btnruntime = new NativeButton();
		btnruntime.setSizeFull();
		Button btnstatic =new Button();
		btnstatic.setSizeFull();
		
		
		
		Button btninstrument =new Button();
		btninstrument.setSizeFull();
		btnstatic.addStyleName("big");
		btnruntime.addStyleName("big");
		btnruntime.setStyleName("borderless");
		btninstrument.addStyleName("big");
		
		setMargin(true);
		//
		HorizontalLayout horiprevnext=new HorizontalLayout(); 
		horiprevnext.addComponent(btnprev);
		horiprevnext.addComponent(btnnext);
		horiprevnext.setComponentAlignment(btnprev, Alignment.TOP_LEFT);
		horiprevnext.setComponentAlignment(btnnext, Alignment.TOP_RIGHT);
		
		HorizontalLayout hlayoutcore=new HorizontalLayout();
		
		
		VerticalLayout vlayoutmenu=new  VerticalLayout();
		//vlayoutmenu.setSpacing(true);
		vlayoutmenu.addComponent(btnstatic);		
		vlayoutmenu.addComponent(btninstrument);
		vlayoutmenu.addComponent(btnruntime);
		vlayoutmenu.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
		vlayoutmenu.setSpacing(false);
		//vlayoutmenu.setExpandRatio(btnstatic, );
		//vlayoutmenu.addComponent();
		//hlayoutmenu.addComponent(btnnext);
		HorizontalLayout hlayoutcoreinside=new  HorizontalLayout();
		Grid gridclasspath=new Grid();
		gridclasspath.addColumn("Classpath", String.class);
		gridclasspath.setEditorEnabled(true);
		gridclasspath.getColumn("Classpath").setEditable(true);
		//VContextMenu vb=new VContextMenu(); 
		//VerticalLayout vlaytables=new VerticalLayout();
		FormLayout fl=new FormLayout();
		fl.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
		fl.addComponent(txtfldname);
		fl.addComponent(txtfldmode);
		fl.addComponent(txtfldpath);
		fl.addComponent(gridclasspath);
		fl.addComponent(txtfldthirdpartylib);
		
		fl.addComponent(txtSDGFile);		
		fl.addComponent(uploadSDGFile);		
		fl.addComponent(txtCGFile);
		fl.addComponent(uploadCGFile);		
		fl.addComponent(txtReportFile);
		fl.addComponent(uploadReportFile);
		fl.addComponent(btnselectsnsFile);
		fl.setMargin(true);
		//vlaytables.addComponent(fl);		
		//VerticalLayout vlaychecboxes=new VerticalLayout();
		//hlayoutcoreinside.addComponent(fl);
		//hlayoutcoreinside.addComponent(vlaychecboxes);
		vlayoutmenu.setSizeFull();
		vlayoutmenu.setMargin(true);
		
		hlayoutcore.addComponent(vlayoutmenu);
		hlayoutcore.addComponent(fl);
		//hlayoutmenu.setComponentAlignment(barmenu, Alignment.TOP_CENTER);
		//hlayoutmenu.setComponentAlignment(btnprev, Alignment.TOP_LEFT);
		//hlayoutmenu.setComponentAlignment(btnnext, Alignment.TOP_RIGHT);
		
		
		
		//
		this.setSpacing(true);
		//this.setExpandRatio(barmenu, 2*1.0f);
		//this.setComponentAlignment(barmenu, Alignment.TOP_CENTER);
		/*HorizontalLayout hlayout=new  HorizontalLayout();
		hlayout.setWidth("500px");*/
		
		/*hlayout.addComponent(chkmultithreaded,0);
		hlayout.addComponent(lblmultithreaded,1);
		hlayout.setExpandRatio(lblmultithreaded,3*1.0f);
		hlayout.addComponent(chkcomputechops,2);
		hlayout.addComponent(lblcomputechops,3);*/
		/*hlayout.setExpandRatio(lblcomputechops,2*1.0f);
		hlayout.addComponent(chksensitiveness,4);
		hlayout.addComponent(lblsensitiveness,5);
		hlayout.setExpandRatio(lblsensitiveness,2*1.0f);
		hlayout.addComponent(chkindirectflows,6);
		hlayout.addComponent(lblindirectflows,7);
		hlayout.setExpandRatio(lblindirectflows,2*1.0f);*/
		addStyleName("backColorGrey");
		
		addComponent(horiprevnext);
		addComponent(hlayoutcore);
		//addComponent(hlayout);
		
		//this.setExpandRatio(hlayout, 1.0f);
		GridLayout childgrid=new GridLayout(4,8);
		/*FormLayout fl=new FormLayout();
		fl.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
		fl.addComponent(txtfldname);
		fl.addComponent(txtfldmode);
		fl.addComponent(txtfldpath);
		fl.addComponent(txtfldthirdpartylib);
		
		
		fl.addComponent(txtSDGFile);
		
		fl.addComponent(uploadSDGFile);
		
		fl.addComponent(txtCGFile);
		fl.addComponent(uploadCGFile);
		
		fl.addComponent(txtReportFile);
		fl.addComponent(uploadReportFile);
		fl.addComponent(btnselectsnsFile);
		*/
		childgrid.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//childgrid.addComponent(fl);
		
		childgrid.addComponent(btnselectsnsFile,1,7,1,7);	
		//addComponent(childgrid);
		
			btnnext.addClickListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{ 
				/*BrowserWindowOpener popupOpener = new BrowserWindowOpener(NextPage.class);
		        popupOpener.setFeatures("height=4000,width=2000");
		        popupOpener.extend(btnnext);*/
		        
	            mainObj.navigator.navigateTo("NextPage");

				// getUI().getPage().setLocation("NextPage.class");
		       	}
		});
			btnrun.addClickListener(new Button.ClickListener() 
			{
				public void buttonClick(ClickEvent event) 
				{ createDirectory(filename);
								       	}
			});
			/*MenuBar.Command mycommand = new MenuBar.Command()
			{
			    @SuppressWarnings("deprecation")
				public void menuSelected(MenuItem selectedItem)
			    {
			    	String value=(String)gridmain.getContainerDataSource().getContainerProperty(rowId,"Action").getValue();
			    	selectedItem.
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
						  mainObj.navigator.navigateTo("Runtime/"+filename);
						  
					}   
			    }

*/			/*	@Override
				public void menuSelected(
						com.vaadin.ui.MenuBar.MenuItem selectedItem) {
					// TODO Auto-generated method stub
					
				}  
			};*/
			
	}
	protected void createDirectory(String Name)
    {
		File directory = new File(Staticanalysispath);
    	//File directory = new File("C:\\Users\\subash\\Documents\\HiwiApp\\App1");
    	try {
    		if (!directory.exists()) {
        		if (directory.mkdirs()) {
        			System.out.println("Directory is created!");
        			
        		} else {
        			System.out.println("Failed to create directory! which is ::"+"C:\\Users\\subash\\Documents\\HiwiApp\\"+Name+"\\StaticAnalysis");
        		}
        	}


            String date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date());
            System.out.println(date);
            Files.write(Paths.get(Staticanalysispath+"/"+Name+date+".txt"),"sample".getBytes());
			//File.createTempFile(Name+date, ".xml", directory) ;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
    }
   /* private FieldGroup getFieldGroup()
    {
    	BeanFieldGroup<NextPage> beanFieldGroup = new BeanFieldGroup<NextPage>(NextPage.class);

		beanFieldGroup.addCommitHandler(new CommitHandler() 
		{

			private static final long serialVersionUID = 6062316515368687380L;

			@Override
			public void preCommit(CommitEvent commitEvent)

					throws CommitException {

			} 

			@Override

			public void postCommit(CommitEvent commitEvent)

					throws CommitException { 

				Notification.show("Saved Successfully!!",Notification.Type.TRAY_NOTIFICATION);
			}

		}); 

		return beanFieldGroup;		
	}
	protected void adddatatogrid()
    {
    	
    	
    }
*/    public Field<?> getComboBox(String requiredErrorMsg, List<String> items) {

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
		// 
				}