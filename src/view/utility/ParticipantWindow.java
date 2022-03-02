package view.utility;

import model.entity.*;
import model.enumeration.ActivityType;
import model.enumeration.UserRole;
import service.ProfesorService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
public class ParticipantWindow extends Casette{
	private User user;
	private boolean isAssigned;
	private ArrayList<ActivityType> activities;
	private ArrayList<Float> grades;

	public ParticipantWindow(String nume,String prenume) {
		infos.add(new JLabel(nume+" "+prenume));
		left.add(infos.get(0));
		this.setPreferredSize(new Dimension(700,150));
		this.setMaximumSize(new Dimension(700,150));
		
	}
	public ParticipantWindow(Profesor p,boolean isAssigned,UserRole who) {		
		this(p.getNume(),p.getPrenume());
		this.isAssigned=isAssigned;
		this.user=p;
		this.whoSearched=who;
		infos.add(new JLabel(p.getDepartament()));
		center.add(infos.get(1));
		for(JLabel i:infos)
			i.setForeground(Color.WHITE);
		super.setBackground(AppWindow.mainColor);
		super.setForeground(Color.WHITE);
		this.colorLabels(Color.WHITE, AppWindow.MAIN_FONT);
	}
	public ParticipantWindow(Student s,UserRole who) {
		
		this(s.getNume(),s.getPrenume());
		this.whoSearched=who;
		this.user=s;
		this.infos.add(new JLabel("An "+s.getAn()));
		left.setLayout(new BoxLayout(left,BoxLayout.X_AXIS));
		left.add(infos.get(1));
		if((s instanceof ParticipantActivitate) && who==UserRole.PROFESOR)
		{
			this.infos.add(new JLabel("Nota: "+((ParticipantActivitate)s).getNota()));
			left.add(this.infos.get(2));
			makeEditable();
		}
		if(s.getCNP()!=null) {
			this.infos.get(0).setText(this.infos.get(0).getText()+"   "+s.getCNP());
		}
		for(JLabel i:infos)
			i.setBorder(new EmptyBorder(4,4,4,4));
		super.setBackground(AppWindow.mainColor);
		super.setForeground(Color.WHITE);
		this.colorLabels(Color.WHITE, AppWindow.MAIN_FONT);

	}
public ParticipantWindow(FisaParticipare s,UserRole who,ProfesorService service) {
		
		this(s,who);
		this.activities=new ArrayList<ActivityType>();
		this.grades=new ArrayList<Float>();
		center.setLayout(new GridLayout(2,0));
		float[] grades=service.getStudentGrades(s.getId(),s.getCursId());
		ArrayList<Activitate>tipuri=service.getActivityTypes(s.getCursId());
		for(Activitate i:tipuri) {
			activities.add(i.getTipActivitate());
			this.grades.add(Float.valueOf(grades[i.getTipActivitate().ordinal()]));
			System.out.println(i.getTipActivitate().ordinal());
			}
		for(ActivityType i:activities) {
			infos.add(new JLabel(i.toString()));
			center.add(infos.get(infos.size()-1));
		}
		infos.add(new JLabel("Total"));
		center.add(infos.get(infos.size()-1));
		for(Float i:this.grades) {
			infos.add(new JLabel(i.toString()));
			center.add(infos.get(infos.size()-1));
		}
		infos.add(new JLabel(Float.toString(grades[3])));
		this.grades.add(Float.valueOf(grades[3]));
		center.add(infos.get(infos.size()-1));
		this.colorLabels(Color.WHITE, AppWindow.MAIN_FONT);
		display.remove(options);
	}
	public static ArrayList<Casette> toWindows(ArrayList<Profesor> source,boolean isAssigned,UserRole who){
		if(source!=null&&source.size()>0)
		{
			ArrayList<Casette> rez=new ArrayList<Casette>();
			for(Profesor i:source)
				rez.add(new ParticipantWindow(i,isAssigned,who));
			return rez;
		}
			return null;
	}
	public static ArrayList<Casette> toWindows(ArrayList<Student> source,UserRole who){
		if(source!=null&&source.size()>0)
		{
			ArrayList<Casette> rez=new ArrayList<Casette>();
			for(Student i:source)
				rez.add(new ParticipantWindow(i,who));
			return rez;
		}
			return null;
	}
	public static ArrayList<Casette> toWindows(ArrayList<Student> source,UserRole who,ProfesorService service){
		if(source!=null&&source.size()>0)
		{
			ArrayList<Casette> rez=new ArrayList<Casette>();
			for(Student i:source)
				rez.add(new ParticipantWindow((FisaParticipare)i,who,service));
			return rez;
		}
			return null;
	}
	public User getUser() {
		return this.user;
	}
	public void makeEditable() {
		this.tabel="participant_curs";
		ArrayList<Field> editDatas=new ArrayList<Field>();
		editDatas.add(new Field("ID",new JLabel(Integer.toString(((ParticipantActivitate)user).getIdParticipant()))));
		editDatas.add(new Field("Nota ",new UnitLabel(((ParticipantActivitate)(user)).getNota())));
		((UnitLabel)editDatas.get(1).getInfoField()).setBackground(AppWindow.mainColor);
		((UnitLabel)editDatas.get(1).getInfoField()).setForeground(Color.WHITE);
		((UnitLabel)editDatas.get(1).getInfoField()).setFont(AppWindow.MAIN_FONT);
		
		(editDatas.get(1).getNameLabel()).setBackground(AppWindow.mainColor);
		(editDatas.get(1).getNameLabel()).setForeground(Color.WHITE);
		(editDatas.get(1).getNameLabel()).setFont(AppWindow.MAIN_FONT);
		super.makeEditableCasette(editDatas);
		JPanel window=new JPanel(new GridLayout(1,0));
		JLabel name=new JLabel(infos.get(0).getText());
		JLabel year=new JLabel("An: "+((ParticipantActivitate)(user)).getAn());
		name.setForeground(Color.WHITE);
		name.setFont(AppWindow.MAIN_FONT);
		year.setForeground(Color.WHITE);
		year.setFont(AppWindow.MAIN_FONT);
		window.add(name);
		window.add(year);
		editDatas.get(1).add(window);
		this.editpane.add(window,BorderLayout.CENTER);
		this.options.add(edit);
		edit.setValue(Integer.parseInt(editDatas.get(0).getInfo()));
		setBackground(AppWindow.mainColor);
		setForeground(Color.WHITE);
		colorLabels(Color.WHITE, AppWindow.MAIN_FONT);
		
		
	}
	@Override
	public void changeDatas() {
		((ParticipantActivitate)(user)).setNota(Float.parseFloat(this.editableDatas.get(1).getInfo()));
		this.infos.get(2).setText("Nota: "+((ParticipantActivitate)user).getNota());
	}
	@Override
	public void addOptionButtons(Field source,ArrayList<ActionListener> actions) {
		if((user instanceof Profesor)) {
		//	System.out.println(this.whoSearched+" "+this.isAssigned);
		if(!isAssigned){
				InsertionButton assign=new InsertionButton(user.getId(),"Adauga");
				assign.addActionListener(actions.get(0));
				 assign.setSource(source);
				options.add(assign);
			}
		}
		else {
			if(this.whoSearched.ordinal()<=UserRole.ADMIN.ordinal()&&(user instanceof MembruGrup)) {
				InsertionButton kick=new InsertionButton(user.getId(),"Da afara");
				kick.addActionListener(actions.get(1));
				kick.setSource(source);
				options.add(kick);
			}
			else {
				if(this.whoSearched==UserRole.STUDENT&&(user instanceof FisaParticipare)&&!(user instanceof ParticipantGrup))
				{
					InsertionButton invite=new InsertionButton(user.getId(),"Invita");
					invite.addActionListener(actions.get(0));
					invite.setSource(source);
					options.add(invite);
				}
			}
		}
	}
	
	public static void toDocument(ArrayList<Casette> source,String courseName) {
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);

		try {
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.setFont(PDType1Font.COURIER, 16);
			contentStream.beginText();
			contentStream.newLineAtOffset(25, 750);			
			contentStream.showText("Catalogul de la materia "+courseName);
			contentStream.endText();
			
			BaseTable table = new BaseTable(730, 6, 5 , 650, 10, document, page, true,
					true);
	//Create Header row
	Row<PDPage> headerRow = table.createRow(15f);
	Cell<PDPage> cell=headerRow.createCell(30.35f, "Student ");
	Cell<PDPage> cell2=headerRow.createCell(8.35f, "An ");
	ArrayList<Cell<PDPage>> activities=new ArrayList<Cell<PDPage>>();
	for(ActivityType i:((ParticipantWindow)source.get(0)).getActivities())
		 activities.add(headerRow.createCell(8.35f, i.toString()));
	activities.add(headerRow.createCell(8.35f, "Total"));
	cell.setFont(PDType1Font.COURIER);
	cell.setFillColor(Color.GRAY);
	cell2.setFont(PDType1Font.COURIER);
	cell2.setFillColor(Color.GRAY);
	for(Cell<PDPage> i: activities)
	{
		i.setFont(PDType1Font.COURIER);
		i.setFillColor(Color.GRAY);
	}
	
	table.addHeaderRow(headerRow);

	for (Casette i : source) {
		Row<PDPage> row = table.createRow(10f);
		cell = row.createCell(30.35f, i.getInfo(0) );
		cell2 = row.createCell(8.35f, i.getInfo(1) );
		int n=0;
		ArrayList<Float> grades=((ParticipantWindow)i).getGrades();
		for(Cell<PDPage>j:activities)
		{
			j = row.createCell(8.35f, grades.get(n).toString() );
			n++;
		}
}
	table.draw();
			
			contentStream.close();
			DateTimeFormatter myFormatObj=DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
			document.save(System.getProperty("user.home")+"\\Downloads\\Catalog_"+courseName+"_"+myFormatObj.format(LocalDateTime.now())+".pdf");
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("gata");
	}
	public ArrayList<ActivityType> getActivities(){
		return this.activities;
	}
	public ArrayList<Float> getGrades(){
		return this.grades;
	}
}
