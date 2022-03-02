package view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import model.entity.Curs;
import model.enumeration.UserRole;
import view.utility.*;

public class CourseView extends SearchView{
	protected final JTextField searched;
	protected JCheckBox isEnrolled;
	protected final UserRole whoViews;
	public CourseView(UserRole whoViews) {
		super("Cursuri",new Field("Momentan nu exista cursuri in baza de date",
				new JLabel("Incercati sa adaugati un curs")));
		if(whoViews==UserRole.PROFESOR)
		{
			this.err.setName("Momentan nu sunteti asignat unui curs");
			((JLabel)(this.err.getInfoField())).setText("Cereti unui administrator sa va asigneze unui curs");
		}

		this.whoViews=whoViews;
		searched=new JTextField();
		searched.setPreferredSize(new Dimension(350,30));

		searchBar.add(searched);

		if(whoViews==UserRole.STUDENT)
		{
			this.err.setName("Nu sunteti inca inrolat la un curs");
			((JLabel)(this.err.getInfoField())).setText("Cautati un curs la care ati dori sa va inrolati");
			isEnrolled=new JCheckBox("Inscris");
			isEnrolled.setBackground(AppWindow.headerColor);
			isEnrolled.setForeground(Color.WHITE);
			isEnrolled.setFont(AppWindow.BUTTON_FONT);
			searchBar.add(isEnrolled);
		}
		if(whoViews.ordinal()<=UserRole.ADMIN.ordinal())
		{
			ArrayList<Field> fields=new ArrayList<Field>();
			fields.add(new Field("Materie:",new JTextField()));
			fields.get(0).getInfoField().setMaximumSize(new Dimension(500,45));
			fields.add(new Field("Descriere:",new JTextArea()));
			fields.add(new Field("An:",new JComboBox(AppWindow.ani)));
			fields.get(2).getInfoField().setPreferredSize(new Dimension(500,45));
			String types[]= {"OPTIONAL","OBLIGATORIU"};
			fields.add(new Field("Tip:",new JComboBox(types)));
			Field.formatFields(fields, AppWindow.darkestBlue, AppWindow.TEXT_FONT);
			super.addCreateForm(fields, "curs");
			searchBar.add(this.createNew);
			ArrayList<Field> fields2=new ArrayList<Field>();
			fields2.add(new Field("Materie:",new JTextField()));
			fields2.get(0).getInfoField().setMaximumSize(new Dimension(500,45));
			fields2.add(new Field("Descriere:",new JTextArea()));
			fields2.add(new Field("An:",new JComboBox(AppWindow.ani)));
			fields2.get(2).getInfoField().setPreferredSize(new Dimension(500,45));
			fields2.add(new Field("Tip:",new JComboBox(types)));
			this.addAdvancedSearch(fields2, "curs");
			searchBar.add(advanced);
		}
		searchBar.add(cancel);

	}
	public void setMain(ArrayList<Curs> courses,UserRole who) {
		this.mainPage.resetFields(CourseWindow.toWindows(courses, who,true));
	}
	public void addResults(ArrayList<Curs> results,UserRole who) {
		if(isEnrolled!=null)
			this.results.resetFields(CourseWindow.toWindows(results, who, isEnrolled.isSelected()));
		else
			this.results.resetFields(CourseWindow.toWindows(results, who, true));
	}
	public boolean getEnrolled() {
		return this.isEnrolled.isSelected();
	}
	public String getSearchedText() {
		return this.searched.getText().trim();
	}
	@Override
	public void resetSearched() {
		this.searched.setText("");
		if(isEnrolled!=null)
			this.isEnrolled.setSelected(false);
	}

}
