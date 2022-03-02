package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import controller.SearchController.SearchListener;
import model.entity.Grup;
import model.entity.User;
import model.enumeration.UserRole;
import service.AdminService;
import service.StudentService;
import view.GroupsView;
import view.SearchUserView;
import view.utility.Casette;
import view.utility.GroupWindow;
import view.utility.InsertionButton;

public class GroupsController extends SearchController{
	public GroupsController(UserController parent) {
		super(new GroupsView((parent.userService).viewCourses(),parent.userService.getSession().getSessionType()),parent);
		refresh();
		((GroupsView)this.view).addSearchButtonListener(new SearchGroupsListener());
		((GroupsView)this.view).addCreateSubmitButtonListener(new CreateGroupListener());
		
		casetteEvents.add(new JoinGroupListener());
		casetteEvents.add(new GroupButtonListener());
		((GroupsView)this.view).setListeners(casetteEvents);
		if(parent.userService.getSession().getSessionType().ordinal()<=UserRole.ADMIN.ordinal())
		{
			((GroupsView)this.view).addAdvancedListener(new AdvancedButtonListener());
			((GroupsView)this.view).addAdvancedSearchListener(new AdvancedSearchListener());
		}
	}
class SearchGroupsListener extends SearchListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		GroupsView v=(GroupsView)view;
		UserRole rol=(parent.userService).getSession().getSessionType();
		if(parent.userService instanceof StudentService)
			v.addResults(((StudentService)(parent.userService)).searchGroupsByCourse(v.getIDMaterie(), v.getEnrolled()),v.getEnrolled(),rol);
		else
			v.addResults(((AdminService)(parent.userService)).searchGroupsByCourse(v.getIDMaterie()), true,rol);
	}
}
class CreateGroupListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		GroupsView v=(GroupsView)view;
		int ID=v.getForm().insertTouple();
		if(ID!=-1) {
			if(((StudentService)(parent.userService)).joinGroup(ID))
			{
				JOptionPane.showMessageDialog(null, "Inserarea s-a realizat cu succes", "Succes", JOptionPane.INFORMATION_MESSAGE);
				v.getForm().resetDatas();
			}
		}
			else
			JOptionPane.showMessageDialog(null, "Nu s-a putut realiza inserarea", "Eroare", JOptionPane.ERROR_MESSAGE);
		
	}
	
}
GroupsController t=this;
class GroupButtonListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		InsertionButton source=(InsertionButton)e.getSource();
		GroupDetailsController groupWin= new GroupDetailsController(t,((GroupWindow)(source.getSource().getInfoField())).getGrup());
		groupWin.view.setVisible(true);
		groupWin.view.setSize(view.getSize());
		groupWin.view.setLocationRelativeTo(view);
		view.setVisible(false);
	}
}
class AdvancedSearchListener extends SearchListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		GroupsView v=((GroupsView)view);
		ArrayList<Grup> rez=null;
		try {
			ResultSet rst=v.getAdvancedFilter().findRowInTableFiltered();
			if(rst.next())
			{
				rez=new ArrayList<Grup>();
				rez.add(Grup.parseGrup(rst, v.getAdvancedFilter().getConnection()));
				while(rst.next())
					rez.add(Grup.parseGrup(rst, v.getAdvancedFilter().getConnection()));
			}
		}catch(Exception e1) {
			e1.printStackTrace();
		}
		v.addResults(rez, true, parent.userService.getSession().getSessionType());
		super.actionPerformed(e);
		
	}
	
}
class JoinGroupListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		InsertionButton source=(InsertionButton)e.getSource();
		if(source.getText().equals("Alatura-te"))
		{
			if(((StudentService)parent.getService()).joinGroup(source.getValue()))
			{
				source.setText("Paraseste");
				((GroupsView)view).removeResult(source.getSource());
			}
			else
				JOptionPane.showMessageDialog(null, "Nu ati putut fi inscris in acest grup!", "Eroare", JOptionPane.ERROR_MESSAGE);
			
		}
		else
		{
			if(((StudentService)parent.getService()).leaveGroup(source.getValue()))
			{
				source.setText("Alatura-te");
				((GroupsView)view).removeCasette(source.getSource());
			}
			else
				JOptionPane.showMessageDialog(null, "Nu s-a putut parasi grupul!", "Eroare", JOptionPane.ERROR_MESSAGE);
			
		}
	}
}
@Override
public void refresh() {
	GroupsView v=(GroupsView)view;
	
		if(parent.userService instanceof StudentService)
			v.setMain(((StudentService)(parent.userService)).viewGroups(),(parent.userService).viewCourses(),UserRole.STUDENT);
		else
			v.setMain(((AdminService)(parent.userService)).viewGroups(),(parent.userService).viewCourses(),UserRole.ADMIN);
		super.refresh();
}
}
