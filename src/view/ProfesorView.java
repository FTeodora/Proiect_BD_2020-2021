package view;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import view.utility.NavigationButton;

public class ProfesorView extends UserView{

	public ProfesorView(JFrame prev) {
		super(prev);
		this.options.add(new NavigationButton(new ImageIcon(StudentView.class.getResource("\\icons\\viewgrades.png")),"Gestionare ponderi"));
		for(NavigationButton o:options)
			this.optionsPane.add(o);
		this.validate();
		this.repaint();
	}
}
