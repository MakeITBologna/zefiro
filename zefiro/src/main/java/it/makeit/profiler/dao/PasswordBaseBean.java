package it.makeit.profiler.dao;

import it.makeit.jbrick.Log;
import it.makeit.jbrick.sql.BaseDaoBean;


/**
 * Bean che rappresenta l'attributo password di un record della tabella users.
 *
 * @author MAKE IT
 */
public abstract class PasswordBaseBean extends BaseDaoBean {
	private static Log mLog = Log.getInstance(PasswordBaseBean.class);
	
    private String password;
    private boolean passwordIsModified = false;
    private String oldPassword;
    private boolean oldPasswordIsModified = false;
    private String confirmPassword;
    private boolean confirmPasswordIsModified = false;
	
    /**
     * Crea una istanza di PasswordBean.
     */
    public PasswordBaseBean() {
    }


    /**
     * Restituisce il valore del campo Password.
     * <br>
     * Meta Data:
     * <ul>
     * <li>nome colonna: users.password
     * <li>dimensione colonna: 15
     * <li>jdbc type restituito dal driver: Types.VARCHAR
     * </ul>
     *
     * @return il valore del campo Password.
     */
    public String getPassword() {
        return password;
    }

    /**
    * Assegna il valore passato in input al campo Password.
    * <br>
    * Il nuovo valore è assegnato solo se il nuovo valore da
    * assegnare e il valore corrente del campo sono differenti.
    * <br>
    * Meta Data:
    * <ul>
    * <li>nome colonna: users.password
    * <li>dimensione colonna: 15
    * <li>jdbc type restituito dal driver: Types.VARCHAR
    * </ul>
    *
    * @param pStrPassword valore da assegnare al campo Password.
    */
    public void setPassword(String pStrPassword) {
        password = pStrPassword;
        passwordIsModified = true;
    }

    /**
     * Indica se il campo Password è stato modificato.
     *
     * @return <tt>true</tt> se il campo Password è stato modificato, <tt>false</tt> se il campo Password non è stato modificato.
     */
    public boolean isPasswordModified() {
        return passwordIsModified;
    }
    
	public void setPasswordIsModified(boolean passwordIsModified) {
		this.passwordIsModified = passwordIsModified;
	}


	public String getOldPassword() {
		return oldPassword;
	}


	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}


	public boolean isOldPasswordModified() {
		return oldPasswordIsModified;
	}


	public void setOldPasswordIsModified(boolean oldPasswordIsModified) {
		this.oldPasswordIsModified = oldPasswordIsModified;
	}


	public String getConfirmPassword() {
		return confirmPassword;
	}


	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}


	public boolean isConfirmPasswordModified() {
		return confirmPasswordIsModified;
	}


	public void setConfirmPasswordIsModified(boolean confirmPasswordIsModified) {
		this.confirmPasswordIsModified = confirmPasswordIsModified;
	}

	/**
     * Restituisce <tt>true</tt> se uno dei campi del bean si trova nello stato 'modificato',
     * <tt>false</tt> altrimenti.
     *
     * @return <tt>true</tt> se uno dei campi del bean si trova nello stato 'modificato',
     * <tt>false</tt> altrimenti.
     */
    public boolean isModified() {
        return passwordIsModified;
    }

    /**
     * Porta lo stato di tutti i campi a 'non modificato' e riporta a <tt>null</tt> il campo orderBy.
    
     */
    public void resetIsModified() {
        passwordIsModified = false;
    }

}
