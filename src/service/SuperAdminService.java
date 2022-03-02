package service;

import model.entity.User;

/**
 * createAdmin-creare unui cont de admin
 * updateAdmin-modificarea unui cont de Admin
 * deleteAdmin- stergerea unui cont de admin
 */
public interface SuperAdminService extends AdminService{
	boolean createAdmin(User admin);
	boolean updateAdmin(User admin);
	boolean deleteAdmin(User admin);


}
