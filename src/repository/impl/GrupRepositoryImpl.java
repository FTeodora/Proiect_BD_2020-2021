package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.entity.Curs;
import model.entity.Grup;
import model.entity.MembruGrup;
import model.entity.Student;
import model.entity.User;
import repository.GrupRepository;

import static model.entity.Grup.parseGrup;

public class GrupRepositoryImpl implements GrupRepository{
	private final JDBConnectionWrapper jdbConnectionWrapper;
	public GrupRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
		this.jdbConnectionWrapper = jdbConnectionWrapper;
	}
	@Override
	public ArrayList<Grup> enrolledGroups(int studentID) {
		ArrayList<Grup> rez=null;
		Connection connection = jdbConnectionWrapper.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM grup "
					+ "INNER JOIN membru_grup "
					+ "ON grup.ID_grup=membru_grup.ID_grup "
					+ "AND membru_grup.ID=?");
			preparedStatement.setInt(1, studentID);
			preparedStatement.executeUpdate();
			ResultSet rst=preparedStatement.getResultSet();
			if(rst.next()) {
				rez=new ArrayList<Grup>();
				rez.add(parseGrup(rst, connection));
				while(rst.next())
					rez.add(parseGrup(rst, connection));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rez;
	}
	@Override
	public ArrayList<Grup> getGroupsByCourse(int studentID,int courseID,boolean isEnrolled) {
		ArrayList<Grup> grupuri=new ArrayList<Grup>();
		Connection connection = jdbConnectionWrapper.getConnection();
		try{

			PreparedStatement preparedStatement;
			if(isEnrolled)
				preparedStatement= connection.prepareStatement("SELECT * "
						+ "FROM grup"
						+ " INNER JOIN membru_grup"
						+ " ON membru_grup.user_ID=membru_grup.user_ID "
						+ " AND membru_grup.user_ID=? "
						+ " AND grup.ID_curs=?");
			else
				preparedStatement= connection.prepareStatement("SELECT * "
						+ "FROM grup"
						+ " WHERE NOT EXISTS("
						+ " SELECT * "
						+ " FROM membru_grup"
						+ " WHERE membru_grup.user_ID=?"
						+ " AND membru_grup.ID_grup=grup.ID_grup) "
						+ " AND ID_curs=?");
			preparedStatement.setInt(1, studentID);
			preparedStatement.setInt(2, courseID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				Grup grup = new Grup();
				grup=parseGrup(resultSet,connection);
				grupuri.add(grup);
			}
			return grupuri;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public ArrayList<Grup> viewGroups(int studentID){
		ArrayList<Grup> grupuri=new ArrayList<Grup>();
		Connection connection = jdbConnectionWrapper.getConnection();
		try{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * "
					+ "FROM grup"
					+ " INNER JOIN membru_grup"
					+ " ON membru_grup.ID_grup=grup.ID_grup "
					+ " AND membru_grup.user_ID=?");
			preparedStatement.setInt(1, studentID);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				Grup grup = new Grup();
				grup=parseGrup(resultSet,connection);
				grupuri.add(grup);
			}
			return grupuri;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<Grup> toateGrupurile() {
	    ArrayList<Grup> grupuri=new ArrayList<Grup>();
	    Connection connection = jdbConnectionWrapper.getConnection();
	    try{
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM grup");
	        ResultSet resultSet = preparedStatement.executeQuery();
	        while(resultSet.next()){
	            Grup grup = new Grup();
	            grup=Grup.parseGrup(resultSet,connection);
	            grupuri.add(grup);
	        }
	            return grupuri;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	@Override
	public ArrayList<Grup> cautaGrupuri(int cursID) {
	    ArrayList<Grup> grupuri=new ArrayList<Grup>();
	    Connection connection = jdbConnectionWrapper.getConnection();
	    try{
	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM grup WHERE ID_curs=?;");
	        preparedStatement.setInt(1, cursID);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        while(resultSet.next()){
	            Grup grup = new Grup();
	            grup=Grup.parseGrup(resultSet,connection);
	            grupuri.add(grup);
	        }
	        if(grupuri!=null)
	            return grupuri;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}