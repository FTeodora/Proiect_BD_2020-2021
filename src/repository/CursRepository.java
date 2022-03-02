package repository;

import java.util.ArrayList;

import model.entity.Curs;

public interface CursRepository {
    public ArrayList<Curs> loadCoursebyName(String nume,int studentID,boolean isEnrolled);
    ArrayList<Curs> loadTeachersCoursesbyName(String nume,int teacherID);//pentru profesori
    ArrayList<Curs> loadAllCoursesbyName(String nume); //pentru admini
    /**
     * @param studentID	ID-ul studentului pentru care se calculeaza notele
     * @param courseID ID-ul cursului pentru care se iau notele
     * @return un vector de float-uri in care se pastreaza notele dupa cum urmeaza
     * float[0]-curs
     * float[1]-seminar
     * float[2]-laborator
     * float[3]-media ponderata
     */
    public float[] getGrades(int studentID,int courseID);
    boolean actualizarePonderiNote(int courseID, String activityType,int profesorPrincipalID,int pondere);
    ArrayList<Curs> getManagedCourses(int teacherID);
}
