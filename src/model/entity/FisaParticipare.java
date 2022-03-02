package model.entity;

public class FisaParticipare extends Student{
    private int IDParticipare;
    private int cursId;
    private float notaFinala;

    public int getIdParticipare() {
        return IDParticipare;
    }

    public void setIdParticipare(int id) {
        this.IDParticipare = id;
    }

    public int getCursId() {
        return cursId;
    }

    public void setCursId(int cursId) {
        this.cursId = cursId;
    }

    public float getNotaFinala() {
        return notaFinala;
    }

    public void setNotaFinala(float notaFinala) {
        this.notaFinala = notaFinala;
    }
}
