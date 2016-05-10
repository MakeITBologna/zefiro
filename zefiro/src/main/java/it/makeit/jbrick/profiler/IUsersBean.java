package it.makeit.jbrick.profiler;

import java.sql.Timestamp;
import java.util.List;


public interface IUsersBean {

	Integer getIdUser();

	boolean isUserInRole(Integer pIntIdRole);

	List<IRolesBean> getRoles();

	String getUsername();

	String getFullName();

	String getEmail();
	
	String getName();
	
	String getSurname();
	
	String getAddress();
	
	Integer getEnabled();
	
	Timestamp getUltimoAccesso();

	Integer getNumeroAccessi();
}
