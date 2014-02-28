//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JLabel;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class BasicGUI{	

	//JTextArea
	private static JTextArea consoleArea;
	//JTextField
	private static JTextField commandField;
	//JFrame 
	private static JFrame mainFrame;
	//JScrollPane 
	private static JScrollPane consoleScroll;
	//Container 
	private static Container storageContainer;
	//JPanel 
	private static JPanel consolePanel;
	private static JPanel commandPanel;

	public static void main(String args[]){

		//create the frame with which the program can work on 
		mainFrame = new JFrame();
		mainFrame.setSize(400, 400);
		mainFrame.setVisible(true);
		mainFrame.setResizable(true);
		mainFrame.setTitle("TaskWorthy");
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//contain what ever you want to add into the GUI
		storageContainer = mainFrame.getContentPane();
		storageContainer.setLayout(null);

		//JPanel for display
		consolePanel = new JPanel(null);
		consolePanel.setBorder(BorderFactory.createTitledBorder("Console"));

		//JPanel for command
		commandPanel = new JPanel(null);
		commandPanel.setBorder(BorderFactory.createTitledBorder("Command"));

		//JTextArea
		consoleArea = new JTextArea();
		//JTextField
		commandField = new JTextField();

		//JLabel
		//JLabel mainLabel = new JLabel("Hello");

		//Storage container 
		storageContainer.setBounds(0,0, 400, 400);
		storageContainer.add(consolePanel);
		storageContainer.add(commandPanel);

		//scroll function 
		consoleScroll = new JScrollPane(consoleArea);

		//displayPanel
		consolePanel.setBounds(5,5,375, 275);
		//part of the display box 
		consolePanel.add(consoleScroll);
		consoleScroll.setBounds(8, 20, 360, 245);
		consoleArea.setEditable(false);
		consoleArea.setBackground(Color.white);
		//commandPanel
		commandPanel.setBounds(5, 280, 375, 70);
		commandPanel.add(commandField);
		commandField.setBounds(8, 20, 360, 40); 



		//input/output field
		//input into the command field can be displayed on Console

		commandField.addActionListener(new ActionListener() {
			@Override

			public void actionPerformed(ActionEvent arg0) {
				if(consoleArea.getText().isEmpty()){
					String[] message = commandField.getText().split(" ", 2);
					consoleArea.setText("added " + message[1]);
					commandField.setText("");
				}
				else{
					String[] message = commandField.getText().split(" ", 2);
					consoleArea.setText(consoleArea.getText() + "\nadded " +message[1]);
					commandField.setText("");
				}
			}
		});

	}

}