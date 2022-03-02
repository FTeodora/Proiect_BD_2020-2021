package service.impl;
import model.entity.User;
import model.enumeration.UserRole;
import repository.impl.JDBConnectionWrapper;
import service.ContextHolder;
public class ContextHolderImpl implements ContextHolder{
	
	private User user;
	private JDBConnectionWrapper jdbConnectionWrapper; 
	public ContextHolderImpl() {
		
	}
	public ContextHolderImpl(User u) {
		this.user=u;
	}
	@Override
	public User getUser() {
		return user;
	}
	@Override
	public String getSessionName() {
		return user.getUsername();
	}
	@Override
	public String getPersonName() {
		return user.getNume()+" "+user.getPrenume();
	}
	
	@Override
	public UserRole  getSessionType() {
		return user.getTipUser();
	}
	
	@Override
	public JDBConnectionWrapper getJdbConnectionWrapper() {
		return jdbConnectionWrapper;
	}
	@Override
	public void setJdbConnectionWrapper(JDBConnectionWrapper jdbConnectionWrapper) {
		this.jdbConnectionWrapper = jdbConnectionWrapper;
	}
	@Override
	public int getUserID() {
		return user.getId();
	}
	
}
