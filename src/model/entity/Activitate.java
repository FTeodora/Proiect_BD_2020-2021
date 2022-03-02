package model.entity;

import model.enumeration.ActivityType;
import model.enumeration.Frequency;

import java.sql.ResultSet;


public class Activitate {
    private int id;
    private int cursId;
    private ActivityType tipActivitate;
    private String dataInceperii;
    private String  dataIncheierii;
    private Frequency frecventa;
    private String durata;
    private int nrMaxParticipanti;
    private int pondere;
    private Profesor activityTeacher;
    private String CourseName;
    Organizator organizator;



	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCursId() {
        return cursId;
    }

    public void setCursId(int cursId) {
        this.cursId = cursId;
    }

    public ActivityType getTipActivitate() {
        return tipActivitate;
    }

    public void setTipActivitate(ActivityType tipActivitate) {
        this.tipActivitate = tipActivitate;
    }

    public String getDataInceperii() {
        return dataInceperii;
    }

    public void setDataInceperii(String dataInceperii) {
        this.dataInceperii = dataInceperii;
    }

    public String getDataIncheierii() {
        return dataIncheierii;
    }

    public void setDataIncheierii(String dataIncheierii) {
        this.dataIncheierii = dataIncheierii;
    }

    public Frequency getFrecventa() {
        return frecventa;
    }

    public void setFrecventa(Frequency frecventa) {
        this.frecventa = frecventa;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public int getNrMaxParticipanti() {
        return nrMaxParticipanti;
    }

    public void setNrMaxParticipanti(int nrMaxParticipanti) {
        this.nrMaxParticipanti = nrMaxParticipanti;
    }

    public int getPondere() {
        return pondere;
    }

    public void setPondere(int pondere) {
        this.pondere = pondere;
    }

    public Profesor getActivityTeacher() {
        return activityTeacher;
    }

    public void setActivityTeacher(Profesor activityTeacher) {
        this.activityTeacher = activityTeacher;
    }
    public String getCourseName() {
		return CourseName;
	}

	public void setCourseName(String courseName) {
		CourseName = courseName;
	}

    public Organizator getOrganizator() {
        return organizator;
    }

    public void setOrganizator(Organizator organizator) {
        this.organizator = organizator;
    }

    public static Activitate parseActivitate(ResultSet rst) {
    	Activitate a=new Activitate();
    	try {
    		a.id=rst.getInt(1);
    		a.cursId=rst.getInt(2);
    		String act=rst.getString(3);

    		switch(act)
    		{
    		case "curs":{
    			 a.tipActivitate=ActivityType.CURS;
    			 break;
    		}
    		case "seminar":{
   			 a.tipActivitate=ActivityType.SEMINAR;
   			 break;
    		}
    		case "laborator":{
   			 a.tipActivitate=ActivityType.LABORATOR;
   			 break;
    		}
    		}
    		a.dataInceperii=rst.getString(4);
    		a.dataIncheierii=rst.getString(5);
    		String freq=rst.getString(6);
    		if(freq.equals("saptamanal"))
    			a.frecventa=Frequency.SAPTAMANAL;
    		else
    			a.frecventa=Frequency.SAPTAMANAL_ALTERNANT;
    		a.setDurata(rst.getString(7));
    		a.setNrMaxParticipanti(8);
    		a.setPondere(rst.getInt(9));
    		
    		
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	return a;
    }
public String toString() {
	return this.tipActivitate.toString();
}
}
