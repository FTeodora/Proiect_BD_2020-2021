package model.entity;

import java.util.List;

public class Profesor extends User {
    private int minOre;
    private int maxOre;
    private String departament;
    private List<Curs> listaCursuri;

    public int getMinOre() {
        return minOre;
    }

    public void setMinOre(int minOre) {
        this.minOre = minOre;
    }

    public int getMaxOre() {
        return maxOre;
    }

    public void setMaxOre(int maxOre) {
        this.maxOre = maxOre;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public List<Curs> getListaCursuri() {
        return listaCursuri;
    }

    public void setListaCursuri(List<Curs> listaCursuri) {
        this.listaCursuri = listaCursuri;
    }
    public String toString(){
        return this.getNume()+" "+this.getPrenume();
    }
}
