package service;

import java.sql.Connection;
import java.util.ArrayList;

import model.entity.*;

/**
 * login- servicul de logare
 * viewPersonalData- vizualizare date personale pentru toti userii
 * viewActivities-vizualizare activitati dintr-o data selectata de pe calendar
 * viewCourseActivities-selecteaza activitatile unui student de la un curs
 *                     -selecteaza toate activitatile de la un curs pe care il preda un profesor
 *                     -selecteaza toate activitatile de la un curs din baza de date
 * viewCourseTeachers-cand un student invita un profesor la o activitate de grup
 *                   -la programarea unei activitati de catre un profesor se afiseaza o lista cu profesorii care pot sa fie asignati la acea activitate
 * viewCourses- vizualizare lista de cursuri la care un student e inscris
 *             -vizualizare lista de cursuri pe care un profesor le preda
 *             -vizualizare a tuturor cursurilor de catre un admin/ super-admin
 * sendNotification-trimitere notificare pentru invitatarea altor studenti in grup/ invitatarea unui profesor la o activitate, cand se modifica datele personale
 * viewNotification-vizualizare notificari
 */
public interface UserService {
          User login(String username, String password);
          User viewPersonalData();
          ContextHolder getSession();
          ArrayList<Activitate> viewActivities(String selectedDate);
          ArrayList<Activitate> viewCourseActivities(int courseID);
          ArrayList<Profesor> viewCourseTeachers(int courseID);
          ArrayList<Student> viewCourseStudents(int courseID);
          ArrayList<Curs> viewCourses();
          Connection getConnection();
          boolean sendNotification(Notificare notificare);
          ArrayList<Notificare> viewNotifications();
}
