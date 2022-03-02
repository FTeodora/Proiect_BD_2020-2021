package repository.impl;

import model.entity.Activitate;
import model.entity.ActivitateGrup;
import model.entity.Profesor;
import model.entity.ProfesoriInvitati;
import repository.ProfesoriInvitatiRepository;

import java.sql.*;
import java.util.ArrayList;

public class ProfesoriInvitatiRepositoryImpl implements ProfesoriInvitatiRepository {
    private final JDBConnectionWrapper jdbConnectionWrapper;

    public JDBConnectionWrapper getJdbConnectionWrapper() {
        return jdbConnectionWrapper;
    }

    public ProfesoriInvitatiRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;
    }
    public boolean invitaProfesor(int userID,int activitateID){
        Connection connection = jdbConnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO profesori_invitati(user_id,ID_activitate) VALUES(?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, activitateID);
            preparedStatement.execute();
            ResultSet resultSet1 = preparedStatement.getGeneratedKeys();
            if (resultSet1.next()) {

                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
    @Override
    public ArrayList<Profesor> getActivityParticipants(ActivitateGrup activitate,boolean isParticipant) {
        ArrayList<Profesor> participanti=new ArrayList<Profesor>();

        Connection connection = jdbConnectionWrapper.getConnection();
        try {
        	PreparedStatement preparedStatement;
        	if(isParticipant)
            preparedStatement = connection.prepareStatement(
                    "SELECT users.id,users.nume,users.prenume,profesor.departament "
                    + "FROM users,profesor,profesori_invitati\n" +
                            "WHERE users.id = profesor.user_id "
                      + "AND profesor.user_id=profesori_invitati.user_id AND ID_activitate=?;");
        	else
            preparedStatement = connection.prepareStatement(
                    "SELECT users.id,users.nume,users.prenume,profesor.departament "
                    + "FROM users,fisa_postului_curs,profesor " +
                     " WHERE users.id = profesor.user_id "
                     + "AND fisa_postului_curs.user_ID=users.ID AND fisa_postului_curs.ID_curs=? "
                     + "AND NOT EXISTS("
                     + "SELECT * FROM profesori_invitati "
                     + "WHERE profesori_invitati.user_ID=users.ID AND profesori_invitati.ID_activitate=?);");
            preparedStatement.setInt(1, activitate.getId());
            if(!isParticipant)
            {
            	 preparedStatement.setInt(1, activitate.getGrupCourse());
            	 preparedStatement.setInt(2, activitate.getId());
            }
            	
            ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()){
                	Profesor p=new ProfesoriInvitati();
                    p.setId(resultSet.getInt(1));
                    p.setNume(resultSet.getString(2));
                    p.setPrenume(resultSet.getString(3));
                    p.setDepartament(resultSet.getString(4));
                    participanti.add(p);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participanti;
    }
}
