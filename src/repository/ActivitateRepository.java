package repository;

import model.entity.Activitate;
import model.entity.Organizator;
import model.entity.Profesor;
import model.enumeration.ActivityType;

import java.util.ArrayList;

public interface ActivitateRepository {

    ArrayList<Activitate> viewActivities(int studentID,int courseID);
    int getMinParticipantsCourse(int courseID);
    ArrayList<Activitate> getPossibleActivitiesByType(String activityType,int profesorPrincipalID,int courseID);
     boolean studentActivitiesCollideWith(int studentID,Activitate activitate);
    int getActivityID(int organizatorID,int courseID,String activityType);
    int isEnrolledInActivity(int studentID,int courseID);
     int getNrParticipants(int activityID);
     ArrayList<Activitate> getTeacherActivities(int userID, int cursID);
    boolean createActivity(Activitate a);
	boolean activitiesCollide(Activitate activitate1, Activitate activitate2);
	ArrayList<Activitate> getStudentActivities(String chosenDate,int studentID);
	ArrayList<Activitate> getTeacherActivities(String chosenDate,int teacherID);
	ArrayList<Activitate> getTimetable(String chosenDate);
	ArrayList<Activitate> getActivitiesforCourse(int cursID);
}
