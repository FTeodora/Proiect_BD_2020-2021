package service;

import model.entity.*;

import java.util.ArrayList;

/**
 * searchCourse-filtrare cursuri dupa nume din cursurile pe care le preda un profesor
 * viewActivityParticipants-vizualizarea studentilor de la o activitate tinuta de profesorul nostru
 * viewManagedCourses-vizualizare lista de cursuri pentru care se pot gestiona ponderile
 * getActivityTypes-selectare tipuri de activitati pe care le are un profesor la un curs
 * actualizarePonderiNote-actualizarea ponderilor de la materiile pe care profesorul le preda
 * createActivity- crearea unei activitati
 * getStudentGrades- afisarea studentilor inscrisi la o materie cu notele lor
 * aaceptGroupInvitation- acceptarea unei invitatii la o activitate dintr-un grup de studiu
 */
public interface ProfesorService {
    ArrayList<Curs>  searchCourse(String nume);
    ArrayList<Student> viewActivityParticipants(int activityID);
    ArrayList<Curs> viewManagedCourses();
    
    ArrayList<Activitate>  getActivityTypes(int courseID);
    boolean actualizarePonderiNote(int courseID,String activityType,int pondere);
    boolean createActivity(Activitate activitate);
    float[] getStudentGrades(int studentID,int courseID);
    boolean acceptGroupInvitation(int activityID);
}
