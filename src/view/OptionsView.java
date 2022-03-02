package view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import view.utility.AppWindow;
import view.utility.NavigationButton;

import java.awt.*;
import java.awt.event.ActionListener;
public class OptionsView extends JFrame{
	private final JPanel header;
	private final NavigationButton back,refresh;
	private final JLabel optionName;
	
	public OptionsView(String optionName) {
		super("Platforma de studiu");
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(475,420));
		
		this.optionName=new JLabel("<html>"+optionName.replaceAll("\n", "<br>")+"</html>");
		this.optionName.setForeground(Color.WHITE);
		this.optionName.setFont(AppWindow.HEADER_FONT);
		
		this.back=new NavigationButton(new ImageIcon(OptionsView.class.getResource("\\icons\\back.png")),new Dimension(50,50));
		this.refresh=new NavigationButton(new ImageIcon(OptionsView.class.getResource("\\icons\\refresh.png")),new Dimension(35,35));
		
		this.header=new JPanel(new FlowLayout(FlowLayout.LEFT));
		header.add(this.back);
		header.add(this.refresh);
		header.add(this.optionName);
		header.setBackground(AppWindow.headerColor);
		this.add(header,BorderLayout.NORTH);
	}
	public static void main(String[] args) {
		OptionsView p=new OptionsView("Test");
		p.setSize(new Dimension(400,400));
		p.setVisible(true);

	}
	public void setBackLink(JFrame parent) {
		back.setLink(parent);
	}
	public JFrame getBackLink() {
		return this.back.getLink();
	}
	public void addBackButtonListener(ActionListener e) {
    	this.back.addActionListener(e);
    }
	public void addRefreshButtonListener(ActionListener e) {
    	this.refresh.addActionListener(e);
    }
	public void refresh() {
		
	}
}
