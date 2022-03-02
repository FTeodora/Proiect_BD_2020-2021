package model.entity;

import java.util.List;

public class MembruGrup extends Student{
    private int IDmembru;
    private int grupId;
    private List<Mesaj> listaMesaje;

    public int getIdMembru() {
        return IDmembru;
    }

    public void setIdMembru(int id) {
        this.IDmembru = id;
    }

    public int getGrupId() {
        return grupId;
    }

    public void setGrupId(int grupId) {
        this.grupId = grupId;
    }

    public List<Mesaj> getListaMesaje() {
        return listaMesaje;
    }

    public void setListaMesaje(List<Mesaj> listaMesaje) {
        this.listaMesaje = listaMesaje;
    }
}
