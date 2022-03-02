package service.impl;

import model.entity.Student;
import model.entity.User;
import service.ContextHolder;
import service.SuperAdminService;

public class SuperAdminServiceImpl extends AdminServiceImpl implements SuperAdminService {
    public SuperAdminServiceImpl(ContextHolder session) {
		super(session);
	}

	@Override
    public boolean createAdmin(User admin) {
    	return userRepository.create(admin);
    }

	@Override
	public boolean updateAdmin(User admin) {
		return userRepository.update(admin);
	}

	@Override
	public boolean deleteAdmin(User admin) {
		return userRepository.delete(admin);
	}
}
