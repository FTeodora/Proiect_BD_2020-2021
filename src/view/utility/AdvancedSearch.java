package view.utility;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import view.CourseView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
public class AdvancedSearch extends JPanel{
	private ScrollablePanel mainFilters;
	private String tabel,tabel2,commonCol;
	private String mainCondition,joinCondition;
	private JButton confirm,clear;
	private Connection connection;
	private boolean doJoin;
	public AdvancedSearch(ArrayList<Field> fields,String tabel) {
		super(new BorderLayout());
		mainFilters=new ScrollablePanel(fields);
		mainFilters.unifyFields();
		
		doJoin=false;
		this.tabel=tabel;
		mainCondition="";
		joinCondition="";
		this.tabel2=null;
		JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		confirm=new JButton("Cauta");
		AppWindow.formatButton(confirm,new Dimension(100,35));
		
	
		Field.formatFields(mainFilters.datas, AppWindow.darkestBlue, AppWindow.TEXT_FONT);
		 for(Field i:mainFilters.datas)
		 {
			 
			 i.getNameLabel().setAlignmentX(-1.0f);
			 i.getInfoField().setAlignmentX(-1.0f);
			 if( i.getInfoField() instanceof JTextField)
				 i.getInfoField().setPreferredSize(new Dimension(400,40));
		 }
		clear=new JButton("Resetare filtru");
		clear.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				mainFilters.resetDatas();
			}
		});
		AppWindow.formatButton(clear,new Dimension(100,35));
		
		buttons.setBackground(AppWindow.darkestBlue);
		buttons.add(confirm);
		buttons.add(clear);
		
		this.add(mainFilters,BorderLayout.CENTER);
		this.add(buttons,BorderLayout.SOUTH);
		this.mainFilters.setFont(AppWindow.BUTTON_FONT);
		buttons.setForeground(AppWindow.darkestBlue);
	}
	/**
	 * Formateaza mai multe Field-uri la o conditie de forma
	 *  [WHERE] coloana1 LIKE 'valoare1' AND coloana2 LIKE 'valoare2' etc.
	 *  De asemenea, ignora spatiile goale
	 */
	public void toCondition() {

		if(!mainCondition.equals(""))
			mainCondition+=" AND";
		if(mainFilters.datas!=null)
		{
			for(Field i:mainFilters.datas)
			{
				if(i.getInfoField() instanceof JPanel || i.getInfoField() instanceof JCheckBox)
					break;
				if(i.getName().equals("Materie:")&&tabel.equals("grup"))
					i.setName("ID_curs");
				if(!i.getInfo().trim().equals(""))
					mainCondition+=" "+i.getName().replace(":", "").replace(" ", "_")+" LIKE '"+i.getInfo()+"' AND";
			}
			if(mainCondition.endsWith("AND"))
				mainCondition=mainCondition.substring(0, mainCondition.length()-3);
		}
	}
	public void setDoJoin(boolean join) {
		this.doJoin=join;
	}
	public void setJoinTable(String tabel,String commonCol) {
		this.tabel2=tabel;
		this.commonCol=commonCol;
	}
	public void addMainCondition(String condition) {
		this.mainCondition+=" "+condition;
	}
	public void addJoinCondition(String condition) {
		this.joinCondition+=" "+condition;
	}
	public void setConnection(Connection c) {
		this.connection=c;
	}
	public Connection getConnection() {
		return this.connection;
	}
	public Field getField(int index)
	{
		return mainFilters.getField(index);
	}
	/**
	 * Select cu o conditie mai complexa. Se bazeaza pe structura generala
	 * SELECT * 
	 * FROM tabel
	 * [INNER JOIN tabel2 ON conditieJoin]
	 * WHERE conditiePrincipala;
	 * @return ResultSet-ul cautarii pentru convertirea in entitati si folosirea lor ulterioara
	 */
	public ResultSet findRowInTableFiltered() {
		toCondition();
		String statement="SELECT * FROM "+tabel;
		try {
			Statement st = connection.createStatement();
			ResultSet rez = null;
			if(tabel2!=null&&doJoin)
			{
				if(tabel.equals("user"));
					statement+=" INNER JOIN "+tabel2+" ON users.ID="+tabel2+"."+commonCol;
				if(!joinCondition.trim().equals(""))
					statement+=" AND "+joinCondition;
				}
			if(!mainCondition.trim().equals(""))
				statement+=" WHERE "+mainCondition;
			System.out.println(statement);
			st.execute(statement);
			rez = st.getResultSet();
			mainCondition="";
			joinCondition="";
			return rez;

		} catch (Exception ae) {
			ae.printStackTrace();
			return null;
		}
	}
	public void incorporateActions() {
		AdvancedSearch t=this;
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(Field i:t.mainFilters.datas)
					i.resetVals();
					
			}
		});
	}
	public void addClearEvent(ActionListener e) {
		this.clear.addActionListener(e);
	}
	public void addSubmitEvent(ActionListener e) {
		this.confirm.addActionListener(e);
	}
	public void formatFields(Color fg,Font font) {
		Field.formatFields(this.mainFilters.datas, fg, font);
	}
}
