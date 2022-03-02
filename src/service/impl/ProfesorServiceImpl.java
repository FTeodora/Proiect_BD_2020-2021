package service.impl;
import java.util.ArrayList;

import model.entity.*;
import repository.*;
import repository.impl.*;
import service.*;
public class ProfesorServiceImpl extends UserServiceImpl implements ProfesorService{
    private final CursRepository cursRepository;
    private final FisaParticipareRepository fisaParticipareRepository;
    private final FisaCursRepository fisaCursRepository;
    private final ActivitateRepository activitateRepository;
    private final FisaParticipantCursRepository fisaParticipantCursRepository;
    private final ProfesoriInvitatiRepository profesoriInvitatiRepository;
    public ProfesorServiceImpl(ContextHolder session) {
        super(session);
        cursRepository=new CursRepositoryImpl(session.getJdbConnectionWrapper());
        fisaParticipareRepository=new FisaParticipareRepositoryImpl(this.session.getJdbConnectionWrapper());
        fisaCursRepository=new FisaCursRepositoryImpl(this.session.getJdbConnectionWrapper());
        this.activitateRepository=new ActivitateRepositoryImpl(this.session.getJdbConnectionWrapper());
        fisaParticipantCursRepository=new FisaParticipantCursRepositoryImpl(this.session.getJdbConnectionWrapper());
        profesoriInvitatiRepository= new ProfesoriInvitatiRepositoryImpl(this.session.getJdbConnectionWrapper());

    }
    @Override
    public ArrayList<Curs> viewCourses() {
        return cursRepository.loadTeachersCoursesbyName("", session.getUserID());
    }

    @Override
    public ArrayList<Curs> searchCourse(String nume) {
        return cursRepository.loadTeachersCoursesbyName(nume, session.getUserID());
    }
    @Override
    public ArrayList<Student> viewCourseStudents(int courseID) {
        return fisaParticipareRepository.getCourseParticipantsByOrganizer(courseID, session.getUserID());
    }
    @Override
    public  ArrayList<Profesor> viewCourseTeachers(int courseID){
        return fisaCursRepository.getCourseTeachers(courseID);
    }

    @Override
    public ArrayList<Activitate> viewCourseActivities(int courseID) {
        return activitateRepository.getTeacherActivities(session.getUserID(),courseID);
    }

    @Override
    public ArrayList<Student> viewActivityParticipants(int activityID) {
        return fisaParticipantCursRepository.getActivityParticipants(activityID);
    }

    @Override
    public ArrayList<Curs> viewManagedCourses() {
        return cursRepository.getManagedCourses(session.getUserID());
    }

    @Override
    public boolean actualizarePonderiNote(int courseID, String activityType, int pondere) {
        return cursRepository.actualizarePonderiNote(courseID,activityType,session.getUserID(),pondere);
    }

    @Override
    public boolean createActivity(Activitate activitate) {
        if(activitateRepository.createActivity(activitate))
            return true;
        return false;
    }

    @Override
	public ArrayList<Activitate> getActivityTypes(int courseID) {
    	ArrayList<Activitate> type= new ArrayList<Activitate>();
    	ArrayList<Activitate> aux=this.activitateRepository.getPossibleActivitiesByType("laborator", session.getUserID(), courseID);
    	if(aux!=null&&aux.size()>0)
    		type.add(aux.get(0));
    	aux=this.activitateRepository.getPossibleActivitiesByType("seminar", session.getUserID(), courseID);
    	if(aux!=null&&aux.size()>0)
    		type.add(aux.get(0));
    	aux=this.activitateRepository.getPossibleActivitiesByType("curs", session.getUserID(), courseID);
    	if(aux!=null&&aux.size()>0)
    		type.add(aux.get(0));
    	return type;
	}
    @Override
    public  ArrayList<Activitate> viewActivities(String chosenDate){
        return activitateRepository.getTeacherActivities(chosenDate,session.getUserID());
    }
	@Override
	public float[] getStudentGrades(int studentID, int courseID) {
		return cursRepository.getGrades(studentID, courseID);
	}
	@Override
	public boolean acceptGroupInvitation(int activityID) {
		return profesoriInvitatiRepository.invitaProfesor(session.getUserID(), activityID);
	}

    
}
