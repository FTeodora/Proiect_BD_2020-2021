package repository;

import model.entity.Profesor;

import java.util.ArrayList;

public interface FisaCursRepository {
    ArrayList<Profesor> getCourseTeachers(int courseID);
    ArrayList<Profesor> notCourseTeachers(int cursID);
    int createFisa(int cursID,int teacherID);
	ArrayList<Profesor> findTeachersbyDepartament(String nume_departament, int courseID);
}
