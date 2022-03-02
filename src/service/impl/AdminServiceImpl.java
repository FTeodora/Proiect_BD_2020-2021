package service.impl;
import model.entity.*;
import model.enumeration.UserRole;
import repository.*;
import repository.impl.*;
import service.AdminService;
import service.ContextHolder;
import java.util.ArrayList;
import java.util.logging.Logger;
public class AdminServiceImpl extends UserServiceImpl implements AdminService {
    private static final Logger LOGGER = Logger.getLogger(AdminServiceImpl.class.getName());
    private final StudentRepository studentRepository;
    private final ProfesorRepository profesorRepository;
    private final CursRepository cursRepository;
    private final FisaParticipareRepository fisaParticipareRepository;
    private final FisaParticipantCursRepository fisaParticipantCursRepository;
    private final FisaCursRepository fisaCursRepository;
    private final ActivitateRepository activitateRepository;
    private final GrupRepository grupRepository;
    private final MembruGrupRepository membruGrupRepository;
    private final ActivitateGrupRepository activitateGrupRepository;
    private final MesajRepository mesajRepository;
    private final ParticipantActivitateGrupRepository participantActivitateGrupRepository;
    private final ProfesoriInvitatiRepository profesoriInvitati;
    public AdminServiceImpl(ContextHolder session) {
        super(session);
    	this.studentRepository = new StudentRepositoryImpl(this.session.getJdbConnectionWrapper(),this.userRepository);
        this.profesorRepository = new ProfesorRepositoryImpl(this.session.getJdbConnectionWrapper(),this.userRepository);
        this.cursRepository=new CursRepositoryImpl(this.session.getJdbConnectionWrapper());
        fisaParticipareRepository=new FisaParticipareRepositoryImpl(this.session.getJdbConnectionWrapper());
        fisaParticipantCursRepository=new FisaParticipantCursRepositoryImpl(this.session.getJdbConnectionWrapper());
        fisaCursRepository=new FisaCursRepositoryImpl(this.session.getJdbConnectionWrapper());
        activitateRepository=new ActivitateRepositoryImpl((this.session.getJdbConnectionWrapper()));
        grupRepository=new GrupRepositoryImpl((this.session.getJdbConnectionWrapper()));
        membruGrupRepository = new MembruGrupRepositoryImpl(session.getJdbConnectionWrapper());
        activitateGrupRepository = new ActivitateGrupRepositoryImpl(session.getJdbConnectionWrapper());
        mesajRepository=new MesajRepositoryImpl(session.getJdbConnectionWrapper());
        this.participantActivitateGrupRepository=new ParticipantActivitateGrupRepositoryImpl(session.getJdbConnectionWrapper());
        this.profesoriInvitati=new ProfesoriInvitatiRepositoryImpl(session.getJdbConnectionWrapper());
    }

    @Override
    public boolean createStudent(Student student) {
        if(!studentRepository.createStudent(student)) {
            LOGGER.warning("Studentul nu a putut fi inserat!");
            return false;
        }
        else {
            LOGGER.warning("Inserarea s-a efectuat cu succes!");
            return true;
        }
    }
    @Override
    public boolean updateStudent(Student student) {
        return studentRepository.updateStudent(student);
    }

    @Override
    public boolean deleteStudent(String userName) {
        if(!studentRepository.deleteStudent(userName)) {
            LOGGER.warning("Studentul nu exista sau a fost sters!");
            return false;
        }
        else {
            LOGGER.warning("Stergerea s-a efectuat cu succes!!");
            return true;
        }
    }
    @Override
    public boolean createProfesor(Profesor profesor) {
        if(!profesorRepository.createProfesor(profesor)) {
            LOGGER.warning("Profesorul nu a putut fi inserat!");
            return false;
        }
        else {
            LOGGER.warning("Inserarea s-a efectuat cu succes!");
            return true;
        }
    }
    @Override
    public boolean updateProfesor(Profesor profesor) {
        return profesorRepository.updateProfesor(profesor);
    }

    @Override
    public boolean deleteProfesor(String userName) {
        if(!profesorRepository.deleteProfesor(userName)) {
            LOGGER.warning("Profesorul nu a putut fi sters");
            return false;
        }
        else {
            LOGGER.warning("Stergerea s-a efectuat cu succes!!");
            return true;
        }
    }
    @Override
    public ArrayList<User> findAllUsersforAdmin(String type_user) {
        ArrayList<User> useri=userRepository.findUsers(type_user);
        return useri;
    }
    @Override
    public ArrayList<User> findStudentsbyYearforAdmin(int an) {
        ArrayList<User> useri=userRepository.findStudentsbyYear(an);
        return useri;
    }
    @Override
    public ArrayList<Profesor> findTeachersbyDepartamentforAdmin(String nume_departament,int courseID) {
        ArrayList<Profesor> useri=fisaCursRepository.findTeachersbyDepartament(nume_departament,courseID);
        return useri;
    }
	@Override
    public ArrayList<Profesor>  getOtherTeachersForCourse(int cursID){
    	return fisaCursRepository.notCourseTeachers(cursID);
    }
    @Override
    public ArrayList<Curs> viewCourses(){
    	return cursRepository.loadAllCoursesbyName("");
    }
    @Override
    public ArrayList<Curs> searchCourse(String courseName) {
        return cursRepository.loadAllCoursesbyName(courseName);
    }
    @Override
    public ArrayList<Student> viewCourseStudents(int courseID) {
        return fisaParticipareRepository.getCourseParticipants(courseID);
    }
    @Override
    public  ArrayList<Profesor> viewCourseTeachers(int courseID){
        return fisaCursRepository.getCourseTeachers(courseID);
    }
    @Override
    public  ArrayList<Activitate> viewActivities(String chosenDate){
        return activitateRepository.getTimetable(chosenDate);
    }
    @Override
    public  ArrayList<Activitate> viewCourseActivities(int cursID){
        return activitateRepository.getActivitiesforCourse(cursID);
    }
    @Override
    public boolean createActivity(Activitate a) {
    	return activitateRepository.createActivity(a);
    }
	@Override
	public ArrayList<Student> getActivityParticipants(int IDactivitate) {
		return fisaParticipantCursRepository.getActivityParticipants(IDactivitate);
	}
	@Override
	public ArrayList<Grup> viewGroups() {
		return grupRepository.toateGrupurile();
	}
	@Override
	public ArrayList<Grup> searchGroupsByCourse(int courseID) {
		return grupRepository.cautaGrupuri(courseID);
	}
	 @Override
	    public ArrayList<ActivitateGrup> viewGroupActivities(int grupID) {
	        return activitateGrupRepository.getActivities(grupID);
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
	 public boolean kickGroup(int groupID,int studentID) {
		 return membruGrupRepository.leaveGroup(groupID, studentID);
	 }

	@Override
	public ArrayList<User> findUsersByName(String nume, String prenume) {
		return userRepository.findUsersbyName(nume, prenume, (session.getSessionType().ordinal()+1));
	}

	@Override
	public boolean assignTeacher(int courseID, int teacherID) {
		if(fisaCursRepository.createFisa(courseID, teacherID)==-1)
			return false;
		return true;
	}
	@Override
	public ArrayList<Profesor> getGroupActivityTeachers(ActivitateGrup activitate) {
		return this.profesoriInvitati.getActivityParticipants(activitate, true);
	}

	@Override
	public ArrayList<Student> groupActivityStudents(int activityID) {
		return this.participantActivitateGrupRepository.getParticipants(activityID);
	}

}