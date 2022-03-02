package model.entity;

import java.util.List;

public class FisaCurs extends Profesor{
    private int id;
    private Curs curs;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Curs getCurs() {
        return curs;
    }

    public void setCurs(Curs curs) {
        this.curs = curs;
    }
}
