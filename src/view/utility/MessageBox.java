package view.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

import model.entity.Mesaj;
import model.enumeration.UserRole;
/**
 * Caseta din interfata grafica in care se retin mesajele de pe grup
 * Pe langa mesaj si informatiile din superclasa Casette, se retine si id-ul persoanei care vede caseta
 *@see Casette;
 */
public class MessageBox extends Casette {
	private Mesaj mesaj;
	private int whoSearchedID;
	private String autorName;
	/**
	 * Constructorul pentru caseta de mesaj
	 * @param mesaj entitatea de mesaj pentru care se face caseta si care se va retine in spate
	 * @param who rangul persoanei care vede caseta de mesaj
	 * @param session id-ul persoanei care vede caseta
	 * <p>
	 * <ul>
	 * <li> infos(0) - numele autorului (stanga sus)
	 * <li> infos(1) - data postarii (stanga jos)
	 * <li> infos(2) - continutul postarii (centru)
	 * </ul>
	 */
	public MessageBox(Mesaj mesaj,UserRole who,int session) {
		this.mesaj=mesaj;
		this.whoSearched=who;
		this.whoSearchedID=session;
		autorName=mesaj.getAuthor().getNume()+" "+mesaj.getAuthor().getPrenume();
		infos.add(new JLabel(autorName));
		infos.add(new JLabel(mesaj.getDataPostare().substring(0,mesaj.getDataPostare().length()-3)));
		infos.add(new JLabel("<html>"+mesaj.getContinut().replaceAll("\n", "<br>")+"</html>"));
		
		left.setLayout(new BorderLayout());
		left.add(infos.get(0),BorderLayout.NORTH);
		left.add(infos.get(1),BorderLayout.CENTER);
		center.add(infos.get(2));
		display.remove(options);
		super.setBackground(AppWindow.mainColor);
		super.setForeground(Color.WHITE);
		if(who==UserRole.ADMIN||session==mesaj.getAuthor().getId());
			makeEditable();
		this.colorLabels(Color.WHITE, AppWindow.MAIN_FONT);
		this.infos.get(0).setFont(AppWindow.HIGHLIGHTED_INFO);
		infos.get(2).setFont(AppWindow.SMALLER_FONT);
	}
	/**
	 * Metoda statica pentru convertirea unui sir de entitati de mesaje la un sir de casete pentru afisarea intr-un panou
	 * @param source	sirul sursa
	 * @param who		rangul persoanei care vede lista de casete
	 * @param session	ID-ul persoanei care vede lista
	 * @return			lista finala de casete convertite
	 * 
	 */	
	public static ArrayList<Casette> toWindows(ArrayList<Mesaj> source,UserRole who,int session){
		if(source!=null)
			if(source.size()>0)
			{
				Collections.reverse(source);
				ArrayList<Casette> rez=new ArrayList<Casette>();
				for(Mesaj i:source)
					rez.add(new MessageBox(i,who,session));
				return rez;
			}
		return null;
	}
	@Override
	public void addOptionButtons(Field source,ArrayList<ActionListener> actions) {
		if(this.whoSearched.ordinal()<=UserRole.ADMIN.ordinal()||mesaj.getAuthor().getId()==this.whoSearchedID)
		{
			buttons.add(this.edit);
			edit.setSource(source);
			edit.setValue(mesaj.getMessageID());
			buttons.add(this.delete);
			delete.setSource(source);
			delete.setValue(mesaj.getMessageID());
		}
	}
	/**
	 * Formateaza versiunea editabila a casetei de mesaj
	 */
	public void makeEditable() {
		ArrayList<Field> editableFields=new ArrayList<Field>();
		tabel="mesaj";
		editableFields.add(new Field("ID",new JLabel(Integer.toString(this.mesaj.getMessageID()))));
		editableFields.add(new Field("Continut",new JTextArea(mesaj.getContinut())));
		editableFields.get(1).getInfoField().setPreferredSize(new Dimension(250,160));
		editableFields.get(1).getInfoField().setBackground(AppWindow.mainColor);
		editableFields.get(1).getInfoField().setForeground(Color.WHITE);
		super.makeEditableCasette(editableFields);
		JPanel left=new JPanel(new GridLayout(2,1));
		JLabel nameLabel=new JLabel(autorName);
		nameLabel.setFont(AppWindow.HIGHLIGHTED_INFO);
		nameLabel.setForeground(Color.WHITE);
		left.add(nameLabel);
		
		JLabel time=new JLabel(mesaj.getDataPostare().substring(0,mesaj.getDataPostare().length()-3));
		time.setFont(AppWindow.MAIN_FONT);
		time.setForeground(Color.WHITE);
		left.add(time);
		this.editpane.add(left,BorderLayout.WEST);
		
		JPanel center=new JPanel();
		this.editpane.add(center,BorderLayout.CENTER);
		center.add(editableFields.get(1).getInfoField());
		super.setBackground(AppWindow.mainColor);
		super.setForeground(Color.WHITE);
		
		this.setMaximumSize(new Dimension(700,250));
		this.colorFields(Color.WHITE, AppWindow.MAIN_FONT);
		this.editableDatas.get(0).getInfoField().setFont(AppWindow.HIGHLIGHTED_INFO);
		this.editableDatas.get(1).getInfoField().setFont(AppWindow.SMALLER_FONT);
	}
	@Override
	public void changeDatas() {
		mesaj.setContinut(this.editableDatas.get(1).getInfo());
		this.infos.get(2).setText("<html>"+mesaj.getContinut().replaceAll("\n", "<br>")+"</html>");
	}
}
