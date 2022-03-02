package view;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import view.utility.NavigationButton;

public class AdminView extends UserView {
	public AdminView(JFrame prev) {
		super(prev);
		this.options.add(new NavigationButton(new ImageIcon(StudentView.class.getResource("\\icons\\viewgroups.png")),"Grupuri"));
		this.options.add(new NavigationButton(new ImageIcon(StudentView.class.getResource("\\icons\\viewaccounts.png")),"Gesionare conturi"));
		for(NavigationButton o:options)
			this.optionsPane.add(o);
		this.validate();
		this.repaint();
	}
}
