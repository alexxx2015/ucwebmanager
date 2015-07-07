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
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
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
	static int buttonnum=0;
	String buttonname="b";
	Properties prop = new Properties();
	String propFileName = "Config.properties";//	 
	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	
	
	
	
	OutputStream output = null;
		
		 
	
	
	@SuppressWarnings("deprecation")
	public   Main(MysampleappUI o)
	{		//final VerticalLayout layout = new VerticalLayout();
//		this.getSession().getConfiguration().getInitParameters();
		//Panel panel = new Panel("Static analysis");
		
	this.mainObj= o;
		 
	 
		
	 
			try {
				
				prop.load(inputStream);
				output = new FileOutputStream("config.properties");				
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
		TextField txtpointtofallback=new TextField("Points To Fallback");
		
		Upload uploadSDGFile = new Upload();
		
		//uploadSDGFile.setButtonCaption("Browse");
		Upload uploadCGFile = new Upload();
		//uploadCGFile.setButtonCaption("Browse");
		
		
		TextField txtfldname=new TextField("Analysis Name");
		
		IndexedContainer cmbocontainer = new IndexedContainer();
		cmbocontainer.addContainerProperty("name", String.class, null);
		
        ComboBox cmbmode=new ComboBox("Mode");
        cmbmode.addItem("load");
        cmbmode.addItem("build");
        
        ComboBox cmbpointstopolicy=new ComboBox("Points To Policy");
        cmbpointstopolicy.addItem("RTA");
        cmbpointstopolicy.addItem("TYPE_BASED");
        cmbpointstopolicy.addItem("INSTANCE_BASED");
        cmbpointstopolicy.addItem("OBJECT_SENSITIVE");
        cmbpointstopolicy.addItem("N1_OBJECT_SENSITIVE");
        cmbpointstopolicy.addItem("UNLIMITED_OBJECT_SENSITIVE");
        cmbpointstopolicy.addItem("N1_CALL_STACK");
        cmbpointstopolicy.addItem("N2_CALL_STACK");
        cmbpointstopolicy.addItem("N3_CALL_STACK");
        
		
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
		Table gridclasspath=new Table();
		gridclasspath.addContainerProperty("Classpath", TextField.class,null);		
		gridclasspath.setPageLength(5);
		gridclasspath.setColumnWidth("Classpath",400);
		Table tblthirdpartylib=new Table();
		tblthirdpartylib.addContainerProperty("ThirdPartyLibrary", TextField.class,null);		
		tblthirdpartylib.setPageLength(5);
		tblthirdpartylib.setColumnWidth("ThirdPartyLibrary",400);
		Table tblpointstoinclude=new Table();
		tblpointstoinclude.addContainerProperty("Points To Include", TextField.class,null);		
		tblpointstoinclude.setPageLength(5);
		tblpointstoinclude.setColumnWidth("Points To Include",400);
		Table tblpointstoexclude=new Table();
		tblpointstoexclude.addContainerProperty("Points To Exclude", TextField.class,null);		
		tblpointstoexclude.setPageLength(5);
		tblpointstoexclude.setColumnWidth("Points To Exclude",400);
		//table for sinks and sources
		Table tblsourcensinks=new Table();
		tblsourcensinks.addContainerProperty("Types", ComboBox.class,null);
		tblsourcensinks.addContainerProperty("Classes", TextField.class,null);
		tblsourcensinks.addContainerProperty("Selector", TextField.class,null);
		tblsourcensinks.addContainerProperty("Param", TextField.class,null);
		tblsourcensinks.addContainerProperty("Include SubClasses", CheckBox.class,null);
		tblsourcensinks.addContainerProperty("Indirect Calls", CheckBox.class,null);
		tblsourcensinks.setPageLength(5);
		tblsourcensinks.setColumnWidth("Classes",200);
		tblsourcensinks.setColumnWidth("Selector",200);
		//source and sinks table code ends
		
		
		FormLayout fl=new FormLayout();
		fl.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
		fl.addComponent(txtfldname);
		fl.addComponent(cmbmode);
		fl.addComponent(txtfldpath);
		fl.addComponent(gridclasspath);
		fl.addComponent(txtfldthirdpartylib);
		fl.addComponent(tblthirdpartylib);
		fl.addComponent(txtSDGFile);		
		fl.addComponent(uploadSDGFile);		
		fl.addComponent(txtCGFile);
		fl.addComponent(uploadCGFile);		
		fl.addComponent(txtReportFile);
		fl.addComponent(cmbpointstopolicy);
		fl.addComponent(txtpointtofallback);
		fl.addComponent(tblpointstoinclude);
		fl.addComponent(tblpointstoexclude);
		fl.addComponent(btnselectsnsFile);
		fl.addComponent(tblsourcensinks);
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
			
			//conext menu for gridclasspath
			
			gridclasspath.addItemClickListener(
					new ItemClickListener() {
			    public void itemClick(ItemClickEvent event) {
			        if (event.getButton()== MouseEventDetails.MouseButton.RIGHT) {
			        	gridclasspath.select(event.getItemId());
			        }
			    }

				
			}
					);
			gridclasspath.addActionHandler(
					new Action.Handler() 
			 {
				   public Action[] getActions(Object target, Object sender)
				   {
					    final Action ACTION_NEW = new Action("New");
					    final Action ACTION_EDIT = new Action("Edit");
					    final Action ACTION_DELETE = new Action("Delete");
					    final Action[] ACTIONS = new Action[] { ACTION_NEW, ACTION_EDIT, ACTION_DELETE };
					    return ACTIONS;

				   }

				@Override
				public void handleAction(Action action, Object sender,
						Object target) {
					
					Item rowItem = gridclasspath.getItem(target);
					
					if(action.getCaption()=="New")
					{
						TextField txt=new TextField("textfield");
					    
						//txt.setValue("Hey you");
						Object newItemId = gridclasspath.addItem();
						Item row = gridclasspath.getItem(newItemId);
						row.getItemProperty("Classpath").setValue(txt);					
						
						gridclasspath.addItem(new Object[]{txt},newItemId);						
						Button buttonnam = new Button(buttonname);
						addComponent(buttonnam);
						
						buttonnam.setClickShortcut(KeyCode.ENTER);		
						buttonnam.addClickListener(new Button.ClickListener() {
						    @Override
						    public void buttonClick(ClickEvent event)
						    {  	gridclasspath.addItemAfter(new Object[]{txt},newItemId);	
						    Item row = gridclasspath.getItem(newItemId);
							row.getItemProperty("Classpath").setValue(txt);
							System.out.println("the value of textfield is "+txt.getValue());
						    
						    	txt.setEnabled(false);
						    	buttonnam.removeClickShortcut();
						    	removeComponent(buttonnam);
						    	
						    	
						    }
						});
												
						
					}
					else if(action.getCaption()=="Delete")
					{
						/*gridclasspath.removeItem(gridclasspath.getValue());						 
						gridclasspath.removeItem(rowItem);*/
						gridclasspath.removeItem(target);
						
					}
					
				
					
				}
			 });

			
			//context menu ends
			
			//context menu for thirdpartylibrary
			tblthirdpartylib.addItemClickListener(
					new ItemClickListener() {
			    public void itemClick(ItemClickEvent event) {
			        if (event.getButton()== MouseEventDetails.MouseButton.RIGHT) {
			        	tblthirdpartylib.select(event.getItemId());
			        }
			    }

				
			}
					);
			tblthirdpartylib.addActionHandler(
					new Action.Handler() 
			 {
				   public Action[] getActions(Object target, Object sender)
				   {
					    final Action ACTION_NEW = new Action("New");
					   
					    final Action ACTION_DELETE = new Action("Delete");
					    final Action[] ACTIONS = new Action[] { ACTION_NEW, ACTION_DELETE };
					    return ACTIONS;

				   }

				@Override
				public void handleAction(Action action, Object sender,
						Object target) {
					
					Item rowItem = tblthirdpartylib.getItem(target);
					
					if(action.getCaption()=="New")
					{
						TextField txt=new TextField("textfield");
					    
						//txt.setValue("Hey you");
						Object newItemId = tblthirdpartylib.addItem();
						Item row = tblthirdpartylib.getItem(newItemId);
						row.getItemProperty("ThirdPartyLibrary").setValue(txt);					
						
						tblthirdpartylib.addItem(new Object[]{txt},newItemId);						
						Button buttonnam = new Button(buttonname);
						addComponent(buttonnam);
						
						buttonnam.setClickShortcut(KeyCode.ENTER);		
						buttonnam.addClickListener(new Button.ClickListener() {
						    @Override
						    public void buttonClick(ClickEvent event)
						    {  	tblthirdpartylib.addItemAfter(new Object[]{txt},newItemId);	
						    Item row = tblthirdpartylib.getItem(newItemId);
							row.getItemProperty("ThirdPartyLibrary").setValue(txt);
							System.out.println("the value of textfield is "+txt.getValue());
						    
						    	txt.setEnabled(false);
						    	buttonnam.removeClickShortcut();
						    	removeComponent(buttonnam);
						    	
						    	
						    }
						});
												
						
					}
					else if(action.getCaption()=="Delete")
					{
						/*gridclasspath.removeItem(gridclasspath.getValue());						 
						gridclasspath.removeItem(rowItem);*/
						tblthirdpartylib.removeItem(target);
						
					}
					
				
					
				}
			 });

			
			//context menu ends
			
			//context menu handler for table points to include
			tblpointstoinclude.addItemClickListener(
					new ItemClickListener() {
			    public void itemClick(ItemClickEvent event) {
			        if (event.getButton()== MouseEventDetails.MouseButton.RIGHT) {
			        	tblpointstoinclude.select(event.getItemId());
			        }
			    }

				
			}
					);
			tblpointstoinclude.addActionHandler(
					new Action.Handler() 
			 {
				   public Action[] getActions(Object target, Object sender)
				   {
					    final Action ACTION_NEW = new Action("New");
					   
					    final Action ACTION_DELETE = new Action("Delete");
					    final Action[] ACTIONS = new Action[] { ACTION_NEW, ACTION_DELETE };
					    return ACTIONS;

				   }

				@Override
				public void handleAction(Action action, Object sender,
						Object target) {
					
					Item rowItem = tblpointstoinclude.getItem(target);
					
					if(action.getCaption()=="New")
					{
						TextField txt=new TextField("textfield");
					    
						//txt.setValue("Hey you");
						Object newItemId = tblpointstoinclude.addItem();
						Item row = tblpointstoinclude.getItem(newItemId);
						row.getItemProperty("Points To Include").setValue(txt);					
						
						tblpointstoinclude.addItem(new Object[]{txt},newItemId);						
						Button buttonnam = new Button(buttonname);
						addComponent(buttonnam);
						
						buttonnam.setClickShortcut(KeyCode.ENTER);		
						buttonnam.addClickListener(new Button.ClickListener() {
						    @Override
						    public void buttonClick(ClickEvent event)
						    {  	tblpointstoinclude.addItemAfter(new Object[]{txt},newItemId);	
						    Item row = tblpointstoinclude.getItem(newItemId);
							row.getItemProperty("Points To Include").setValue(txt);
							System.out.println("the value of textfield is "+txt.getValue());
						    
						    	txt.setEnabled(false);
						    	buttonnam.removeClickShortcut();
						    	removeComponent(buttonnam);
						    	
						    	
						    }
						});
												
					}
					else if(action.getCaption()=="Delete")
					{
						/*gridclasspath.removeItem(gridclasspath.getValue());						 
						gridclasspath.removeItem(rowItem);*/
						tblpointstoinclude.removeItem(target);
						
					}
					
				
					
				}
			 });
//ends
			//context menu for table points to exclude
			tblpointstoexclude.addItemClickListener(
					new ItemClickListener() {
			    public void itemClick(ItemClickEvent event) {
			        if (event.getButton()== MouseEventDetails.MouseButton.RIGHT) {
			        	tblpointstoexclude.select(event.getItemId());
			        }
			    }

				
			}
					);
			tblpointstoexclude.addActionHandler(
					new Action.Handler() 
			 {
				   public Action[] getActions(Object target, Object sender)
				   {
					    final Action ACTION_NEW = new Action("New");
					   
					    final Action ACTION_DELETE = new Action("Delete");
					    final Action[] ACTIONS = new Action[] { ACTION_NEW, ACTION_DELETE };
					    return ACTIONS;

				   }

				@Override
				public void handleAction(Action action, Object sender,
						Object target) {
					
					Item rowItem = tblpointstoexclude.getItem(target);
					
					if(action.getCaption()=="New")
					{
						TextField txt=new TextField("textfield");
					    
						//txt.setValue("Hey you");
						Object newItemId = tblpointstoexclude.addItem();
						Item row = tblpointstoexclude.getItem(newItemId);
						row.getItemProperty("Points To Exclude").setValue(txt);					
						
						tblpointstoexclude.addItem(new Object[]{txt},newItemId);						
						Button buttonnam = new Button(buttonname);
						addComponent(buttonnam);
						
						buttonnam.setClickShortcut(KeyCode.ENTER);		
						buttonnam.addClickListener(new Button.ClickListener() {
						    @Override
						    public void buttonClick(ClickEvent event)
						    {  	tblpointstoexclude.addItemAfter(new Object[]{txt},newItemId);	
						    Item row = tblpointstoexclude.getItem(newItemId);
							row.getItemProperty("Points To Exclude").setValue(txt);
							System.out.println("the value of textfield is "+txt.getValue());
						    
						    	txt.setEnabled(false);
						    	buttonnam.removeClickShortcut();
						    	removeComponent(buttonnam);
						    	
						    	
						    }
						});
												
						
					}
					else if(action.getCaption()=="Delete")
					{
						/*gridclasspath.removeItem(gridclasspath.getValue());						 
						gridclasspath.removeItem(rowItem);*/
						tblpointstoexclude.removeItem(target);
						
					}
					
				
					
				}
			 });
			//ends
           //context menu for table sourcensinks
			tblsourcensinks.addItemClickListener(
					new ItemClickListener() {
			    public void itemClick(ItemClickEvent event) {
			        if (event.getButton()== MouseEventDetails.MouseButton.RIGHT) {
			        	tblsourcensinks.select(event.getItemId());
			        }
			    }

				
			}
					);
			tblsourcensinks.addActionHandler(
					new Action.Handler() 
			 {
				   public Action[] getActions(Object target, Object sender)
				   {
					    final Action ACTION_NEW = new Action("New");
					   
					    final Action ACTION_DELETE = new Action("Delete");
					    final Action[] ACTIONS = new Action[] { ACTION_NEW, ACTION_DELETE };
					    return ACTIONS;

				   }

				@Override
				public void handleAction(Action action, Object sender,
						Object target) {
					
					Item rowItem = tblsourcensinks.getItem(target);
					
					if(action.getCaption()=="New")
					{
						TextField txt=new TextField("textfield");
					    
						
						Object newItemId = tblsourcensinks.addItem();
						Item row = tblsourcensinks.getItem(newItemId);
						row.getItemProperty("Points To Exclude").setValue(txt);					
						
						tblsourcensinks.addItem(new Object[]{txt},newItemId);						
						Button buttonnam = new Button(buttonname);
						addComponent(buttonnam);
						
						buttonnam.setClickShortcut(KeyCode.ENTER);		
						buttonnam.addClickListener(new Button.ClickListener() {
						    @Override
						    public void buttonClick(ClickEvent event)
						    {  	tblsourcensinks.addItemAfter(new Object[]{txt},newItemId);	
						    Item row = tblsourcensinks.getItem(newItemId);
							row.getItemProperty("Points To Exclude").setValue(txt);
							System.out.println("the value of textfield is "+txt.getValue());
						    
						    	txt.setEnabled(false);
						    	buttonnam.removeClickShortcut();
						    	removeComponent(buttonnam);
						    	
						    	
						    }
						});
												
						
					}
					else if(action.getCaption()=="Delete")
					{
						
						tblpointstoexclude.removeItem(target);
						
					}
					
				
					
				}
			 });

		   //ends
			 cmbmode.addValueChangeListener(new Property.ValueChangeListener() 
			 {
		            private static final long serialVersionUID = -5188369735622627751L;

		            public void valueChange(ValueChangeEvent event) {
		                if (cmbmode.getValue() != null&&(cmbmode.getValue()=="build")) 
		                {
		                	uploadSDGFile.setVisible(false);
		                	uploadCGFile.setVisible(false);
		                
		                 
		                }
		                else 
		                	{
		                	uploadSDGFile.setVisible(true);
		                	uploadCGFile.setVisible(true);
		                	
		                	}
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
    		prop.load(inputStream);
    		if (!directory.exists()) {
        		if (directory.mkdirs()) {
        			System.out.println("Directory is created!");
        			
        		} else {
        			System.out.println("Failed to create directory! which is ::"+prop.getProperty("BaseFolderPath")+Name+prop.getProperty("StaticAnalysisFoldername"));
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