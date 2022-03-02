package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import view.utility.NavigationButton;

public class AccountsView extends OptionsView{
	private NavigationButton newAcc,modifyAcc;
	 public AccountsView(){
		 super("Gestionare conturi");
		 newAcc=new NavigationButton(new ImageIcon(AccountsView.class.getResource("\\icons\\newaccount.png")),"Creare");
		 modifyAcc=new NavigationButton(new ImageIcon(AccountsView.class.getResource("\\icons\\modifyaccount.png")),"Modificare/Stergere");
		 JPanel content=new JPanel();
		 content.setBorder(new EmptyBorder(20,20,20,20));
		 content.add(newAcc);
		 content.add(modifyAcc);
		 this.add(content,BorderLayout.CENTER);
	 }
public void addNewAccListener(ActionListener e) {
		newAcc.addActionListener(e);
}
public void addModifyAccListener(ActionListener e) {
	modifyAcc.addActionListener(e);
}
}
