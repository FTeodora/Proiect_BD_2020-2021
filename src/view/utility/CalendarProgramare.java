package view.utility;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Month;

import javax.swing.*;

//1+(9+selectedIndex-1)%12
/**
 * O clasa pentru un calendar fie pentru programarea de activitati, fie pentru vizualizarea orarului
 * @see javax.swing.JPanel
 * Calendarul retine urmatoarele lucruri
 *  <ul>
 * <li> JPanel all,content,footer  zone in care se retin componentele swing din calendar
 * <li> JLabel selectedDate arata data selectata curent formatata ca data pentru MySql
 * <li> Color inMonth,uinMonth,outMonth,uoutMonth  culorile pentru zilele care se afla in luna selectata si in afara ei; cele cu u in fata sunt pentru zile de dinainte de rootDate (butoane dezactivate)
 * <li> Color markedColor
 * <li> String[] options  lunile pentru JComboBox-ul cu selectia lunii
 * <li> JComboBox<String> months JComboBox-ul pentru lunile ce pot fi selectate
 * <li> int d,m,y day(zi),month(luna),year(an) in care se salveaza data selectata
 * <li> UnitLabel time setarea timpului pentru data (daca se doreste)
 * <li> JPanel[] desiredMonth zonele in care se pastreaza butoanele de zile separate pe luni
 * <li> LocalDate rootDate o data inaintea careia nu poti selecta zile
 * <li> LocalDate markedDate o data marcata care nu limiteaza cu nimic selectia de zile
 * </ul>
 * <p>
 * */
public class CalendarProgramare extends JPanel{
	private JPanel all,content,footer; 
	
	
	private JLabel selectedDate; 
	
	private Color inMonth=AppWindow.mainColor,uMonth=AppWindow.lightestBlue;
	private Color outMonth=AppWindow.lightBlue;
	private Color markedColor=AppWindow.headerColor;
	private String[] options= {"Octombire","Noiembrie","Decembrie","Ianuarie","Februarie","Martie","Aprilie","Mai","Iunie","Iulie","August","Septembrie"};

	JComboBox<String> months;
	
	private int d,m,y; 
	private UnitLabel time;
	
	private JPanel[] desiredMonth; 

	private LocalDate rootDate;
	private LocalDate markedDate;
	
	/**
	 * Constructor pentru calendar in care se marcheaza o data diferita de cea setata la root 
	 * si in care se poate doar selecta o data anume
	 * @param date 		data rootata inaintea careia nu se pot selecta zile
	 * @param marked 	o data marcata
	 */
	public CalendarProgramare(LocalDate date,LocalDate marked) {
		y=marked.getYear();
		m=marked.getMonthValue();
		d=marked.getDayOfMonth();
		rootDate=null;
		markedDate=marked;
		initAll(false);
		
		
	}
	/**
	 * Constructorul pentru Calendar cu o data rootata=data marcata, in care se pot edita ore si minute
	 * @param date rootedDate-ul inainte careia nu se mai poate selecta o zi de pe calendar
	 */
	public CalendarProgramare(LocalDate date){
		
		this.markedDate=date;
		this.rootDate=date;
		this.d=rootDate.getDayOfMonth();
		this.m=rootDate.getMonthValue();
		this.y=rootDate.getYear();
		initAll(true);
	}
	/**
	 * Adauga inca un ActionListener la butoanele de zi care se va executa inainte de schimbarea datei
	 * @param e ActionListener-ul de adaugat
	 */
	public void additionalListener(ActionListener e) {
		for(int i=0;i<12;i++)
		{
			Component[] aux=desiredMonth[i].getComponents();
			for(Component j:aux)
				if(j instanceof JButton)
				{
					ActionListener first=((JButton) j).getActionListeners()[0];
					((JButton) j).removeActionListener(first);
					((JButton) j).addActionListener(e);
					((JButton) j).addActionListener(first);
				}
		}
	}
	/**
	 * Initializeaza componentele in constructor ca sa nu se rescrie blocul mare de cod
	 * @param hasHours daca sa se adauge camp de ore editabil
	 */
	private void initAll(boolean hasHours) {
		content=new JPanel();
		int startYear=y;
		if(m<10)
			startYear--;
		content.setLayout(new BorderLayout());	
		
		all=new JPanel(new CardLayout());
		
		desiredMonth=new JPanel[12];
		for(int i=0;i<12;i++) {
			
			if(i<3)
			{
				desiredMonth[i]=buildDays(1+(9+i)%12,startYear);
				options[i]=options[i]+" "+Integer.toString(startYear);
			}
			else
				{
				desiredMonth[i]=buildDays(1+(9+i)%12,startYear+1);
				options[i]=options[i]+" "+Integer.toString(startYear+1);
				}
			all.add(desiredMonth[i],options[i]);
		}
		
		int beginning=0;
		if(m>=10)
			beginning=m-10;
		else beginning=2+m;
		((CardLayout)(all.getLayout())).show(all,options[beginning]);
		months=new JComboBox<String>(options);
		months.setSelectedIndex(beginning);	
		months.setPreferredSize(new Dimension(400,40));
		months.setFont(AppWindow.MAIN_FONT);
		months.setForeground(AppWindow.darkestBlue);
		
		CalendarProgramare t=this;
		months.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				t.setM(1+(9+months.getSelectedIndex())%12);
				CardLayout cl = (CardLayout)(all.getLayout());
				cl.show(all,options[months.getSelectedIndex()]);
			}
			
		});
		
		footer=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		footer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		selectedDate=new JLabel(this.computeDate());
		selectedDate.setFont(AppWindow.MAIN_FONT);
		selectedDate.setForeground(AppWindow.darkestBlue);
		
		footer.add(selectedDate);
		if(hasHours) {
			time=new UnitLabel(8,23,0);
			time.setForeground(AppWindow.darkestBlue);
			time.setFont(AppWindow.MAIN_FONT);
			footer.add(time);
			}
		content.add(months,BorderLayout.NORTH);
		content.add(all,BorderLayout.CENTER);
		content.add(footer,BorderLayout.SOUTH);
		content.validate();
		content.repaint();
		this.add(content);
	}
	/**
	 * Formateaza orice data la format MySql
	 * @param y anul datei
	 * @param m luna datei
	 * @param d ziua datei
	 * @return un String cu data formatata
	 */
	public static String computeDate(int y,int m,int d) {
		String s=y+"-";
		if(m<10)
			s+="0";
		s+=m+"-";
		if(d<10)
			s+="0";
		s+=d;
		return s;
	}
	/**
	 * Formateaza data retinuta in caledar la format MySql
	 * @return un String cu data formatata
	 */
	public String computeDate() {
		String s=y+"-";
		if(m<10)
			s+="0";
		s+=m+"-";
		if(d<10)
			s+="0";
		s+=d;
		return s;
	}
	/**
	 * Constuieste butoanele de zile ale unei luni din calendar
	 * @param month luna selectata
	 * @param year anul selectat
	 * @return un JPanel cu butoanele construite
	 */
	private JPanel buildDays(int month,int year) {
		JPanel fin=new JPanel();
		fin.setLayout(new GridLayout(0,7));
		String[] days= {"Lu","Ma","Mi","Joi","Vi","Sa","Du"};
		JLabel[] day=new JLabel[7];
		for(int i=0;i<7;i++)
		{
			day[i]=new JLabel(days[i]);
			day[i].setFont(AppWindow.MAIN_FONT);
			day[i].setForeground(AppWindow.darkestBlue);
			day[i].setMaximumSize(new Dimension(60,60));
			day[i].setPreferredSize(new Dimension(30,30));
			fin.add(day[i]);
			
		}
		LocalDate d;
		d=LocalDate.parse(CalendarProgramare.computeDate(year, month, 1));
		
		JButton[] btn=new JButton[42];
		 int nr=0;
		while(!d.getDayOfWeek().toString().equals("MONDAY")) {
			d=d.minusDays(1);
		}
		
		while(d.getMonthValue()!=month) {
			
			btn[nr]=this.createDayButton( d,  outMonth);
			fin.add(btn[nr]);
			nr++;
			d=d.plusDays(1);
			
			
		}
		
		while(d.getMonthValue()==month)
		{
			btn[nr]=this.createDayButton( d,  inMonth);
			fin.add(btn[nr]);
			nr++;
			d=d.plusDays(1);
		}
		
		if(!d.getDayOfWeek().toString().equals("MONDAY")) {
			while(!d.getDayOfWeek().toString().equals("SUNDAY")) {
				btn[nr]=this.createDayButton( d,  outMonth);
				fin.add(btn[nr]);
				nr++;
				d=d.plusDays(1);
			}	
			btn[nr]=this.createDayButton( d,  outMonth);
				fin.add(btn[nr]);
		}
		fin.validate();
		fin.repaint();
		return fin;
	}
	/**
	 * Construieste un buton de zi, adaugandu-i si ActionListener si colorandu-l corespunzator in functie de situatie
	 * @param date data butonului din care se extrage ziua din luna
	 * @param c1 culoarea care se aplica daca e inaintea lui rootDate
	 * @param c2 culoarea care se aplica daca e dupa rootDate
	 * @return butonul obtinut
	 */
	private JButton createDayButton(LocalDate date,Color c1) {
		JButton btn=new JButton(String.valueOf(date.getDayOfMonth()));
		btn.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		btn.setFont(AppWindow.MAIN_FONT);
		btn.setForeground(Color.WHITE);
		UIManager.getDefaults().put("Button.disabledText",Color.WHITE);
		if(this.getRootDate()!=null&&date.isBefore(this.getRootDate())) {
			btn.setBackground(this.uMonth);
			btn.setEnabled(false);
			
		}
		else {
			btn.setBackground(c1);
		}
		if(date.isEqual(markedDate))
			btn.setBackground(markedColor);
		LocalDate g=date;
		CalendarProgramare t=this;
		JButton b=btn;
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int D=0;
				D=Integer.valueOf(b.getText());
				d=D;
				m=g.getMonthValue();
				y=g.getYear();
				t.setSelectedDate(t.computeDate());
			}
		});
		return btn;
	}
	/**
	 * Obtine data retinuta
	 * @return data retinuta in forma de String
	 */
	public String retrieveDate() {
		String rez=this.computeDate();
		if(this.time!=null)
			rez+=" "+time.retrieveData();
		System.out.println(rez);
		return rez;
	}
	public int getD() {
		return d;
	}
	public void setD(int d) {
		this.d = d;
	}
	public int getM() {
		return m;
	}
	public void setM(int m) {
		this.m = m;
	}
	@Override
	public int getY() {
		return y;
	}
	public JPanel getContent() {
		return content;
	}
	public String getSelectedDate() {
		return selectedDate.getText();
	}
	public void setSelectedDate(String selectedDate) {
		this.selectedDate.setText(selectedDate);
	}
	public void setY(int y) {
		this.y = y;
	}
	public LocalDate getRootDate() {
		return rootDate;
	}
	public void setRootDate(LocalDate rootDate) {
		this.rootDate = rootDate;
	}
	public void addDownloadButton(ActionListener e) {
		JButton download=new JButton("Descarca");
		download.setBackground(AppWindow.headerColor);
		download.setForeground(Color.WHITE);
		download.setFont(AppWindow.TEXT_FONT);
		download.addActionListener(e);
		footer.add(download);
		this.validate();
		this.repaint();
		}

}
