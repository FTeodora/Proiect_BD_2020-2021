package view.utility;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;

public class CreateNewForm extends ScrollablePanel{
	private ArrayList<Field> rootedValues;
	protected Connection connection;
	protected String tabel;
	protected JPanel btns;
	
	protected JButton submit,clear;
	protected JPanel helperPanel;
	public CreateNewForm(ArrayList<Field> comp,String tabel){
		super(comp);
		this.tabel=tabel;
		 btns=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		 
		 submit=new JButton("Confirmare");
		 if(tabel.equals("mesaj"))
			 submit.setText("Postare");
		 clear=new JButton("Resetare");
		 AppWindow.formatButton(submit,new Dimension(80,30));
		 AppWindow.formatButton(clear,new Dimension(80,30));
		 CreateNewForm t=this;
		 clear.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent e) {
				 t.resetDatas();
			 }
		 });
		 this.getContent().setBackground(AppWindow.lightestBlue);
		 Field.formatFields(datas, AppWindow.darkestBlue, AppWindow.TEXT_FONT);
		 for(Field i:datas)
		 {
			 if(i.getInfoField() instanceof UnitLabel)
				 ((UnitLabel)i.getInfoField()).setBackground(AppWindow.lightestBlue);
			 i.getNameLabel().setAlignmentX(-1.0f);
			 i.getInfoField().setAlignmentX(-1.0f);
			 if( i.getInfoField() instanceof JTextField)
				 i.getInfoField().setPreferredSize(new Dimension(400,40));
		 }
		 btns.add(submit);
		 btns.add(clear);
		 btns.setBackground(AppWindow.headerColor);
		 this.add(btns,BorderLayout.SOUTH);
		 this.setLabelFont(AppWindow.BUTTON_FONT);
		 this.setForeground(AppWindow.darkestBlue);
	}
	/**
	 * formateaza o lista de valori/coloane din tabel din lista de String-uri in forma de tupla de inserat
	 * adica (coloana1,coloana2,...,coloanaN) sau ('valoare1','valoare2',...,'valoareN') in functie de isValue
	 * 
	 * @param tabel tabelul pe care se va face insertul, retinut doar pentru un caz special
	 * @param source lista de informatii in format string din care extrag informatiile
	 * @param isValue indica daca trebuie sa se puna ghilimele
	 * @return returneaza valorile formatate obtinute, unite intr-un singur string
	 */
	public static String makeInsertedVals(String tabel,ArrayList<String> source,boolean isValue){
		String rez="(";
		for(String i: source)
		{
			if(i.equals("Materie:")&&tabel.equals("grup"))
				i="ID_curs";
			if(isValue==true)
				rez+="'"+i+"'";
			else
				rez+=i.replaceAll(" ", "_").replaceAll(":","");
			rez+=",";
		}
		if(rez.endsWith(","))
			rez=rez.substring(0, rez.length()-1);
		rez+=")";
		return rez;
	}
	public ArrayList<String> getRootedInfos(boolean isValue){
		ArrayList<String> rez=null;
		rez=new ArrayList<String>();
			for(Field i:this.rootedValues)
			{
				if(!isValue)
					rez.add(i.getName());
				else
					rez.add(i.getInfo());
			}
		
			return rez;
	}
	/**
	 * Insereaza un set de valori intr-un tabel, stiind ca sintaxa pentru inserare este
	 * INSERT INTO tabel(coloana1,...,coloanan)
	 * VALUES('valoare1',...,'valoare2')
	 * 
	 * @return ID-ul tuplei inserate pentru operatii ulterioare, sau 0 daca nu s-a putut insera
	 */
	public int insertTouple() {
		
		
		int ID=-1;
		try {
			ArrayList<String> col=this.getLabels();
			ArrayList<String> row=this.getDatas();
			
			if(this.rootedValues!=null)
			{
				ArrayList<String> rootedCol=this.getRootedInfos(false);
				col.addAll(rootedCol);
				ArrayList<String> rootedRow=this.getRootedInfos(true);
				row.addAll(rootedRow);
			}
			if(tabel.equals("mesaj"))
			{
				col.add("data_postare");
				DateTimeFormatter myFormatObj=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				row.add(myFormatObj.format(LocalDateTime.now()));
			}
			String cols=CreateNewForm.makeInsertedVals(tabel,col, false);
			String vals=CreateNewForm.makeInsertedVals(tabel,row, true);
			PreparedStatement s=connection.prepareStatement(
					"INSERT INTO "+tabel+cols+" VALUES"+vals, Statement.RETURN_GENERATED_KEYS);
			System.out.println(
					"INSERT INTO "+tabel+cols+" VALUES"+vals);
			s.execute();
			ResultSet rst=s.getGeneratedKeys();
			if(rst.next())
			{
				System.out.println(rst.getString(1));
				ID =rst.getInt(1);
			}
			else System.out.println("NULL");
			
		}catch(Exception e) {
			ID=-1;
			e.printStackTrace();
			
		}
		return ID;
	}	
	public void addSubmitListener(ActionListener e) {
		this.submit.addActionListener(e);
	}
	public void setConnection(Connection c) {
		this.connection=c;
	}
	public void addRootedData(Field f) {
		if(this.rootedValues==null)
			this.rootedValues=new ArrayList<Field>();
		this.rootedValues.add(f);
	}
	public void addButton(JButton b,ActionListener e) {
		this.btns.add(b);
		b.addActionListener(e);
	}
}