package repository.impl;

import model.entity.Activitate;
import model.entity.Mesaj;
import model.entity.User;
import repository.MesajRepository;

import java.sql.*;
import java.util.ArrayList;

public class MesajRepositoryImpl implements MesajRepository {
    JDBConnectionWrapper jdbconnectionWrapper;

    public MesajRepositoryImpl(JDBConnectionWrapper jdbconnectionWrapper) {
        this.jdbconnectionWrapper = jdbconnectionWrapper;
    }

  @Override
    public ArrayList<Mesaj> getMessages(int groupID) {
        Connection connection = jdbconnectionWrapper.getConnection();
        try {
            ArrayList<Mesaj> mesaje=new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT users.id,users.nume,users.prenume,mesaj.continut,mesaj.data_postare,mesaj.ID\n" +
                    " FROM users,mesaj\n" +
                    " WHERE users.id=mesaj.user_id\n" +
                    " AND mesaj.ID_grup=?\n" +
                    " ORDER BY mesaj.data_postare DESC" +
                    " LIMIT 15");
            preparedStatement.setInt(1, groupID);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Mesaj mesajNou=new Mesaj();
                User autor=new User();
                autor.setId(resultSet.getInt(1));
                autor.setNume(resultSet.getString(2));
                autor.setPrenume(resultSet.getString(3));
                mesajNou.setAuthor(autor);
                mesajNou.setContinut(resultSet.getString(4));
                mesajNou.setDataPostare(resultSet.getString(5));
                mesajNou.setMessageID(resultSet.getInt(6));
                mesaje.add(mesajNou);

            }

            return mesaje;

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

        }
}
