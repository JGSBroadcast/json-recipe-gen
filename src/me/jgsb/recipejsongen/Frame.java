package me.jgsb.recipejsongen;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame{

	private static final long serialVersionUID = 1L;
	public static int WIDTH;
	public static int HEIGHT;

	public static void main(String[]args){
		new Frame();
	}

	public Frame(){
		this.setTitle("Recipe JSON Generator");
		this.setResizable(true);
		this.setUndecorated(false);
		this.setExtendedState(NORMAL);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMaximumSize(new Dimension (360, 360));
		this.setMinimumSize(new Dimension (360, 360));
		this.setPreferredSize(new Dimension (360, 360));
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		WIDTH = getWidth();
		HEIGHT = getHeight();
		this.add(new Screen(this));
	}

}
