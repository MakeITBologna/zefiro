package it.makeit.alfresco.publicapi.model;

import com.google.api.client.util.Key;


/**
 * L'entità {@code Person} modella un utente Alfresco.<br/>
 * <br/>
 * Consente di interagire con l'API {@code people} di Alfresco.<br/>
 * <br/>
 * People are the users of Alfresco. A person entity describes the user as they are known to
 * Alfresco. There are API methods to get the sites a person is a member of, to get the details of a
 * person, their favorite sites, preferences, and networks they are a member of. Methods are also
 * available to process activities related to a person.<br/>
 * <br/>
 * @author Alessio Gaeta
 *
 */
public class Person extends Entry {

	/**
	 * Is this person currently enabled?
	 */
	@Key
	private boolean enabled;

	/**
	 * The person's last name
	 */
	@Key
	private String lastName;

	/**
	 * The person's location or address
	 */
	@Key
	private String location;

	/**
	 * The id of the person's avatar
	 */
	@Key
	private String avatarId;

	/**
	 * The person's instant message Id
	 */
	@Key
	private String instantMessageId;

	/**
	 * The person's Google Id
	 */
	@Key
	private String googleId;

	/**
	 * The person's {@code personId}<br/>
	 * <br/>
	 * The email address with which the person registered.
	 */
	@Key
	private String id;

	/**
	 * The person's Skype Id
	 */
	@Key
	private String skypeId;

	/**
	 * The person's description
	 */
	@Key
	private String description;

	/**
	 * An embedded company object describing the person's company
	 */
	@Key
	private Company company;

	/**
	 * The person's first name
	 */
	@Key
	private String firstName;

	/**
	 * 	The person's telephone number
	 */
	@Key
	private String telephone;

	/**
	 * The person's job title
	 */
	@Key
	private String jobTitle;

	/**
	 * 	The person's mobile number
	 */
	@Key
	private String mobile;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(String avatarId) {
		this.avatarId = avatarId;
	}

	public String getInstantMessageId() {
		return instantMessageId;
	}

	public void setInstantMessageId(String instantMessageId) {
		this.instantMessageId = instantMessageId;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
