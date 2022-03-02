package controller;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import view.CourseView;
import view.OptionsView;
import view.SearchView;

public class SearchController extends OptionsController {
	protected ArrayList<ActionListener> casetteEvents;
	public SearchController(SearchView view,UserController parent) {
		super(view,parent);
		
		((SearchView)this.view).addCancelButtonListener(new CancelButtonListener());
		((SearchView)this.view).setPanelsConnection(parent.userService.getSession().getJdbConnectionWrapper().getConnection());
		((SearchView)this.view).addAdvancedListener(new AdvancedButtonListener());
		((SearchView)this.view).addCreateButtonListener(new CreateNewButtonListener());
		casetteEvents=new ArrayList<ActionListener>();
		
	}
	public SearchController(SearchView view,OptionsController prev) {
		super(view,prev);
		
		((SearchView)this.view).addCancelButtonListener(new CancelButtonListener());
		((SearchView)this.view).setPanelsConnection(parent.userService.getSession().getJdbConnectionWrapper().getConnection());
		((SearchView)this.view).addAdvancedListener(new AdvancedButtonListener());
		((SearchView)this.view).addCreateButtonListener(new CreateNewButtonListener());
		casetteEvents=new ArrayList<ActionListener>();
		
	}
SearchController t=this;
class CancelButtonListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		SearchView v=((SearchView)view);
		CardLayout c=(CardLayout)(v.getViewedCasettes().getLayout());
		c.first(v.getViewedCasettes());
		v.resetSearched();
		v.getCancelButton().setEnabled(false);
		t.refresh();
		
	}
}
class SearchListener implements ActionListener{
	@Override
	 public void actionPerformed(ActionEvent e) {
		SearchView v=((SearchView)view);
			CardLayout c=(CardLayout)(v.getViewedCasettes().getLayout());
			c.show(v.getViewedCasettes(),"results");
			v.getCancelButton().setEnabled(true);
	}
}
class AdvancedButtonListener implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		SearchView v=((SearchView)view);
		CardLayout c=(CardLayout)(v.getViewedCasettes().getLayout());
		c.show(v.getViewedCasettes(),"filter");
		v.getCancelButton().setEnabled(true);
	}
}
class CreateNewButtonListener implements ActionListener{
	@Override 
	public void actionPerformed(ActionEvent e) {
		SearchView v=((SearchView)view);
		CardLayout c=(CardLayout)(v.getViewedCasettes().getLayout());
		c.show(v.getViewedCasettes(),"create");
		v.getCancelButton().setEnabled(true);
	}
}
@Override
public void refresh() {
	CardLayout cl=(CardLayout)(((SearchView)this.view).getViewedCasettes()).getLayout();
	cl.first(((SearchView)this.view).getViewedCasettes());
	((SearchView)this.view).getCancelButton().setEnabled(false);
}
}
