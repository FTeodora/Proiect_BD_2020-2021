package repository.impl;

import repository.ParticipantActivitateGrupRepository;

import java.sql.*;
import java.util.ArrayList;

import model.entity.ParticipantGrup;
import model.entity.Student;

public class ParticipantActivitateGrupRepositoryImpl implements ParticipantActivitateGrupRepository {
    JDBConnectionWrapper jdbconnectionWrapper;

    public ParticipantActivitateGrupRepositoryImpl(JDBConnectionWrapper jdbconnectionWrapper) {
        this.jdbconnectionWrapper = jdbconnectionWrapper;
    }

    @Override
    public boolean joinActivity(int studentID, int activitategrupID) {
        Connection connection = jdbconnectionWrapper.getConnection();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO participant_grup(user_id,ID_activitate) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, studentID);
            preparedStatement.setInt(2, activitategrupID);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public ArrayList<Student> getParticipants(int activitateID){
    		ArrayList<Student> rez=null;
    		Connection connection = jdbconnectionWrapper.getConnection();
    		try {

    			PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT users.ID,nume,prenume,an "
    					+ "FROM users,student "
    					+ "INNER JOIN participant_grup "
    					+ "ON student.user_id=participant_grup.user_id AND participant_grup.id_activitate=? "
    					+ "WHERE users.id=student.user_id");
    			preparedStatement.setInt(1, activitateID);
    			ResultSet resultSet = preparedStatement.executeQuery();
    			if(resultSet.next())
    			{
    				rez=new ArrayList<Student>();
    				ParticipantGrup s=new ParticipantGrup();
    				s.setId(resultSet.getInt(1));
    				s.setNume(resultSet.getString(2));
    				s.setPrenume(resultSet.getString(3));
    				s.setAn(resultSet.getInt(4));
    				rez.add(s);
    				while(resultSet.next()) {
    					s=new ParticipantGrup();
    					s.setId(resultSet.getInt(1));
    					s.setNume(resultSet.getString(2));
    					s.setPrenume(resultSet.getString(3));
    					s.setAn(resultSet.getInt(4));
    					rez.add(s);
    				}
    			}


    		} catch (SQLException e) {
    			e.printStackTrace();
    			return null;
    		}
    		return rez;
    }
}
