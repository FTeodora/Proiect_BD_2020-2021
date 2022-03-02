package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDate;

import javax.swing.*;

import model.entity.ActivitateGrup;
import model.entity.Grup;
import model.entity.Mesaj;
import model.entity.Student;
import model.enumeration.UserRole;
import view.utility.*;

import java.util.ArrayList;

public class GroupDetailsView extends OptionsView{
	private final JPanel mesaje,activitati,participanti;
	private final CasettePanel messageList, activitiesList,participantsList;
	private CreateNewForm newMessage,newActivity;
	private CasettePanel inviteMember;
	private JButton adaugareActivitate;
	private JButton invitaMembru,anulareInvita;
	public GroupDetailsView(Grup g,int session,UserRole who) {
		super("Grupul "+g.getNume()+"\nla materia "+g.getNumeCurs());
		
		JTabbedPane details = new JTabbedPane(JTabbedPane.TOP);
		this.add(details, BorderLayout.CENTER);
		
		mesaje=new JPanel(new BorderLayout());
		details.addTab("Mesaje", null, mesaje, null);
		
		messageList=new CasettePanel(new Field(new JLabel("Momentan nu exista mesaje pe acest grup")));
		mesaje.add(messageList,BorderLayout.CENTER);
		ArrayList<Field> messageFields=new ArrayList<Field>();
		messageFields.add(new Field("Continut:",new JTextArea()));
		messageFields.get(0).getInfoField().setPreferredSize(new Dimension(400,75));
		newMessage=new CreateNewForm(messageFields,"mesaj");
		newMessage.addRootedData(new Field("ID_grup",new JLabel(Integer.toString(g.getId()))));
		newMessage.addRootedData(new Field("user_ID",new JLabel(Integer.toString(session))));
		newMessage.getContent().setLayout(new FlowLayout());
		JPanel messageFooter=new JPanel();
		messageFooter.setBackground(AppWindow.headerColor);
		messageFooter.add(newMessage);
		mesaje.add(messageFooter,BorderLayout.SOUTH);
		
		activitati=new JPanel(new CardLayout());
		details.addTab("Activitati", null, activitati, null);
		
		JPanel currentActivities=new JPanel(new BorderLayout());
		activitiesList=new CasettePanel(new Field(new JLabel("Momentan nu exista activitati in desfasurare pe acest grup")));
		currentActivities.add(activitiesList,BorderLayout.CENTER);
		activitati.add(currentActivities,"curente");	
		
		participanti=new JPanel(new CardLayout());
		details.addTab("Membri", null, participanti, null);
		
		JPanel currentParticipants=new JPanel(new BorderLayout());
		participantsList=new CasettePanel(new Field(new JLabel("Momentan nu exista membri in acest grup")));
		currentParticipants.add(participantsList,BorderLayout.CENTER);
		participanti.add(currentParticipants,"membri");
		
		if(who==UserRole.STUDENT)
		{
			//se face un formular pentru activitati
			ArrayList<Field> formFields=new ArrayList<Field>();
			formFields.add(new Field("Nr minim participanti:",new UnitLabel(100)));
			formFields.add(new Field("Timp anuntare:",new UnitLabel(99,60)));
			formFields.add(new Field("Data incepere:",new CalendarProgramare(LocalDate.now().plusDays(1))));	
			formFields.add(new Field("Descriere:",new JTextArea()));
			newActivity=new CreateNewForm(formFields, "activitati_grup");
			newActivity.addRootedData(new Field("ID_grup",new JLabel(Integer.toString(g.getId()))));
			
			JPanel activitiesFooter=new JPanel(new FlowLayout(FlowLayout.RIGHT));
			activitiesFooter.setBackground(AppWindow.mainColor);
			adaugareActivitate=new JButton("Creare");
			AppWindow.formatButton(adaugareActivitate, new Dimension(80,30));
			activitiesFooter.add(adaugareActivitate);
			currentActivities.add(activitiesFooter,BorderLayout.SOUTH);
			activitati.add(newActivity,"creare");
			activitiesFooter.setBackground(AppWindow.headerColor);
			
			//se face un formular pentru crearea unei noi activitati
			JPanel footer1=new JPanel(new FlowLayout(FlowLayout.RIGHT));
			footer1.setBackground(AppWindow.headerColor);
			invitaMembru=new JButton("Invita");
			AppWindow.formatButton(invitaMembru, new Dimension(80,30));
			

			footer1.add(invitaMembru);
			
			//se face o zona pentru adaugarea de noi membri
			JPanel newMembers=new JPanel(new BorderLayout());
			JPanel footer2=new JPanel(new FlowLayout(FlowLayout.RIGHT));
			footer2.setBackground(AppWindow.headerColor);
			JLabel sugerate=new JLabel("Persoane sugerate");
			sugerate.setFont(AppWindow.TEXT_FONT);
			sugerate.setForeground(Color.WHITE);
			footer2.add(sugerate);
			anulareInvita=new JButton("Anuleaza");
			AppWindow.formatButton(anulareInvita, new Dimension(80,30));
			footer2.add(anulareInvita);
			currentParticipants.add(footer1,BorderLayout.SOUTH);
			
			inviteMember=new CasettePanel(new Field(new JLabel("Momentan nu s-au putut gasi sugestii")));
			newMembers.add(inviteMember,BorderLayout.CENTER);
			newMembers.add(footer2,BorderLayout.SOUTH);
			
			participanti.add(newMembers,"invita");
		}
			
		
	}
public void addMessages(ArrayList<Mesaj> source, UserRole who,int session) {
	messageList.resetFields(MessageBox.toWindows(source, who,session));
}
public void addActivities(ArrayList<ActivitateGrup> source,UserRole who) {
	activitiesList.resetFields(GroupActivityWindow.toWindows(source, who));
}
public void addParticipants(ArrayList<Student> source,UserRole who) {
	participantsList.resetFields(ParticipantWindow.toWindows(source, who));
}
public void addParticipantsSuggestions(ArrayList<Student> source,UserRole who) {
	inviteMember.resetFields(ParticipantWindow.toWindows(source, who));
}
public void removeMember(Field f) {
	this.participantsList.removeField(f);
}
public CreateNewForm getNewMessageForm() {
	return this.newMessage;
}
public CreateNewForm getNewActivityForm() {
	return this.newActivity;
}
public CasettePanel getInviteMember() {
	return this.inviteMember;
}
public CasettePanel getMemberList() {
	return this.participantsList;
}
public JPanel getActivitiesPanel() {
	return this.activitati;
}
public JPanel getMembersPanel() {
	return this.participanti;
}
public void addSubmitMessageListener(ActionListener e) {
	this.newMessage.addSubmitListener(e);
}
public void addMessageActions(ArrayList<ActionListener> actions) {
	messageList.setActions(actions);
}
public void addActivitiesActions(ArrayList<ActionListener> actions) {
	activitiesList.setActions(actions);
}
public void addParticipantsActions(ArrayList<ActionListener> actions) {
	participantsList.setActions(actions);
	if(inviteMember!=null)
		inviteMember.setActions(actions);
}
public void addCreateListener(ActionListener e) {
	this.adaugareActivitate.addActionListener(e);
}
public void addCreateActivityListener(ActionListener e) {
	this.newActivity.addSubmitListener(e);
}
public void addInviteListener(ActionListener e) {
	this.invitaMembru.addActionListener(e);
}
public void addCancelInviteListener(ActionListener e) {
	this.anulareInvita.addActionListener(e);
}
public void addCreateCancelListener(ActionListener e) {
	JButton b=new JButton("Anulare");
	AppWindow.formatButton(b, new Dimension(80,30));
	this.newActivity.addButton(b, e);
}

public void setConnection(Connection c) {
	this.newMessage.setConnection(c);
	this.messageList.setConnection(c);
	if(newActivity!=null)
		this.newActivity.setConnection(c);
	this.activitiesList.setConnection(c);
	if(inviteMember!=null)
		this.inviteMember.setConnection(c);
	this.participantsList.setConnection(c);;
	
}
}
