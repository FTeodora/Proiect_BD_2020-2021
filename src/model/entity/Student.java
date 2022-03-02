package model.entity;

import java.util.List;

public class Student extends User {
      private int an;
    private int nrOre;
    private List<Curs> listaCursuri;


    public int getAn() {
        return an;
    }

    public void setAn(int an) {
        this.an = an;
    }

    public int getNrOre() {
        return nrOre;
    }

    public void setNrOre(int nrOre) {
        this.nrOre = nrOre;
    }

    public List<Curs> getListaCursuri() {
        return listaCursuri;
    }

    public void setListaCursuri(List<Curs> listaCursuri) {
        this.listaCursuri = listaCursuri;
    }
}
