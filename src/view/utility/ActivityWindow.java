package view.utility;

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
import model.entity.Activitate;
import model.entity.Profesor;
import model.enumeration.Frequency;
import model.enumeration.UserRole;


public class ActivityWindow extends Casette{
	
	private Activitate activitate;

	/**
	 * Constructorul pentru o caseta de activitate de curs
	 * @param act activitatea pentru care se face caseta si care se retine in spate
	 * @param who cine vede caseta
	 * <ul>
	 * <li>infos(0) - tipul activitatii (partea de sus)
	 * <ul>In partea stanga, sub forma de GridLayout:
	 * 
	 * <li>infos(1) - data inceperii (stanga)
	 * <li>infos(2) - data incheierii (mijloc)
	 * <li>infos(3) - durata activitatii(dreapta)
	 * </ul>
	 * <li>infos(4) - ponderea activitatii
	 * </ul>
	 */
	public ActivityWindow(Activitate act,UserRole who) {
		this.activitate=act;
		this.whoSearched=who;
		infos.add(new JLabel(act.getTipActivitate().name()));
		if(act.getCourseName()==null)
		{
			infos.add(new JLabel("Data inceperii: "+act.getDataInceperii()));
			infos.add(new JLabel("Data incheierii: "+act.getDataIncheierii()));
			infos.add(new JLabel("Durata: "+act.getDurata()));
			infos.add(new JLabel("Pondere: "+act.getPondere()+"%"));
			left.add(infos.get(1));
			left.add(infos.get(2));
			left.add(infos.get(3));
			buttons.add(infos.get(4));
			
			if(act.getActivityTeacher()!=null) {
				infos.add(new JLabel("Profesor: "+act.getActivityTeacher()));
				options.add(infos.get(5));
			}
		}
		else
		{

			infos.add(new JLabel("Ora inceperii: "+act.getDataInceperii().substring(10,act.getDataInceperii().length()-3)));
			infos.add(new JLabel("Durata: "+act.getDurata().substring(0,act.getDurata().length()-3)));
			left.add(infos.get(1));
			left.add(infos.get(2));
			infos.get(0).setText(act.getCourseName()+" - "+infos.get(0).getText());
		}
		for(JLabel i:infos) {
			i.setBorder(new EmptyBorder(3,3,3,3));
		}
		header.add(infos.get(0));
		left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
		
		
		

		this.setBackground(AppWindow.mainColor);
		this.setForeground(Color.WHITE);
		this.colorLabels(Color.WHITE, AppWindow.MAIN_FONT);
		infos.get(0).setFont(AppWindow.HIGHLIGHTED_INFO);
	}
	/**
	 * Metoda care transforma o lista de activitati intr-o lista de casete de activitati
	 * @param source	lista sursa de activitati
	 * @param who	cine vede casetele
	 * @return	Lista de casete obtinuta din activitati
	 */
	public static ArrayList<Casette> toWindows(ArrayList<Activitate> source,UserRole who) {
		ArrayList<Casette> rez=null;
		if(source!=null)
			if(source.size()>0){ 
			rez=new ArrayList<Casette>();
			for(Activitate i:source)
				rez.add(new ActivityWindow(i,who));
			}
		return rez;
	}
	/**
	 * Adauga optiuni pentru butoanele posibile din caseta in functie de situatie
	 */
	public void addOptionButtons(Field source,ArrayList<ActionListener> actions) {
		if(this.whoSearched!=UserRole.STUDENT) {
			InsertionButton participants=new InsertionButton(activitate.getId(),"Participanti");
			participants.addActionListener(actions.get(0));
			participants.setSource(source);
			options.add(participants);
		}
	}
	public Activitate getActivitate() {
		return this.activitate;
	}
	public static void toDocument(ArrayList<Casette> source,String date,String user) {
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);

		try {
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.setFont(PDType1Font.COURIER, 16);
			contentStream.beginText();
			contentStream.newLineAtOffset(25, 750);			
			contentStream.showText("Activitati din data de "+date);
			//contentStream.newLineAtOffset(0, 15);
			contentStream.endText();
			
			BaseTable table = new BaseTable(700, 6, 5 , 650, 10, document, page, true,
					true);
	//Create Header row
	Row<PDPage> headerRow = table.createRow(15f);
	Cell<PDPage> cell = headerRow.createCell(7.35f, "Ora inceperii");
	Cell<PDPage> cell2 = headerRow.createCell(65.7f, "Activitate");
	Cell<PDPage> cell3 = headerRow.createCell(7.35f, "Durata");

	cell.setFont(PDType1Font.COURIER);
	cell.setFillColor(Color.GRAY);
	cell2.setFont(PDType1Font.COURIER);
	cell2.setFillColor(Color.GRAY);
	cell3.setFont(PDType1Font.COURIER);
	cell3.setFillColor(Color.GRAY);
	table.addHeaderRow(headerRow);

	for (Casette i : source) {
		Row<PDPage> row = table.createRow(10f);
		cell = row.createCell(7.35f, i.getInfo(1).substring(i.getInfo(1).length()-5) );
		cell2 = row.createCell(65.7f, i.getInfo(0) );
		cell3 = row.createCell(7.35f, i.getInfo(2).substring(i.getInfo(2).length()-5) );
}
	table.draw();
			
			contentStream.close();
			document.save(System.getProperty("user.home")+"\\Downloads\\Activitati_"+user+"_"+date+".pdf");
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
