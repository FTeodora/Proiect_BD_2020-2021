package view.utility;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import javax.swing.*;

import model.entity.Grup;
import model.enumeration.UserRole;

public class GroupWindow extends Casette {
	private Grup grup;
	private boolean isEnrolled;

	NavigationButton activities;
	GroupWindow(Grup g,boolean isEnrolled,UserRole who){
		super();
		this.whoSearched=who;
		grup=g;
		this.isEnrolled=isEnrolled;
		infos.add(new JLabel(g.getNume()));
		infos.add(new JLabel(g.getNumeCurs()));
		infos.add(new JLabel("<html>"+g.getDescriere().replaceAll("\n", "<br>")+"</html>"));
		header.add(infos.get(0));
		header.add(infos.get(1));
		header.setLayout(new BoxLayout(header,BoxLayout.Y_AXIS));
		center.add(infos.get(2));
		super.setBackground(AppWindow.mainColor);
		super.setForeground(Color.WHITE);
		this.colorLabels(Color.WHITE, AppWindow.MAIN_FONT);
		infos.get(0).setFont(AppWindow.HIGHLIGHTED_INFO);
		infos.get(2).setFont(AppWindow.SMALLER_FONT);
		if(whoSearched.ordinal()<=UserRole.ADMIN.ordinal())
			makeEditable();
	}
	public static ArrayList<Casette> toWindows(ArrayList<Grup> source,boolean isEnrolled,UserRole who) {
		ArrayList<Casette> rez=null;
		if(source!=null)
		{
			rez=new ArrayList<Casette>();
			for(Grup i:source)
				rez.add(new GroupWindow(i,isEnrolled,who));
		}
		return rez;
	}
	@Override
	public void addOptionButtons(Field source,ArrayList<ActionListener> actions) {
		super.resetOptions();
		if(this.whoSearched==UserRole.STUDENT)
		{
			if(isEnrolled)
			{
				InsertionButton leave=new InsertionButton(grup.getId(),"Paraseste");
				leave.addActionListener(actions.get(0));
				leave.setSource(source);
				options.add(leave);
			}
			else {
				InsertionButton join=new InsertionButton(grup.getId(),"Alatura-te");
				join.addActionListener(actions.get(0));
				join.setSource(source);
				options.add(join);
			}
		}
		if(this.whoSearched.ordinal()<=UserRole.ADMIN.ordinal()||isEnrolled)
		{
			InsertionButton activities=new InsertionButton(grup.getId(),"Grup");
			activities.addActionListener(actions.get(1));
			activities.setSource(source);
			options.add(activities);
		}

	}
	public Grup getGrup() {
		return this.grup;
	}
	public void makeEditable() {
		this.tabel="grup";
		ArrayList<Field>fields=new ArrayList<Field>();
		fields.add(new Field("ID_grup",new JLabel(Integer.toString(this.grup.getId()))));
		fields.add(new Field("Nume:",new JTextField(grup.getNume())));
		fields.add(new Field("Descriere:",new JTextArea(grup.getDescriere())));
		((JTextArea)fields.get(2).getInfoField()).setBorder(new LineBorder(Color.WHITE));
		((JTextArea)fields.get(2).getInfoField()).setPreferredSize(new Dimension(500,150));
		((JTextArea)fields.get(2).getInfoField()).setBackground(AppWindow.mainColor);
		
		super.makeEditableCasette(fields);
		JPanel header=new JPanel();
		header.setLayout(new BoxLayout(header,BoxLayout.Y_AXIS));
		fields.get(1).add(header);
		fields.get(1).setBackground(AppWindow.mainColor);
		JLabel courseNameLabel=new JLabel(grup.getNumeCurs());
		courseNameLabel.setForeground(Color.WHITE);
		courseNameLabel.setFont(AppWindow.MAIN_FONT);
		
		header.add(courseNameLabel);
		header.setBackground(AppWindow.mainColor);
		this.editpane.add(header,BorderLayout.NORTH);
		JPanel center=new JPanel();
		fields.get(2).add(center);
		this.editpane.add(center,BorderLayout.CENTER);
		this.colorFields(Color.WHITE, AppWindow.MAIN_FONT);
		this.editableDatas.get(2).getInfoField().setFont(AppWindow.SMALLER_FONT);
		this.setBackground(AppWindow.mainColor);
		edit.setValue(this.grup.getId());
		this.buttons.add(edit);
		this.buttons.add(delete);
	}
	@Override
	public void changeDatas() {
		infos.get(0).setText(editableDatas.get(1).getInfo());
		infos.get(2).setText("<html>"+editableDatas.get(2).getInfo().replaceAll("\n", "<br>")+"</html>");
	}
}

