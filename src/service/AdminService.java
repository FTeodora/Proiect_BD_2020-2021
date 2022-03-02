package service;

import model.entity.Activitate;
import model.entity.ActivitateGrup;
import model.entity.Curs;
import model.entity.Grup;
import model.entity.Mesaj;
import model.entity.Profesor;
import model.entity.Student;
import model.entity.User;

import java.util.ArrayList;

/**
 * createStudent, updateStudent, deleteStudent- operatii de creare/modificare/ stergere cont de student
 * createProfesor,updateProfesor,deleteProfesor-operatii de creare/ modificare/ stergere cont de profesor
 * findUsersByName-cautarea studentilor dupa nume
 * findAllUsersforAdmin-afisarea tuturor utilizatorilor
 * findStudentbyYearforAdmin- filtreaza studentii dupa an
 * findTeachersbyDepartmentforAdmin-filtreaza profesorii dupa departament
 * assignTeacher-asignarea unui profesor la un curs
 * searchCourse-cautarea unui curs dupa nume
 * createActivity-crearea unei activitati
 * getActivityParticipant-afisarea tuturor participantilor la o activitate
 * viewGroups-afisarea tuturor grupurilor
 * searchGroupsByCourse- filtreaza grupurile dupa curs
 * viewGroupActivities-afiseaza activitatile de la un grup
 * viewGroupMembers-afiseaza toti membrii de la un grup
 * viewGroupMessages-afiseaza toate mesajele de pe un grup de studiu
 * kickGroup-scoaterea unui membru dintr-un grup
 * getOtherTeachersForCourse-la asignare afiseaza profesorii care nu sunt deja la curs
 * getGroupActivityTeachers-afisarea profesorilor participanti la o activitate
 * groupActivityStudents-afisarea studentilor participanti la o activitate
 */
public interface AdminService {

     boolean createStudent(Student student);
     boolean updateStudent(Student student);
     boolean deleteStudent(String userName);
     boolean createProfesor(Profesor profesor);
     boolean updateProfesor(Profesor profesor);
     boolean deleteProfesor(String userName);
 
    ArrayList<User> findUsersByName(String nume,String prenume);
    ArrayList<User> findAllUsersforAdmin(String type_user);
    ArrayList<User> findStudentsbyYearforAdmin(int an);
    ArrayList<Profesor> findTeachersbyDepartamentforAdmin(String nume_departament,int courseID);

    boolean assignTeacher(int courseID,int teacherID);
    ArrayList<Curs> searchCourse(String courseName);
    boolean createActivity(Activitate a);
    ArrayList<Student> getActivityParticipants(int IDactivitate);
    
    ArrayList<Grup> viewGroups();
    ArrayList<Grup> searchGroupsByCourse(int courseID);
	ArrayList<ActivitateGrup> viewGroupActivities(int grupID);
	ArrayList<Student> viewGroupMembers(int groupID);
	ArrayList<Mesaj> viewGroupMessages(int groupID);
	boolean kickGroup(int studentID, int groupID);
	ArrayList<Profesor> getOtherTeachersForCourse(int courseID);
	ArrayList<Profesor> getGroupActivityTeachers(ActivitateGrup activitate);
	ArrayList<Student> groupActivityStudents(int activityID);
	
}