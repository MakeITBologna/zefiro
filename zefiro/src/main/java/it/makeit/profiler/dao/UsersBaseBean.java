package it.makeit.profiler.dao;

import it.makeit.jbrick.Log;
import it.makeit.jbrick.profiler.IRolesBean;
import it.makeit.jbrick.sql.BaseDaoBean;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;



/**
 * Bean che rappresenta un record della tabella users.
 *
 * @author MAKE IT
 */
public abstract class UsersBaseBean extends BaseDaoBean {
	private static Log mLog = Log.getInstance(UsersBaseBean.class);
	
    private Integer idUser;
    private boolean idUserIsModified = false;
    private String username;
    private boolean usernameIsModified = false;
    private String name;
    private boolean nameIsModified = false;
    private String surname;
    private boolean surnameIsModified = false;
    private String email;
    private boolean emailIsModified = false;
    private String phone;
    private boolean phoneIsModified = false;
    private String address;
    private boolean addressIsModified = false;
    private Integer enabled;
    private boolean enabledIsModified = false;
    private java.sql.Timestamp ultimoAccesso;
    private boolean ultimoAccessoIsModified = false;
    private Integer numeroAccessi;
    private boolean numeroAccessiIsModified = false;
    private String fullName;
    private boolean fullNameIsModified = false;
	
    private String orderByClause = null;
    private List<IRolesBean> roles;
    private Map<String, Object> parametersMap;

    /**
     * Crea una istanza di UsersBean.
     */
    public UsersBaseBean() {
    }

    /**
     * Restituisce il valore del campo IdUser.
     * <br>
     * Meta Data:
     * <ul>
     * <li>nome colonna: users.id_user
     * <li>dimensione colonna: 10
     * <li>jdbc type restituito dal driver: Types.INTEGER
     * </ul>
     *
     * @return il valore del campo IdUser.
     */
    public Integer getIdUser() {
        return idUser;
    }

    /**
    * Assegna il valore passato in input al campo IdUser.
    * <br>
    * Il nuovo valore è assegnato solo se il nuovo valore da
    * assegnare e il valore corrente del campo sono differenti.
    * <br>
    * Meta Data:
    * <ul>
    * <li>nome colonna: users.id_user
    * <li>dimensione colonna: 10
    * <li>jdbc type restituito dal driver: Types.INTEGER
    * </ul>
    *
    * @param pIntIdUser valore da assegnare al campo IdUser.
    */
    public void setIdUser(Integer pIntIdUser) {
        idUser = pIntIdUser;
        idUserIsModified = true;
    }

    /**
     * Indica se il campo IdUser è stato modificato.
     *
     * @return <tt>true</tt> se il campo IdUser è stato modificato, <tt>false</tt> se il campo IdUser non è stato modificato.
     */
    public boolean isIdUserModified() {
        return idUserIsModified;
    }

    /**
     * Restituisce il valore del campo Username.
     * <br>
     * Meta Data:
     * <ul>
     * <li>nome colonna: users.username
     * <li>dimensione colonna: 15
     * <li>jdbc type restituito dal driver: Types.VARCHAR
     * </ul>
     *
     * @return il valore del campo Username.
     */
    public String getUsername() {
        return username;
    }

    /**
    * Assegna il valore passato in input al campo Username.
    * <br>
    * Il nuovo valore è assegnato solo se il nuovo valore da
    * assegnare e il valore corrente del campo sono differenti.
    * <br>
    * Meta Data:
    * <ul>
    * <li>nome colonna: users.username
    * <li>dimensione colonna: 15
    * <li>jdbc type restituito dal driver: Types.VARCHAR
    * </ul>
    *
    * @param pStrUsername valore da assegnare al campo Username.
    */
    public void setUsername(String pStrUsername) {
        username = pStrUsername;
        usernameIsModified = true;
    }

    /**
     * Indica se il campo Username è stato modificato.
     *
     * @return <tt>true</tt> se il campo Username è stato modificato, <tt>false</tt> se il campo Username non è stato modificato.
     */
    public boolean isUsernameModified() {
        return usernameIsModified;
    }


    /**
     * Restituisce il valore del campo Name.
     * <br>
     * Meta Data:
     * <ul>
     * <li>nome colonna: users.name
     * <li>dimensione colonna: 40
     * <li>jdbc type restituito dal driver: Types.VARCHAR
     * </ul>
     *
     * @return il valore del campo Name.
     */
    public String getName() {
        return name;
    }

    /**
    * Assegna il valore passato in input al campo Name.
    * <br>
    * Il nuovo valore è assegnato solo se il nuovo valore da
    * assegnare e il valore corrente del campo sono differenti.
    * <br>
    * Meta Data:
    * <ul>
    * <li>nome colonna: users.name
    * <li>dimensione colonna: 40
    * <li>jdbc type restituito dal driver: Types.VARCHAR
    * </ul>
    *
    * @param pStrName valore da assegnare al campo Name.
    */
    public void setName(String pStrName) {
        name = pStrName;
        nameIsModified = true;
    }

    /**
     * Indica se il campo Name è stato modificato.
     *
     * @return <tt>true</tt> se il campo Name è stato modificato, <tt>false</tt> se il campo Name non è stato modificato.
     */
    public boolean isNameModified() {
        return nameIsModified;
    }

    /**
     * Restituisce il valore del campo Surname.
     * <br>
     * Meta Data:
     * <ul>
     * <li>nome colonna: users.surname
     * <li>dimensione colonna: 40
     * <li>jdbc type restituito dal driver: Types.VARCHAR
     * </ul>
     *
     * @return il valore del campo Surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
    * Assegna il valore passato in input al campo Surname.
    * <br>
    * Il nuovo valore è assegnato solo se il nuovo valore da
    * assegnare e il valore corrente del campo sono differenti.
    * <br>
    * Meta Data:
    * <ul>
    * <li>nome colonna: users.surname
    * <li>dimensione colonna: 40
    * <li>jdbc type restituito dal driver: Types.VARCHAR
    * </ul>
    *
    * @param pStrSurname valore da assegnare al campo Surname.
    */
    public void setSurname(String pStrSurname) {
        surname = pStrSurname;
        surnameIsModified = true;
    }

    /**
     * Indica se il campo Surname è stato modificato.
     *
     * @return <tt>true</tt> se il campo Surname è stato modificato, <tt>false</tt> se il campo Surname non è stato modificato.
     */
    public boolean isSurnameModified() {
        return surnameIsModified;
    }

    /**
     * Restituisce il valore del campo Email.
     * <br>
     * Meta Data:
     * <ul>
     * <li>nome colonna: users.email
     * <li>dimensione colonna: 60
     * <li>jdbc type restituito dal driver: Types.VARCHAR
     * </ul>
     *
     * @return il valore del campo Email.
     */
    public String getEmail() {
        return email;
    }

    /**
    * Assegna il valore passato in input al campo Email.
    * <br>
    * Il nuovo valore è assegnato solo se il nuovo valore da
    * assegnare e il valore corrente del campo sono differenti.
    * <br>
    * Meta Data:
    * <ul>
    * <li>nome colonna: users.email
    * <li>dimensione colonna: 60
    * <li>jdbc type restituito dal driver: Types.VARCHAR
    * </ul>
    *
    * @param pStrEmail valore da assegnare al campo Email.
    */
    public void setEmail(String pStrEmail) {
        email = pStrEmail;
        emailIsModified = true;
    }

    /**
     * Indica se il campo Email è stato modificato.
     *
     * @return <tt>true</tt> se il campo Email è stato modificato, <tt>false</tt> se il campo Email non è stato modificato.
     */
    public boolean isEmailModified() {
        return emailIsModified;
    }

    /**
     * Restituisce il valore del campo Phone.
     * <br>
     * Meta Data:
     * <ul>
     * <li>nome colonna: users.phone
     * <li>dimensione colonna: 20
     * <li>jdbc type restituito dal driver: Types.VARCHAR
     * </ul>
     *
     * @return il valore del campo Phone.
     */
    public String getPhone() {
        return phone;
    }

    /**
    * Assegna il valore passato in input al campo Phone.
    * <br>
    * Il nuovo valore è assegnato solo se il nuovo valore da
    * assegnare e il valore corrente del campo sono differenti.
    * <br>
    * Meta Data:
    * <ul>
    * <li>nome colonna: users.phone
    * <li>dimensione colonna: 20
    * <li>jdbc type restituito dal driver: Types.VARCHAR
    * </ul>
    *
    * @param pStrPhone valore da assegnare al campo Phone.
    */
    public void setPhone(String pStrPhone) {
        phone = pStrPhone;
        phoneIsModified = true;
    }

    /**
     * Indica se il campo Phone è stato modificato.
     *
     * @return <tt>true</tt> se il campo Phone è stato modificato, <tt>false</tt> se il campo Phone non è stato modificato.
     */
    public boolean isPhoneModified() {
        return phoneIsModified;
    }

    /**
     * Restituisce il valore del campo Address.
     * <br>
     * Meta Data:
     * <ul>
     * <li>nome colonna: users.address
     * <li>dimensione colonna: 255
     * <li>jdbc type restituito dal driver: Types.VARCHAR
     * </ul>
     *
     * @return il valore del campo Address.
     */
    public String getAddress() {
        return address;
    }

    /**
    * Assegna il valore passato in input al campo Address.
    * <br>
    * Il nuovo valore è assegnato solo se il nuovo valore da
    * assegnare e il valore corrente del campo sono differenti.
    * <br>
    * Meta Data:
    * <ul>
    * <li>nome colonna: users.address
    * <li>dimensione colonna: 255
    * <li>jdbc type restituito dal driver: Types.VARCHAR
    * </ul>
    *
    * @param pStrAddress valore da assegnare al campo Address.
    */
    public void setAddress(String pStrAddress) {
        address = pStrAddress;
        addressIsModified = true;
    }

    /**
     * Indica se il campo Address è stato modificato.
     *
     * @return <tt>true</tt> se il campo Address è stato modificato, <tt>false</tt> se il campo Address non è stato modificato.
     */
    public boolean isAddressModified() {
        return addressIsModified;
    }

    /**
     * Restituisce il valore del campo Enabled.
     * <br>
     * Meta Data:
     * <ul>
     * <li>nome colonna: users.enabled
     * <li>dimensione colonna: 1
     * <li>jdbc type restituito dal driver: Types.CHAR
     * </ul>
     *
     * @return il valore del campo Enabled.
     */
    public Integer getEnabled() {
        return enabled;
    }

    /**
    * Assegna il valore passato in input al campo Enabled.
    * <br>
    * Il nuovo valore è assegnato solo se il nuovo valore da
    * assegnare e il valore corrente del campo sono differenti.
    * <br>
    * Meta Data:
    * <ul>
    * <li>nome colonna: users.enabled
    * <li>dimensione colonna: 1
    * <li>jdbc type restituito dal driver: Types.CHAR
    * </ul>
    *
    * @param pStrEnabled valore da assegnare al campo Enabled.
    */
    public void setEnabled(Integer pStrEnabled) {
        enabled = pStrEnabled;
        enabledIsModified = true;
    }

    /**
     * Indica se il campo Enabled è stato modificato.
     *
     * @return <tt>true</tt> se il campo Enabled è stato modificato, <tt>false</tt> se il campo Enabled non è stato modificato.
     */
    public boolean isEnabledModified() {
        return enabledIsModified;
    }

    /**
     * Restituisce il valore del campo UltimoAccesso.
     * <br>
     * Meta Data:
     * <ul>
     * <li>nome colonna: users.ultimo_accesso
     * <li>dimensione colonna: 0
     * <li>jdbc type restituito dal driver: Types.TIMESTAMP
     * </ul>
     *
     * @return il valore del campo UltimoAccesso.
     */
    public java.sql.Timestamp getUltimoAccesso() {
        return ultimoAccesso;
    }

    /**
    * Assegna il valore passato in input al campo UltimoAccesso.
    * <br>
    * Il nuovo valore è assegnato solo se il nuovo valore da
    * assegnare e il valore corrente del campo sono differenti.
    * <br>
    * Meta Data:
    * <ul>
    * <li>nome colonna: users.ultimo_accesso
    * <li>dimensione colonna: 0
    * <li>jdbc type restituito dal driver: Types.TIMESTAMP
    * </ul>
    *
    * @param pTimestampUltimoAccesso valore da assegnare al campo UltimoAccesso.
    */
    public void setUltimoAccesso(java.sql.Timestamp pTimestampUltimoAccesso) {
        ultimoAccesso = pTimestampUltimoAccesso;
        ultimoAccessoIsModified = true;
    }

    /**
     * Indica se il campo UltimoAccesso è stato modificato.
     *
     * @return <tt>true</tt> se il campo UltimoAccesso è stato modificato, <tt>false</tt> se il campo UltimoAccesso non è stato modificato.
     */
    public boolean isUltimoAccessoModified() {
        return ultimoAccessoIsModified;
    }

    /**
     * Restituisce il valore del campo NumeroAccessi.
     * <br>
     * Meta Data:
     * <ul>
     * <li>nome colonna: users.numero_accessi
     * <li>dimensione colonna: 10
     * <li>jdbc type restituito dal driver: Types.INTEGER
     * </ul>
     *
     * @return il valore del campo NumeroAccessi.
     */
    public Integer getNumeroAccessi() {
        return numeroAccessi;
    }

    /**
    * Assegna il valore passato in input al campo NumeroAccessi.
    * <br>
    * Il nuovo valore è assegnato solo se il nuovo valore da
    * assegnare e il valore corrente del campo sono differenti.
    * <br>
    * Meta Data:
    * <ul>
    * <li>nome colonna: users.numero_accessi
    * <li>dimensione colonna: 10
    * <li>jdbc type restituito dal driver: Types.INTEGER
    * </ul>
    *
    * @param pIntNumeroAccessi valore da assegnare al campo NumeroAccessi.
    */
    public void setNumeroAccessi(Integer pIntNumeroAccessi) {
        numeroAccessi = pIntNumeroAccessi;
        numeroAccessiIsModified = true;
    }

    /**
     * Indica se il campo NumeroAccessi è stato modificato.
     *
     * @return <tt>true</tt> se il campo NumeroAccessi è stato modificato, <tt>false</tt> se il campo NumeroAccessi non è stato modificato.
     */
    public boolean isNumeroAccessiModified() {
        return numeroAccessiIsModified;
    }

    /**
     * Restituisce <tt>true</tt> se uno dei campi del bean si trova nello stato 'modificato',
     * <tt>false</tt> altrimenti.
     *
     * @return <tt>true</tt> se uno dei campi del bean si trova nello stato 'modificato',
     * <tt>false</tt> altrimenti.
     */
    public boolean isModified() {
        return idUserIsModified || usernameIsModified ||
        nameIsModified || surnameIsModified || emailIsModified ||
        phoneIsModified || addressIsModified || enabledIsModified ||
        ultimoAccessoIsModified || numeroAccessiIsModified || fullNameIsModified;
    }

    /**
     * Porta lo stato di tutti i campi a 'non modificato' e riporta a <tt>null</tt> il campo orderBy.
    
     */
    public void resetIsModified() {
        idUserIsModified = false;
        usernameIsModified = false;
        nameIsModified = false;
        surnameIsModified = false;
        emailIsModified = false;
        phoneIsModified = false;
        addressIsModified = false;
        enabledIsModified = false;
        ultimoAccessoIsModified = false;
        numeroAccessiIsModified = false;
        fullNameIsModified = false;
    }

    /**
     * Copia le proprietà del bean passato in input nel bean corrente.
     *
     * @param bean da cui copiare le proprietà.
     */
    public void copy(UsersBean pUsersBean) {
        setIdUser(pUsersBean.getIdUser());
        setUsername(pUsersBean.getUsername());
        setName(pUsersBean.getName());
        setSurname(pUsersBean.getSurname());
        setEmail(pUsersBean.getEmail());
        setPhone(pUsersBean.getPhone());
        setAddress(pUsersBean.getAddress());
        setEnabled(pUsersBean.getEnabled());
        setUltimoAccesso(pUsersBean.getUltimoAccesso());
        setNumeroAccessi(pUsersBean.getNumeroAccessi());
        setFullName(pUsersBean.getFullName());
    }

    /**
     * Ritorna la rappresentazione del bean in forma di stringa.
     *
     * @return rappresentazione del bean in forma di stringa.
     */
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     * Restituisce i nomi dei campi sui cui deve impostata la clausola ORDER BY nelle operazioni di SELECT.
     *
     * @return nomi del campi sui cui deve impostata la clausola ORDER BY nelle operazioni di SELECT.
     */
    public String getOrderBy() {
        return orderByClause;
    }

    /**
    * Assegna i nomi dei campi sui cui deve impostata la clausola ORDER BY nelle operazioni di SELECT.
    *
    * @param pStrOrderByClauses nomi dei campi sui cui deve impostata la clausola ORDER BY nelle operazioni di SELECT.
    */
    public void setOrderBy(String pStrOrderByClause) {
        orderByClause = pStrOrderByClause;
    }
    
    public List<IRolesBean> getRoles() {
		return roles;
	}

	public void setRoles(List<IRolesBean> list) {
		this.roles = list;
	}
	
	
	/**
	*Verifica se l'utente ha il ruolo passato come parametro
	*
	*@param pIdRole ruolo da cercare
	*/
	public boolean isUserInRole(Integer pIdRole) {
		for (int i = 0; i < this.roles.size(); i++) {
			if (this.roles.get(i).getIdRole() == pIdRole)
				return true;
		}
		return false;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
		fullNameIsModified = true;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @return the fullNameIsModified
	 */
	public boolean isFullNameModified() {
		return fullNameIsModified;
	}

	/**
	 * Restituisce la mappa dei parametri associata all'utente.
	 * 
	 * @return mappa dei parametri associata all'utente.
	 */
	public Map<String, Object> getParametersMap() {
		return parametersMap;
	}

	/**
	 * Assegna la mappa dei parametri associata all'utente.
	 * 
	 * @return mappa dei parametri associata all'utente.
	 */
	public void setParametersMap(Map<String, Object> parametersMap) {
		this.parametersMap = parametersMap;
	}
}
