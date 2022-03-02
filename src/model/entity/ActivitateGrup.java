package model.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ActivitateGrup {
    private int id;
    private Grup grup;
    private String descriere;
    private int nrMinParticipanti;
    private String timpAnuntare;
    private String dataInceperii;
    private int participantiActuali;
    private boolean isEnrolled;
    public ActivitateGrup() {
    	grup=new Grup();
    }
    public void setGrup(Grup g) {
    	this.grup=g;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGrupId() {
        return grup.getId();
    }
    public int getGrupCourse() {
    	return grup.getCursId();
    }
    public void setGrupId(int grupId) {
        this.grup.setId(grupId);
    }
    public String getGrupName() {
    	return grup.getNume();
    }
    public void setGrupName(String grupName) {
    	this.grup.setNume(grupName);
    }
    public int getNrMinParticipanti() {
        return nrMinParticipanti;
    }

    public void setNrMinParticipanti(int nrMinParticipanti) {
        this.nrMinParticipanti = nrMinParticipanti;
    }

    public String getTimpAnuntare() {
        return timpAnuntare;
    }

    public void setTimpAnuntare(String timpAnuntare) {
        this.timpAnuntare = timpAnuntare;
    }

    public String getDataInceperii() {
        return dataInceperii;
    }

    public void setDataInceperii(String dataInceperii) {
        this.dataInceperii = dataInceperii;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public int getNrParticipanti() {
        return participantiActuali;
    }
    public boolean getEnrolled() {
    	return this.isEnrolled;
    }
    public void setEnrolled(boolean isEnrolled) {
    	this.isEnrolled=isEnrolled;
    }
    public static ActivitateGrup parseActivitate(ResultSet resultSet,Connection c) {
        ActivitateGrup rez=null;
        try {
            if(resultSet!=null)
            {
                rez=new ActivitateGrup();
                rez.id=resultSet.getInt(1);
                rez.setGrupId(resultSet.getInt(2));
                rez.nrMinParticipanti=resultSet.getInt(3);
                rez.timpAnuntare=resultSet.getString(4);
                rez.dataInceperii=resultSet.getString(5);
                rez.descriere=resultSet.getString(6);
                PreparedStatement preparedStatement = c.prepareStatement("SELECT nr_participanti_grup(?)");
             
                preparedStatement.setInt(1, rez.id);
                ResultSet rst = preparedStatement.executeQuery();
                if(rst.next())
                	rez.participantiActuali=rst.getInt(1);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return rez;
    }
}
