package model.entity;

public class Notificare {
    private int receiverID;
    private int senderID;
    private String senderName;
    private String descriere;
    private String expirare;

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getExpirare() {
        return expirare;
    }

    public void setExpirare(String expirare) {
        this.expirare = expirare;
    }
}
