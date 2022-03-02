package repository.impl;

import repository.MembruGrupRepository;

import java.sql.*;
import java.util.ArrayList;

import model.entity.MembruGrup;
import model.entity.Student;
import model.entity.User;

public class MembruGrupRepositoryImpl implements MembruGrupRepository{

    private final JDBConnectionWrapper jdbConnectionWrapper;
	public MembruGrupRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
            this.jdbConnectionWrapper = jdbConnectionWrapper;
        }

    public boolean joinGroup(int groupID,int studentID){
        Connection connection = jdbConnectionWrapper.getConnection();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO membru_grup(ID_grup,user_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, groupID);
            preparedStatement.setInt(2, studentID);
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
    public boolean leaveGroup(int groupID, int studentID) {
        Connection connection = jdbConnectionWrapper.getConnection();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM membru_grup WHERE ID_grup=? AND user_id=?", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, groupID);
            preparedStatement.setInt(2, studentID);

            int updatedRows = preparedStatement.executeUpdate();

            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
	public ArrayList<Student> listParticipants(int groupID) {
		ArrayList<Student> users=null;
		Connection connection = jdbConnectionWrapper.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT users.ID,nume,prenume,student.an,ID_grup,membru_grup.ID"
					+ " FROM users,student,membru_grup \n" +
					"WHERE ID_grup=? AND membru_grup.user_ID=users.ID AND users.ID=student.user_ID");
			preparedStatement.setInt(1, groupID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				users=new ArrayList<Student>();
				MembruGrup user=new MembruGrup();
				user.setId(resultSet.getInt(1));
				user.setNume(resultSet.getString(2));
				user.setPrenume(resultSet.getString(3));
				user.setAn(resultSet.getInt(4));
				user.setGrupId(resultSet.getInt(5));
				users.add(user);
				while (resultSet.next())
				{
					user=new MembruGrup();
					user.setId(resultSet.getInt(1));
					user.setNume(resultSet.getString(2));
					user.setPrenume(resultSet.getString(3));
					user.setAn(resultSet.getInt(4));
					user.setGrupId(resultSet.getInt(5));
					users.add(user);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	@Override
	public int getNrParticipants(int groupID) {
		int nr=0;
		Connection connection = jdbConnectionWrapper.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM membru_grup\n" +
					"WHERE ID_grup=?;");
			preparedStatement.setInt(1, groupID);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				nr=resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nr;
	}
	
}
