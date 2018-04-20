/**
 * 
 */
package it.makeit.jbrick.sql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author mcallegari
 *
 */
public class BaseDaoBean {
	
	public static final String PAGE_MODE_NONE = "none";
	public static final String PAGE_MODE_CLIENT = "client";
	public static final String PAGE_MODE_Server = "server";
	
	public static final String BEAN_NAME = "beanName";
	
	//Lo decide il FE?? La servlet??
	@JsonIgnoreProperties private Integer pageNumber;
	//Lo decide il FE?? La servlet??
	@JsonIgnoreProperties private Integer pageSize;
	//Lo valorizza il Manager
	@JsonIgnoreProperties private Integer recordCount;
	//Lo decide il FE?? La servlet??
	@JsonIgnoreProperties private String pageMode;
	//Clausola di order by per i record
	@JsonIgnoreProperties private String orderBy = null;


	/**
	 * @return the pageNumber
	 */
	public Integer getPageNumber() {
		return pageNumber;
	}
	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the recordCount
	 */
	public Integer getRecordCount() {
		return recordCount;
	}
	/**
	 * @param recordCount the recordCount to set
	 */
	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}
	/**
	 * @return the pageMode
	 */
	public String getPageMode() {
		return pageMode;
	}
	/**
	 * @param pageMode the pageMode to set
	 */
	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}

	/**
     * Restituisce i nomi dei campi sui cui deve impostata la clausola ORDER BY nelle operazioni di SELECT.
     *
     * @return nomi del campi sui cui deve impostata la clausola ORDER BY nelle operazioni di SELECT.
     */
    public String getOrderBy() {
        return orderBy;
    }

     /**
    * Assegna i nomi dei campi sui cui deve impostata la clausola ORDER BY nelle operazioni di SELECT.
    *
    * @param pStrOrderByClauses nomi dei campi sui cui deve impostata la clausola ORDER BY nelle operazioni di SELECT.
    */
    public void setOrderBy(String pStrOrderBy) {
    		orderBy = pStrOrderBy;
    }
}
