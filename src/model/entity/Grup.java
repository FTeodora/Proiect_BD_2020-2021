package model.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class Grup {
    private int id;
    private int cursId;
    private String nume;
    private String numeCurs;
    private String descriere;




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
    public String getNume() {
        return this.nume;
    }
    public void setNume(String nume) {
        this.nume=nume;
    }
    public String getNumeCurs() {
        return numeCurs;
    }

    public void setNumeCurs(String numeCurs) {
        this.numeCurs = numeCurs;
    }
    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }


    public static Grup parseGrup(ResultSet resultSet,Connection connection) {
        Grup g=null;
        if(resultSet!=null) {
            try {
                g=new Grup();
                g.id=resultSet.getInt(1);
                g.cursId=resultSet.getInt(2);
                g.nume=resultSet.getString(3);
                g.descriere=resultSet.getString(4);
                if(g.descriere==null)
                    g.descriere="  ";
                PreparedStatement s=connection.prepareStatement("SELECT materie FROM curs WHERE ID_curs=?");
                s.setInt(1, g.cursId);
                ResultSet rst =s.executeQuery();
                if(rst.next())
                    g.numeCurs=rst.getString(1);

            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return g;
    }
}