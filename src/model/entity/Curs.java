package model.entity;

import java.sql.ResultSet;

import model.enumeration.CourseType;

public class Curs {
    private int id;
    private String materie;
    private int an;
    private CourseType tipCurs;
    private String descriere;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterie() {
        return materie;
    }

    public void setMaterie(String materie) {
        this.materie = materie;
    }

    public int getAn() {
        return an;
    }

    public void setAn(int an) {
        this.an = an;
    }

    public CourseType getTipCurs() {
        return tipCurs;
    }

    public void setTipCurs(CourseType tipCurs) {
        this.tipCurs = tipCurs;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
    public static Curs parseCurs(ResultSet row) {
    	Curs c=new Curs();
		try {
			c.setId(row.getInt(1));
			c.setMaterie(row.getString(2));
			c.setAn(row.getInt(3));
			if(row.getString(4).equals("OBLIGATORIU"))
				c.setTipCurs(CourseType.OBLIGATORIU);
			else
				c.setTipCurs(CourseType.OPTIONAL);
			c.setDescriere(row.getString(5));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return c;
    }
    public String toString() {
        return this.materie;
    }
}
