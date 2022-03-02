package service.impl;

import model.entity.*;
import model.enumeration.ActivityType;
import model.enumeration.UserRole;
import repository.*;
import repository.impl.*;
import service.ContextHolder;
import service.StudentService;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StudentServiceImpl extends UserServiceImpl implements StudentService {
    private static final Logger LOGGER = Logger.getLogger(StudentServiceImpl.class.getName());
    private final CursRepository cursRepository;
    private final FisaParticipareRepository fisaParticipareRepository;
    private final FisaParticipantCursRepository fisaParticipantCursRepository;
    private final OrganizatorRepository organizatorRepository;
    private final ProfesorRepository profesorRepository;
    private final ActivitateRepository activitateRepository;
    private final GrupRepository grupRepository;
    private final MembruGrupRepository membruGrupRepository;
    private final ActivitateGrupRepository activitateGrupRepository;
    private final MesajRepository mesajRepository;
    private final ParticipantActivitateGrupRepository participantActivitateGrupRepository;
    private final ProfesoriInvitatiRepository profesoriInvitati;
    public StudentServiceImpl(ContextHolder session) {
        super(session);
        this.cursRepository = new CursRepositoryImpl(session.getJdbConnectionWrapper());
        this.fisaParticipareRepository = new FisaParticipareRepositoryImpl(session.getJdbConnectionWrapper());
        this.profesorRepository = new ProfesorRepositoryImpl(session.getJdbConnectionWrapper(), new UserRepositoryImpl(session.getJdbConnectionWrapper()));
        this.activitateRepository = new ActivitateRepositoryImpl(session.getJdbConnectionWrapper());
        this.fisaParticipantCursRepository = new FisaParticipantCursRepositoryImpl(session.getJdbConnectionWrapper());
        this.organizatorRepository = new OrganizatorRepositoryImpl(session.getJdbConnectionWrapper());
        this.grupRepository = new GrupRepositoryImpl(session.getJdbConnectionWrapper());
        this.membruGrupRepository = new MembruGrupRepositoryImpl(session.getJdbConnectionWrapper());
        this.activitateGrupRepository = new ActivitateGrupRepositoryImpl(session.getJdbConnectionWrapper());
        this.mesajRepository=new MesajRepositoryImpl(session.getJdbConnectionWrapper());
        this.participantActivitateGrupRepository=new ParticipantActivitateGrupRepositoryImpl(session.getJdbConnectionWrapper());
        this.profesoriInvitati=new ProfesoriInvitatiRepositoryImpl(session.getJdbConnectionWrapper());
    }

    public ArrayList<Curs> searchCourse(String nume, boolean isEnrolled) {
        return cursRepository.loadCoursebyName(nume, session.getUserID(), isEnrolled);
    }

    @Override
    public boolean enrollInCourse(int cursID) {
        int organizerID = activitateRepository.getMinParticipantsCourse(cursID);
        System.out.println(organizerID);
        if(organizerID==-1)
        	return false;
        int[] arr= {-1,-1,-1};
        Activitate[] selected=new Activitate[3];
        //verificare laborator
        ArrayList<Activitate> laboratoare=activitateRepository.getPossibleActivitiesByType("laborator",organizerID, cursID);
        if(laboratoare.size()==0)
        	arr[0]=0;
        else 
        	for(Activitate i:laboratoare)
            {
        		
            	if(i.getNrMaxParticipanti()>activitateRepository.getNrParticipants(i.getId())&&
            			!activitateRepository.studentActivitiesCollideWith(session.getUserID(),i)
            			)
            	{
            		arr[0]=i.getId();
            		selected[0]=i;
            		break;
            	}
            }
        System.out.println(laboratoare.size()+" "+arr[0]);
        if(arr[0]==-1)
        	return false;
        
        //verificare seminar
        ArrayList<Activitate> seminar=activitateRepository.getPossibleActivitiesByType("seminar",organizerID, cursID);
             if(seminar.size()==0)
             	arr[1]=0;
             else {
             	for(Activitate i:seminar)
                 {System.out.println(i.getNrMaxParticipanti());
                 	if(i.getNrMaxParticipanti()>activitateRepository.getNrParticipants(i.getId())&&
                 			!activitateRepository.studentActivitiesCollideWith(session.getUserID(),i)//&&
                 			//!activitateRepository.activitiesCollide(i, selected[0])
                 			)
                 	{
                 		arr[1]=i.getId();
                 		selected[1]=i;
                 		break;
                 	}
                 }
             }
             System.out.println(seminar.size()+" "+arr[1]);
          if(arr[1]==-1)
        	  return false;
          //verificare curs
          ArrayList<Activitate> curs=activitateRepository.getPossibleActivitiesByType("curs",organizerID, cursID);
          if(curs.size()==0)
          	arr[2]=0;
          else {
          	for(Activitate i:curs)
              {
              	if(i.getNrMaxParticipanti()>activitateRepository.getNrParticipants(i.getId())&&
              			!activitateRepository.studentActivitiesCollideWith(session.getUserID(),i)
              			)
              	{
              		arr[2]=i.getId();
              		selected[2]=i;
              		break;
              	}
              }
          }
          System.out.println(curs.size()+" "+arr[2]);
       if(arr[2]==-1)
     	  return false;
       int fisaparticipareID=fisaParticipareRepository.createFisaParticipare(session.getUserID(), cursID);
       boolean test=true;
       for(int i:arr)
       {
    	   System.out.print(i+" ");
    	   if(i!=0)
    		   test=fisaParticipantCursRepository.createFisaParticipantCurs(session.getUserID(), i, fisaparticipareID);
       }
       System.out.println("Test:"+test); 
       return true;
    }

    public boolean leaveCourse(int cursID) {
        if (fisaParticipantCursRepository.deleteFisaParticipantCurs(session.getUserID(), cursID)) {
            if (fisaParticipareRepository.deleteFisaParticipare(session.getUserID(), cursID)) {
                LOGGER.warning("Nu mai sunteti inscris la acest curs!");
                return true;

            }
            else
                return false;
        } else {

            LOGGER.warning("Nu ati fost inscris la acest curs!");
            return false;

        }
    }



    @Override
    public float[] viewGrades(int courseID) {
        return cursRepository.getGrades(session.getUserID(), courseID);

    }

    @Override
    public ArrayList<Curs> viewCourses(){
        return cursRepository.loadCoursebyName("", session.getUserID(), true);
    }
    @Override
    public ArrayList<Activitate> viewCourseActivities(int courseID){
    	return activitateRepository.viewActivities(session.getUserID(), courseID);
    }
    public boolean leaveGroup(int groupID) {

        if (membruGrupRepository.leaveGroup(groupID, session.getUserID()))
            return true;
        return false;

    }

    public boolean joinGroup(int groupID) {

        if (membruGrupRepository.joinGroup(groupID, session.getUserID()))
            return true;
        return false;

    }

    public ArrayList<Grup> viewGroups() {
        return grupRepository.viewGroups(session.getUserID());
    }

    public static void main(String[] args) {
        JDBConnectionWrapper jdbcWrapper = new JDBConnectionWrapper("studiu");
        ContextHolder c = new ContextHolderImpl();
        c.setJdbConnectionWrapper(jdbcWrapper);
        ActivitateRepository a = new ActivitateRepositoryImpl(jdbcWrapper);
        CursRepository cc = new CursRepositoryImpl(jdbcWrapper);
        UserRepository u = new UserRepositoryImpl(jdbcWrapper);
        ProfesorRepository p = new ProfesorRepositoryImpl(jdbcWrapper, u);
        StudentService s = new StudentServiceImpl(c);


    }

    public ArrayList<Grup> searchGroupsByCourse(int courseID,boolean isEnrolled){
        return grupRepository.getGroupsByCourse(session.getUserID(), courseID, isEnrolled);
    }

   /* @Override
    public int numberOfGroupMembers(int groupID) {
        return membruGrupRepository.getNrParticipants(groupID);
    }*/

    @Override
    public ArrayList<ActivitateGrup> viewGroupActivities(int grupID,boolean isEnrolled) {
        return activitateGrupRepository.getActivities(grupID,session.getUserID(),isEnrolled);
    }

    @Override
    public ArrayList<Student> viewGroupMembers(int groupID) {
        return membruGrupRepository.listParticipants(groupID);
    }

    @Override
    public ArrayList<Mesaj> viewGroupMessages(int groupID) {
        return mesajRepository.getMessages(groupID);
    }
    @Override
    public boolean joinGroupActivity(int activityID) {
    	return participantActivitateGrupRepository.joinActivity(session.getUserID(), activityID);
    }

    @Override
    public boolean invitaProfesor(int userID, int activitateID) {
        return profesoriInvitati.invitaProfesor(userID,activitateID);

    }

    @Override
   public  ArrayList<Activitate> viewActivities(String chosenDate){
        return activitateRepository.getStudentActivities(chosenDate,session.getUserID());

    }
    @Override
    public ArrayList<Student> groupMemberSuggestions(Grup g){
    	return this.fisaParticipareRepository.suggestedGroupMembers(g.getCursId(), g.getId());
    }

	@Override
	public ArrayList<Profesor> getGroupActivityTeachers(ActivitateGrup activitate) {
		return this.profesoriInvitati.getActivityParticipants(activitate, true);
	}

	@Override
	public ArrayList<Student> groupActivityStudents(int activityID) {
		return this.participantActivitateGrupRepository.getParticipants(activityID);
	}
	@Override 
	public ArrayList<Profesor> suggestedTeachers(ActivitateGrup activitate){
		return this.profesoriInvitati.getActivityParticipants(activitate, false);
	}
}



