package repository.impl;

import model.entity.Curs;
import repository.CursRepository;

import java.sql.*;
import java.util.ArrayList;


public class CursRepositoryImpl implements CursRepository {
    private final JDBConnectionWrapper jdbConnectionWrapper;
    public CursRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }
	@Override
	public ArrayList<Curs> loadCoursebyName(String nume,int studentID,boolean isEnrolled) {
		ArrayList<Curs> rez=null;

		Connection connection = jdbConnectionWrapper.getConnection();
		try {

			Statement statement = connection.createStatement();
			if(isEnrolled==true)
				statement.execute("SELECT *"
						+ " FROM curs c "
						+ "INNER JOIN fisa_participare f "
						+ "ON c.ID_curs=f.ID_curs "
						+ "WHERE user_ID="+Integer.toString(studentID)
						+" AND materie LIKE '%"+nume+"%'");
			else
				statement.execute("SELECT* FROM curs WHERE NOT EXISTS"
						+ "(SELECT  ID_curs"
						+ " FROM  fisa_participare "
						+ " WHERE fisa_participare.user_id="+Integer.toString(studentID)
						+" AND curs.ID_curs=fisa_participare.ID_curs ) AND materie LIKE '%"+nume+"%'");
			ResultSet resultSet = statement.getResultSet();
			if(resultSet.next())
			{
				rez=new ArrayList<Curs>();
				rez.add(Curs.parseCurs(resultSet));
				while (resultSet.next())
					rez.add(Curs.parseCurs(resultSet));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rez;
	}

	@Override
	public ArrayList<Curs> loadTeachersCoursesbyName(String nume,int teacherID){
		Connection connection = jdbConnectionWrapper.getConnection();
		ArrayList<Curs> rez=null;
		try {
			Statement statement=connection.createStatement();
			statement.execute("SELECT * FROM curs "+
					" INNER JOIN fisa_postului_curs ON curs.ID_curs=fisa_postului_curs.ID_curs "
					+ "AND fisa_postului_curs.user_ID="+Integer.toString(teacherID)
					+ " AND materie LIKE '%"+nume+"%'");

			ResultSet rst=statement.getResultSet();
			if(rst.next())
			{
				rez=new ArrayList<Curs>();
				rez.add(Curs.parseCurs(rst));
				while(rst.next()) {
					rez.add(Curs.parseCurs(rst));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return rez;
	}
	@Override
	public ArrayList<Curs> loadAllCoursesbyName(String nume){
		Connection connection = jdbConnectionWrapper.getConnection();
		ArrayList<Curs> rez=null;
		try {
			Statement statement=connection.createStatement();
			statement.execute("SELECT *"
					+ " FROM curs"
					+ " WHERE materie LIKE '%"+nume+"%'");
			ResultSet rst=statement.getResultSet();
			if(rst.next())
			{
				rez=new ArrayList<Curs>();
				rez.add(Curs.parseCurs(rst));
				while(rst.next()) {
					rez.add(Curs.parseCurs(rst));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return rez;
	}

	@Override
	public float[] getGrades(int studentID,int courseID) {
		Connection connection = jdbConnectionWrapper.getConnection();
		float[]  note=new float[4];
		try{
			PreparedStatement preparedStatement=connection.prepareStatement("SELECT activitate.tip,participant_curs.nota,activitate.pondere\n" +
					"FROM participant_curs,activitate,curs\n" +
					"WHERE participant_curs.ID_activitate=activitate.ID_activitate\n" +
					"AND activitate.ID_curs=curs.ID_curs\n"+
					"AND activitate.ID_curs=?\n" +
					"AND participant_curs.user_id=?;");
			preparedStatement.setInt(1,courseID);
			preparedStatement.setInt(2,studentID);
			ResultSet rst=preparedStatement.executeQuery();


				float total=0;
				float notaFinala;
				int ponderi= 0;
				while(rst.next()){
					if(rst.getString(1).equals("curs")){
						note[0]=rst.getFloat(2);
						total=total+rst.getFloat(2)*rst.getInt(3)/100f;
						ponderi+=rst.getInt(3);


					}
					if(rst.getString(1).equals("seminar")){
						note[1]=rst.getFloat(2);
						total=total+rst.getFloat(2)*rst.getInt(3)/100f;
						ponderi+=rst.getInt(3);


					}
					if(rst.getString(1).equals("laborator")){
						note[2]=rst.getFloat(2);
						total=total+rst.getFloat(2)*rst.getInt(3)/100f;
						ponderi+=rst.getInt(3);

					}
				}
				int oficiu=100-ponderi;
				notaFinala=total+oficiu/10f;

				note[3]=notaFinala;


		}catch(Exception ae) {
			ae.printStackTrace();
		}
		return note;
	}
	@Override
	public boolean actualizarePonderiNote(int courseID, String activityType,int profesorPrincipalID,int pondere){
		Connection connection = jdbConnectionWrapper.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE activitate,organizator SET activitate.pondere=? " +
							"WHERE organizator.profesor_principal=? AND activitate.ID_curs=? AND activitate.tip=?;");

		preparedStatement.setInt(1,pondere);
		preparedStatement.setInt(2,profesorPrincipalID);
		preparedStatement.setInt(3,courseID);
		preparedStatement.setString(4,activityType);

			int changedRows = preparedStatement.executeUpdate();
			return changedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public ArrayList<Curs> getManagedCourses(int teacherID){
		Connection connection = jdbConnectionWrapper.getConnection();
		ArrayList<Curs> rez=new ArrayList<Curs>();
		try {
			PreparedStatement preparedStatement=connection.prepareStatement(
					"SELECT * from curs\n" +
					"JOIN activitate\n" +
					"ON curs.ID_curs=activitate.ID_curs\n" +
					"JOIN organizator ON\n" +
					"activitate.ID_activitate=organizator.ID_activitate\n" +
					"WHERE organizator.profesor_principal=?\n" +
					"GROUP BY curs.ID_curs;");
			preparedStatement.setInt(1,teacherID);
			ResultSet rst=preparedStatement.executeQuery();
				while(rst.next()) {
					rez.add(Curs.parseCurs(rst));
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return rez;

	}
}