package repository.impl;
import repository.OrganizatorRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class OrganizatorRepositoryImpl implements OrganizatorRepository {
    private final JDBConnectionWrapper jdbConnectionWrapper;



    public OrganizatorRepositoryImpl(JDBConnectionWrapper jdbConnectionWrapper) {
        this.jdbConnectionWrapper = jdbConnectionWrapper;

    }
    @Override
    public int getIDOraganizator(int activityID) {
        Connection connection = jdbConnectionWrapper.getConnection();
        int organizerID=-1;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id "
            		+ "FROM organizator WHERE ID_activitate=?;");
             preparedStatement.setInt(1,activityID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                organizerID=resultSet.getInt(1);
            }
            return  organizerID;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return organizerID;
    }

}
