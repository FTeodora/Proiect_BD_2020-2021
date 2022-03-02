package view.utility;


import java.util.ArrayList;

import javax.swing.*;

import model.entity.Curs;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Field{
	
	private JLabel name;
	private JComponent info;
	public Field(JComponent info){
		this.name=new JLabel(" ");
		this.info=info;
	}
	public Field(String name,JComponent info){
		this.name=new JLabel(name);
		this.info=info;
		this.info.setAlignmentX(Component.LEFT_ALIGNMENT);
	}
	public JLabel getNameLabel() {
		return name;
	}
	public void setNameLabel(JLabel name) {
		this.name=name;
	}
	public JComponent getInfoField() {
		return info;
	}
	public String getName() {
		return name.getText();
	}
	public void setName(String name) {
		this.name.setText(name);
	}
	public void setInfoField(JComponent field) {
		this.info=field;
	}
	public String getInfo() {
		if(this.info instanceof JFormattedTextField)
			return ((JFormattedTextField)this.info).getText();
		if(this.info  instanceof JTextArea)
			return ((JTextArea)this.info ).getText();
		if(this.info instanceof UnitLabel)
			return ((UnitLabel)this.info ).retrieveData();
		if(this.info instanceof CalendarProgramare)
			return ((CalendarProgramare)this.info).retrieveDate();
		if(this.info instanceof JPasswordField)		
			return String.valueOf(((JPasswordField) this.info).getPassword());
		if(this.info  instanceof JTextField)
			return ((JTextField)this.info ).getText();

		if(this.info instanceof JCheckBox)
			return Boolean.toString(((JCheckBox)this.info).isSelected());
		if(this.info  instanceof JComboBox)
		{
			Object item=((JComboBox)this.info ).getSelectedItem();
			if(item instanceof Curs)
				return Integer.toString(((Curs)item).getId());
			return item.toString();
		}
		if(this.info  instanceof JLabel)
			return ((JLabel)this.info ).getText();
		if(this.info  instanceof JSpinner)
			return ((JSpinner)this.info ).getValue().toString();
		
		return null;
	}
	public void resetVals() {
		if(this.info instanceof JFormattedTextField)
			((JFormattedTextField)this.info).setText("");
		if(this.info  instanceof JTextArea)
			((JTextArea)this.info ).setText("");
		if(this.info instanceof JPasswordField)
			((JPasswordField) this.info).setText("");
		if(this.info  instanceof JTextField)
			((JTextField)this.info ).setText("");
		if(this.info  instanceof JComboBox)
			((JComboBox)this.info ).setSelectedIndex(0);
		if(this.info  instanceof JLabel)
			((JLabel)this.info ).setText("");
		if(this.info  instanceof JSpinner)
			((JSpinner)this.info ).setValue((new Integer(30)));
	}
	public void setInfo(JComponent info) {
		this.info = info;
	}
	public void add(JPanel source) {
		source.add(this.getNameLabel());
		if(this.info instanceof CalendarProgramare)
			source.add(((CalendarProgramare)this.info).getContent());
		else
			source.add(this.info);
	}
	public void add(JLabel source) {
		source.add(this.getNameLabel());
		source.add(this.getInfoField());
	}
	public void add(ScrollablePanel source) {
		source.getContent().add(this.name);
		if(this.info instanceof CalendarProgramare)
			source.getContent().add(((CalendarProgramare)this.info).getContent());
		else
			source.getContent().add(this.info);
	}
	public void remove(ScrollablePanel source) {
		source.getContent().remove(this.getNameLabel());
		source.getContent().remove(this.getInfoField());
	}
	public void setBackground(Color bg) {
		name.setBackground(bg);
		info.setBackground(bg);
	}
	public void setForeground(Color fg) {
		name.setForeground(fg);
		info.setForeground(fg);
	}
	public static void formatFields(ArrayList<Field> f,Color fg,Font font) {
		for(Field i:f)
		{
			i.name.setForeground(fg);;
			i.name.setFont(font);
			i.info.setForeground(fg);
			i.info.setFont(font);
		}
	}
	public boolean checkNumericConstraint() throws Exception {
		try {
			Long.parseLong(this.getInfo().trim());
		}catch(Exception e) {
			throw new Exception("Campul "+getName()+" trebuie sa contina doar numere!");
		}
		return true;
	}
	public boolean checkDateRelativeTo(Field f) throws Exception{
		DateTimeFormatter myFormatObj=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		if(LocalDateTime.parse(f.getInfo(),myFormatObj).isBefore(LocalDateTime.parse(this.getInfo(),myFormatObj)))
			throw new Exception("Va rugam sa va asigurati ca data inceperii este inainte de data incheierii!");
		return true;
	}
	public boolean checkExactCharacters(int nr) throws Exception{
		if(getInfo().trim().length()!=nr)
			throw new Exception("Campul "+getName()+" trebuie sa contina exact "+nr+" caractere, ci nu "+getInfo().length()+"!");
		return true;
	}
	public boolean checkNumberRelativeTo(Field f) throws Exception{
		if(Integer.parseInt(f.getInfo())<Integer.parseInt(this.getInfo()))
			throw new Exception("Valoarea din "+getName()+" trebuie sa fie mai mic sau egala decat valoarea din campul "+getName());
		return true;
	}
}