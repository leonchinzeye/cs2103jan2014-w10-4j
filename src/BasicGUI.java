//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JLabel;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class BasicGUI{	

	//JTextField
	private static JTextArea displayField;
	private static JTextField commandField;
	
	//JScrollPane 
	//private static JScrollPane displayScroll;
	
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
		JPanel displayPanel = new JPanel(null);
		displayPanel.setBorder(BorderFactory.createTitledBorder("Display"));
		
		//JPanel for command
		JPanel commandPanel = new JPanel(null);
		commandPanel.setBorder(BorderFactory.createTitledBorder("Command"));
		
		//JTextField
		displayField = new JTextArea();
		commandField = new JTextField();
		
		//JLabel
		//JLabel mainLabel = new JLabel("Hello");
		
		//Storage container 
		storageContainer.setBounds(0,0, 400, 400);
		storageContainer.add(displayPanel);
		storageContainer.add(commandPanel);
		
		//displayPanel
		displayPanel.setBounds(5,5,375, 275);
		//part of the display box 
		displayPanel.add(displayField);
		displayField.setBounds(8, 20, 360, 245);
		displayField.setEditable(false);
		displayField.setBackground(Color.white);
		//commandPanel
		commandPanel.setBounds(5, 280, 375, 70);
		commandPanel.add(commandField);
		commandField.setBounds(8, 20, 360, 40); 
		
		
		
		//input/output field
		
		//displayScroll  = new JScrollPane(displayField);
		
		commandField.addActionListener(new ActionListener() {
			@Override
			
			public void actionPerformed(ActionEvent arg0) {
				if(displayField.getText().isEmpty()){
					displayField.setText(commandField.getText());
					commandField.setText("");
				}
				else{
				displayField.setText(displayField.getText() + "\n" +commandField.getText());
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