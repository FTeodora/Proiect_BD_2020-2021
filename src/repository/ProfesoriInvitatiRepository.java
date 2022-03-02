package repository;

import java.util.ArrayList;

import model.entity.ActivitateGrup;
import model.entity.Profesor;

public interface ProfesoriInvitatiRepository {
    boolean invitaProfesor(int userID,int activitateID);

	ArrayList<Profesor> getActivityParticipants(ActivitateGrup activitate, boolean isParticipant);
}
