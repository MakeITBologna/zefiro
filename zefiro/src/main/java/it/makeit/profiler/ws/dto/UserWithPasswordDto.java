package it.makeit.profiler.ws.dto;

import it.makeit.profiler.dao.PasswordBean;
import it.makeit.profiler.dao.UsersBean;

/**
 * DTO che contiene un {@link UsersBean} e un {@link PasswordBean}.
 * 
 * Serve all'inserimento di un nuovo utente, che deve essere necessariamente associato ad una password.
 * 
 */

public class UserWithPasswordDto {
	public UsersBean usersBean;
	public PasswordBean passwordBean;
}
