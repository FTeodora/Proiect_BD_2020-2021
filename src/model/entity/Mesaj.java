package model.entity;


public class Mesaj {
	private int messageID;
    

	private User author;
    private int IDGrup;
    private String continut;
    private String  dataPostare;
    public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getIDGrup() {
        return IDGrup;
    }

    public void setIDGrup(int IDGrup) {
        this.IDGrup = IDGrup;
    }

    public String getContinut() {
        return continut;
    }

    public void setContinut(String continut) {
        this.continut = continut;
    }

    public String getDataPostare() {
        return dataPostare;
    }

    public void setDataPostare(String dataPostare) {
        this.dataPostare = dataPostare;
    }
}
