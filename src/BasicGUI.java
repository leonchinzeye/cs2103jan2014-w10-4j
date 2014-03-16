
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class BasicGUI{	

	//JTextArea
	private static JTextArea consoleArea;
	private static JTextArea actionArea;
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
	private static JPanel actionPanel;

	public static void main(String args[]){

		//create the frame with which the program can work on 
		mainFrameSetUp();

		//contain what ever you want to add into the GUI
		storageContainer = mainFrame.getContentPane();
		storageContainer.setLayout(null);

		jpanelSetUp();

		//JTextArea
		consoleArea = new JTextArea();
		//JTextField
		commandField = new JTextField();

		//JLabel
		//JLabel mainLabel = new JLabel("Hello");

		//Storage container 
		storageContainer.setBounds(0,0, 410, 410);
		storageContainer.add(consolePanel);
		storageContainer.add(commandPanel);
		storageContainer.add(actionPanel);

		//scroll function 
		consoleScroll = new JScrollPane(consoleArea);

		//displayPanel
		consolePanel.setBounds(5,5,375, 235);
		
		//part of the display box 
		consolePanel.add(consoleScroll);
		consoleScroll.setBounds(8, 20, 360, 205);
		consoleArea.setEditable(false);
		consoleArea.setBackground(Color.white);
		
		//commandPanel
		commandPanel.setBounds(5, 290, 375, 60);
		commandPanel.add(commandField);
		commandField.setBounds(8, 20, 360, 30); 
		
		//actionPanel 
		actionPanel.setBounds(5, 240, 375, 50);
		actionArea = new JTextArea();
		actionArea.setEditable(false);
		actionPanel.add(actionArea);
		actionArea.setBackground(Color.lightGray);
		actionArea.setBounds(8, 20, 360, 20);
		
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

	private static void jpanelSetUp() {
		//JPanel for console
		consolePanel = new JPanel(null);
		consolePanel.setBorder(BorderFactory.createTitledBorder("Console"));

		//JPanel for command
		commandPanel = new JPanel(null);
		commandPanel.setBorder(BorderFactory.createTitledBorder("Command"));
		
		//JPanel for the action that has been done 
		actionPanel = new JPanel(null);
		actionPanel.setBorder(BorderFactory.createTitledBorder("Action"));
	}

	private static void mainFrameSetUp() {
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame = new JFrame();
		//mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setSize(390, 390);
		//mainFrame.setSize(screenSize);
		mainFrame.setVisible(true);
		mainFrame.setResizable(false);
		mainFrame.setTitle("TaskWorthy");
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setLocationRelativeTo(consoleArea);
	}

}