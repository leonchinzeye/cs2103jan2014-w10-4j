package application;

import java.util.Stack;

public class Undo {

	private Stack<String> cmdTypeUndo;
	private Stack<String> cmdTypeRedo;

	public Undo() {
		initVariables();
	}

	private void initVariables() {
	  cmdTypeUndo = new Stack<String>();
	  cmdTypeRedo = new Stack<String>();
	  
  }
}
