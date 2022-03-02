package model.entity;

public class ParticipantActivitate extends FisaParticipare{
    private int idParticipant;
    private int activitateId;
    private int idfisaparticipare;
    private float nota;

    public int getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(int id) {
        this.idParticipant = id;
    }

    public int getActivitateId() {
        return activitateId;
    }

    public void setActivitateId(int activitateId) {
        this.activitateId = activitateId;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public int getIdfisaparticipare() {
        return idfisaparticipare;
    }

    public void setIdfisaparticipare(int idfisaparticipare) {
        this.idfisaparticipare = idfisaparticipare;
    }
}
