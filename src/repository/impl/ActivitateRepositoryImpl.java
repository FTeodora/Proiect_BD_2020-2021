package repository.impl;

import model.entity.Activitate;
import model.entity.Organizator;
import model.entity.Profesor;

import repository.ActivitateRepository;
import java.sql.*;
import java.util.ArrayList;

public  class ActivitateRepositoryImpl implements ActivitateRepository {
    private final JDBConnectionWrapper jdbconnectionWrapper;
    public ActivitateRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbconnectionWrapper = jdbConnectionWrapper;
    }

    public ArrayList<Activitate> viewActivities(int studentID,int courseID) {
        Connection connection = jdbconnectionWrapper.getConnection();

        ArrayList<Activitate> activities=new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from activitate \n" +
                    "JOIN participant_curs \n" +
                    "ON participant_curs.ID_activitate=activitate.ID_activitate\n" +
                    "WHERE activitate.ID_curs=?\n" +
                    "AND participant_curs.user_id=?;");

            preparedStatement.setInt(1, courseID);
            preparedStatement.setInt(2,studentID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Activitate activitate=Activitate.parseActivitate(resultSet);
                activitate.setActivityTeacher(activityTeacher(activitate.getId()));
                activities.add(activitate);
            }


            return activities;


        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getMinParticipantsCourse(int courseID) {
        Connection connection = jdbconnectionWrapper.getConnection();
        int teacherID=-1;
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT o.profesor_principal,IFNULL(nr_participanti(act.ID_activitate),0) as nr FROM organizator o\r\n"
            		+ "	INNER JOIN activitate act ON act.ID_activitate=o.ID_activitate AND act.ID_curs=?\r\n"
            		+ " GROUP BY o.profesor_principal\r\n"
            		+ "  ORDER BY nr ASC LIMIT 1");

            preparedStatement.setInt(1, courseID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
               teacherID = resultSet.getInt(1);
            }

            return teacherID;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return teacherID;
    }

    @Override
    public ArrayList<Activitate> getPossibleActivitiesByType(String activityType, int profesorPrincipalID,int courseID) {
        Connection connection = jdbconnectionWrapper.getConnection();

        ArrayList<Activitate> activities=new ArrayList<>();
        try{


            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM activitate\n" +
                    "JOIN organizator ON\n" +
                    "activitate.ID_activitate=organizator.ID_activitate\n" +
                    "AND organizator.profesor_principal=?\n" +
                    "AND activitate.ID_curs=?\n" +
                    "AND activitate.tip=?" +
                    "ORDER BY nr_participanti(activitate.ID_activitate);");

            preparedStatement.setInt(1, profesorPrincipalID);
            preparedStatement.setInt(2,courseID);
            preparedStatement.setString(3,activityType);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
               Activitate activitate=Activitate.parseActivitate(resultSet);
                activities.add(activitate);

            }

            return activities;

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getActivityID(int organizatorID,int courseID,String tipActivitate) {
        Connection connection = jdbconnectionWrapper.getConnection();
        int id = -1;
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT activitate.tip,activitate.ID_activitate\n" +
                    "FROM activitate\n" +
                    "JOIN organizator\n" +
                    "ON activitate.ID_activitate=organizator.ID_activitate\n" +
                    "WHERE organizator.user_id=? AND activitate.ID_curs=? AND activitate.tip=? ;");
            preparedStatement.setInt(1, organizatorID);
            preparedStatement.setInt(2,courseID);
            preparedStatement.setString(3,tipActivitate);
        ;
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                id = resultSet.getInt(2);
                return id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    public int isEnrolledInActivity(int studentID,int courseID){
        Connection connection = jdbconnectionWrapper.getConnection();
        int id = -1;
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT activitate.ID_activitate FROM participant_curs\n" +
                    "RIGHT JOIN activitate\n" +
                    "ON activitate.ID_activitate=participant_curs.ID_activitate\n" +
                    "WHERE activitate.ID_curs=?\n" +
                    "AND activitate.tip='LABORATOR'\n" +
                    "AND participant_curs.user_id=?\n" +
                    "GROUP BY user_id;");
            preparedStatement.setInt(1, courseID);
            preparedStatement.setInt(2,studentID);


            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                id = resultSet.getInt(1);
                return id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;

    }


    public ArrayList<Profesor> laboratoryTeachers(int courseID) {
        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            ArrayList<Profesor> laboratoryTeachers=new ArrayList<Profesor>();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT users.nume,users.prenume,users.id FROM users\n" +
                    "JOIN organizator\n" +
                    "ON users.id=organizator.user_id\n" +
                    "WHERE ID_activitate IN(SELECT ID_activitate FROM activitate WHERE tip='laborator'\n" +
                    "AND ID_curs=?);");
            preparedStatement.setInt(1, courseID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {

                Profesor profesor=new Profesor();
                profesor.setId(resultSet.getInt(3));
                profesor.setNume(resultSet.getString(1));
                profesor.setPrenume(resultSet.getString(2));
                laboratoryTeachers.add(profesor);

            }
            return laboratoryTeachers;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Profesor activityTeacher(int activityID) {
        Connection connection = jdbconnectionWrapper.getConnection();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("\n" +
                            "SELECT users.nume,users.prenume,users.id FROM users\n" +
                            "JOIN organizator\n" +
                            "ON users.id=organizator.user_id\n" +
                            "WHERE ID_activitate=?;");
            preparedStatement.setInt(1, activityID);


            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {


                Profesor profesor=new Profesor();
;               profesor.setId(resultSet.getInt(3));
                profesor.setNume(resultSet.getString(1));
                profesor.setPrenume(resultSet.getString(2));
                return profesor;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getNrParticipants(int activityID) {
        int nr=0;
        try {
            Connection connection = jdbconnectionWrapper.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) "
                    + "FROM participant_curs p"
                    +" INNER JOIN activitate act ON p.ID_activitate=act.ID_activitate "
                    + " AND act.ID_activitate=?");
            preparedStatement.setInt(1, activityID);
            ResultSet resultSet = preparedStatement.executeQuery();


            if(resultSet.next()) {
                nr=resultSet.getInt(1);
                return nr;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nr;
    }

    @Override
    public ArrayList<Activitate> getTeacherActivities(int userID, int cursID) {
        ArrayList<Activitate> activitati=new ArrayList<Activitate>();
        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * from activitate\n" +
                            "JOIN organizator\n" +
                            "ON activitate.ID_activitate =organizator.ID_activitate\n" +
                            "AND (organizator.user_id=? OR organizator.profesor_principal=?)\n" +
                            "WHERE activitate.ID_curs=?;");
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, userID);
            preparedStatement.setInt(3, cursID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Activitate a=Activitate.parseActivitate(resultSet);
                a.setActivityTeacher(activityTeacher(a.getId()));
                activitati.add(a);
            }
            return activitati;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean createActivity(Activitate a) {
        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO activitate(ID_curs,tip,data_inceperii,data_incheierii,frecventa,durata,nr_maxim_participanti,pondere) VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,a.getCursId());
            preparedStatement.setString(2,a.getTipActivitate().toString());
            preparedStatement.setString(3,a.getDataInceperii().toString());
            preparedStatement.setString(4,a.getDataIncheierii().toString());
            preparedStatement.setString(5,a.getFrecventa().toString());
            preparedStatement.setString(6,a.getDurata());
            preparedStatement.setInt(7,a.getNrMaxParticipanti());
            preparedStatement.setInt(8,a.getPondere());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                a.setId(resultSet.getInt(1));
                PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES(?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                preparedStatement1.setInt(1,a.getOrganizator().getProfesorPrincipal());
                preparedStatement1.setInt(2,a.getOrganizator().getUserId());
                preparedStatement1.setInt(3,a.getId());
                preparedStatement1.execute();
                ResultSet resultSet1 = preparedStatement1.getGeneratedKeys();
                if (resultSet1.next()) {

                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean studentActivitiesCollideWith(int studentID,Activitate activitate) {
         Connection connection = jdbconnectionWrapper.getConnection();
         try {
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM activitate act "
                     + "INNER JOIN participant_curs p ON act.ID_activitate=p.ID_activitate "
                     + "AND p.user_ID=? "
                     + "WHERE days_collide(data_inceperii,?,frecventa) "
                     + "AND times_collide(data_inceperii,durata,?,?) ");
             preparedStatement.setInt(1, studentID);
             preparedStatement.setString(2, activitate.getDataInceperii());
             preparedStatement.setString(3, activitate.getDataInceperii());
             preparedStatement.setString(4, activitate.getDurata());

             ResultSet resultSet = preparedStatement.executeQuery();
             if (resultSet.next()) 
            	 return true;
         }
         catch (SQLException e) {
             e.printStackTrace();
         }
         return false;
    }
	@Override
    public boolean activitiesCollide(Activitate activitate1,Activitate activitate2) {
		
		if(activitate1==null || activitate2==null)
			return true;
         Connection connection = jdbconnectionWrapper.getConnection();
         try {
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT days_collide(?,?,?) "
                     + "AND times_collide(?,?,?,?) ");
             preparedStatement.setString(1, activitate1.getDataInceperii());
             preparedStatement.setString(2, activitate2.getDataInceperii());
             if(activitate1.getFrecventa().ordinal()<activitate2.getFrecventa().ordinal())
            	 preparedStatement.setString(3, activitate1.getFrecventa().toString());
             else
            	 preparedStatement.setString(3, activitate2.getFrecventa().toString());
             preparedStatement.setString(4, activitate1.getDataInceperii());
             preparedStatement.setString(5, activitate1.getDurata());
             preparedStatement.setString(6, activitate2.getDataInceperii());
             preparedStatement.setString(7, activitate2.getDurata());
             ResultSet resultSet = preparedStatement.executeQuery();
             if (resultSet.next()) 
            	 return resultSet.getBoolean(1);
         }
         catch (SQLException e) {
             e.printStackTrace();
         }
         return false;
    }

    @Override
    public ArrayList<Activitate> getStudentActivities(String chosenDate,int studentID) {
        ArrayList<Activitate> activitati=new ArrayList<Activitate>();
        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM activitate \n" +
                            "JOIN participant_curs ON activitate.ID_activitate=participant_curs.ID_activitate AND participant_curs.user_id=?\n" +
                            "AND days_collide(activitate.data_inceperii,?,activitate.frecventa)"+
                            " AND ? BETWEEN data_inceperii AND data_incheierii"+
                            " ORDER BY DATE_FORMAT(data_inceperii,'%H:%i')");
           
            preparedStatement.setInt(1,studentID);
            preparedStatement.setString(2,chosenDate);
            preparedStatement.setString(3,chosenDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Activitate a=Activitate.parseActivitate(resultSet);

                PreparedStatement preparedStatement1 = connection.prepareStatement(
                        "SELECT curs.materie FROM curs WHERE ID_curs=?");
                preparedStatement1.setInt(1,a.getCursId());
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                if(resultSet1.next())
                	a.setCourseName(resultSet1.getString(1));
                activitati.add(a);

            }
            return activitati;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Activitate> getTeacherActivities(String chosenDate,int teacherID) {

        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            ArrayList<Activitate> activitati=new ArrayList<Activitate>();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM activitate \n" +
                            "JOIN organizator ON activitate.ID_activitate=organizator.ID_activitate AND organizator.user_id=?\n" +
                            "AND days_collide(activitate.data_inceperii,?,activitate.frecventa)"
                            + " AND ? BETWEEN data_inceperii AND data_incheierii"+
                            " ORDER BY DATE_FORMAT(data_inceperii,'%H:%i')");
            
            preparedStatement.setInt(1,teacherID);
            preparedStatement.setString(2,chosenDate);
            preparedStatement.setString(3,chosenDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Activitate a=Activitate.parseActivitate(resultSet);

                PreparedStatement preparedStatement1 = connection.prepareStatement(
                        "SELECT curs.materie FROM curs WHERE ID_curs=?;");
                
                preparedStatement1.setInt(1,a.getCursId());
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                if(resultSet1.next())
                	a.setCourseName(resultSet1.getString(1));
                activitati.add(a);

            }
            return activitati;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Activitate> getTimetable(String chosenDate) {
        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            ArrayList<Activitate> activitati=new ArrayList<Activitate>();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM activitate \n" +
                            " WHERE days_collide(activitate.data_inceperii,?,activitate.frecventa)"
                            + " AND ? BETWEEN data_inceperii AND data_incheierii"+
                            " ORDER BY DATE_FORMAT(data_inceperii,'%H:%i')"
                            );
            preparedStatement.setString(1,chosenDate);
            preparedStatement.setString(2,chosenDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Activitate a=Activitate.parseActivitate(resultSet);
                PreparedStatement preparedStatement1 = connection.prepareStatement(
                        "SELECT curs.materie FROM curs WHERE ID_curs=?;");
                preparedStatement1.setInt(1,a.getCursId());
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                if(resultSet1.next())
                	a.setCourseName(resultSet1.getString(1));
                activitati.add(a);
            }
            return activitati;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ArrayList<Activitate> getActivitiesforCourse(int cursID){
    	ArrayList<Activitate> activitati=new ArrayList<Activitate>();
        Connection connection = jdbconnectionWrapper.getConnection();
        try{
        	PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM activitate WHERE ID_curs=?;");
            preparedStatement.setInt(1, cursID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
            	Activitate a=Activitate.parseActivitate(resultSet);
            	 a.setActivityTeacher(activityTeacher(a.getId()));
                activitati.add(a);
                }
            return activitati;
            }
        catch (SQLException e){
        	e.printStackTrace();
        }
        return null;
    }
/*
    @Override
    public ArrayList<ActivityType> getTypesOfActivities(int courseID) {
        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            ArrayList<ActivityType> activityTypes=new ArrayList<ActivityType>();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT activitate.tip FROM activitate\n" +
                    "                    JOIN curs ON activitate.ID_curs=curs.ID_curs\n" +
                    "                    WHERE curs.ID_curs=?\n" +
                    "                    GROUP BY activitate.tip;");
            preparedStatement.setInt(1, courseID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String tipActivitate=resultSet.getString(1);
                if(tipActivitate.equals("laborator")) {
                    activityTypes.add(ActivityType.LABORATOR);
                }
                if(tipActivitate.equals("curs")) {
                    activityTypes.add(ActivityType.CURS);
                }
                    if(tipActivitate.equals("seminar")) {
                    activityTypes.add(ActivityType.SEMINAR);
                }
            }
            return activityTypes;
        }
         catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
}
*/


}
