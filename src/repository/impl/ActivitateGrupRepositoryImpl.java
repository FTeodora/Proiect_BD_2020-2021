package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.entity.ActivitateGrup;
import repository.ActivitateGrupRepository;

public class ActivitateGrupRepositoryImpl implements ActivitateGrupRepository{

	private final JDBConnectionWrapper jdbConnectionWrapper;
	public ActivitateGrupRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
		this.jdbConnectionWrapper = jdbConnectionWrapper;
	}

	@Override
	public ArrayList<ActivitateGrup> getActivities(int groupID) {
		ArrayList<ActivitateGrup> grupuri=new ArrayList<ActivitateGrup>();
		Connection connection = jdbConnectionWrapper.getConnection();
		try{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * "
																+ " FROM activitati_grup"
																+ " WHERE ID_grup=? AND NOW()<data_incepere");
			preparedStatement.setInt(1, groupID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				grupuri.add(ActivitateGrup.parseActivitate(resultSet,connection));
				grupuri.get(grupuri.size()-1).setEnrolled(false);
			}
			return grupuri;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public ArrayList<ActivitateGrup> getActivities(int groupID,int studentID,boolean isEnrolled) {
		ArrayList<ActivitateGrup> grupuri=new ArrayList<ActivitateGrup>();
		Connection connection = jdbConnectionWrapper.getConnection();
		try{
			PreparedStatement preparedStatement;
			if(isEnrolled)
				preparedStatement= connection.prepareStatement("SELECT * FROM activitati_grup "
																+ "INNER JOIN participant_grup "
																+ "ON participant_grup.ID_activitate=activitati_grup.ID_activitate "
																+ "AND participant_grup.user_ID=? "
																+ "AND activitati_grup.ID_grup=? AND NOW()<data_incepere");
			else
				preparedStatement= connection.prepareStatement("SELECT * FROM activitati_grup "
						+ "WHERE NOT EXISTS ("
						+ "SELECT ID_activitate FROM participant_grup "
						+ "WHERE participant_grup.user_ID=?"
						+ " AND participant_grup.ID_activitate=activitati_grup.ID_activitate)"
						+ " AND ID_grup=? AND NOW()<data_incepere");
			preparedStatement.setInt(1, studentID);
			preparedStatement.setInt(2, groupID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				grupuri.add(ActivitateGrup.parseActivitate(resultSet,connection));
				grupuri.get(grupuri.size()-1).setEnrolled(isEnrolled);
			}
			return grupuri;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
