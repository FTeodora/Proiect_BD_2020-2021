package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.entity.Notificare;
import model.entity.Profesor;
import model.entity.Student;
import model.entity.User;
import service.AdminService;
import service.SuperAdminService;

import view.SearchUserView;
import view.utility.InsertionButton;
import view.utility.PersonalInfo;

public class SearchUserController extends SearchController{
	public SearchUserController(OptionsController prev) {
		super(new SearchUserView(prev.parent.userService.getSession().getSessionType()),prev);
		((SearchUserView)this.view).addSearchButtonListener(new SearchUserListener());
		((SearchUserView)this.view).addAdvancedSearchListener(new AdvancedSearchListener());
		((SearchUserView)this.view).setPanelsConnection(prev.parent.userService.getSession().getJdbConnectionWrapper().getConnection());
		this.casetteEvents.add(new EditUserListener());
		this.casetteEvents.add(new DeleteUserListener());
		((SearchUserView)this.view).setListeners(casetteEvents);
	}
class EditUserListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean succes=false;
		PersonalInfo source=(PersonalInfo)(((InsertionButton)(e.getSource())).getSource()).getInfoField();
		User u=source.retrieveInfo();
		if(u instanceof Student)
		{
			succes=((AdminService)parent.userService).updateStudent((Student)u);
		}
		else if(u instanceof Profesor)
		{
			succes=((AdminService)parent.userService).updateProfesor((Profesor)u);
		}
		else
			succes=((SuperAdminService)parent.userService).updateAdmin(u);
		if(succes)
		{
			Notificare n=new Notificare();
			n.setReceiverID(u.getId());
			n.setSenderName(parent.userService.getSession().getSessionType().toString());
			DateTimeFormatter myFormatObj=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			n.setDescriere("Datele dumneavoastra personale au fost modificate\npe data de "+myFormatObj.format(LocalDateTime.now()));
			//n.setExpirare("24:00:00");
			parent.userService.sendNotification(n);
			JOptionPane.showMessageDialog(null, "Actualizarea s-a realizat cu succes", "Succes",JOptionPane.INFORMATION_MESSAGE);
			source.changeDatas();
		}
		else
			JOptionPane.showMessageDialog(null, "Nu s-au putut actualiza datele", "Eroare",JOptionPane.ERROR_MESSAGE);
		
	}
}
class DeleteUserListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		PersonalInfo source=(PersonalInfo)(((InsertionButton)(e.getSource())).getSource()).getInfoField();
		boolean succes=false;
		if(source.getUser() instanceof Student)
		{
			succes=((AdminService)parent.userService).deleteStudent(source.getUser().getUsername());
		}
		else if(source.getUser() instanceof Profesor)
		{
			succes=((AdminService)parent.userService).deleteProfesor(source.getUser().getUsername());
		}
		else
			succes=((SuperAdminService)parent.userService).deleteAdmin(source.getUser());
		if(succes)
		{
			
			JOptionPane.showMessageDialog(null, "Stergerea s-a realizat cu succes", "Succes",JOptionPane.INFORMATION_MESSAGE);
			((SearchUserView)view).removeResult(((InsertionButton)(e.getSource())).getSource());
		}
		else
			JOptionPane.showMessageDialog(null, "Nu s-a putut sterge utilizatorul", "Eroare",JOptionPane.ERROR_MESSAGE);
		
	}
}
class AdvancedSearchListener extends SearchListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		SearchUserView v=((SearchUserView)view);
		String tip=v.getSelectedType();
		if(tip.equals("PROFESOR")||tip.equals("STUDENT"))
		{
			v.setFilterSecondTable(tip, "user_ID");
			v.getAdvancedFilter().setDoJoin(true);
			String extra=v.getExtraInfo().trim();
				if(!extra.equals(""))
				{
					if(tip.equals("STUDENT"))
						v.getAdvancedFilter().addJoinCondition("an="+extra);
					else
						v.getAdvancedFilter().addJoinCondition("departament='"+extra+"'");
				}
			
		}
		else {
			
			if(tip.equals("ADMIN"))
				tip+="S";
			else
				v.getAdvancedFilter().addMainCondition("tip_user>"+(parent.userService.getSession().getSessionType().ordinal()+1)+" ");
			
			v.getAdvancedFilter().setDoJoin(false);
		}
		ArrayList<User> rez=null;
		try {
			ResultSet rst=v.getAdvancedFilter().findRowInTableFiltered();
			if(rst.next())
			{
				rez=new ArrayList<User>();
				rez.add(User.parseUser(rst, v.getAdvancedFilter().getConnection()));
				while(rst.next())
					rez.add(User.parseUser(rst, v.getAdvancedFilter().getConnection()));
			}
		}catch(Exception e1) {
			e1.printStackTrace();
		}
			v.addUserResults(rez);
		super.actionPerformed(e);
	}
}
class SearchUserListener extends SearchListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		SearchUserView v=((SearchUserView)view);
		v.addUserResults(((AdminService)parent.userService).findUsersByName(v.getNume(), v.getPrenume()));
		super.actionPerformed(e);
	}
}
}
