package model.entity;

public class Organizator {
   private  int id;
   private int userId;
   private int activitateId;
   private int profesorPrincipal;

    public int getProfesorPrincipal() {
        return profesorPrincipal;
    }

    public void setProfesorPrincipal(int profesorPrincipal) {
        this.profesorPrincipal = profesorPrincipal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getActivitateId() {
        return activitateId;
    }

    public void setActivitateId(int activitateId) {
        this.activitateId = activitateId;
    }
}
