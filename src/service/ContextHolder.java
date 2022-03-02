package service;

import model.entity.User;
import model.enumeration.UserRole;
import repository.impl.JDBConnectionWrapper;

public interface ContextHolder {
	User getUser();
    String getSessionName();
    UserRole  getSessionType();
    public JDBConnectionWrapper getJdbConnectionWrapper();
	public void setJdbConnectionWrapper(JDBConnectionWrapper jdbConnectionWrapper);
	public int getUserID();
	String getPersonName();
}
