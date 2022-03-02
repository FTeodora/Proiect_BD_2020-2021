package view.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.entity.Activitate;
import model.entity.Curs;
import service.ProfesorService;

public class WeightWindow extends Casette{
	Curs course;
	int oficiu;
	int courseAmounts;
	ProfesorService service;
	ArrayList<Activitate> activitati;
		public WeightWindow(Curs course,ProfesorService service) {
			this.service=service;
			this.course=course;
			oficiu=0;
			ArrayList<JLabel> weights=new ArrayList<JLabel>();
			activitati=service.getActivityTypes(course.getId());
			center.setLayout(new GridLayout(2,0));
			for(Activitate i:activitati)
				if(i!=null) {
					infos.add(new JLabel(i.getTipActivitate().toString()));
					weights.add(new JLabel(i.getPondere()+"%"));
					oficiu+=i.getPondere();
				}
			infos.add(new JLabel("Oficiu"));
			weights.add(new JLabel(Integer.toString(100-oficiu)+"%"));
			infos.addAll(weights);
			for(int i=0;i<infos.size();i++)
				center.add(infos.get(i));
			infos.add(new JLabel(course.getMaterie()));
			this.remove(left);
			this.remove(options);
			courseAmounts=infos.size()/2;
			header.add(infos.get(infos.size()-1));
			this.setPreferredSize(new Dimension(700,180));
			this.setMaximumSize(new Dimension(700,180));
			makeEditable();
			this.colorLabels(Color.WHITE, AppWindow.MAIN_FONT);
			infos.get(infos.size()-1).setFont(AppWindow.HIGHLIGHTED_INFO);
		}
		public static ArrayList<Casette> toWindows(ArrayList<Curs> source,ProfesorService service){
			if(source!=null)
				if(source.size()>0) {
					ArrayList<Casette> rez=new ArrayList<Casette>();
					for(Curs i:source)
					{
						rez.add(new WeightWindow(i,service));
					}
					return rez;
				}
			return null;
		}
		public void makeEditable() {
			ArrayList<Field> field=new ArrayList<Field>();
			super.makeEditableCasette(field);
			JPanel center=new JPanel(new GridLayout(2,0));
			
			JLabel labelOficiu=new JLabel(Integer.toString(100-oficiu)+"%");
			labelOficiu.setFont(AppWindow.MAIN_FONT);
			labelOficiu.setForeground(Color.WHITE);
			for(int i=0;i<courseAmounts;i++)
			{
				JLabel type=new JLabel(infos.get(i).getText());
				type.setForeground(Color.WHITE);
				type.setFont(AppWindow.MAIN_FONT);
				center.add(type);
			}
			for(int i=0;i<courseAmounts-1;i++)
			{
				UnitLabel aux=new UnitLabel((short)activitati.get(i).getPondere());
					aux.setFont(AppWindow.MAIN_FONT);
					aux.setForeground(Color.white);
					aux.setBackground(AppWindow.mainColor);
				aux.addActionListener(new ChangeListener() {      
			  @Override
			  public void stateChanged(ChangeEvent e) {
				 oficiu=0;
			    for(int i=0;i<courseAmounts-1;i++)
			    	oficiu+=Integer.parseInt(((UnitLabel)center.getComponent(courseAmounts+i)).retrieveData());
			    labelOficiu.setText(Integer.toString(100-oficiu)+"%");
			    labelOficiu.validate();
			    labelOficiu.repaint();
			  }
			});
				center.add(aux);
			}
			center.add(labelOficiu);
			submit.removeActionListener(submit.getActionListeners()[0]);
			submit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
					if(oficiu>100||oficiu<0)
						JOptionPane.showMessageDialog(null, "Va rugam sa va asigurati ca suma ponderilor este intre 0 si 100","Eroare",JOptionPane.ERROR_MESSAGE);
					else {
						 for(int i=0;i<courseAmounts-1;i++)
							 if(!service.actualizarePonderiNote(course.getId(), infos.get(i).getText(), Integer.parseInt(((UnitLabel)center.getComponent(courseAmounts+i)).retrieveData())))
								 JOptionPane.showMessageDialog(null, "Nu s-a putut actualiza ponderea la "+infos.get(i).getText(),"Eroare",JOptionPane.ERROR_MESSAGE);
							 else {
								 int newWeight=Integer.parseInt(((UnitLabel)center.getComponent(courseAmounts+i)).retrieveData());
								 activitati.get(i).setPondere(newWeight);
								 infos.get(courseAmounts+i).setText(newWeight+"%");
							 }
					infos.get(infos.size()-2).setText(Integer.toString(100-oficiu)+"%");
					infos.get(infos.size()-2).validate();
					infos.get(infos.size()-2).repaint();
					JOptionPane.showMessageDialog(null, "Actualizarea s-a realizat cu succes!","Succes",JOptionPane.INFORMATION_MESSAGE);
					}
					}
				});
			JLabel materie=new JLabel(course.getMaterie());
			materie.setForeground(Color.WHITE);
			materie.setFont(AppWindow.HIGHLIGHTED_INFO);
			editpane.add(materie,BorderLayout.NORTH);
			editpane.add(center);
			options.add(edit);
			this.setBackground(AppWindow.mainColor);
		}
}