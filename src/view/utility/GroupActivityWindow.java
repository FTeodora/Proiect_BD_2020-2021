package view.utility;
import model.entity.ActivitateGrup;
import model.enumeration.UserRole;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.*;
public class GroupActivityWindow extends Casette{
	private ActivitateGrup activitate;
	public GroupActivityWindow(ActivitateGrup act,UserRole whoViews) {
		this.activitate=act;
		this.whoSearched=whoViews;
		infos.add(new JLabel("Data inceperii: "+act.getDataInceperii()));
		infos.add(new JLabel("Timp anuntare: "+act.getTimpAnuntare()));
		infos.add(new JLabel("Numar minim participanti:"+act.getNrMinParticipanti()));
		infos.add(new JLabel("Numar curent participanti:"+act.getNrParticipanti()));
		infos.add(new JLabel("<html>"+act.getDescriere().replaceAll("\n", "<br>")+"</html>"));
		display.remove(center);
		left.setLayout(new GridLayout(0,1));
		super.setBackground(AppWindow.mainColor);
		this.colorLabels(Color.WHITE, AppWindow.MAIN_FONT);
		for(JLabel i:infos)
			left.add(i);
	}
	public static ArrayList<Casette> toWindows(ArrayList<ActivitateGrup> source,UserRole whoViews){
		ArrayList<Casette> rez=null;
		if(source!=null&&source.size()>0) {
			rez=new ArrayList<Casette>();
			for(ActivitateGrup i:source)
				rez.add(new GroupActivityWindow(i,whoViews));
		}
		else System.out.println("NULL");
		return rez;
	}
	@Override
	public void addOptionButtons(Field source,ArrayList<ActionListener> actions) {
		InsertionButton participants=new InsertionButton(this.activitate.getId(),"Participanti");
		participants.addActionListener(actions.get(0));
		participants.setSource(source);
		options.add(participants);
		if(this.whoSearched.ordinal()>UserRole.ADMIN.ordinal()&&!activitate.getTimpAnuntare().equals("00:00:00")
				&&!activitate.getEnrolled())
		{
			InsertionButton join=new InsertionButton(this.activitate.getId(),"Participa");
				join.addActionListener(actions.get(1));
				join.setSource(source);
				options.add(join);
			
		}
	}
	public ActivitateGrup getActivitate() {
		return activitate;
	}
	public void makeEditable() {
		ArrayList<Field> field=new ArrayList<Field>();
		field.add(new Field("ID_activitate",new JLabel(Integer.toString(this.activitate.getId()))));
		buttons.add(delete);
		}
}
