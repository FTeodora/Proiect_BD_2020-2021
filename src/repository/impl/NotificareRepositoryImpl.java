package repository.impl;

import model.entity.Grup;
import model.entity.Notificare;
import repository.NotificareRepository;

import static model.entity.Grup.parseGrup;

import java.sql.*;
import java.util.ArrayList;

public class NotificareRepositoryImpl implements NotificareRepository {
    JDBConnectionWrapper jdbconnectionWrapper;

    public NotificareRepositoryImpl(JDBConnectionWrapper jdbconnectionWrapper) {
        this.jdbconnectionWrapper = jdbconnectionWrapper;
    }

    @Override
    public boolean sendNotification(Notificare notificare) {
        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO notificare(receiver_ID,sender_ID,sender_name,descriere,expirare) "
            		+ "VALUES(?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, notificare.getReceiverID());
            preparedStatement.setInt(2, notificare.getSenderID());
            preparedStatement.setString(3, notificare.getSenderName());
            preparedStatement.setString(4, notificare.getDescriere());
            preparedStatement.setString(5, notificare.getExpirare());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) 
               return true;
            
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

	@Override
	public ArrayList<Notificare> getNotifications(int userID) {
		ArrayList<Notificare> rez=null;
		Connection connection = jdbconnectionWrapper.getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM notificare "
					+ "WHERE receiver_ID=?");
			preparedStatement.setInt(1, userID);
			preparedStatement.executeQuery();
			ResultSet rst=preparedStatement.getResultSet();
			if(rst.next()) {
				rez=new ArrayList<Notificare>();
				Notificare n=new Notificare();
				n.setReceiverID(userID);
				n.setSenderID(rst.getInt(2));
				n.setSenderName(rst.getString(3));
				n.setDescriere(rst.getString(4));
				n.setExpirare(rst.getString(5));
				rez.add(n);
				while(rst.next())
					{
					n=new Notificare();
					n.setReceiverID(userID);
					n.setSenderID(rst.getInt(2));
					n.setSenderName(rst.getString(3));
					n.setDescriere(rst.getString(4));
					n.setExpirare(rst.getString(5));
					rez.add(n);
					}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rez;
	}
}
