package repository;

import java.util.ArrayList;

import model.entity.Student;

public interface ParticipantActivitateGrupRepository {
    boolean joinActivity(int studentID,int activitategrupID);
	ArrayList<Student> getParticipants(int activitateID);
}
