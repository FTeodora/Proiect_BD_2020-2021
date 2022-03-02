package view.utility;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class NavigationButton extends JButton{
	private JFrame link;
	public NavigationButton(String name) {
		super(name);
		setBackground(AppWindow.headerColor);
        setForeground(Color.WHITE);
	}
	public NavigationButton(ImageIcon i,String l) {
			super(l,i);
		   setBorder(new EmptyBorder(20,20,20,20));
		   setPreferredSize(new Dimension(195,195));
		   setBackground(AppWindow.headerColor);
		   setHorizontalTextPosition(SwingConstants.CENTER);
		   setVerticalTextPosition(SwingConstants.BOTTOM);
		   setForeground(Color.WHITE);
		   setFont(AppWindow.BUTTON_FONT);
		   }
	public NavigationButton(ImageIcon i,Dimension size){
		super(i);
		setBorder(new EmptyBorder(10,10,10,10));
		setPreferredSize(size);
		setBackground(AppWindow.headerColor);
	}
	public JFrame getLink() {
		return link;
	}
	public void setLink(JFrame link) {
		this.link = link;
	}
}