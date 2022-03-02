package repository;

import model.entity.Admin;
import model.entity.User;

import java.util.List;

public interface AdminRepository extends  UserRepository {
    List<Admin> findAll();


}
