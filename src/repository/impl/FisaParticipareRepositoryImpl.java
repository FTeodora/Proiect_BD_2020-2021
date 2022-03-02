package repository.impl;
import model.entity.FisaParticipare;
import model.entity.MembruGrup;
import model.entity.Student;
import repository.FisaParticipareRepository;

import java.sql.*;
import java.util.ArrayList;

public class FisaParticipareRepositoryImpl implements FisaParticipareRepository {
	JDBConnectionWrapper jdbconnectionWrapper;

	public FisaParticipareRepositoryImpl(JDBConnectionWrapper jdbconnectionWrapper) {
		this.jdbconnectionWrapper = jdbconnectionWrapper;
	}


	@Override
	public int createFisaParticipare(int studentID, int courseID) {
		Connection connection = jdbconnectionWrapper.getConnection();
		try {

			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO fisa_participare (ID_curs,user_id) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, courseID);
			preparedStatement.setInt(2, studentID);
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if(resultSet.next()) {
				return resultSet.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public boolean deleteFisaParticipare(int studentID,int courseID) {
		Connection connection = jdbconnectionWrapper.getConnection();
		try {

			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM fisa_participare WHERE user_id=? AND ID_curs=?");
			preparedStatement.setInt(1, studentID);
			preparedStatement.setInt(2,courseID);
			int updatedRows = preparedStatement.executeUpdate();
			System.out.println(updatedRows);
			return updatedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public ArrayList<Student> getCourseParticipants(int courseID){
		ArrayList<Student> rez=null;
		Connection connection = jdbconnectionWrapper.getConnection();
		try {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT users.ID,nume,prenume,an,fisa_participare.ID "
					+ "FROM users,student "
					+ "INNER JOIN fisa_participare "
					+ "ON student.user_id=fisa_participare.user_id AND fisa_participare.id_curs=? "
					+ "WHERE users.id=student.user_id;");
			preparedStatement.setInt(1, courseID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				rez=new ArrayList<Student>();
				FisaParticipare s=new FisaParticipare();
				s.setId(resultSet.getInt(1));
				s.setNume(resultSet.getString(2));
				s.setPrenume(resultSet.getString(3));
				s.setAn(resultSet.getInt(4));
				s.setIdParticipare(resultSet.getInt(5));
				rez.add(s);
				while(resultSet.next()) {
					s=new FisaParticipare();
					s.setId(resultSet.getInt(1));
					s.setNume(resultSet.getString(2));
					s.setPrenume(resultSet.getString(3));
					s.setAn(resultSet.getInt(4));
					s.setIdParticipare(resultSet.getInt(5));
					rez.add(s);
				}
			}


		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return rez;
	}
	@Override
	public ArrayList<Student> getCourseParticipantsByOrganizer(int courseID,int organizerID){
		ArrayList<Student> rez=null;
		Connection connection = jdbconnectionWrapper.getConnection();
		try {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT  users.ID,nume,prenume,an,participant_curs.id_fisa_participare,CNP "
					+ "      FROM users,student,participant_curs"
					+ "      WHERE users.id=student.user_id"
					+ "      AND student.user_id=participant_curs.user_id"
					+ "      AND participant_curs.ID_activitate IN"
					+ "      (SELECT activitate.ID_activitate  from activitate"
					+ "      JOIN organizator"
					+ "      ON organizator.ID_activitate=activitate.ID_activitate"
					+ "      WHERE activitate.ID_curs=? AND organizator.profesor_principal=?)");
			preparedStatement.setInt(1, courseID);
			preparedStatement.setInt(2, organizerID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				rez=new ArrayList<Student>();
				FisaParticipare s=new FisaParticipare();
				s.setId(resultSet.getInt(1));
				s.setNume(resultSet.getString(2));
				s.setPrenume(resultSet.getString(3));
				s.setAn(resultSet.getInt(4));
				s.setIdParticipare(resultSet.getInt(5));
				s.setCNP(resultSet.getString(6));
				s.setCursId(courseID);
				rez.add(s);
				while(resultSet.next()) {
					s=new FisaParticipare();
					s.setId(resultSet.getInt(1));
					s.setNume(resultSet.getString(2));
					s.setPrenume(resultSet.getString(3));
					s.setAn(resultSet.getInt(4));
					s.setIdParticipare(resultSet.getInt(5));
					s.setCNP(resultSet.getString(6));
					s.setCursId(courseID);
					rez.add(s);
				}
			}


		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return rez;
	}
	@Override
	public int getFisaParticipareID(int studentID, int courseID) {
		Connection connection = jdbconnectionWrapper.getConnection();
		int indx=-1;
		try {

			PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM fisa_participare WHERE user_id=? AND ID_curs=?;");
			preparedStatement.setInt(1, studentID);
			preparedStatement.setInt(2, courseID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				indx=resultSet.getInt(1);
				return indx;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return indx;
	}
	@Override
	public ArrayList<Student> suggestedGroupMembers(int courseID,int groupID){
		ArrayList<Student> users=null;
		Connection connection = jdbconnectionWrapper.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT users.ID,nume,prenume,an from users,fisa_participare,student\r\n"
					+ "WHERE fisa_participare.ID_curs=? "
					+ "AND users.ID=fisa_participare.user_ID "
					+ "AND users.ID=student.user_ID "
					+ "AND NOT EXISTS(SELECT * "
					+ "			FROM membru_grup "
					+ "            WHERE membru_grup.user_ID=fisa_participare.user_ID"
					+ "            AND ID_grup=?)");
			preparedStatement.setInt(1, courseID);
			preparedStatement.setInt(2, groupID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				users=new ArrayList<Student>();
				FisaParticipare user=new FisaParticipare();
				user.setId(resultSet.getInt(1));
				user.setNume(resultSet.getString(2));
				user.setPrenume(resultSet.getString(3));
				user.setAn(resultSet.getInt(4));
				users.add(user);
				while (resultSet.next())
				{
					user=new FisaParticipare();
					user.setId(resultSet.getInt(1));
					user.setNume(resultSet.getString(2));
					user.setPrenume(resultSet.getString(3));
					user.setAn(resultSet.getInt(4));
					users.add(user);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
}

