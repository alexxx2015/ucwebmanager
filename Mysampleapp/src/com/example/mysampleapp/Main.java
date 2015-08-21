package com.example.mysampleapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
@Theme("tests-valo-dark")
public class Main extends VerticalLayout implements View, Receiver,
		SucceededListener {
	public static final String CONFIG_PROP="CONFIG_PROP";
	MysampleappUI mainObj;
	static String appname, Staticanalysispath, applicationname;
	static int buttonnum = 0;
	String buttonname = "b";
	Properties prop = new Properties();
	String propFileName = "Config.properties";//
	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
			propFileName);
	// create global variables
	String analysisname, modvalue, classpath, thirdpartylib, stubs, temppath;
	boolean multhithreaded;
	String sdgvalue, cgvalue;
	boolean ignoreindirectflowvalue;
	boolean computechopsvalue, objectsensitivevalue;
	String snsfilevalue, strUploadFilePathCG;
	Object itemid, selecteditemidsns;
	OutputStream output = null;
	Table gridclasspath, tblthirdpartylib, tblpointstoinclude,
			tblpointstoexclude, tblsourcensinks;
	TextField txtfldSnSFile = new TextField("File Name");
	TextField txtfldname = new TextField("Analysis Name");
	CheckBox chkmultithreaded = new CheckBox("Multithreaded");
	CheckBox chkcomputechops = new CheckBox("Compute Chops");
	CheckBox chksensitiveness = new CheckBox("Object Sensitiveness");
	CheckBox chkindirectflows = new CheckBox("Indirect Flows");
	CheckBox chkSystemOut = new CheckBox("System Out");
	TextField txtSDGFile = new TextField("SDGFile");
	TextField txtCGFile = new TextField("CGFile");
	TextField txtReportFile = new TextField("Report File");
	TextField txtpointtofallback = new TextField("Points To Fallback");
	ComboBox cmbmode = new ComboBox("Mode");
	ComboBox cmbstub = new ComboBox("stubs");
	ComboBox cmbpointstopolicy = new ComboBox("Points To Policy");
	Upload uploadSDGFile = new Upload("SDGFile", this);
	TextField ReportFile = new TextField("reportFile");
	TextField txtlogFile = new TextField("logFile");
	Label lblsuccessmessageCG, lblsuccessmessageReport, lblsuccessmessageSDG;
	TextField txtfldEntryPoint = new TextField("Entry Point");
	StaticAnalyser analyser;
	String relativepath;

	// uploadSDGFile.setButtonCaption("Browse");
	Upload uploadCGFile = new Upload("CGFile", this);

	@SuppressWarnings("deprecation")
	public Main(MysampleappUI o) { // final VerticalLayout layout = new
									// VerticalLayout();
		// this.getSession().getConfiguration().getInitParameters();
		// Panel panel = new Panel("Static analysis");

		this.mainObj = o;

		try {

			final Subwindow subwin = new Subwindow();
			prop.load(inputStream);
			output = new FileOutputStream("config.properties");
			Staticanalysispath = prop.getProperty("StaticAnalysisoutputpath");
			String temppath = new File("").getAbsolutePath()
					+ "/StaticAnalysis";
			System.out.println("relativepath is  is " + temppath
					+ " and absolutepath is " + relativepath);
			prop.setProperty("database", "localhost");
			prop.setProperty("dbuser", "mkyong");
			prop.setProperty("dbpassword", "password");
			strUploadFilePathCG = prop.getProperty("UploadFilePath");

			// save properties to project root folder
			prop.store(output, null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// this.setSizeFull();
		Label lblwelcome = new Label("Static Analysis");
		lblwelcome.setStyleName("labelwelcome");
		ComboBox cmbselectpr = new ComboBox();
		cmbselectpr.setCaption("Select Program");

		lblsuccessmessageCG = new Label();
		lblsuccessmessageReport = new Label();
		lblsuccessmessageSDG = new Label();

		Label lblmultithreaded = new Label("Multithreaded");
		Label lblcomputechops = new Label("Compute Chops");
		Label lblsensitiveness = new Label("Object Sensitiveness");
		Label lblindirectflows = new Label("Indirect Flows");

		// Label lblname=new Label("Analysis Name");
		// Label lblmode=new Label("Mode");
		// Label lblpath=new Label("ClassPath");
		// Label lblsource=new Label("Source and Sinks");
		Label lblthirdpartylib = new Label("ThirdParty Library");
		// Label lblSDGFile=new Label("SDGFile");
		// Label lblCGFile=new Label("CGFile");
		// Label lblReportFile=new Label("Report File");
		// TextField txtfldSnSFile = new TextField("File Name");

		txtSDGFile.setWidth(24.6f, ComboBox.UNITS_EM);
		txtCGFile.setWidth(24.6f, ComboBox.UNITS_EM);
		txtReportFile.setWidth(24.6f, ComboBox.UNITS_EM);
		txtpointtofallback.setWidth(24.6f, ComboBox.UNITS_EM);
		cmbmode.setWidth(24.6f, ComboBox.UNITS_EM);
		cmbstub.setWidth(24.6f, ComboBox.UNITS_EM);
		txtfldEntryPoint.setWidth(24.6f, ComboBox.UNITS_EM);
		txtlogFile.setWidth(24.6f, ComboBox.UNITS_EM);
		cmbpointstopolicy.setWidth(24.6f, ComboBox.UNITS_EM);
		txtfldSnSFile.setWidth(24.6f, ComboBox.UNITS_EM);
		relativepath = new File("").getAbsolutePath();

		uploadCGFile.addSucceededListener(this);
		uploadSDGFile.addSucceededListener(this);
		// uploadReportFile.addSucceededListener(this);
		Upload uploadSnSfile = new Upload("Source and sink File", this);
		uploadSnSfile.addSucceededListener(this);

		txtfldname.setWidth(24.6f, ComboBox.UNITS_EM);
		txtfldname.setValue(appname);
		IndexedContainer cmbocontainer = new IndexedContainer();
		cmbocontainer.addContainerProperty("name", String.class, null);

		cmbmode.setNullSelectionAllowed(false);

		cmbmode.addItem("load");
		cmbmode.addItem("build");
		cmbmode.setValue("build");
		if (cmbmode.getValue() != null && (cmbmode.getValue() == "build")) {
			uploadSDGFile.setVisible(false);
			uploadCGFile.setVisible(false);

		} else {
			uploadSDGFile.setVisible(true);
			uploadCGFile.setVisible(true);

		}
		cmbstub.setNullSelectionAllowed(false);

		cmbstub.addItem("JRE_14");
		cmbstub.addItem("JRE_15");
		cmbstub.addItem("NO_STUBS");
		cmbstub.setValue("NO_STUBS");

		cmbpointstopolicy.setNullSelectionAllowed(false);

		cmbpointstopolicy.addItem("RTA");
		cmbpointstopolicy.addItem("TYPE_BASED");
		cmbpointstopolicy.addItem("INSTANCE_BASED");
		cmbpointstopolicy.addItem("OBJECT_SENSITIVE");
		cmbpointstopolicy.addItem("N1_OBJECT_SENSITIVE");
		cmbpointstopolicy.addItem("UNLIMITED_OBJECT_SENSITIVE");
		cmbpointstopolicy.addItem("N1_CALL_STACK");
		cmbpointstopolicy.addItem("N2_CALL_STACK");
		cmbpointstopolicy.addItem("N3_CALL_STACK");
		cmbpointstopolicy.setValue("RTA");

		TextField txtfldpath = new TextField("Class Path");
		TextField txtfldthirdpartylib = new TextField("Third Party Library");

		Button btnnext = new Button("Next");
		Button btnprev = new Button("Prev");

		Button btnsave = new Button("Save Configuration");
		Button btnrun = new Button("Run Analysis");
		Button btnselectsnsFile = new Button("Select sns File");

		MenuBar barmenu = new MenuBar();

		@SuppressWarnings("deprecation")
		MenuBar.MenuItem menusa = barmenu.addItem("Home", null, null);
		MenuBar.MenuItem menura = barmenu.addItem("Home", null, null);
		MenuBar.MenuItem menuinstr = barmenu.addItem("Home", null, null);
		MenuBar.MenuItem menuhome = barmenu.addItem("Home", null, null);

		// Button btnruntime =new Button();
		Button btnruntime = new Button("RunTime Analysis");
		btnruntime.setSizeFull();
		Button btnstatic = new Button("Static Analysis");
		btnstatic.setSizeFull();

		Button btninstrument = new Button("Instrumentation");
		btninstrument.setSizeFull();
		// btnstatic.addStyleName("big");
		// btnruntime.addStyleName("big");
		// btnruntime.setStyleName("borderless");
		// btninstrument.addStyleName("big");

		setMargin(true);
		//
		HorizontalLayout horiprevnext = new HorizontalLayout();
		horiprevnext.addComponent(btnprev);
		horiprevnext.addComponent(btnnext);
		horiprevnext.setComponentAlignment(btnprev, Alignment.TOP_LEFT);
		horiprevnext.setComponentAlignment(btnnext, Alignment.TOP_RIGHT);

		HorizontalLayout hlayoutcore = new HorizontalLayout();

		VerticalLayout vlayoutmenu = new VerticalLayout();
		// vlayoutmenu.setSpacing(true);
		vlayoutmenu.addComponent(btnstatic);
		vlayoutmenu.addComponent(btninstrument);
		vlayoutmenu.addComponent(btnruntime);
		vlayoutmenu.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
		vlayoutmenu.setSpacing(false);

		HorizontalLayout hlayoutcoreinside = new HorizontalLayout();
		gridclasspath = new Table("ClassPath");
		gridclasspath.addContainerProperty("Classpath", TextField.class, null);
		gridclasspath.setPageLength(5);
		gridclasspath.setColumnWidth("Classpath", 400);
		tblthirdpartylib = new Table("Third Party Library");
		tblthirdpartylib.addContainerProperty("ThirdPartyLibrary",
				TextField.class, null);
		tblthirdpartylib.setPageLength(5);
		tblthirdpartylib.setColumnWidth("ThirdPartyLibrary", 400);
		tblpointstoinclude = new Table("Points To Include");
		tblpointstoinclude.addContainerProperty("Points To Include",
				TextField.class, null);
		tblpointstoinclude.setPageLength(5);
		tblpointstoinclude.setColumnWidth("Points To Include", 400);
		tblpointstoexclude = new Table("Points To Exclude");
		tblpointstoexclude.addContainerProperty("Points To Exclude",
				TextField.class, null);
		tblpointstoexclude.setPageLength(5);
		tblpointstoexclude.setColumnWidth("Points To Exclude", 400);
		// table for sinks and sources
		// Button btnsubwindow=new Button("open subwindow");

		tblsourcensinks = new Table("Source and Sinks");
		tblsourcensinks.addContainerProperty("Types", TextField.class, null);
		tblsourcensinks.addContainerProperty("Classes", TextField.class, null);
		tblsourcensinks.addContainerProperty("Selector", TextField.class, null);
		tblsourcensinks.addContainerProperty("Param", TextField.class, null);
		tblsourcensinks.addContainerProperty("Include SubClasses",
				CheckBox.class, null);
		tblsourcensinks.addContainerProperty("Indirect Calls", CheckBox.class,
				null);
		tblsourcensinks.setPageLength(5);
		tblsourcensinks.setColumnWidth("Types", 130);
		tblsourcensinks.setColumnWidth("Include SubClasses", 130);
		tblsourcensinks.setColumnWidth("Classes", 190);
		tblsourcensinks.setColumnWidth("Selector", 190);
		tblsourcensinks.setColumnWidth("Param", 190);
		tblsourcensinks.setColumnWidth("Indirect Calls", 130);
		// source and sinks table code ends

		Panel pnltables = new Panel();

		FormLayout fl = new FormLayout();
		TextArea txterror = new TextArea(
				"You cannot add the values to the table since another duplicate row is already present");
		fl.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);

		// HorizontalLayout chkhorilay=new HorizontalLayout();
		// VerticalLayout lblvlay=new VerticalLayout();
		// VerticalLayout chkvlay=new VerticalLayout();
		// lblvlay.addComponent(lblmultithreaded);
		// lblvlay.addComponent(lblsensitiveness);
		// lblvlay.addComponent(lblindirectflows);
		// lblvlay.addComponent(lblcomputechops);

		// chkvlay.addComponent(chksensitiveness);
		// chkvlay.addComponent(chkindirectflows);
		// chkvlay.addComponent(chkcomputechops);
		// chkvlay.addComponent(chkmultithreaded);
		// chkhorilay.addComponents(lblvlay,chkvlay);
		// fl.addComponent(chkhorilay);

		fl.addComponent(txtfldname);
		fl.addComponent(cmbmode);
		fl.addComponent(cmbstub);
		fl.addComponent(txtfldEntryPoint);
		fl.addComponent(gridclasspath);
		fl.addComponent(tblthirdpartylib);
		fl.addComponent(txtSDGFile);
		fl.addComponent(uploadSDGFile);
		fl.addComponent(txtCGFile);
		fl.addComponent(uploadCGFile);

		fl.addComponent(txtReportFile);
		fl.addComponent(txtlogFile);
		fl.addComponent(cmbpointstopolicy);
		fl.addComponent(txtpointtofallback);

		fl.addComponent(tblpointstoinclude);
		HorizontalLayout HorizontalLayout = new HorizontalLayout();
		HorizontalLayout.addComponent(tblpointstoexclude);
		HorizontalLayout.addComponent(txterror);
		txterror.setVisible(false);
		fl.addComponent(HorizontalLayout);

		fl.addComponent(tblsourcensinks);
		fl.addComponent(txtfldSnSFile);
		fl.addComponent(uploadSnSfile);
		// fl.addComponent(txterror);
		fl.addComponent(chkmultithreaded);
		fl.addComponent(chksensitiveness);
		fl.addComponent(chkindirectflows);
		fl.addComponent(chkcomputechops);
		fl.addComponent(chkSystemOut);
		fl.addComponent(btnsave);
		fl.addComponent(btnrun);

		fl.setMargin(true);
		// assign values to global variables
		analysisname = txtfldname.getValue();
		modvalue = (String) cmbmode.getValue();
		ignoreindirectflowvalue = chkindirectflows.getValue();
		multhithreaded = chkmultithreaded.getValue();
		computechopsvalue = chkcomputechops.getValue();
		objectsensitivevalue = chksensitiveness.getValue();
		snsfilevalue = txtfldSnSFile.getValue();

		// assigning values ends

		pnltables.setContent(fl);
		fl.setSizeUndefined();
		pnltables.getContent().setSizeUndefined();
		pnltables.setScrollLeft(6);

		vlayoutmenu.setSizeFull();
		vlayoutmenu.setMargin(true);

		hlayoutcore.addComponent(vlayoutmenu);
		hlayoutcore.addComponent(pnltables);
		Button buttonnam = new Button(buttonname);
		// addComponent(buttonnam);

		//
		this.setSpacing(true);

		addStyleName("backColorGrey");

		addComponent(horiprevnext);
		addComponent(hlayoutcore);

		GridLayout childgrid = new GridLayout(4, 8);

		childgrid.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		// childgrid.addComponent(fl);

		childgrid.addComponent(btnselectsnsFile, 1, 7, 1, 7);
		// addComponent(childgrid);

		btnnext.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				mainObj.navigator.navigateTo("");

			}
		});

		btnsave.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				saveConfigurationxml("StaticAnalysis");

			}
		});

		btninstrument.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				mainObj.navigator.navigateTo("Instrumentation");

			}
		});
		btnstatic.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				mainObj.navigator.navigateTo("Main");

			}
		});
		btnruntime.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				mainObj.navigator.navigateTo("Runtime");

			}
		});

		btnrun.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				analyser = new StaticAnalyser();
				saveConfigurationxml("StaticAnalysis");
				if(new File(relativepath + "/apps/ws-flowAnaluser" + "/"
						+ appname + "/" + txtfldname.getValue()).exists())
				{
					Window msgbox = new Window("MessageBox");
					// msgbox.addStyleName("Messageboxdesign");

					FormLayout subContent = new FormLayout();
					Button btnYes=new Button("Yes");
					Button btnNo=new Button("No");
					Label lblmessage = new Label("A Static analysis has already started and finished with the same name. Do you want to overwrite it?");
					lblmessage.addStyleName("Messageboxdesign");
					subContent.setMargin(true);
					subContent.addComponent(lblmessage);
					subContent.addComponent(btnYes);
					subContent.addComponent(btnNo);
					msgbox.setModal(true);
					msgbox.setSizeUndefined();
					msgbox.setContent(subContent);
					btnYes.addClickListener(new Button.ClickListener()
					{
						public void buttonClick(ClickEvent event)
						{
							
							analyser.analyseFile(relativepath + "/apps/ws-flowAnaluser" + "/"
									+ appname + "/" + txtfldname.getValue(),
									Staticanalysispath + "/apps/ws-flowAnaluser" + "/" + appname + "/"
											+ txtfldname.getValue() + "/"
											+ "StaticAnalysis" + ".xml",appname,mainObj);	
							msgbox.close();
							//mainObj.navigator.navigateTo("/Refresh");
						}
					});
					btnNo.addClickListener(new Button.ClickListener()
					{
						public void buttonClick(ClickEvent event)
						{
							
							msgbox.close();
						}
					});
					UI.getCurrent().addWindow(msgbox);	
				}
				/*analyser.analyseFile(Staticanalysispath + "/apps/ws-flowAnaluser" + "/"
						+ appname + "/" + txtfldname.getValue(),
						Staticanalysispath + "/apps/ws-flowAnaluser" + "/" + appname + "/"
								+ txtfldname.getValue() + "/"
								+ "StaticAnalysis" + ".xml",appname,mainObj);*/
				
			}
		});
		// open new subwindow for tblsubclasses
		Window subWindow = new Window("Sub-window");
		FormLayout subContent = new FormLayout();
		subContent.setMargin(true);
		subWindow.setModal(true);
		subWindow.setSizeUndefined();
		subWindow.setContent(subContent);

		// Put some components in it
		ComboBox cmbtypes = new ComboBox("Types");
		cmbtypes.addItem("Source");
		cmbtypes.addItem("Sink");
		cmbtypes.setNullSelectionAllowed(false);
		cmbtypes.setValue("Source");
		TextField txtclasses = new TextField("Classes");
		TextField txtselector = new TextField("Selector");
		TextField txtparam = new TextField("Param");
		CheckBox chkinclude = new CheckBox("Include SubClasses");
		CheckBox chkindirectcalls = new CheckBox("Indirect Calls");
		TextField txtparent = new TextField();

		subContent.addComponent(cmbtypes);
		subContent.addComponent(txtclasses);
		subContent.addComponent(txtselector);
		subContent.addComponent(txtparam);
		subContent.addComponent(chkinclude);
		subContent.addComponent(chkindirectcalls);
		Button btnsavetotable = new Button("Save");

		btnsavetotable.addClickListener(new Button.ClickListener() {
			int counter = 0;

			@SuppressWarnings("unchecked")
			public void buttonClick(ClickEvent event) {
				// subWindow.close();
				try {

					TextField tempClass = new TextField();
					TextField txtTypes = new TextField();
					TextField tempSelector = new TextField();
					TextField tempParam = new TextField();
					CheckBox tempIncludeSubClass = new CheckBox();
					CheckBox tempIndirectCalls = new CheckBox();

					String text = txtclasses.getValue();
					tempClass.setValue(text);
					text = txtselector.getValue();
					tempSelector.setValue(text);
					text = txtparam.getValue();
					tempParam.setValue(text);

					if (cmbtypes.getValue() != null) {

						// Object itemid=cmbtypes.getConvertedValue();
						txtTypes.setValue(cmbtypes.getValue().toString());
					}

					boolean tempbool = chkinclude.getValue();
					tempIncludeSubClass.setValue(tempbool);
					tempbool = chkindirectcalls.getValue();
					tempIndirectCalls.setValue(tempbool);

					Object newItemId;// = tblsourcensinks.addItem();
					tblsourcensinks.getContainerPropertyIds();

					int numrows = tblsourcensinks.size();
					newItemId = numrows + 1;

					boolean isduplicate = false, isMandatoryfieldsfilled = true;
					if (("".equalsIgnoreCase(tempClass.getValue()))
							|| ("".equalsIgnoreCase(tempSelector.getValue()))
							|| ("".equalsIgnoreCase(tempParam.getValue())))
						isMandatoryfieldsfilled = false;

					for (int i = 1; i < (int) newItemId; i++) {
						Item row = tblsourcensinks.getItem(i);

						TextField txtClasses = (TextField) row.getItemProperty(
								"Classes").getValue();
						String strClasses = txtClasses.getValue();

						TextField txtSelector = (TextField) row
								.getItemProperty("Selector").getValue();

						String strSelector = txtSelector.getValue();
						TextField txtParam = (TextField) row.getItemProperty(
								"Param").getValue();
						String strParam = txtParam.getValue();
						TextField txtretTypes = (TextField) row
								.getItemProperty("Types").getValue();
						String strTypes = txtretTypes.getValue();
						CheckBox chkSubclassIncluded = (CheckBox) row
								.getItemProperty("Include SubClasses")
								.getValue();
						boolean isSubclassIncluded = chkSubclassIncluded
								.getValue();
						if ((strClasses.equalsIgnoreCase(tempClass.getValue()))
								&& (strSelector.equalsIgnoreCase(tempSelector
										.getValue()))
								&& (strParam.equalsIgnoreCase(tempParam
										.getValue()))
								&& (strTypes.equalsIgnoreCase(txtTypes
										.getValue()))
								&& (isSubclassIncluded == tempIncludeSubClass
										.getValue()))
							isduplicate = true;
						if (isduplicate == true)
							break;

					}

					if (!isduplicate && isMandatoryfieldsfilled) {
						newItemId = tblsourcensinks.addItem();
						Item row = tblsourcensinks.getItem(newItemId);

						row.getItemProperty("Classes").setValue(tempClass);

						row.getItemProperty("Selector").setValue(tempSelector);
						row.getItemProperty("Param").setValue(tempParam);
						row.getItemProperty("Include SubClasses").setValue(
								tempIncludeSubClass);
						row.getItemProperty("Indirect Calls").setValue(
								tempIndirectCalls);
						row.getItemProperty("Types").setValue(txtTypes);

						tempClass.setVisible(true);
						tempClass.setEnabled(false);
						tempSelector.setVisible(true);
						tempSelector.setEnabled(false);
						tempParam.setVisible(true);
						tempParam.setEnabled(false);
						tempIncludeSubClass.setVisible(true);
						tempIncludeSubClass.setEnabled(false);
						tempIndirectCalls.setVisible(true);
						tempIndirectCalls.setEnabled(false);
						txtTypes.setVisible(true);
						txtTypes.setEnabled(false);
						txterror.setVisible(false);

						subWindow.close();
					} else if (isMandatoryfieldsfilled == false) {

						Window msgbox = new Window("MessageBox");
						// msgbox.addStyleName("Messageboxdesign");

						FormLayout subContent = new FormLayout();
						Label lblmessage = new Label(
								"Please fill all the mandatory fields");
						lblmessage.addStyleName("Messageboxdesign");
						subContent.setMargin(true);
						subContent.addComponent(lblmessage);
						msgbox.setModal(true);
						msgbox.setSizeUndefined();
						msgbox.setContent(subContent);
						UI.getCurrent().addWindow(msgbox);
					} else {
						Window msgbox = new Window("MessageBox");
						FormLayout subContent = new FormLayout();
						Label lblmessage = new Label(
								"You cannot add the values to the table since another duplicate row is already present");
						subContent.setMargin(true);
						subContent.addComponent(lblmessage);
						msgbox.setModal(true);
						msgbox.setSizeUndefined();
						msgbox.setContent(subContent);
						UI.getCurrent().addWindow(msgbox);

						boolean contains = tblsourcensinks
								.containsId(newItemId);
						if (contains == true) {
							tblsourcensinks.removeItem(newItemId);

						}

					}
				}

				catch (Exception ex) {
					System.out.println(ex.getMessage());

				}

			}
		});
		final Subwindow subwin = new Subwindow();
		subContent.addComponent(btnsavetotable);
		// Center it in the browser window
		subWindow.center();

		gridclasspath.addItemClickListener(new ItemClickListener() {
			public void itemClick(ItemClickEvent event) {
				if (event.getButton() == MouseEventDetails.MouseButton.RIGHT) {
					gridclasspath.select(event.getItemId());
				}
			}

		});
		tblthirdpartylib.addItemClickListener(new ItemClickListener() {
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					Object newItemId = tblthirdpartylib.getValue();
					newItemId = event.getItemId();
					itemid = newItemId;
					Item row = tblthirdpartylib.getItem(newItemId);
					TextField txt = (TextField) row.getItemProperty(
							"ThirdPartyLibrary").getValue();

					txt.setEnabled(true);
					Button buttonnam = new Button(buttonname);
					addComponent(buttonnam);
					buttonnam.setClickShortcut(KeyCode.ENTER);
					buttonnam.addClickListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {

							tblthirdpartylib.addItem(new Object[] { txt },
									itemid);
							Item row = tblthirdpartylib.getItem(itemid);
							row.getItemProperty("ThirdPartyLibrary").setValue(
									txt);
							System.out.println("the value of row added is "
									+ row.getItemProperty("ThirdPartyLibrary")
											.getValue());

							txt.setEnabled(false);
							txt.setVisible(true);
							buttonnam.removeClickShortcut();
							removeComponent(buttonnam);

						}
					});
				}
			}

		});
		gridclasspath.addActionHandler(new Action.Handler() {
			public Action[] getActions(Object target, Object sender) {
				final Action ACTION_NEW = new Action("New");
				// final Action ACTION_EDIT = new Action("Edit");
				final Action ACTION_DELETE = new Action("Delete");
				final Action[] ACTIONS = new Action[] { ACTION_NEW,
						ACTION_DELETE };
				return ACTIONS;

			}

			@Override
			public void handleAction(Action action, Object sender, Object target) {

				Item rowItem = gridclasspath.getItem(target);

				if (action.getCaption() == "New") {
					TextField txt = new TextField("textfield");

					txt.setWidth(24.6f, ComboBox.UNITS_EM);
					Object newItemId = gridclasspath.addItem();
					Item row = gridclasspath.getItem(newItemId);
					row.getItemProperty("Classpath").setValue(txt);

					gridclasspath.addItem(new Object[] { txt }, newItemId);
					Button buttonnam = new Button(buttonname);
					addComponent(buttonnam);

					buttonnam.setClickShortcut(KeyCode.ENTER);
					/*
					 * buttonnam.addClickListener(new Button.ClickListener() {
					 * 
					 * @Override public void buttonClick(ClickEvent event) {
					 * gridclasspath.addItem(new Object[] { txt }, newItemId);
					 * Item row = gridclasspath.getItem(newItemId);
					 * row.getItemProperty("Classpath").setValue(txt);
					 * System.out.println("the value of textfield is " +
					 * txt.getValue());
					 * 
					 * txt.setVisible(true); txt.setEnabled(false);
					 * 
					 * buttonnam.removeClickShortcut();
					 * removeComponent(buttonnam);
					 * 
					 * } });
					 */

				} else if (action.getCaption() == "Delete") {
					/*
					 * gridclasspath.removeItem(gridclasspath.getValue());
					 * gridclasspath.removeItem(rowItem);
					 */
					gridclasspath.removeItem(target);

				}

			}
		});

		// context menu ends

		// context menu for thirdpartylibrary

		tblthirdpartylib.addActionHandler(new Action.Handler() {
			public Action[] getActions(Object target, Object sender) {
				final Action ACTION_NEW = new Action("New");

				final Action ACTION_DELETE = new Action("Delete");
				final Action[] ACTIONS = new Action[] { ACTION_NEW,
						ACTION_DELETE };
				return ACTIONS;

			}

			@Override
			public void handleAction(Action action, Object sender, Object target) {

				Item rowItem = tblthirdpartylib.getItem(target);

				if (action.getCaption() == "New") {

					TextField txt = new TextField("textfield");
					txt.setWidth(24.6f, ComboBox.UNITS_EM);
					Object newItemId = tblthirdpartylib.addItem();
					Item row = tblthirdpartylib.getItem(newItemId);
					row.getItemProperty("ThirdPartyLibrary").setValue(txt);

					tblthirdpartylib.addItem(new Object[] { txt }, newItemId);

					Button buttonnamin = new Button(buttonname);
					addComponent(buttonnamin);

					buttonnamin.setClickShortcut(KeyCode.ENTER);
					buttonnamin.addClickListener(new Button.ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							// tblthirdpartylib.addItem(new Object[] { txt },
							// newItemId);
							// Item row = tblthirdpartylib.getItem(newItemId);
							// row.getItemProperty("ThirdPartyLibrary").setValue(txt);
							// System.out.println(
							// "the value of row added is " +
							// row.getItemProperty("ThirdPartyLibrary").getValue());

							// txt.setEnabled(false);
							txt.setVisible(true);

							buttonnamin.removeClickShortcut();
							removeComponent(buttonnamin);

						}
					});

				} else if (action.getCaption() == "Delete") {
					/*
					 * gridclasspath.removeItem(gridclasspath.getValue());
					 * gridclasspath.removeItem(rowItem);
					 */
					tblthirdpartylib.removeItem(target);

				}

			}
		});
		tblthirdpartylib.addListener(new ItemClickEvent.ItemClickListener() {

			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					txtfldthirdpartylib.setEnabled(true);
				}
			}
		});

		// context menu ends

		// context menu handler for table points to include
		tblpointstoinclude.addItemClickListener(new ItemClickListener() {
			public void itemClick(ItemClickEvent event) {
				if (event.getButton() == MouseEventDetails.MouseButton.RIGHT) {
					tblpointstoinclude.select(event.getItemId());
				}
			}

		});
		tblpointstoinclude.addActionHandler(new Action.Handler() {
			public Action[] getActions(Object target, Object sender) {
				final Action ACTION_NEW = new Action("New");

				final Action ACTION_DELETE = new Action("Delete");
				final Action[] ACTIONS = new Action[] { ACTION_NEW,
						ACTION_DELETE };
				return ACTIONS;

			}

			@Override
			public void handleAction(Action action, Object sender, Object target) {

				Item rowItem = tblpointstoinclude.getItem(target);

				if (action.getCaption() == "New") {
					TextField txt = new TextField("textfield");

					txt.setWidth(24.6f, ComboBox.UNITS_EM);

					Object newItemId = tblpointstoinclude.addItem();
					Item row = tblpointstoinclude.getItem(newItemId);
					row.getItemProperty("Points To Include").setValue(txt);

					tblpointstoinclude.addItem(new Object[] { txt }, newItemId);
					/*
					 * Button buttonnam = new Button(buttonname);
					 * addComponent(buttonnam);
					 * 
					 * buttonnam.setClickShortcut(KeyCode.ENTER);
					 * buttonnam.addClickListener(new Button.ClickListener() {
					 * 
					 * @Override public void buttonClick(ClickEvent event) {
					 * tblpointstoinclude.addItemAfter(new Object[] { txt },
					 * newItemId); Item row =
					 * tblpointstoinclude.getItem(newItemId);
					 * row.getItemProperty("Points To Include").setValue(txt);
					 * System.out.println("the value of textfield is " +
					 * txt.getValue());
					 * 
					 * txt.setEnabled(false); buttonnam.removeClickShortcut();
					 * removeComponent(buttonnam);
					 * 
					 * } });
					 */

				} else if (action.getCaption() == "Delete") {
					/*
					 * gridclasspath.removeItem(gridclasspath.getValue());
					 * gridclasspath.removeItem(rowItem);
					 */
					tblpointstoinclude.removeItem(target);

				}

			}
		});
		// ends
		// context menu for table points to exclude
		tblpointstoexclude.addItemClickListener(new ItemClickListener() {
			public void itemClick(ItemClickEvent event) {
				if (event.getButton() == MouseEventDetails.MouseButton.RIGHT) {
					tblpointstoexclude.select(event.getItemId());
				}
			}

		});
		tblpointstoexclude.addActionHandler(new Action.Handler() {
			public Action[] getActions(Object target, Object sender) {
				final Action ACTION_NEW = new Action("New");

				final Action ACTION_DELETE = new Action("Delete");
				final Action[] ACTIONS = new Action[] { ACTION_NEW,
						ACTION_DELETE };
				return ACTIONS;

			}

			@Override
			public void handleAction(Action action, Object sender, Object target) {

				Item rowItem = tblpointstoexclude.getItem(target);

				if (action.getCaption() == "New") {
					TextField txt = new TextField("textfield");

					txt.setWidth(24.6f, ComboBox.UNITS_EM);
					Object newItemId = tblpointstoexclude.addItem();
					Item row = tblpointstoexclude.getItem(newItemId);
					row.getItemProperty("Points To Exclude").setValue(txt);

					tblpointstoexclude.addItem(new Object[] { txt }, newItemId);
					/*
					 * Button buttonnam = new Button(buttonname);
					 * addComponent(buttonnam);
					 * 
					 * buttonnam.setClickShortcut(KeyCode.ENTER);
					 * buttonnam.addClickListener(new Button.ClickListener() {
					 * 
					 * @Override public void buttonClick(ClickEvent event) {
					 * tblpointstoexclude.addItemAfter(new Object[] { txt },
					 * newItemId); Item row =
					 * tblpointstoexclude.getItem(newItemId);
					 * row.getItemProperty("Points To Exclude").setValue(txt);
					 * System.out.println("the value of textfield is " +
					 * txt.getValue());
					 * 
					 * txt.setEnabled(false); buttonnam.removeClickShortcut();
					 * removeComponent(buttonnam);
					 * 
					 * } });
					 */

				} else if (action.getCaption() == "Delete") {
					/*
					 * gridclasspath.removeItem(gridclasspath.getValue());
					 * gridclasspath.removeItem(rowItem);
					 */
					tblpointstoexclude.removeItem(target);

				}

			}
		});
		// ends

		// context menu for table sourcensinks
		tblsourcensinks.addItemClickListener(new ItemClickListener() {
			public void itemClick(ItemClickEvent event) {
				if (event.getButton() == MouseEventDetails.MouseButton.RIGHT) {
					tblsourcensinks.select(event.getItemId());
				} else if (event.isDoubleClick()) {
					UI.getCurrent().addWindow(subWindow);
					selecteditemidsns = event.getItemId();

				}
			}

		});
		tblsourcensinks.addActionHandler(new Action.Handler() {
			public Action[] getActions(Object target, Object sender) {
				final Action ACTION_NEW = new Action("New");

				final Action ACTION_DELETE = new Action("Delete");
				final Action[] ACTIONS = new Action[] { ACTION_NEW,
						ACTION_DELETE };
				return ACTIONS;

			}

			@Override
			public void handleAction(Action action, Object sender, Object target) {

				Item rowItem = tblsourcensinks.getItem(target);

				if (action.getCaption() == "New") {

					// Open it in the UI
					UI.getCurrent().addWindow(subWindow);
					txtclasses.setValue("");
					txtselector.setValue("");
					;
					txtparam.setValue("");
					;
					chkinclude.setValue(false);
					chkindirectcalls.setValue(false);
					;

				} else if (action.getCaption() == "Delete") {

					tblsourcensinks.removeItem(target);

				}

			}
		});

		cmbmode.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = -5188369735622627751L;

			public void valueChange(ValueChangeEvent event) {
				if (cmbmode.getValue() != null
						&& (cmbmode.getValue() == "build")) {
					uploadSDGFile.setVisible(false);
					uploadCGFile.setVisible(false);

				} else {
					uploadSDGFile.setVisible(true);
					uploadCGFile.setVisible(true);

				}
			}

		});

	}

	protected void saveConfigurationxml(String Name) {

		File directory = new File(relativepath + "/apps/ws-flowAnaluser" + "/" + appname
				+ "/" + txtfldname.getValue());
		// temppath=temppath.replace('/',"\");
		// File directory = new File(temppath+"/"+txtfldname.getValue());
		String strTableData;
		List<String> listtabledata;

		try {
			prop.load(inputStream);
			if (!directory.exists()) {
				if (directory.mkdirs()) {
					System.out.println("Directory is created!");

				} else {
					System.out
							.println("Failed to create directory! which is ::"
									+ prop.getProperty("BaseFolderPath")
									+ Name
									+ prop.getProperty("StaticAnalysisFoldername"));
				}
			}

			String date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss")
					.format(new Date());
			System.out.println(date);
			// Files.write(Paths.get(Staticanalysispath
			// +"/"+txtfldname.getValue()+ "/" + Name + date +
			// ".xml"),"".getBytes());

			// code to generate and save static analysis config xml file

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("analysises");
			doc.appendChild(rootElement);

			// staff elements
			Element analysis = doc.createElement("analysis");
			rootElement.appendChild(analysis);

			// set attribute to staff element
			Attr attrname = doc.createAttribute("name");
			attrname.setValue("/apps/ws-flowAnaluser" + "/" + appname + "/"
					+ txtfldname.getValue());
			analysis.setAttributeNode(attrname);

			// mode elements
			Element mode = doc.createElement("mode");

			analysis.appendChild(mode);

			Attr attrvalue = doc.createAttribute("value");
			attrvalue.setValue((String) cmbmode.getValue());
			mode.setAttributeNode(attrvalue);

			listtabledata = Readfromcommontable(gridclasspath);
			strTableData = "";
			for (int i = 0; i < listtabledata.size(); i++) {
				if (i != 0)
					strTableData = strTableData + "::" + listtabledata.get(i);
				else
					strTableData = strTableData + listtabledata.get(i);
			}
			String strClasspath = strTableData;
			if (strClasspath == "")
				applicationname = relativepath + "/apps/ws-flowAnaluser" + "/" + appname
						+ "/" + "Source";
			else
				applicationname = relativepath + "/apps/ws-flowAnaluser" + "/" + appname
						+ "/" + "Source" + "/" + strClasspath;

			Element classpath = doc.createElement("classpath");

			analysis.appendChild(classpath);

			Attr attrvaluepath = doc.createAttribute("value");

			attrvaluepath.setValue(applicationname);
			classpath.setAttributeNode(attrvaluepath);
			// temp
			// thirdpartylib elements
			Element thirdPartyLibs = doc.createElement("thirdPartyLibs");

			analysis.appendChild(thirdPartyLibs);

			Attr attrvaluetpl = doc.createAttribute("value");
			listtabledata = Readfromcommontable(tblthirdpartylib);
			strTableData = "";
			for (int i = 0; i < listtabledata.size(); i++) {
				if (i != 0)
					strTableData = strTableData + "::" + listtabledata.get(i);
				else
					strTableData = strTableData + listtabledata.get(i);
			}
			String tblvalue = strTableData;
			attrvaluetpl.setValue(tblvalue);
			thirdPartyLibs.setAttributeNode(attrvaluetpl);

			// stubs elements
			Element stubs = doc.createElement("stubs");
			// stubs.appendChild(doc.createTextNode("100000"));
			analysis.appendChild(stubs);

			Attr attrvaluestub = doc.createAttribute("value");
			attrvaluestub.setValue((String) cmbstub.getValue());
			stubs.setAttributeNode(attrvaluestub);

			Element entrypoint = doc.createElement("entrypoint");

			analysis.appendChild(entrypoint);

			Attr attrvalueentry = doc.createAttribute("value");
			attrvalueentry.setValue(txtfldEntryPoint.getValue());
			entrypoint.setAttributeNode(attrvalueentry);
			Element pointsto = doc.createElement("points-to");

			analysis.appendChild(pointsto);
			Attr attrpolicy = doc.createAttribute("policy");

			attrpolicy.setValue((String) cmbpointstopolicy.getValue());
			pointsto.setAttributeNode(attrpolicy);
			Attr attrfallback = doc.createAttribute("fallback");
			attrfallback.setValue(txtpointtofallback.getValue());
			pointsto.setAttributeNode(attrfallback);
			// read data from points to include and exclude
			listtabledata = Readfromcommontable(tblpointstoinclude);
			strTableData = "";

			for (int i = 0; i < listtabledata.size(); i++) {
				Element includeclass = doc.createElement("include-classes");
				pointsto.appendChild(includeclass);
				Attr attriincludeclass = doc.createAttribute("value");
				attriincludeclass.setValue(listtabledata.get(i));
				includeclass.setAttributeNode(attriincludeclass);
			}

			listtabledata = Readfromcommontable(tblpointstoexclude);
			strTableData = "";
			for (int i = 0; i < listtabledata.size(); i++) {
				Element excludeclass = doc.createElement("exclude-classes");
				pointsto.appendChild(excludeclass);
				Attr attriexcludeclass = doc.createAttribute("value");
				attriexcludeclass.setValue(listtabledata.get(i));
				excludeclass.setAttributeNode(attriexcludeclass);
			}
			// ends

			Element ignoreIndirectFlows = doc
					.createElement("ignoreIndirectFlows");

			analysis.appendChild(ignoreIndirectFlows);
			Attr attrignoreindirect = doc.createAttribute("value");
			attrignoreindirect.setValue(Boolean.toString(chkindirectflows
					.getValue()));
			ignoreIndirectFlows.setAttributeNode(attrignoreindirect);

			Element multithreaded = doc.createElement("multithreaded");

			analysis.appendChild(multithreaded);
			Attr attrmultith = doc.createAttribute("value");
			attrmultith.setValue(Boolean.toString(chkmultithreaded.getValue()));
			multithreaded.setAttributeNode(attrmultith);

			Element sdgfile = doc.createElement("sdgfile");

			analysis.appendChild(sdgfile);
			Attr attrsdg = doc.createAttribute("value");

			sdgfile.setAttributeNode(attrsdg);

			Element cgfile = doc.createElement("cgfile");

			analysis.appendChild(cgfile);
			Attr attrcgfile = doc.createAttribute("value");

			cgfile.setAttributeNode(attrcgfile);

			Element reportfile = doc.createElement("reportfile");
			Element logfile = doc.createElement("logFile");
			analysis.appendChild(reportfile);
			Attr attrreport = doc.createAttribute("value");
			Attr attrlog = doc.createAttribute("value");
			if (cmbmode.getValue() == "load") {
				attrreport.setValue(txtReportFile.getValue());
				attrlog.setValue(txtlogFile.getValue());
				attrcgfile.setValue(txtCGFile.getValue());
				attrsdg.setValue(txtSDGFile.getValue());
			} else if (cmbmode.getValue() == "build") {
				attrreport.setValue(txtReportFile.getValue());
				attrlog.setValue(txtlogFile.getValue());
				attrcgfile.setValue(txtCGFile.getValue());
				attrsdg.setValue(txtSDGFile.getValue());
			}
			reportfile.setAttributeNode(attrreport);
			logfile.setAttributeNode(attrlog);

			Element computeChops = doc.createElement("computeChops");

			analysis.appendChild(computeChops);
			Attr attrcompute = doc.createAttribute("value");
			attrcompute.setValue(Boolean.toString(chkcomputechops.getValue()));
			computeChops.setAttributeNode(attrcompute);

			Element systemout = doc.createElement("systemout");

			analysis.appendChild(systemout);
			Attr attrsystemout = doc.createAttribute("value");
			attrsystemout.setValue(Boolean.toString(chkSystemOut.getValue()));
			systemout.setAttributeNode(attrsystemout);

			Element objectsensitivenes = doc
					.createElement("objectsensitivenes");

			analysis.appendChild(objectsensitivenes);
			Attr attrsensitive = doc.createAttribute("value");
			attrsensitive
					.setValue(Boolean.toString(chksensitiveness.getValue()));
			objectsensitivenes.setAttributeNode(attrsensitive);

			Element sourcesandsinks = doc.createElement("sourcesandsinks");
			analysis.appendChild(sourcesandsinks);
			// read tablesourcensinks data
			List<Map<String, String>> listdatasourcensinks = ReadfromSourcenSinkstable(tblsourcensinks);

			for (int i = 0; i < listdatasourcensinks.size(); i++) {
				if (listdatasourcensinks.get(i).get("Types") == "Source") {
					Element source = doc.createElement("source");
					sourcesandsinks.appendChild(source);
					Attr classvalue = doc.createAttribute("Classes");
					classvalue.setValue(listdatasourcensinks.get(i).get(
							"Classes"));
					source.setAttributeNode(classvalue);
					Attr selectorvalue = doc.createAttribute("selector");
					selectorvalue.setValue(listdatasourcensinks.get(i).get(
							"Selector"));
					source.setAttributeNode(selectorvalue);
					Attr paramsvalue = doc.createAttribute("params");
					paramsvalue.setValue(listdatasourcensinks.get(i).get(
							"Param"));
					source.setAttributeNode(paramsvalue);
					if (listdatasourcensinks.get(i).get("Include SubClasses") != "") {
						Attr includeclassvalue = doc
								.createAttribute("includeSubclasses");
						includeclassvalue.setValue(listdatasourcensinks.get(i)
								.get("Include SubClasses"));
						source.setAttributeNode(includeclassvalue);
					}
					if (listdatasourcensinks.get(i).get("Indirect Calls") != "") {
						Attr indirectcallsvalue = doc
								.createAttribute("indirectCalls");
						indirectcallsvalue.setValue(listdatasourcensinks.get(i)
								.get("Indirect Calls"));
						source.setAttributeNode(indirectcallsvalue);
					}
				} else if (listdatasourcensinks.get(i).get("Types") == "Sink") {
					Element sink = doc.createElement("sink");
					sourcesandsinks.appendChild(sink);
					Attr classvalue = doc.createAttribute("Classes");
					classvalue.setValue(listdatasourcensinks.get(i).get(
							"Classes"));
					sink.setAttributeNode(classvalue);
					Attr selectorvalue = doc.createAttribute("selector");
					selectorvalue.setValue(listdatasourcensinks.get(i).get(
							"Selector"));
					sink.setAttributeNode(selectorvalue);
					Attr paramsvalue = doc.createAttribute("params");
					paramsvalue.setValue(listdatasourcensinks.get(i).get(
							"Param"));
					sink.setAttributeNode(paramsvalue);
					Attr includeclassvalue = doc
							.createAttribute("includeSubclasses");
					includeclassvalue.setValue(listdatasourcensinks.get(i).get(
							"Include SubClasses"));
					sink.setAttributeNode(includeclassvalue);
					Attr indirectcallsvalue = doc
							.createAttribute("indirectCalls");
					indirectcallsvalue.setValue(listdatasourcensinks.get(i)
							.get("Indirect Calls"));
					sink.setAttributeNode(indirectcallsvalue);

				}
			}
			if (txtfldSnSFile.getValue() != "") {
				Element Fileelement = doc.createElement("file");
				sourcesandsinks.appendChild(Fileelement);
				Attr Filevalue = doc.createAttribute("value");
				txtfldSnSFile.getValue().replace('\\','/');
				Filevalue.setValue(txtfldSnSFile.getValue());
				Fileelement.setAttributeNode(Filevalue);
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(relativepath
					+ "/apps/ws-flowAnaluser" + "/" + appname + "/" + txtfldname.getValue()
					+ "/" + Name + ".xml"));

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// code ends

	private List<String> Readfromcommontable(Table tablename) {

		List<String> strpaths = new ArrayList<>();
		int newItemId = tablename.size();
		try {
			for (int i = 1; i <= newItemId; i++) {
				Item row = tablename.getItem(i);
				TextField temp = new TextField();
				if (tablename.getCaption() == "ClassPath") {
					temp = (TextField) row.getItemProperty("Classpath")
							.getValue();

				} else if (tablename.getCaption() == "Third Party Library")
					temp = (TextField) row.getItemProperty("ThirdPartyLibrary")
							.getValue();
				else if (tablename.getCaption() == "Points To Include")
					temp = (TextField) row.getItemProperty("Points To Include")
							.getValue();
				else if (tablename.getCaption() == "Points To Exclude")
					temp = (TextField) row.getItemProperty("Points To Exclude")
							.getValue();

				strpaths.add(temp.getValue());
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return strpaths;

	}

	private List<Map<String, String>> ReadfromSourcenSinkstable(Table tablename) {
		List<Map<String, String>> listsnsinks = new ArrayList<Map<String, String>>();

		int newItemId = tablename.size();
		for (int i = 1; i <= newItemId; i++) {
			Map<String, String> listmapvalues = new HashMap<String, String>();

			Item row = tablename.getItem(i);
			TextField temp = new TextField();
			CheckBox chktemp = new CheckBox();

			temp = (TextField) row.getItemProperty("Types").getValue();
			listmapvalues.put("Types", temp.getValue());
			temp = (TextField) row.getItemProperty("Classes").getValue();
			listmapvalues.put("Classes", temp.getValue());
			temp = (TextField) row.getItemProperty("Selector").getValue();
			listmapvalues.put("Selector", temp.getValue());
			temp = (TextField) row.getItemProperty("Param").getValue();
			listmapvalues.put("Param", temp.getValue());
			chktemp = (CheckBox) row.getItemProperty("Include SubClasses")
					.getValue();
			if (chktemp != null)
				listmapvalues.put("Include SubClasses",
						Boolean.toString(chktemp.getValue()));
			else
				listmapvalues.put("Include SubClasses", "");
			chktemp = (CheckBox) row.getItemProperty("Indirect Calls")
					.getValue();
			if (chktemp != null)
				listmapvalues.put("Indirect Calls",
						Boolean.toString(chktemp.getValue()));
			else
				listmapvalues.put("Include SubClasses", "");
			listsnsinks.add(listmapvalues);

		}
		return listsnsinks;

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
		if (event.getParameters() != null) {
			// split at "/", add each part as a label
			String[] msgs = event.getParameters().split("/");
			for (String msg : msgs) {
				appname = msg;
				System.out.println("enter view changeevent " + appname);
			}
			txtfldname.setValue(appname);
		}

		// TODO Auto-generated method stub

	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		// TODO Auto-generated method stub
		if (event.getComponent().getCaption() == "CGFile")
			txtCGFile.setValue(relativepath + "/" + txtfldname.getValue()
					+ "/" + event.getFilename());
		else if (event.getComponent().getCaption() == "SDGFile")
			txtSDGFile.setValue(relativepath + "/"
					+ txtfldname.getValue() + "/" + event.getFilename());
		else if (event.getComponent().getCaption() == "reportFile")
			txtReportFile.setValue(relativepath + "/"
					+ txtfldname.getValue() + "/" + event.getFilename());

		else if (event.getComponent().getCaption() == "Source and sink File")
			txtfldSnSFile.setValue(relativepath +"/apps/ws-flowAnaluser/"+appname+"/"
					+ txtfldname.getValue() + "/" + event.getFilename());

	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {

		File directory = new File(Staticanalysispath + "/apps/ws-flowAnaluser" + "/" + appname+"/"+txtfldname.getValue());

		if (!directory.exists()) {
			if (directory.mkdirs()) {
				System.out.println("Directory is created!");

			}
		}
		OutputStream fos = null; // Output stream to write to
		File file = new File(Staticanalysispath + "/apps/ws-flowAnaluser" + "/" + appname + "/"+txtfldname.getValue()+"/"
				+ filename);// strUploadFilePathCG+
		// file.renameTo(new File(strUploadFilePathCG + filename));

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

	public void uploadFailed(Upload.FailedEvent event) {
		// Log the failure on screen.

	}

	//
}