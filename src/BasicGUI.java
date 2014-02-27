//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JLabel;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class BasicGUI{	

	//JTextField
	private static JTextArea consoleField;
	private static JTextField commandField;
	
	//JScrollPane 
	private static JScrollPane consoleScroll;
	
	public static void main(String args[]){
		
		//create the frame with which the program can work on 
		JFrame mainFrame = new JFrame();
		mainFrame.setSize(400, 400);
		mainFrame.setVisible(true);
		mainFrame.setResizable(true);
		mainFrame.setTitle("TaskWorthy");
		
		//contain what ever you want to add into the GUI
		Container storageContainer = mainFrame.getContentPane();
		storageContainer.setLayout(null);
		
		//JPanel for display
		JPanel consolePanel = new JPanel(null);
		consolePanel.setBorder(BorderFactory.createTitledBorder("Console"));
		
		//JPanel for command
		JPanel commandPanel = new JPanel(null);
		commandPanel.setBorder(BorderFactory.createTitledBorder("Command"));
		
		//JTextArea
		consoleField = new JTextArea();
		//JTextField
		commandField = new JTextField();
		
		//JLabel
		//JLabel mainLabel = new JLabel("Hello");
		
		//Storage container 
		storageContainer.setBounds(0,0, 400, 400);
		storageContainer.add(consolePanel);
		storageContainer.add(commandPanel);
		
		//scroll function 
		consoleScroll = new JScrollPane(consoleField);
		
		//displayPanel
		consolePanel.setBounds(5,5,375, 275);
		//part of the display box 
		consolePanel.add(consoleScroll);
		consoleScroll.setBounds(8, 20, 360, 245);
		consoleField.setEditable(false);
		consoleField.setBackground(Color.white);
		//commandPanel
		commandPanel.setBounds(5, 280, 375, 70);
		commandPanel.add(commandField);
		commandField.setBounds(8, 20, 360, 40); 
		
		
		
		//input/output field
		//input into the command field can be displayed on Console
		commandField.addActionListener(new ActionListener() {
			@Override
			
			public void actionPerformed(ActionEvent arg0) {
				if(consoleField.getText().isEmpty()){
					consoleField.setText(commandField.getText());
					commandField.setText("");
				}
				else{
				consoleField.setText(consoleField.getText() + "\n" +commandField.getText());
				commandField.setText("");
				}
			}
		});
		
		
		/*
		part of the command box
		mainPanel.add(commadField);
		commadField.setBounds(180, 118, 360, 180);
		
		displayPanel.add(mainLabel);
		mainLabel.setBounds(5, 20, 50, 20);		
		 */
	}
	
}