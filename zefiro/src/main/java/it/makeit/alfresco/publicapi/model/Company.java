package it.makeit.alfresco.publicapi.model;

import com.google.api.client.util.Key;

/**
 * L'entità {@code AlfrescoCompany} modella un'azienda in Alfresco.<br/>
 * <br/>
 * Consente di interagire con l'API {@code people} di Alfresco, essendo una proprietà di
 * {@code AlfrescoPerson}.<br/>
 * 
 * @author Alessio Gaeta
 *
 */
public class Company {

	@Key
	private String organization;

	@Key
	private String address1;

	@Key
	private String address2;

	@Key
	private String postcode;

	@Key
	private String telephone;

	@Key
	private String fax;

	@Key
	private String email;

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
