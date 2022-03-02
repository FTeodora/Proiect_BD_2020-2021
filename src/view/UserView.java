package view;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import service.ContextHolder;
import view.utility.AppWindow;
import view.utility.NavigationButton;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Partea de vedere pentru ce apare dupa ce un utilizator se logheaza indiferent de rolul sau
 * Aceasta vedere cuprinde
 * <ul>
 * <li> Un antet cu rolul si numele persoanei logate, alaturi de butonul de delogare
 * <li> Butoanele de actiunile pe care le poate face
 * </ul>
 * Orice user poate, pe langa delogare, sa
 * <ul>
 * <li> 1.Isi vada datele personale
 * <li> 2.Vada anumite notificari
 * <li> 3.Vada anumite activitati
 * <li> 4.Vada anumite cursuri
 * </ul>
 * 	<p>
 *	Butoanele de optiuni sunt retinute intr-o lista, deoarece acestea pot varia in functie de rol
 */
public class UserView extends JFrame{
	protected NavigationButton logOut;
	protected JPanel optionsPane;
	protected ArrayList<NavigationButton> options;
	
	protected JLabel welcomeMessage;
	
	/**
	 * Constructorul care initializeaza si componentele de pe fereastra
	 * De asemenea, el adauga optiunile de vizualizare de date personale si vizualizare de activitati,
	 * comune pentru toate tipurile de utilizator
	 * @param prev fereastra anterioara din care s-a ajuns la acesta fereastra pentru setarea pozitiei relativ la aceasta
	 */
	public UserView(JFrame prev) {
		super("Platforma studiu");
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(prev);
		this.setMinimumSize(new Dimension(775,720));
		
		JPanel header=new JPanel(new BorderLayout());
		header.setBackground(AppWindow.headerColor);
		header.setMinimumSize(new Dimension(300,400));
		header.setBorder(new EmptyBorder(20,20,20,20));
		
		welcomeMessage=new JLabel();
		welcomeMessage.setForeground(Color.WHITE);
		welcomeMessage.setFont(AppWindow.HEADER_FONT);
		logOut=new NavigationButton(new ImageIcon(UserView.class.getResource("\\icons\\logout.png")),new Dimension(50,50));
		logOut.setLink(prev);
		logOut.setBackground(AppWindow.headerColor);
		
		header.add(new JLabel(new ImageIcon(UserView.class.getResource("\\icons\\logo.png"))),BorderLayout.WEST);
		header.add(welcomeMessage,BorderLayout.CENTER);
		header.add(logOut,BorderLayout.EAST);
		
		optionsPane=new JPanel(new FlowLayout());
		optionsPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		options=new ArrayList<NavigationButton>();
		
		NavigationButton personalInfo=new NavigationButton(new ImageIcon(UserView.class.getResource("\\icons\\viewpersonaldata.png")),"Date personale");
		options.add(personalInfo);
		optionsPane.add(personalInfo);
		
		NavigationButton notifications=new NavigationButton(new ImageIcon(StudentView.class.getResource("\\icons\\notification.png")),"Notificari");
		options.add(notifications);
		optionsPane.add(notifications);
		
		NavigationButton timetable=new NavigationButton(new ImageIcon(UserView.class.getResource("\\icons\\viewtimetable.png")),"Orar");
		options.add(timetable);
		optionsPane.add(timetable);
		
		NavigationButton courses=new NavigationButton(new ImageIcon(StudentView.class.getResource("\\icons\\viewcourses.png")),"Cursuri");
		options.add(courses);
		optionsPane.add(courses);
		
		this.add(header, BorderLayout.NORTH);
		this.add(optionsPane,BorderLayout.CENTER);
		this.setVisible(true);
		
	}
	/**
	 * 
	 * @return butoanele de optiune adaugate in vedere
	 */
	public ArrayList<NavigationButton> getOptions() {
		return options;
	}
	/**
	 * 
	 * @return legatura la fereastra de logare
	 */
	public JFrame getLogOutLink() {
		return this.logOut.getLink();
	}
	/**
	 * Obtine legatura din butonul de optiune cu numarul n
	 * @param n	  al catelea buton de optiune se seteaza(incepand de la 0)
	 * @return		legatura butonului ales
	 */
	public JFrame getOptionLink(int n) {
		return options.get(n).getLink();
	}
	/**
	 * Seteaza legatura la fereastra urmatoare pentru un buton de optiune
	 * @param n al catelea buton de optiune se seteaza(incepand de la 0)
	 * @param next	fereastra la care se seteaza legatura
	 */
	public void setOptionLink(int n,JFrame next) {
		this.options.get(n).setLink(next);
	}
	/**
	 * Seteaza mesajul de bun venit care va contine username-ul si rangul persoanei logate
	 * @param session	informatiile despre persoana conectata
	 */
	public void setSessionMessage(ContextHolder session) {
		this.welcomeMessage.setText("Bine ai venit, "+session.getSessionType().toString().toLowerCase()+" "+session.getPersonName()+"!");
	}
	/**
	 * Adauga actiunea pentru butonul de delogare (revenirea la pagina de logare)
	 * @param listener
	 */
	public void addLogOutButtonListener(ActionListener listener){
        this.logOut.addActionListener(listener);
    }
	/**
	 * Adauga o actiune pentru un buton de optiune
	 * @param n    numarul butonului(indexare de la 0)
	 * @param listener  actiunea de executat la apasares
	 */
    public void addOptionButtonListener(int n,ActionListener listener){
        this.options.get(n).addActionListener(listener);
    }

}