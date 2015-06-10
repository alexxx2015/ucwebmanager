package com.example.mysampleapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;

@SuppressWarnings("serial")
@Theme("Main")
public class Main extends GridLayout implements View
{
	MysampleappUI mainObj;

	/*@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = NextPage.class)
	public static class Servlet extends VaadinServlet
	{
	}*/
	
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
		Label lblname=new Label("Analysis Name");		
		Label lblmode=new Label("Mode");		
		Label lblpath=new Label("ClassPath");
		Label lblsource=new Label("Source and Sinks");
		Label lblthirdpartylib=new Label("ThirdParty Library");
		Label lblSDGFile=new Label("SDGFile");
		Label lblCGFile=new Label("CGFile");
		Label lblReportFile=new Label("Report File");
		TextField txtSDGFile=new TextField("");
		TextField txtCGFile=new TextField("");
		TextField txtReportFile=new TextField("");
		Upload uploadSDGFile = new Upload();
		//uploadSDGFile.setButtonCaption("Browse");
		Upload uploadCGFile = new Upload();
		//uploadCGFile.setButtonCaption("Browse");
		Upload uploadReportFile = new Upload();
		//uploadReportFile.setButtonCaption("Browse");
		TextField txtfldname=new TextField("");
		TextField txtfldmode=new TextField("");
		TextField txtfldpath=new TextField("");
		TextField txtfldthirdpartylib=new TextField("");
		
	    Button btnnext = new Button("Next");
		Button btnprev = new Button("Prev");
		Button btnload = new Button("Instrument");
		Button btnsave = new Button("Save saveconfiguration");
		Button btnrun = new Button("Run Analysis");
		Button btnselectsnsFile = new Button("Select sns File");
				
		//GridLayout glayout=new GridLayout(8,15);
		//glayout.set
		setRows(15);
		setColumns(8);
		setSizeFull();
		setMargin(true);
		addComponents(chkmultithreaded,lblmultithreaded,chkcomputechops,lblcomputechops,chksensitiveness,lblsensitiveness,chkindirectflows,lblindirectflows);
		addComponent(lblname,0,1,1,1);
		addComponent(txtfldname,2,1,5,1);
		addComponent(cmbselectpr,6,1,7,1);
		addComponent(lblmode,0,2,1,2);
		addComponent(txtfldmode,2,2,5,2);
		addComponent(lblpath,0,3,1,3);
		addComponent(txtfldpath,2,3,5,3);
		addComponent(lblthirdpartylib,0,4,1,4);
		addComponent(txtfldthirdpartylib,2,4,5,4);
		
		addComponent(lblSDGFile,0,5,1,5);
		addComponent(txtSDGFile,2,5,5,5);
		addComponent(uploadSDGFile,6,5,7,5);
		addComponent(lblCGFile,0,6,1,6);
		addComponent(txtCGFile,2,6,5,6);
		addComponent(uploadCGFile,6,6,7,6);
		
		addComponent(lblReportFile,0,8,1,8);
		addComponent(txtReportFile,2,8,5,8);
		addComponent(uploadReportFile,6,8,7,8);
		
		addComponent(lblsource,0,9,1,9);		
		addComponent(btnselectsnsFile,2,9,7,9);		
		addComponent(btnload,0,10,2,10);
		addComponent(btnsave,3,10,4,10);
		addComponent(btnrun,5,10,6,10);
		addComponent(btnprev,2,11,4,11);
		addComponent(btnnext,6,11,7,11);
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
				{ createDirectory(txtfldname.getValue());
								       	}
			});
			
			
	}
	protected void createDirectory(String Name)
    {
    	File directory = new File("C:/Users/subash/Documents/HiwiApp/"+Name+"/StaticAnalysis");
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
			File.createTempFile(Name+date, ".xml", directory) ;
			
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
	public void enter(ViewChangeEvent event) {}
		// 
				}