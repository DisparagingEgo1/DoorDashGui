package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main implements ActionListener,FocusListener{
	
	
	private final String RESTORE_FILE = "temp.dd";
	private final double SS_TAX_RATE = 0.153;
	private final double MILE_RATE = 0.585;
	private final double[][] INCOME_BRACKETS = new double [][] {{0.1,9950},{0.12,40525},{0.22,86375}};
	private final int STANDARD_DEDUCTION = 12950;
	private static final double versionId = 1.0;
	
	private ArrayList<Shift>shifts;
	private double earning = 0.00;
	private int hoursWorked = 0;
	private int minutesWorked = 0;
	private double grossHourlyWage = 0.00;
	private double netHourlyWage = 0.00;
	private double expectedSSTaxes = 0.00;
	private double expectedIncomeTax = 0.00;
	private int totalMileage=0;
	private String userFirstName = "";
	private String userLastName = "";
	private String state = "";
	private String year = "";
	
	private JFrame mainFrame;
	private JLabel earningsLabel;
	private JLabel timeWorked;
	private JLabel grossWage;
	private JLabel netWage;
	private JLabel ssTaxes;
	private JLabel incomeTaxes;
	private JLabel mileage;
	private JTextField startMileage;
	private JTextField endMileage;
	private JTextField date;
	private JTextField earnings;
	private JTextField startTime;
	private JTextField endTime;
	private JComboBox<String> startAmPm;
	private JComboBox<String> endAmPm;
	private JButton submit;
	private JTable shiftTable;
	private JButton delete;
	private JMenuItem newForm;
	private JMenuItem save;
	private JMenuItem load;
	private Timer timer;
	private ArrayList<String> newFormFields;
	private JTabbedPane fdPane;
	
	public Main() {
		this.mainFrame = new JFrame("DoorDash Data Tracker v"+Main.versionId);
		//Panels
		JPanel mainPanel = new JPanel();
		
		JPanel menuPanel = new JPanel();
		
		JPanel formPanel = new JPanel();
		JPanel startingMileagePanel = new JPanel();
		JPanel endingMileagePanel = new JPanel();
		JPanel datePanel = new JPanel();
		JPanel formEarningsPanel = new JPanel();
		JPanel formStartTimePanel = new JPanel();
		JPanel formEndTimePanel = new JPanel();
		JPanel formSubmitButtonPanel = new JPanel();
		
		JPanel statsPanel = new JPanel();
		JPanel earningsPanel = new JPanel();
		JPanel statsTitlePanel = new JPanel();
		JPanel timeWorkedPanel = new JPanel();
		JPanel grossWagePanel = new JPanel();
		JPanel netWagePanel = new JPanel();
		JPanel ssTaxPanel = new JPanel();
		JPanel incomeTaxPanel = new JPanel();
		JPanel milePanel = new JPanel();
		
		JPanel dataPanel = new JPanel();
		
		
		 this.fdPane = new JTabbedPane();
		
		//Shifts Panel Config
		dataPanel.setLayout(new BorderLayout());
		String[]columnNames = {"","Date", "Start Time", "End Time", "Earnings"};
		DefaultTableModel model = new DefaultTableModel(356, 5);
		model.setColumnIdentifiers(columnNames);
		//creates anonymous class
		this.shiftTable = new JTable(model) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {
			     switch (col) {
			         case 0:
			             return true;
			         default:
			             return false;
			      }
			}
			//creates the checkboxes in the column
			@Override
		    public Class<?> getColumnClass(int column) {
		        if(column == 0){
		            return Boolean.class;
		        }
		        return String.class;
		    } 
		};
		for(int i = 0; i < 356; i++) {
			this.shiftTable.setValueAt(false, i, 0);
		}
		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(JLabel.CENTER);
		this.shiftTable.setDefaultRenderer(String.class,center);
		JScrollPane dataScrollPane= new JScrollPane(shiftTable);
		this.delete = new JButton("Delete");
		this.delete.addActionListener(this);
		dataPanel.add(dataScrollPane,BorderLayout.CENTER);
		dataPanel.add(this.delete,BorderLayout.PAGE_END);
		
		//Menu Panel Config
		JMenuBar jmb = new JMenuBar();
		JMenu file = new JMenu("File");
		this.newForm = new JMenuItem("New");
		this.save = new JMenuItem("Save");
		this.load = new JMenuItem("Load");
		this.newForm.addActionListener(this);
		this.save.addActionListener(this);
		this.load.addActionListener(this);
		file.add(this.newForm);
		file.add(this.save);
		file.add(this.load);
		jmb.add(file);
		menuPanel.add(jmb);
		menuPanel.setLayout(new GridLayout(1,1));
		
		//Stats Panel Config
		JLabel title = new JLabel();
		this.earningsLabel = new JLabel("Earnings: $0.00    ");//pad 4 spaces
		this.timeWorked = new JLabel("Hours: 0    Minutes: 0 ");//pad 3 spaces
		this.grossWage = new JLabel("Gross Wage: $0.00 ");//pad 1 space
		this.netWage = new JLabel("Net Wage: $0.00 ");//pad 1 space
		this.ssTaxes = new JLabel("SS Tax: $0.00   ");//pad 3 spaces
		this.incomeTaxes = new JLabel("Income Tax: $0.00   ");//pad 3 spaces
		this.mileage = new JLabel("Mileage: 0   ");//pad 3 spaces
		earningsPanel.add(earningsLabel);
		title.setText("<html><u>Stats</u></html>");
		statsTitlePanel.add(title);
		timeWorkedPanel.add(timeWorked);
		grossWagePanel.add(grossWage);
		ssTaxPanel.add(ssTaxes);
		incomeTaxPanel.add(incomeTaxes);
		milePanel.add(mileage);
		netWagePanel.add(netWage);
		statsPanel.setLayout(new BoxLayout(statsPanel,BoxLayout.Y_AXIS));
		statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		statsTitlePanel.setMaximumSize(new Dimension(100,250));
		earningsPanel.setMaximumSize(new Dimension(150,250));
		timeWorkedPanel.setMaximumSize(new Dimension(200,250));
		grossWagePanel.setMaximumSize(new Dimension(250,250));
		ssTaxPanel.setMaximumSize(new Dimension(250,250));
		incomeTaxPanel.setMaximumSize(new Dimension(250,250));
		netWagePanel.setMaximumSize(new Dimension(250,250));
		statsPanel.add(statsTitlePanel);
		statsPanel.add(earningsPanel);
		statsPanel.add(timeWorkedPanel);
		statsPanel.add(grossWagePanel);
		statsPanel.add(netWagePanel);
		statsPanel.add(ssTaxPanel);
		statsPanel.add(incomeTaxPanel);
		statsPanel.add(milePanel);
		
		//Tabbed Pane Config
		this.fdPane.addTab("Trip", formPanel);
		this.fdPane.addTab("Shifts",dataPanel);
		
		//Form Panel Config
		GridBagConstraints gbc = new GridBagConstraints();
		this.startMileage = new JTextField(10);
		JLabel startMileageLabel = new JLabel("Starting Miles:");
		this.endMileage = new JTextField(10);
		JLabel endMileageLabel = new JLabel("Ending Miles:");
		JLabel dateLabel = new JLabel("Date:");
		this.date = new JTextField(10);
		JLabel eLabel = new JLabel("Earnings: $");
		this.earnings = new JTextField(10);
		JLabel startTimeLabel = new JLabel("Start Time:");
		this.startTime = new JTextField(10);
		String[] time = new String[]{"AM","PM"};
		this.startAmPm = new JComboBox<String>(time);
		JLabel endTimeLabel = new JLabel("End Time:");
		this.endTime = new JTextField(10);
		this.endAmPm = new JComboBox<String>(time);
		this.submit = new JButton("Submit");
		
		formPanel.setLayout(new GridBagLayout());
		datePanel.add(dateLabel);
		datePanel.add(date);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(0,0,-50,0);//top,left,bottom,right
		formPanel.add(datePanel,gbc);
		
		formEarningsPanel.add(eLabel);
		formEarningsPanel.add(earnings);
		gbc.gridx = 1;
		formPanel.add(formEarningsPanel,gbc);
		
		startingMileagePanel.add(startMileageLabel);
		startingMileagePanel.add(startMileage);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(-25,0,0,0);//top,left,bottom,right
		formPanel.add(startingMileagePanel,gbc);
		gbc.gridx = 1;
		endingMileagePanel.add(endMileageLabel);
		endingMileagePanel.add(endMileage);
		formPanel.add(endingMileagePanel,gbc);
		
		startAmPm.setSelectedIndex(0);
		formStartTimePanel.add(startTimeLabel);
		formStartTimePanel.add(startTime);
		formStartTimePanel.add(startAmPm);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(-50,0,0,0);//top,left,bottom,right
		formPanel.add(formStartTimePanel,gbc);
		
		endAmPm.setSelectedIndex(0);
		formEndTimePanel.add(endTimeLabel);
		formEndTimePanel.add(endTime);
		formEndTimePanel.add(endAmPm);
		gbc.gridx = 1;
		formPanel.add(formEndTimePanel,gbc);
		
		formSubmitButtonPanel.add(submit);
		submit.addActionListener(this);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.insets = new Insets(-50,0,0,0);//top,left,bottom,right
		formPanel.add(formSubmitButtonPanel,gbc);
		
		//Main Panel Config
		mainPanel.setLayout(new BorderLayout());
		//fdPane.setVisible(false);
		mainPanel.add(this.fdPane,BorderLayout.CENTER);
		this.fdPane.setVisible(false);
		mainPanel.add(statsPanel,BorderLayout.LINE_END);
		mainPanel.add(menuPanel,BorderLayout.PAGE_START);
		
		//Frame Config and Initialize
		ImageIcon img = new ImageIcon("doordash.png");
		this.mainFrame.setIconImage(img.getImage());
		this.mainFrame.setSize(new Dimension(700,350));
		this.mainFrame.setResizable(false);
		this.mainFrame.setLocationRelativeTo(null);
		this.mainFrame.add(mainPanel);
		this.timer = new Timer(60000,this);
		this.timer.start();
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.setVisible(true);
		this.newFormFields = new ArrayList<String>(4);
		for(int i = 1; i < 5; i++)this.newFormFields.add("");
		initialize();
		
	}
	private void initialize() {
		this.shifts = new ArrayList<Shift>();
		load("");
	}
	private void update(final Shift s, final int mode) {
		updateEarnings(s.getEarnings(),mode);
		updateMileage(s.getStartMileage(),s.getEndMileage(),mode);
		updateTime(s.getDuration(),mode);
		updateTaxesAndWages();
		updateDataTable();
		if(mode == 1) {
			this.startMileage.setText("");
			this.endMileage.setText("");
			this.date.setText("");
			this.earnings.setText("");
			this.startTime.setText("");
			this.endTime.setText("");
			this.startAmPm.setSelectedIndex(0);
			this.endAmPm.setSelectedIndex(0);
		}
	}
	private void updateDataTable() {
		//date, start, end, earnings
		Collections.sort(this.shifts);
		for(int i = 0; i <356;i++) {
			for(int j = 0; j < 5; j++) {
				if(j==0)this.shiftTable.setValueAt(Boolean.FALSE, i, j);
				else {
					if(i<this.shifts.size()) {
						switch(j) {
						case 1:
							this.shiftTable.setValueAt(this.shifts.get(i).getDate(), i, j);
							break;
						case 2:
							this.shiftTable.setValueAt(this.shifts.get(i).getStartTime(), i, j);
							break;
						case 3:
							this.shiftTable.setValueAt(this.shifts.get(i).getEndTime(), i, j);
							break;
						case 4:
							this.shiftTable.setValueAt(String.format("$%.2f", this.shifts.get(i).getEarnings()), i, j);
							break;
						}
					}
					else this.shiftTable.setValueAt("", i, j);
				}
			}
		}
		
	}
	private void updateTaxesAndWages() {
		if(this.hoursWorked!= 0 || this.minutesWorked!= 0)this.grossHourlyWage = (double)this.earning/(this.hoursWorked + (this.minutesWorked/60));
		else this.grossHourlyWage = 0.00;
		this.expectedSSTaxes = this.earning*this.SS_TAX_RATE;//deduct 7.65%, 58.5 cents per mile, standard deduction is 12,950
		double taxableIncome = 0.00;
		if((this.expectedSSTaxes*.5) + (this.totalMileage * 0.585) > 12950) {
			taxableIncome = this.earning - ((this.expectedSSTaxes*.5) + (this.totalMileage * this.MILE_RATE));
		}
		else {
			taxableIncome = this.earning - this.STANDARD_DEDUCTION;
		}
		if(taxableIncome < 0)taxableIncome = 0;
		else if(taxableIncome > 0) {
			int index = 0;
			while(taxableIncome >0) {
				this.expectedIncomeTax+=this.INCOME_BRACKETS[index][0]*taxableIncome;
				taxableIncome -= this.INCOME_BRACKETS[index][1];
				index++;
			}
		}
		
		if(this.hoursWorked!= 0 || this.minutesWorked!= 0)this.netHourlyWage = (double) (this.earning - this.expectedSSTaxes - this.expectedIncomeTax)/(this.hoursWorked + (this.minutesWorked/60));
		else this.netHourlyWage = 0.00;
		
		if(this.grossHourlyWage <10) {
			this.grossWage.setText(String.format("Gross Wage: $%.2f",this.grossHourlyWage)+" ");
		}
		else {
			this.grossWage.setText(String.format("Gross Wage: $%.2f",this.grossHourlyWage));
		}
		
		if(this.netHourlyWage < 10) {
			this.netWage.setText(String.format("Net Wage: $%.2f",this.netHourlyWage)+" ");
		}
		else {
			this.netWage.setText(String.format("Net Wage: $%.2f",this.netHourlyWage));
		}
		
		if(this.expectedSSTaxes < 10) {
			this.ssTaxes.setText(String.format("SS Tax: $%.2f",this.expectedSSTaxes)+"   ");
		}
		else if(this.expectedSSTaxes < 100) {
			this.ssTaxes.setText(String.format("SS Tax: $%.2f",this.expectedSSTaxes)+"  ");
		}
		else if(this.expectedSSTaxes < 1000) {
			this.ssTaxes.setText(String.format("SS Tax: $%.2f",this.expectedSSTaxes)+" ");
		}
		else {
			this.ssTaxes.setText(String.format("SS Tax: $%.2f",this.expectedSSTaxes));
		}
		if(this.expectedIncomeTax < 10) {
			this.incomeTaxes.setText(String.format("Income Tax: $%.2f",this.expectedIncomeTax) +"   ");
		}
		else if(this.expectedIncomeTax  < 100) {
			this.incomeTaxes.setText(String.format("Income Tax: $%.2f",this.expectedIncomeTax) +"  ");
		}
		else if(this.expectedIncomeTax  < 1000) {
			this.incomeTaxes.setText(String.format("Income Tax: $%.2f",this.expectedIncomeTax) +" ");
		}
		else {
			this.incomeTaxes.setText(String.format("Income Tax: $%.2f",this.expectedIncomeTax) );
		}
	}
	private void updateTime(final int duration, final int mode) {
		int totalMinsWorked, totalHoursWorked;
		totalMinsWorked = duration;
		totalHoursWorked = totalMinsWorked /60;
		totalMinsWorked %= 60;
		if(mode == 1) {
			this.minutesWorked += totalMinsWorked;
			this.hoursWorked += totalHoursWorked;
			if(this.minutesWorked > 60) {
				this.minutesWorked %=60;
				this.hoursWorked+=1;
			}
		}
		else {
			this.minutesWorked -= totalMinsWorked;
			this.hoursWorked -= totalHoursWorked;
			if(this.minutesWorked < 0) {
				this.minutesWorked +=60;
				this.hoursWorked-=1;
			}
		}
		
		//this.timeWorked = new JLabel("Hours: XX0  Minutes: X0");//pad 3 spaces
		String labelString = "Hours: ";
		if(this.hoursWorked < 10) {
			labelString +=this.hoursWorked+"  ";
		}
		else if(this.hoursWorked < 100) {
			labelString +=this.hoursWorked + " ";
		}
		else {
			labelString +=this.hoursWorked;
		}
		labelString += " Minutes: ";
		if(this.minutesWorked < 10) {
			labelString += this.minutesWorked+" ";
		}
		else {
			labelString += this.minutesWorked;
		}
		this.timeWorked.setText(labelString);
	}
	private void updateMileage(final int start, final int end, final int mode) {
		if(mode == 1)this.totalMileage += end-start;
		else this.totalMileage -= end - start;
		if(this.totalMileage < 10) {
			this.mileage.setText("Mileage: "+this.totalMileage+"   ");//pad 3 spaces
		}
		else if( this.totalMileage < 100) {
			this.mileage.setText("Mileage: "+this.totalMileage+"   ");//pad 2 spaces
		}
		else if(this.totalMileage < 1000) {
			this.mileage.setText("Mileage: "+this.totalMileage+" ");//pad 1 space
		}
		else {
			this.mileage.setText("Mileage: "+this.totalMileage);//pad 0 spaces
		}
	}
	private void updateEarnings(final double shiftEarnings, int mode) {
		if(mode == 1)this.earning += shiftEarnings;
		else this.earning -= shiftEarnings;
		if(this.earning < 10) {
			this.earningsLabel.setText(String.format("Earnings: $%.2f    ",this.earning));//"Earnings: $0.00    ");//pad 4 spaces
		}
		else if(this.earning < 100) {
			this.earningsLabel.setText(String.format("Earnings: $%.2f   ",this.earning));//"Earnings: $0.00    ");//pad 3 spaces
		}
		else if(this.earning < 1000) {
			this.earningsLabel.setText(String.format("Earnings: $%.2f  ",this.earning));//"Earnings: $0.00    ");//pad 2 spaces
		}
		else if(this.earning < 10000) {
			this.earningsLabel.setText(String.format("Earnings: $%.2f ",this.earning));//"Earnings: $0.00    ");//pad 1 spaces
		}
		else {
			this.earningsLabel.setText(String.format("Earnings: $%.2f",this.earning));//"Earnings: $0.00    ");//pad 0 spaces
		}
	}
	
	private boolean verifyFormData() {
		boolean d = verifyDate(),m = verifyMileage(),e = verifyEarnings(),t = verifyTime();
		return d && m && e && t;
	}
	private boolean verifyTime() {
		if(this.startTime.getText().matches(".*[a-zA-Z].*")||!(this.startTime.getText().matches("1[0-2]:[0-5][0-9]")||this.startTime.getText().matches("[0-9]:[0-5][0-9]"))){
			this.startTime.setForeground(Color.red);
		}
		else {
			this.startTime.setForeground(Color.black);
		}
		if(this.endTime.getText().matches(".*[a-zA-Z].*")||!(this.endTime.getText().matches("1[0-2]:[0-5][0-9]")||this.endTime.getText().matches("[0-9]:[0-5][0-9]"))){
			this.endTime.setForeground(Color.red);
		}
		else this.endTime.setForeground(Color.black);
		if(this.startTime.getForeground().equals(Color.red)||this.endTime.getForeground().equals(Color.red))return false;
		return true;
	}
	private boolean verifyEarnings() {
		try {
			Double.parseDouble(this.earnings.getText());
			if(this.earnings.getText().matches("[0-9]*[0-9]\\.[0-9][0-9]")||this.earnings.getText().matches("[0-9]*[0-9]")) {
				this.earnings.setForeground(Color.black);
				return true;
			}
			
		}
		catch(NumberFormatException e) {
			
		}
		this.earnings.setForeground(Color.red);
		return false;
	}
	private boolean verifyMileage() {
		try {
			int begin = Integer.parseInt(this.startMileage.getText()),end =Integer.parseInt(this.endMileage.getText());
			if(begin<end) {
				this.startMileage.setForeground(Color.black);
				this.endMileage.setForeground(Color.black);
				return true;
			}
			else {
				this.startMileage.setForeground(Color.red);
				this.endMileage.setForeground(Color.red);
			}
		}
		catch(NumberFormatException e) {
			if(this.startMileage.getText().matches(".*[a-zA-Z].*")) {
				this.startMileage.setForeground(Color.red);
			}
			else {
				this.endMileage.setForeground(Color.red);
			}
		}
		
		return false;
	}
	private boolean verifyDate() {
		String date = this.date.getText();
		if(!date.matches(".*[a-zA-Z].*")&&(date.matches("[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]")||date.matches("[0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]")||
				date.matches("[0-9][0-9]/[0-9]/[0-9][0-9][0-9][0-9]")||date.matches("[0-9]/[0-9]/[0-9][0-9][0-9][0-9]"))) {
			this.date.setForeground(Color.black);
			return true;
		}
		this.date.setForeground(Color.RED);
		return false;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.submit)) {
			if(!this.startMileage.getText().equals("")&&!this.endMileage.getText().equals("")&&!this.date.getText().equals("")&&!this.earnings.getText().equals("")
					&&!this.startTime.getText().equals("")&&!this.endTime.getText().equals("")) {
				if(verifyFormData()) {
					//final String date, final String startTime, final String endTime, final double earnings, final int startMileage, final int endMileage)
					shifts.add(new Shift(this.date.getText(),this.startTime.getText()+this.startAmPm.getSelectedItem(),this.endTime.getText()+this.endAmPm.getSelectedItem(),
							Double.parseDouble(this.earnings.getText()),Integer.parseInt(this.startMileage.getText()),Integer.parseInt(this.endMileage.getText())));
					
					update(shifts.get(shifts.size()-1),1);
				}
				else Toolkit.getDefaultToolkit().beep();
				
			}
			else {
				Toolkit.getDefaultToolkit().beep();
			}
		}
		else if(e.getSource().equals(this.delete)) {
			ArrayList<Shift>toRemove = new ArrayList<Shift>();
			for(int i=shifts.size()-1; i >=0; i--) {
				if(shiftTable.getValueAt(i, 0)==Boolean.TRUE) {
					toRemove.add(shifts.remove(i));
				}
			}
			
			for(Shift s: toRemove) {
				update(s,2);
			}
		}
		else if(e.getSource().equals(this.save)) {
			if(this.fdPane.isVisible()) {
				this.timer.restart();
				this.mainFrame.setTitle("DoorDash Data Tracker v"+Main.versionId+ " -"+this.userFirstName + " "+this.userLastName + " "+this.year);
				save();
			}
			else Toolkit.getDefaultToolkit().beep();
			
		}
		else if(e.getSource().equals(this.load)) {
			fileChooser();
		}
		else if(e.getSource().equals(this.timer)) {
			if(this.fdPane.isVisible()) {
				this.mainFrame.setTitle("DoorDash Data Tracker v"+Main.versionId+ " -"+this.userFirstName + " "+this.userLastName + " "+this.year+ " (Autosaved)");
				save();
			}
		}
		else if(e.getSource().equals(this.newForm)) {
			createNewForm();
		}
		else if(e.getActionCommand().equals("stateComboBox")) {
			this.newFormFields.set(2,(String)((JComboBox<String>)e.getSource()).getSelectedItem());
		}
		else if(e.getActionCommand().equals("formSubmit")) {
			if(checkNewFormFields()) {
				this.userFirstName = this.newFormFields.get(0);
				this.userLastName = this.newFormFields.get(1);
				this.state = this.newFormFields.get(2);
				this.year = this.newFormFields.get(3);
				newForm();
				for(int i = 0; i < 4; i++)this.newFormFields.set(i, "");
			}
			else {
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}
	
	private void fileChooser() {
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Door Dash User Files","user");
		jfc.setFileFilter(filter);
		if(jfc.showOpenDialog(this.mainFrame) == JFileChooser.APPROVE_OPTION) {
			load(jfc.getSelectedFile().getAbsolutePath());
		}
	}
	
	private void newForm() {
		this.mainFrame.setTitle("DoorDash Data Tracker v"+Main.versionId+ " -"+this.userFirstName + " "+this.userLastName + " "+this.year);
		if(!this.fdPane.isVisible())this.fdPane.setVisible(true);
		this.shifts = new ArrayList<Shift>();
		this.earning = 0.00;
		this.hoursWorked = 0;
		this.minutesWorked = 0;
		this.grossHourlyWage = 0.00;
		this.netHourlyWage = 0.00;
		this.expectedSSTaxes = 0.00;
		this.expectedIncomeTax = 0.00;
		this.totalMileage=0;
		updateEarnings(this.earning,1);
		updateMileage(0,0,1);
		updateTime(0,1);
		updateTaxesAndWages();
		updateDataTable();
	}
	
	private boolean checkNewFormFields() {
		for(String s: this.newFormFields) {
			if(s.equals("") || s.isEmpty())return false;
		}
		return true;
	}
	
	private void createNewForm() {
		this.mainFrame.setVisible(false);
		JFrame newFormFrame = new JFrame("DoorDash Data Tracker v"+Main.versionId);
		
		JPanel formMainPanel = new JPanel();
		JPanel statePanel = new JPanel();
		JPanel yearPanel = new JPanel();
		JPanel firstNamePanel = new JPanel();
		JPanel lastNamePanel = new JPanel();
		
		//First Name Panel Config
		JLabel firstNameLabel = new JLabel("First Name:");
		JTextField firstNameTextField = new JTextField(10);
		firstNamePanel.add(firstNameLabel);
		firstNamePanel.add(firstNameTextField);
		firstNameTextField.setName("firstNameTextField");
		firstNameTextField.addFocusListener(this);
		
		//Last Name Panel Config
		JLabel lastNameLabel = new JLabel("Last Name:");
		JTextField lastNameTextField = new JTextField(10);
		lastNamePanel.add(lastNameLabel);
		lastNamePanel.add(lastNameTextField);
		lastNameTextField.setName("lastNameTextField");
		lastNameTextField.addFocusListener(this);
		
		//State Panel Config
		String[] states = {"","OR","WA"};
		JLabel stateLabel = new JLabel("State");
		JComboBox<String>stateComboBox = new JComboBox<String>(states);
		stateComboBox.setSelectedIndex(0);
		stateComboBox.setActionCommand("stateComboBox");
		stateComboBox.addActionListener(this);
		statePanel.add(stateLabel);
		statePanel.add(stateComboBox);
		
		//Year Panel Config
		JLabel yearLabel = new JLabel("Year:");
		JTextField yearTextField = new JTextField(5);
		yearTextField.setName("yearTextField");
		yearTextField.addFocusListener(this);
		yearPanel.add(yearLabel);
		yearPanel.add(yearTextField);
		
		//Form Main Panel Config
		JButton formSubmit = new JButton("Submit");
		formSubmit.setActionCommand("formSubmit");
		formSubmit.addActionListener(this);
		formSubmit.addActionListener(new CustomActionListener(newFormFrame,this.newFormFields));
		//formSubmit.setEnabled(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		formMainPanel.setLayout(new GridBagLayout());
		formMainPanel.add(firstNamePanel,gbc);
		gbc.gridx = 1;
		formMainPanel.add(lastNamePanel,gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		formMainPanel.add(statePanel,gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		formMainPanel.add(yearPanel,gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		formMainPanel.add(formSubmit,gbc);
		
		//New Form Frame Config
		newFormFrame.add(formMainPanel);
		ImageIcon img = new ImageIcon("doordash.png");
		newFormFrame.setIconImage(img.getImage());
		newFormFrame.setSize(new Dimension(500,250));
		newFormFrame.setResizable(false);
		newFormFrame.setLocationRelativeTo(this.mainFrame);
		newFormFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		newFormFrame.addWindowListener(new CustomWindowListener(this.mainFrame,newFormFrame));
		newFormFrame.pack();
		newFormFrame.setVisible(true);
		
	}
	
	private void load(final String fileName) {
		if(this.shifts.size() > 0) {
			this.shifts = new ArrayList<Shift>();
			this.earning = 0.00;
			this.hoursWorked = 0;
			this.minutesWorked = 0;
			this.grossHourlyWage = 0.00;
			this.netHourlyWage = 0.00;
			this.expectedSSTaxes = 0.00;
			this.expectedIncomeTax = 0.00;
			this.totalMileage=0;
			updateEarnings(this.earning,1);
			updateMileage(0,0,1);
			updateTime(0,1);
			updateTaxesAndWages();
			updateDataTable();
		}
		try {
			Scanner fin;
			if(fileName.length() == 0) {
				Scanner restore = new Scanner(new File(RESTORE_FILE));
				fin = new Scanner(new File(restore.nextLine()));
				restore.close();
			}
			else {
				fin = new Scanner(new File(fileName));
			}
			String[]data = new String[6];
			int index = 0, count = 1;
			while(fin.hasNextLine()) {
				if(count < 5) {
					switch(count) {
					case 1:
						this.userFirstName = fin.nextLine();
						break;
					case 2:
						this.userLastName = fin.nextLine();
						break;
					case 3:
						this.state = fin.nextLine();
						break;
					case 4:
						this.year = fin.nextLine();
						break;
					}
					count++;
				}
				else if(index == 6) {
					//final String date, final String startTime, final String endTime, final double earnings, final int startMileage, final int endMileage
					shifts.add(new Shift(data[0],data[1],data[2], Double.parseDouble(data[3]),Integer.parseInt(data[4]),Integer.parseInt(data[5])));
					update(shifts.get(shifts.size()-1),1);
					index = 0;
				}
				else {
					data[index]=fin.nextLine();
					index++;
				}
				
				
			}
			shifts.add(new Shift(data[0],data[1],data[2], Double.parseDouble(data[3]),Integer.parseInt(data[4]),Integer.parseInt(data[5])));
			update(shifts.get(shifts.size()-1),1);
			fin.close();
			this.mainFrame.setTitle("DoorDash Data Tracker v"+Main.versionId+ " -"+this.userFirstName + " "+this.userLastName + " "+this.year);
			this.fdPane.setVisible(true);
			updateRestoreFile();
		}
		catch(Exception e) {
		}
	}
	private void updateRestoreFile() {
		try {
			PrintWriter fout = new PrintWriter(RESTORE_FILE); 
			fout.println(this.userFirstName+this.userLastName+this.year+".user");
			fout.close();
		}
		catch(Exception e) {
			
		}
	}
	private void save() {
		try {
			PrintWriter fout = new PrintWriter(this.userFirstName+this.userLastName+this.year+".user");
			fout.println(this.userFirstName);
			fout.println(this.userLastName);
			fout.println(this.state);
			fout.println(this.year);
			for(Shift s: this.shifts) {
				fout.println(s.getDate());
				fout.println(s.getStartTime());
				fout.println(s.getEndTime());
				fout.println(s.getEarnings());
				fout.println(s.getStartMileage());
				fout.println(s.getEndMileage());
			}
			fout.close();
			updateRestoreFile();
		}
		catch(Exception e) {
			
		}
	}
	public static void main(String[] args) {
		new Main();
	}
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() instanceof JTextField) {
			if(((JTextField)e.getSource()).getName().equals("yearTextField")) {
				String date = ((JTextField)e.getSource()).getText();
				if(date.matches("[0-9][0-9][0-9][0-9]")) {
					this.newFormFields.set(3, ((JTextField)e.getSource()).getText());
					((JTextField)e.getSource()).setForeground(Color.BLACK);
				}
				else {
					((JTextField)e.getSource()).setForeground(Color.RED);
				}
				
			}
			else if(((JTextField)e.getSource()).getName().equals("firstNameTextField")) {
				this.newFormFields.set(0, ((JTextField)e.getSource()).getText());
			}
			else if(((JTextField)e.getSource()).getName().equals("lastNameTextField")) {
				this.newFormFields.set(1, ((JTextField)e.getSource()).getText());
			}
		}
	}
}
