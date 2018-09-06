package it.makeit.profiler.dao;

import it.makeit.jbrick.profiler.IUsersBean;


/**
 * Bean che rappresenta un record della tabella users.
 *
 * @author MAKE IT
 */
public class UsersBean extends UsersBaseBean implements IUsersBean{
    public UsersBean() {
        super();
    }
    boolean notLoggedIn = true;
	public boolean isNotLoggedIn() {
		return notLoggedIn;
	}
	public void setNotLoggedIn(boolean notLoggedIn) {
		this.notLoggedIn = notLoggedIn;
	}
    
    
    
}
