package view.utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

public class CasettePanel extends ScrollablePanel{
	protected ArrayList<ActionListener> optionsActions=null;
	protected Connection c;
	protected Field invalid;
	public CasettePanel(ArrayList<Casette> datas,Field invalid){
		this.datas=CasettePanel.toField(datas);
		this.invalid=invalid;
		this.invalid.getNameLabel().setFont(new Font("Tahoma",Font.BOLD,22));
		this.invalid.getInfoField().setFont(new Font("Tahoma",Font.PLAIN,16));
		this.invalid.getNameLabel().setForeground(AppWindow.mainColor);
		this.invalid.getInfoField().setForeground(AppWindow.mainColor);
		this.resetFields(datas);
		
	}
	public CasettePanel(Field invalid){
		this.datas=new ArrayList<Field>();
		this.invalid=invalid;
		this.invalid.getNameLabel().setFont(new Font("Tahoma",Font.BOLD,22));
		this.invalid.getInfoField().setFont(new Font("Tahoma",Font.PLAIN,16));
		this.invalid.getNameLabel().setForeground(AppWindow.mainColor);
		this.invalid.getInfoField().setForeground(AppWindow.mainColor);
		invalid.add(this);
	}
	public static ArrayList<Field> toField(ArrayList<Casette> datas) {
		ArrayList<Field> rez=new ArrayList<Field>();
		if(datas!=null)
			for(Casette i:datas)
				rez.add(new Field(i));
			
		return rez;
	}
	public void resetFields(ArrayList<Casette> newFields) {
		content.removeAll();
		content.revalidate();
		datas.clear();
		if(newFields==null||newFields.size()==0)
			invalid.add(this);
		else {
			datas=CasettePanel.toField(newFields);
			 for(Field i: datas)
				 {
				 
				 i.add(this);
				((Casette)i.getInfoField()).setConnection(c);
				((Casette)i.getInfoField()).setSource(i);
				if(optionsActions!=null)
					((Casette)i.getInfoField()).addOptionButtons(i, optionsActions);
				
				 }
		}
		this.validate();
		this.repaint();
	}
	public void removeField(Field f) {
		((Casette)f.getInfoField()).resetOptions();
		this.datas.remove(f);
		if(datas.size()==0)
			invalid.add(this);
		f.remove(this);
		content.revalidate();
		content.repaint();
	}
	public void setActions(ArrayList<ActionListener> actions) {
		this.optionsActions=actions;
	}
	public void setConnection(Connection c) {
		this.c=c;
	}
	public ArrayList<Casette> getCasettes() {
		ArrayList<Casette> rez=new ArrayList<Casette>();
		if(this.datas!=null&&this.datas.size()>0)
			for(Field i:datas)
			rez.add((Casette)i.getInfoField());
		return rez;
	}

}
