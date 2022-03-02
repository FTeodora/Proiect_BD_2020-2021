package repository;

import model.entity.Profesor;
import model.entity.Student;

import java.util.ArrayList;

public interface FisaParticipantCursRepository {
    public boolean createFisaParticipantCurs(int studentID,int activityID,int fisaparticipareID);
    public boolean deleteFisaParticipantCurs(int studentID,int courseID);
    public ArrayList<Student> getActivityParticipants(int activitateID);
	
    
}
