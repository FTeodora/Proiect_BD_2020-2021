package service.impl;

import model.entity.*;
import repository.NotificareRepository;
import repository.UserRepository;
import repository.impl.JDBConnectionWrapper;
import repository.impl.NotificareRepositoryImpl;
import repository.impl.UserRepositoryImpl;
import service.ContextHolder;
import service.UserService;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    protected ContextHolder session;
	protected final UserRepository userRepository;
	protected final NotificareRepository notificareRepository;
	public UserServiceImpl(JDBConnectionWrapper connection) { //nu retine un user specific; pentru pagina de dinainte de logare
		userRepository=new UserRepositoryImpl(connection);
		this.session=new ContextHolderImpl();
		this.session.setJdbConnectionWrapper(connection);
		notificareRepository=new NotificareRepositoryImpl(connection);

	}
	public UserServiceImpl(ContextHolder session) { //pentru pagina de dupa logare
		this.session=session;
		userRepository=new UserRepositoryImpl(this.session.getJdbConnectionWrapper());
        notificareRepository=new NotificareRepositoryImpl(this.session.getJdbConnectionWrapper());
	}
	@Override
	public ContextHolder getSession() {
		return this.session;
	}
    @Override
    public User login(String username, String password) {
        User user = userRepository.loadByUserName(username);
        if(user != null) {
            if(Objects.equals(user.getParola(), password)) {
                return user;
            } else {
                LOGGER.warning("Wrong password for user " + username);
            }
        } else {
            LOGGER.warning("User with username: " + username + " was not found");
        }
        return null;
    }

    @Override

    public User viewPersonalData() {
        return userRepository.loadByUserName(session.getSessionName());

    }
    @Override
    public ArrayList<Activitate> viewActivities(String selectedDate){
    	return null;
    }
    @Override
    public ArrayList<Curs> viewCourses(){
    	return null;
    }
    @Override
    public ArrayList<Activitate> viewCourseActivities(int courseID){
    	return null;
    }
    @Override
    public Connection getConnection() {
    	return session.getJdbConnectionWrapper().getConnection();
    }

    @Override
    public boolean sendNotification(Notificare notificare) {
        return notificareRepository.sendNotification(notificare);
    }

    @Override
    public ArrayList<Student> viewCourseStudents(int courseID){
    	return null;
    }
    @Override
    public ArrayList<Profesor> viewCourseTeachers(int courseID){
    	return null;
    }
   /* @Override
    public String login(String username, String password) {
        if(username.equals(""))
        return "Campul username este gol!";
        if(password.equals(""))
            return "Campul password este gol!";
        User user = userRepository.loadByUserName(username);
        if(user==null)
           return "Utilizatorul nu exista sau username incorect!";
        else {

            if (user.getParola().equals(password))
                return "Succes!";
            else
                return "Parola incorecta!";
        }
    }*/
	@Override
	public ArrayList<Notificare> viewNotifications() {
		return this.notificareRepository.getNotifications(session.getUserID());
	}


}