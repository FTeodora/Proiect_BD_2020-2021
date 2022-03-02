package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import view.utility.*;



public class SearchView extends OptionsView{
	protected final JPanel viewedCasettes;
	protected final Field err;
	protected CasettePanel results;
	protected CasettePanel mainPage;
	protected JPanel searchBar;
	protected final JButton search;
	protected final JButton cancel;
	
	protected JButton advanced;
	protected AdvancedSearch advancedFilter;
	
	protected JButton createNew;
	protected CreateNewForm form;
	public SearchView(String name,Field invalid) {
		super(name);
		viewedCasettes=new JPanel(new CardLayout());
		
		this.err=invalid;
		Field err404=new Field("Nu au fost gasite rezultate",new JLabel("Asigurati-va ca textul introdus este corect"));
		mainPage=new CasettePanel(err);
		results=new CasettePanel(new ArrayList<Casette>(),err404);
		
		viewedCasettes.add(mainPage,"main");
		viewedCasettes.add(results,"results");
		
		this.add(viewedCasettes,BorderLayout.CENTER);
		
		searchBar=new JPanel();
		search=new JButton(new ImageIcon(CourseView.class.getResource("\\icons\\searchIcon.png")));
		search.setBackground(AppWindow.headerColor);
		search.setBorder(new EmptyBorder(0,0,0,0));
		
		
		cancel=new JButton(new ImageIcon(CourseView.class.getResource("\\icons\\cancel.png")));
		cancel.setBackground(AppWindow.headerColor);
		cancel.setBorder(new EmptyBorder(0,0,0,0));
		cancel.setEnabled(false);
		
		searchBar.add(search);
		searchBar.setBackground(AppWindow.headerColor);
		this.add(searchBar,BorderLayout.SOUTH);
		
	}
public boolean hasFilter() {
	return advancedFilter==null;
}

public void setListeners(ArrayList<ActionListener> actions) {
	this.mainPage.setActions(actions);
	this.results.setActions(actions);
}
public void addSearchButtonListener(ActionListener e) {
	this.search.addActionListener(e);
}
public void addCancelButtonListener(ActionListener e) {
	this.cancel.addActionListener(e);
}
public JPanel getViewedCasettes() {
	return this.viewedCasettes;
}
public JButton getCancelButton() {
	return this.cancel;
}
public void removeResult(Field newCasette) {
	this.results.removeField(newCasette);
}
public void removeCasette(Field Casette) {
	this.mainPage.removeField(Casette);
}
public void addAdvancedSearch(ArrayList<Field> filters,String tabel) {
	this.advanced=new JButton("Avansat");
	AppWindow.formatButton(advanced, new Dimension(90,30));
	advancedFilter=new AdvancedSearch(filters,tabel);
	this.viewedCasettes.add(advancedFilter,"filter");
}
public void setFilterSecondTable(String tabel,String commonCol) {
	advancedFilter.setJoinTable(tabel, commonCol);
}
public void addAdvancedListener(ActionListener e) {
	if(advanced!=null)
		advanced.addActionListener(e);;
}
public void addAdvancedSearchListener(ActionListener e) {
	if(advancedFilter!=null)
		advancedFilter.addSubmitEvent(e);
}
public void addAdvancedClearListener(ActionListener e) {
	if(advancedFilter!=null)
		advancedFilter.addClearEvent(e);
}
public AdvancedSearch getAdvancedFilter() {
	return this.advancedFilter;
}
public void addCreateForm(ArrayList<Field> fields, String tabel) {
	createNew=new JButton("Creare");
	AppWindow.formatButton(createNew, new Dimension(80,30));
	this.form=new CreateNewForm(fields,tabel);
	this.viewedCasettes.add(this.form,"create");
}
public CreateNewForm getForm() {
	return this.form;
}
public void addCreateButtonListener(ActionListener e) {
	if(createNew!=null)
		this.createNew.addActionListener(e);
}
public void addCreateSubmitButtonListener(ActionListener e) {
	if(createNew!=null)
		this.form.addSubmitListener(e);
}
public void setPanelsConnection(Connection c) {
	this.mainPage.setConnection(c);
	this.results.setConnection(c);
	if(advancedFilter!=null)
		this.advancedFilter.setConnection(c);
	if(form!=null)
		this.form.setConnection(c);
}
public void resetSearched() {
	
}
}
