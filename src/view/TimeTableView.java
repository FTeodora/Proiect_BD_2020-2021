package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.*;

import model.entity.Activitate;
import model.enumeration.UserRole;
import view.utility.*;

public class TimeTableView extends OptionsView {
	private ArrayList<Casette> activitati;
	private CalendarProgramare selectDate;
	private CasettePanel activities;
	private Field err;
	public TimeTableView(UserRole who) {
	super("Orar");
	err=new Field("Nu aveti activitati in data de "+LocalDate.now(),new JLabel("Selectati alta zi din calendar"));
	
	selectDate= new CalendarProgramare(LocalDate.now(),LocalDate.now());
	activities=new CasettePanel(err);
	this.add(selectDate.getContent(),BorderLayout.SOUTH);
	this.add(activities,BorderLayout.CENTER);
	}
	public void resetActivities(ArrayList<Activitate> newActivities,UserRole who) {
		this.err.setName("Nu aveti activitati in data de "+selectDate.getSelectedDate());
		this.err.getNameLabel().validate();
		this.err.getNameLabel().repaint();
		this.activitati=ActivityWindow.toWindows(newActivities, who);
		this.activities.resetFields(activitati);
	}
	public void initializeDays(ActionListener e) {
		selectDate.additionalListener(e);
	}
	public void addDownloadListener(ActionListener e) {
		selectDate.addDownloadButton(e);
	}
	public ArrayList<Casette> getActivities()
	{
		return this.activitati;
	}
	public String getDate() {
		return selectDate.getSelectedDate();
	}

}
