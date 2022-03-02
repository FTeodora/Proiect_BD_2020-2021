package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.*;

import model.entity.User;
import model.enumeration.UserRole;
import service.AdminService;
import service.UserService;
import view.utility.*;

public class SearchUserView extends SearchView{

	Field nume,prenume;
	public SearchUserView(UserRole who) {
		super("Cautare/Modificare/Stergere utilizator",new Field("Cautati in casetele de text dupa nume si prenume",new JLabel("sau cautati dupa mai multe criterii simultan")));
		nume=new Field("Nume:",new JTextField());
		nume.getNameLabel().setForeground(Color.WHITE);
		nume.getNameLabel().setBackground(AppWindow.headerColor);
		nume.getInfoField().setPreferredSize(new Dimension(150,30));
		
		prenume=new Field("Prenume:",new JTextField());
		prenume.getNameLabel().setForeground(Color.WHITE);
		prenume.getNameLabel().setBackground(AppWindow.headerColor);
		prenume.getInfoField().setPreferredSize(new Dimension(150,30));
		ArrayList<Field> fields=new ArrayList<Field>();
		fields.add(new Field("username:",new JTextField()));
		fields.add(new Field("CNP:",new JTextField()));
		fields.add(new Field("Nume:",new JTextField()));
		fields.add(new Field("Prenume:",new JTextField()));
		fields.add(new Field("Adresa:",new JTextField()));
		fields.add(new Field("Telefon:",new JTextField()));
		fields.add(new Field("email:",new JTextField()));
		fields.add(new Field("IBAN:",new JTextField()));
		fields.add(new Field("Nr contract:",new JTextField()));
		String[] types= {" ","STUDENT","PROFESOR"};
		
		JComboBox<String> type=new JComboBox<String>(types);
		if(who==UserRole.SUPERADMIN)
			type.addItem("ADMIN");
		fields.add(new Field("Tip user:",type));
		
		JPanel extras=new JPanel(new CardLayout());
		String[] ani= {" ","1","2","3","4","5","6"};

		extras.add(new JLabel(),"-");
		extras.add(new JComboBox<String>(ani),"STUDENT");
		extras.add(new JComboBox<String>(AppWindow.departamente),"PROFESOR");
		extras.add(new JLabel(),"ADMIN");
		
		((JComboBox<String>)fields.get(9).getInfoField()).addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			CardLayout cl=(CardLayout)extras.getLayout();
			cl.show(extras, fields.get(9).getInfo().replace(" ", "-"));
		}
		});
		
		
		fields.add(new Field(extras));
		this.addAdvancedSearch(fields, "users");
		advancedFilter.setPreferredSize(new Dimension(400,500));
		
		Field.formatFields(fields, AppWindow.darkestBlue, AppWindow.TEXT_FONT);
		nume.add(searchBar);
		prenume.add(searchBar);
		searchBar.add(cancel);
		searchBar.add(advanced);
	}

public void addUserResults(ArrayList<User> results) {
		this.results.resetFields(PersonalInfo.toWindows(results));
	}
public String getExtraInfo() {
	if(getSelectedType().equals("STUDENT"))
		return ((JComboBox<String>)((((Field)(advancedFilter.getField(10))).getInfoField().getComponent(1)))).getSelectedItem().toString();
	if(getSelectedType().equals("PROFESOR"))
		return ((JComboBox<String>)((((Field)(advancedFilter.getField(10))).getInfoField().getComponent(2)))).getSelectedItem().toString();
	return "";
}
public String getSelectedType() {
	return advancedFilter.getField(9).getInfo();
}
public String getNume() {
	return nume.getInfo().trim();
}
public String getPrenume() {
	return prenume.getInfo().trim();
}
}
