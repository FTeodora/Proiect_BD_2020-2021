package service;

import model.entity.*;

import java.util.ArrayList;

/**
 * searchCourse-pentru cautarea unui curs dupa nume
 * enrollInCourse-inscrierea automata a unui student la toate activitatile de la o materie, si la profesorul cu nr minim de student daca se poate
 * leaveCourse-parasirea unui curs
 * viewGrades- vizualizare note
 * joinGroup-inscriere intr-un grup de studiu
 * leaveGroup-parasire grup de studiu
 * viewGroups-vizualizare grupuri de studiu
 * searchGroupsByCourse- filtarerea grupurilor dupa o anumita materie
 * viewGroupMembers-vizualizare membrii dintr-un grup
 * viewGroupMessages-vizualizare mesajelor dintr-un grup
 * viewGroupActivities- vizualizarea activitatilor dintr-un grup
 * joinGroupActivity-inscriere la o activitate dintr-un grup
 * invitaProfesor-trimite o invitatie unui profesor la o activitate dintr-un grup de studiu
 * groupMembersSuggestions-afiseaza sugestii de studenti pentru a-i invita in grup
 * getGroupActivityTeachers-afisare profesori participanti la o activitate de grup
 * groupActivityStudents-participantii unei activitati de pe un grup
 * suggestedTeachers-afiseaza sugestii de profesori pentru a-i invita la o activitate de grup
 *
 */
public interface StudentService {
	
	ArrayList<Curs> searchCourse(String nume,boolean isEnrolled);
	boolean enrollInCourse(int cursID);
	boolean leaveCourse(int cursID);
    float[] viewGrades(int courseID);
    boolean leaveGroup(int groupID);
    boolean joinGroup(int groupID);
    ArrayList<Grup> viewGroups();
    ArrayList<Grup> searchGroupsByCourse(int courseID,boolean isEnrolled);
    //int numberOfGroupMembers(int groupID);
    ArrayList<Student> viewGroupMembers(int groupID);
    ArrayList<Mesaj> viewGroupMessages(int groupID);
	ArrayList<ActivitateGrup> viewGroupActivities(int grupID, boolean isEnrolled);
	boolean joinGroupActivity(int activityID);
	boolean invitaProfesor(int userID,int activitateID);
	ArrayList<Student> groupMemberSuggestions(Grup g);
	ArrayList<Profesor> getGroupActivityTeachers(ActivitateGrup activitate);
	ArrayList<Student> groupActivityStudents(int activityID);
	ArrayList<Profesor> suggestedTeachers(ActivitateGrup activitate);
	
}
