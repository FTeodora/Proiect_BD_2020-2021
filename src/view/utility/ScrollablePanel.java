package view.utility;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.border.EmptyBorder;

import javax.swing.*;

public class ScrollablePanel extends JPanel{
	protected ArrayList<Field> datas;
	protected JPanel content;
	public JPanel getContent() {
		return content;
	}
	
	public ScrollablePanel(){
		this.setLayout(new BorderLayout());
		content=new JPanel();
		JScrollPane CONT=new JScrollPane(content);
		this.setAutoscrolls(true);
		content.setAutoscrolls(true);
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setMinimumSize(new Dimension(300,300));
		this.add(CONT,BorderLayout.CENTER);
	}
	public ScrollablePanel(ArrayList<Field> comp){
		this.setLayout(new BorderLayout());
		content=new JPanel();
		JScrollPane CONT=new JScrollPane(content);
		this.setAutoscrolls(true);
		content.setAutoscrolls(true);
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.setBorder(new EmptyBorder(20, 20, 20, 20));
		datas=comp;
		for(Field i: datas) 
			i.add(this.content);
		this.setMinimumSize(new Dimension(300,300));
		this.add(CONT,BorderLayout.CENTER);
	}
	
	public void  resetDatas() {
		for(Field i:datas) {
			if(i.getName().trim().equals(""))
				break;
			i.resetVals();
		}
	}
	protected ArrayList<String>  getLabels() {
		ArrayList<String> rez=new ArrayList<String>();
		for(Field i:this.datas) {
				rez.add(i.getName());
		}
		return rez;
	}
	public ArrayList<String>  getDatas() throws Exception{
		ArrayList<String> rez=new ArrayList<String>();
		for(Field i:this.datas) {
			if(i.getInfoField() instanceof JCheckBox)
				break;
			if(i.getName().trim().equals(""))
				break;
			if(i.getInfo().trim().equals(""))
				throw(new Exception("Campul "+i.getName()+" este gol"));
			rez.add(i.getInfo());
			
		}
		return rez;
	}public ArrayList<String>  getDatasIgnoreNull() {
		ArrayList<String> rez=new ArrayList<String>();
		for(Field i:this.datas) {
			if(i.getName().trim().equals(""))
				break;
			rez.add(i.getInfo());
		}
		return rez;
	}
	 public Field getField(int index) {
		 return datas.get(index);
	 }
	public void unifyFields() {
		this.getContent().removeAll();
		for(Field i:datas) {
			JPanel aux=new JPanel();
			aux.setLayout(new BoxLayout(aux,BoxLayout.X_AXIS));
			aux.add(i.getNameLabel(),JComponent.LEFT_ALIGNMENT);
			aux.add(i.getInfoField(),JComponent.LEFT_ALIGNMENT);
			aux.setBackground(i.getNameLabel().getBackground());
			aux.setBorder(new EmptyBorder(1,1,1,1));
			aux.setAlignmentX(-1f);
			this.getContent().add(aux,JComponent.LEFT_ALIGNMENT);
		}
		this.validate();
		this.repaint();
	}
	public void setBg(Color bg) {
		this.getContent().setBackground(bg);
		for(Field i:datas)
		i.setBackground(bg);
	}
	public void setFg(Color fg) {
		this.getContent().setForeground(fg);
		for(Field i:datas)
			i.setForeground(fg);
	}
	public void setLabelFont(Font f) {
		for(Field i:datas)
			if(!i.getName().trim().equals(""))
				i.getNameLabel().setFont(f);
	}
	public void setFieldFont(Font f) {
		for(Field i:datas)
			if(!(i.getInfoField() instanceof JPanel))
				i.getInfoField().setFont(f);
	}
	public void formatFields(Color fg,Font font){
		Field.formatFields(datas, fg, font);
	}
}


