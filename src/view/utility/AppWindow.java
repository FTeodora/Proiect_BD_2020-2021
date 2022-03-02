package view.utility;
import javax.swing.*;
import javax.swing.border.LineBorder;

import model.entity.Curs;

import java.awt.*;
import java.util.ArrayList;
public class AppWindow extends JFrame {
public static final String[] ani= {" ","1","2","3","4","5","6"};
public static final String[] departamente= {" ","Matematica","Limbi straine","Calculatoare"};
public static final Color darkestBlue=new Color(27,74,102);
public static final Color mainColor=new Color(70,130,180);
public static final Color headerColor=new Color(49,100,135);
public static final Color mediumBlue=new Color(95, 188, 217);
public static final Color lightBlue=new Color(134,190,222); //119, 162, 212
public static final Color lightestBlue=new Color(188,224,223);

public static final Font SMALLER_FONT=new Font("Rockwell",Font.PLAIN,14);
public static final Font MAIN_FONT=new Font("Rockwell",Font.PLAIN,16);
public static final Font HIGHLIGHTED_INFO=new Font("Rockwell",Font.PLAIN,20);
public static final Font TEXT_FONT=new Font("Verdana",Font.PLAIN,16);
public static final Font HEADER_FONT=new Font("Poor Richard",Font.ITALIC,20);
public static final Font BUTTON_FONT=new Font("Tahoma",Font.PLAIN,14);
public AppWindow() {
	super("Platforma studiu");
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setMinimumSize(new Dimension(300,300));
}
public static void formatButton(JButton buton,Dimension d) {
	buton.setBackground(AppWindow.mainColor);
	buton.setForeground(Color.WHITE);
	buton.setFont(AppWindow.BUTTON_FONT);
	buton.setBorder(new LineBorder(Color.WHITE));
	buton.setPreferredSize(d);
	
}
public static void main(String[] args)
{
	AppWindow test=new AppWindow();
	JPanel colors=new JPanel(new GridLayout(1,0));
	colors.setPreferredSize(new Dimension(100,30));
	
	JPanel dark=new JPanel();
	dark.setBackground(AppWindow.darkestBlue);
	colors.add(dark);
	
	JPanel header=new JPanel();
	header.setBackground(AppWindow.headerColor);
	colors.add(header);
	
	JPanel main=new JPanel();
	main.setBackground(AppWindow.mainColor);
	colors.add(main);
	
	JPanel medium=new JPanel();
	medium.setBackground(AppWindow.mediumBlue);
	colors.add(medium);
	
	JPanel light=new JPanel();
	light.setBackground(AppWindow.lightBlue);
	colors.add(light);
	
	JPanel lightest=new JPanel();
	lightest.setBackground(AppWindow.lightestBlue);
	colors.add(lightest);
	
	ScrollablePanel fontList=new ScrollablePanel();
	test.add(fontList);
	(new Field("Culori: ",colors)).add(fontList);
  String fonts[] = 
    GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

  for ( int i = 0; i < fonts.length; i++ )
  {
	  JLabel font=new JLabel(fonts[i]);
	  Field field=new Field(font);
	  field.add(fontList);
	  font.setFont(new Font(fonts[i],Font.PLAIN,14));
  }
  test.setVisible(true);
}
}
