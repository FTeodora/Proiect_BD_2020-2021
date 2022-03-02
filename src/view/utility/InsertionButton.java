package view.utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.LineBorder;
/**
 * O clasa speciala de butoane care retine ID-ul unei anumite entitati din baza de date pentru
 * a apela mai usor functii din servicii. De asemenea, se retine si sursa campului (care are in principiu
 * o informatie de tip caseta) din care a venit pentru a avea ulterior un acces mai lent la restul informatiilor
 *
 */
public class InsertionButton extends JButton{
	private Field source;
	private int ID;
	/**
	 * Constructor in care se initializeaza valoarea ID-ului si numele butonului, fara sursa
	 * @param value		valoarea ID-ului care va fi retinut
	 * @param option	numele optiunii de pe buton
	 */
	public InsertionButton(int value,String option) {
		this(option);
		this.ID=value;
	}
	/**
	 * Constructorul unde are loc si formatarea butonului. Restulconstructorilor il apeleaza pe acesta
	 * Din nou, nu se seteaza sursa din punct de vedere al campului
	 * @param option	numele optiunii de pe buton
	 */
	public InsertionButton(String option) {
		super(option);
		setFont(AppWindow.SMALLER_FONT);
		setBorder(new LineBorder(Color.WHITE));
		setBackground(AppWindow.headerColor);
		setForeground(Color.WHITE);
		setPreferredSize(new Dimension(80,30));
	}
	public void setValue(int ID) {
		 this.ID=ID;
	}
	public int getValue() {
		return this.ID;
	}
	public void setSource(Field source) {
		 this.source=source;
	}
	public Field getSource() {
		return this.source;
	}
}
