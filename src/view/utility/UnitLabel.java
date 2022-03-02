package view.utility;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

/**
 * O clasa de unul sau doua JSpinnere unite cu o unitate de masura
 * @see javax.swing.JPanel
 * Deoarece nu se afla multe componente, JSpinnerele nu sunt retinute in parametri,
 * valorile lor fiind returnate direct cu numarul componentei adaugate
 * <ul>
 * <li>unit - retine unitatea de masura dorita, in functie de care se vor obtine valorile din JSpinnere
 * </ul>
 * <p>
 * Unitatile de masura retinute pot fi:
 * <ul>
 * <li> minute - pentru masurarea timpului. Singurul UnitLabel cu doua JSpinnere
 * <li> participanti - pentru numar minim sau maxim de participanti la o activitate
 * <li> % - procente(ponderi)
 * <li> nicio unitate - pentru note
 * <li>unit - retine unitatea de masura dorita, in functie de care se vor obtine valorile din JSpinnere
 * </ul>
 * Unitatile de masura sunt determinate in functie de constructor
 * 
 */
public class UnitLabel extends JPanel{
	private String unit;
	
	/**
	 * Constructorul pentru setarea timpului
	 * @param hours   numarul de ore maxime care se pot seta
	 * @param minutes   numarul de minute maxime care se pot seta. Mai mult pentru diferentiera de constructorul de persoane
	 * Orele si minutele incep de la 0
	 * */
	public UnitLabel(int hours,int minutes) {
			this.add(new JSpinner(new SpinnerNumberModel(0, 0, hours, 1)));
			this.add(new JLabel("ore"));
			this.add(new JSpinner(new SpinnerNumberModel(0, 0, minutes, 1)));
			unit="minute";
			this.add(new JLabel(unit));
	}
	/**
	 * Constructorul pentru setarea timpului intr-un calendar de programari. 
	 * Parametri in plus sunt doar pentru distinctia de alti constructori
	 * @param initialHour	ora initiala (8)
	 * @param maximumHour	ora maxima pana la care poti programa (23)
	 * @param minimumHour	ora minima pana la care poti programa (0)
	 */
	public UnitLabel(int initialHour,int maximumHour,int minimumHour) {
			this.add(new JSpinner(new SpinnerNumberModel(initialHour, minimumHour, maximumHour, 1)));
			this.add(new JLabel(":"));
			this.add(new JSpinner(new SpinnerNumberModel(0, 0, 60, 1)));
			unit="";
			this.add(new JLabel(unit));
	}
	/**
	* Constructorul pentru setarea numarului de persoane
	* @param nr   numarul de persoane care se pot seta
	* Numarul incepe de la 1
	* */
	public UnitLabel(int nr) {
		this.add(new JSpinner(new SpinnerNumberModel(1, 1, nr, 1)));
		unit=" persoane";
		this.add(new JLabel(unit));
	}
	/**
	* Constructorul pentru editarea procentelor
	* @param nr   procentul initial
	* Procentul minim este 1, maxim este 100 si se itereaza din 5 in 5
	* */
	public UnitLabel(short nr) {
		this.add(new JSpinner(new SpinnerNumberModel(nr, 0, 100, 5)));
		unit="%";
		this.add(new JLabel(unit));
	}
	/**
	* Constructorul pentru editarea notei
	* @param nota    nota initiala
	* Se lucreaza cu numere cu 2 zecimale intre 1.0 si 10.0 si se intereaza din 0.5
	* */
	public UnitLabel(float nota) {
		this.add(new JSpinner(new SpinnerNumberModel(nota, 0.0f, 10.0f, 0.5f)));
		unit="";
		this.add(new JLabel(""));
	}
	public static void main(String[] args) {
		AppWindow win=new AppWindow();
		UnitLabel test=new UnitLabel(2,13,1);
		test.setBackground(AppWindow.mainColor);
		test.setForeground(Color.WHITE);
		win.add(test);
		win.setVisible(true);
		
	}
	/**
	* Obtine datele din UnitLabel
	* @returns   valoarea din JSpinnere (formatata in cazul timpului) sub forma de String
	*
	* */
	public String retrieveData() {
		if(this.getComponentCount()>2)
		{
			String formattedMinute,formattedHour;
			formattedHour=((JSpinner)(this.getComponent(0))).getValue().toString();
			if(formattedHour.length()==1)
				formattedHour="0"+formattedHour;
			formattedMinute=((JSpinner)(this.getComponent(2))).getValue().toString();
			if(formattedMinute.length()==1)
				formattedMinute="0"+formattedMinute;
			return (formattedHour+":"+formattedMinute+":00");
			
		}
		return ((JSpinner)(this.getComponent(0))).getValue().toString();
			
	}
	/**
	* Adauga un ActionListener pentru JSpinner
	* Folosit la editarea ponderilor pentru modificarea in timp real al oficiului
	*
	* */
	public void addActionListener(ChangeListener l) {
		((JSpinner)this.getComponent(0)).addChangeListener(l);
	}
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		for(Component i:this.getComponents())
		{
	
			if(i instanceof JSpinner)
				((JSpinner)i).getEditor().getComponent(0).setBackground(bg);
			i.setBackground(bg);
		}
	}
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		for(Component i:this.getComponents())
		{
			i.setForeground(fg);
			if(i instanceof JSpinner)
				((JSpinner)i).getEditor().getComponent(0).setForeground(fg);
		}
	}
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		for(Component i:this.getComponents())
			i.setFont(font);
	}
	@Override
	public void setPreferredSize(Dimension d) {
		super.setPreferredSize(d);
		for(Component i:this.getComponents())
			i.setPreferredSize(d);
	}
}
