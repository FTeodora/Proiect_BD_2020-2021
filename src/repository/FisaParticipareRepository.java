package repository;

import model.entity.Student;

import java.util.ArrayList;

public interface FisaParticipareRepository {
	int createFisaParticipare(int studentID,int courseID);
	boolean deleteFisaParticipare(int studentID,int courseID);
	ArrayList<Student> getCourseParticipants(int courseID);
	ArrayList<Student> getCourseParticipantsByOrganizer(int courseID,int organizerID);
	int getFisaParticipareID(int studentID,int courseID);
	ArrayList<Student> suggestedGroupMembers(int courseID,int groupID);
}
