package repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.entity.FisaCurs;
import model.entity.Profesor;
import model.entity.Student;
import repository.FisaCursRepository;

public class FisaCursRepositoryImpl implements FisaCursRepository{
    JDBConnectionWrapper jdbconnectionWrapper;

    public FisaCursRepositoryImpl(JDBConnectionWrapper jdbconnectionWrapper) {
        this.jdbconnectionWrapper = jdbconnectionWrapper;
    }
    @Override
    public ArrayList<Profesor> getCourseTeachers(int courseID) {
        ArrayList<Profesor> rez=null;
        Connection connection = jdbconnectionWrapper.getConnection();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("select users.ID,nume,prenume,departament "
                    + "FROM users,profesor "
                    + "INNER JOIN fisa_postului_curs ON profesor.user_id=fisa_postului_curs.user_id "
                    + "AND fisa_postului_curs.id_curs=? WHERE users.id=profesor.user_id");
            preparedStatement.setInt(1, courseID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                rez=new ArrayList<Profesor>();
                Profesor s=new Profesor();
                s.setId(resultSet.getInt(1));
                s.setNume(resultSet.getString(2));
                s.setPrenume(resultSet.getString(3));
                s.setDepartament(resultSet.getString(4));
                rez.add(s);
                while(resultSet.next()) {
                    s=new Profesor();
                    s.setId(resultSet.getInt(1));
                    s.setNume(resultSet.getString(2));
                    s.setPrenume(resultSet.getString(3));
                    s.setDepartament(resultSet.getString(4));
                    rez.add(s);
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return rez;
    }
    
    public ArrayList<Profesor> getOtherTeachers(int courseID) {
        ArrayList<Profesor> rez=null;
        Connection connection = jdbconnectionWrapper.getConnection();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("select users.ID,nume,prenume,departament "
                    + "FROM users,profesor,fisa_postului_curs WHERE fisa_postului_curs.ID_curs!=?");
            preparedStatement.setInt(1, courseID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                rez=new ArrayList<Profesor>();
                Profesor s=new Profesor();
                s.setId(resultSet.getInt(1));
                s.setNume(resultSet.getString(2));
                s.setPrenume(resultSet.getString(3));
                s.setDepartament(resultSet.getString(4));
                rez.add(s);
                while(resultSet.next()) {
                    s=new Profesor();
                    s.setId(resultSet.getInt(1));
                    s.setNume(resultSet.getString(2));
                    s.setPrenume(resultSet.getString(3));
                    s.setDepartament(resultSet.getString(4));
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
    public ArrayList<Profesor> notCourseTeachers(int cursID) {
        ArrayList<Profesor> profesori=new ArrayList<Profesor>();
        Connection connection = jdbconnectionWrapper.getConnection();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT users.ID,nume,prenume,profesor.departament FROM users,profesor "
            		+ "WHERE users.ID=profesor.user_ID  AND NOT EXISTS( "
            		+ "SELECT * FROM fisa_postului_curs WHERE users.ID=fisa_postului_curs.user_id AND fisa_postului_curs.ID_curs=?)");
            preparedStatement.setInt(1, cursID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Profesor p = new Profesor();
                p.setId(resultSet.getInt(1));
                p.setNume(resultSet.getString(2));
                p.setPrenume(resultSet.getString(3));
                p.setDepartament(resultSet.getString(4));
                profesori.add(p);
            }
            if(profesori!=null)
                return profesori;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	@Override
	public int createFisa(int cursID,int teacherID) {
			Connection connection = jdbconnectionWrapper.getConnection();
			try {

				PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO fisa_postului_curs (ID_curs,user_id) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setInt(1, cursID);
				preparedStatement.setInt(2, teacherID);

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
    public ArrayList<Profesor> findTeachersbyDepartament(String nume_departament,int courseID) {
        ArrayList<Profesor> useri = new ArrayList<Profesor>();
        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT users.ID,nume,prenume,profesor.departament FROM users,profesor "
            		+ "WHERE users.ID=profesor.user_ID "
            		+ "AND departament=?"
            		+ " AND NOT EXISTS( "
            		+ "SELECT * FROM fisa_postului_curs WHERE users.ID=fisa_postului_curs.user_id AND fisa_postului_curs.ID_curs=?)");
            preparedStatement.setString(1, nume_departament);
            preparedStatement.setInt(2, courseID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                FisaCurs user = new FisaCurs();
                user.setId(resultSet.getInt(1));
                user.setNume(resultSet.getString(2));
                user.setPrenume(resultSet.getString(3));
                user.setDepartament(resultSet.getString(4));
                useri.add(user);
            }
               return useri;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
