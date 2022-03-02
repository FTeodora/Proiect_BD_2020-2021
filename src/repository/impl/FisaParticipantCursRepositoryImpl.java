package repository.impl;

import model.entity.ParticipantActivitate;
import model.entity.Profesor;
import model.entity.Student;
import repository.FisaParticipantCursRepository;

import java.sql.*;
import java.util.ArrayList;

public class FisaParticipantCursRepositoryImpl implements FisaParticipantCursRepository {
    JDBConnectionWrapper jdbconnectionWrapper;

    public FisaParticipantCursRepositoryImpl(JDBConnectionWrapper jdbconnectionWrapper) {
        this.jdbconnectionWrapper = jdbconnectionWrapper;
    }



    public boolean createFisaParticipantCurs(int studentID, int activityID,int fisaparticipareID) {
        Connection connection = jdbconnectionWrapper.getConnection();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO participant_curs(ID_activitate,id_fisa_participare,user_id) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, activityID);
            preparedStatement.setInt(2, fisaparticipareID);
            preparedStatement.setInt(3, studentID);
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


    public boolean deleteFisaParticipantCurs(int studentID,int courseID) {
        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            System.out.println("studentID "+ studentID+" courseID "+courseID);

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM participant_curs\n" +
                    "WHERE ID_activitate IN\n" +
                    "(SELECT ID_activitate FROM activitate,curs\n" +
                    "where activitate.ID_Curs=curs.ID_curs\n" +
                    "AND activitate.ID_curs=?)\n" +
                    "AND user_id=?;");
            preparedStatement.setInt(1, courseID);
            preparedStatement.setInt(2,studentID);
            int updatedRows = preparedStatement.executeUpdate();
            System.out.println(updatedRows);
            return updatedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<Student> getActivityParticipants(int activitateID) {
        ArrayList<Student> participanti=new ArrayList<>();

        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT participant_curs.id,users.CNP,users.nume,users.prenume,student.an,participant_curs.nota FROM users,student,participant_curs\n" +
                            "WHERE users.id = student.user_id AND student.user_id=participant_curs.user_id AND ID_activitate=?;");
            preparedStatement.setInt(1, activitateID);
            ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()){
                    ParticipantActivitate p=new ParticipantActivitate();
                    p.setIdParticipant(resultSet.getInt(1));
                    p.setCNP(resultSet.getString(2));
                    p.setNume(resultSet.getString(3));
                    p.setPrenume(resultSet.getString(4));
                    p.setAn(resultSet.getInt(5));
                    p.setNota(resultSet.getFloat(6));
                    participanti.add(p);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participanti;
    }
    
}
