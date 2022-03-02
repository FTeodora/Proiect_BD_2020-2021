package view;
import javax.swing.*;

import view.utility.NavigationButton;

import java.awt.*;
import java.awt.event.ActionListener;

public class StudentView extends UserView{
	public StudentView(JFrame prev){
		super(prev);
		this.options.add(new NavigationButton(new ImageIcon(StudentView.class.getResource("\\icons\\viewgrades.png")),"Note"));
		this.options.add(new NavigationButton(new ImageIcon(StudentView.class.getResource("\\icons\\viewgroups.png")),"Grupuri")); //Grupuri
		
		for(NavigationButton o:options)
			this.optionsPane.add(o);
		this.validate();
		this.repaint();
	}
}
