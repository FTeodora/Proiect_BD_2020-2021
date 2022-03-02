package repository;
import model.entity.User;
import repository.impl.JDBConnectionWrapper;

import java.util.ArrayList;
import model.enumeration.UserRole;
public interface UserRepository {
    public JDBConnectionWrapper getJdbConnectionWrapper();
    ArrayList<User> findUsers(String type_user);
    ArrayList<User> findStudentsbyYear(int an);
    User findByCNP(String CNP);
    User loadByUserName(String userName);
    boolean create(User user);
    boolean update(User user);
    boolean delete(User user);
	ArrayList<User> findUsersbyName(String lastName, String firstName,int role);
}