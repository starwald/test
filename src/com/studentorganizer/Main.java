package com.studentorganizer;

import com.studentorganizer.gui.LoginFrame;
import javax.swing.*;

public class Main {
	public static void main(String[] args) {
	    try {
	        UIManager.setLookAndFeel(UIManager.getLookAndFeel());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    
	    SwingUtilities.invokeLater(() -> {
	        new LoginFrame().setVisible(true);
	    });
	}
}