package it.makeit.jbrick;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;

/**
 * Classe singleton deputata alla lettura del file di configurazione
 * jbrick2Config.xml.
 * 
 * @author MAKE IT
 */
/**
 * @author frametta
 *
 */
public final class JBrickConfigManager {

	private static Map<String, JBrickConfigManager> mMapConfigManager = new HashMap();
	public static final String CONFIG_FILENAME = "jbrickConfig.xml";
	private static final String ENCODING = "UTF-8";
	private XMLConfiguration mXMLConfiguration;
	private static Log mLog = Log.getInstance(JBrickConfigManager.class);
	private String mStrConfigFileName;

	/**
	 * Restituisce l'istanza ConfigManager creandola se non esiste.
	 * Questo è il metodo usato in tutto il codice generato e quindi
	 * in ambiente di runtime guida sempre il nostro amico jbrick2config.xml.
	 * @param pStrBasePath
	 *            cartella di configurazione
	 * @return istanza di ConfigManager.
	 * @throws ConfigException
	 */
	
	//Non è  più un singleton! i metodi getInstance restano per garantire la trasparenza rispetto agli utilizzi precedenti
	public static JBrickConfigManager getInstance() throws JBrickException {
		JBrickConfigManager lConfigManager = getInstance("/"+CONFIG_FILENAME);
		if (lConfigManager == null) {
			mLog.error(CONFIG_FILENAME , " non trovato");
			throw new JBrickException(JBrickException.FATAL);
		}
		return lConfigManager;
	}
	
	public static JBrickConfigManager getInstanceForProcess(String pStrProcessName) throws JBrickException {
		String lStrCfgFileName = "/it/makeit/"+pStrProcessName.toLowerCase()+"/ProcessConfig.xml";
		JBrickConfigManager lConfigManager = getInstance(lStrCfgFileName);
		if (lConfigManager == null) {
			mLog.debug(lStrCfgFileName , " non trovato, uso quello generale");
			lConfigManager = getInstance();
		}
		return lConfigManager;
	}
	
	private static JBrickConfigManager getInstance(String pStrCfgFileName) throws JBrickException {
		JBrickConfigManager lConfigManager = mMapConfigManager.get(pStrCfgFileName);
		if (lConfigManager == null) {
			mLog.debug(pStrCfgFileName , " nullo");
			InputStream lInputStream = JBrickConfigManager.class.getResourceAsStream(pStrCfgFileName);
			if (lInputStream != null) {
				mLog.debug(pStrCfgFileName , " trovato");
				lConfigManager = new JBrickConfigManager(lInputStream, pStrCfgFileName);
				mMapConfigManager.put(pStrCfgFileName, lConfigManager);
			}
		} else {
			mLog.debug(pStrCfgFileName , " trovato in memoria");
		}
		return lConfigManager;
	}
	
	private JBrickConfigManager(InputStream pInputStream, String pStrCfgFileName) {
		this.setStrConfigFileName(pStrCfgFileName);
		readConfiguration(pInputStream);
	}
	private void readConfiguration(InputStream pInputStream) {
		mXMLConfiguration = getXMLConfiguration(pInputStream);
		mLog.debug("Configuration read from file " + this.getStrConfigFileName());
	}

	/**
	 * Restituisce l'istanza ConfigManager di un generico file di configurazione di cui l'utilizzatore
	 * deve indicare la posizione assoluta sul filesystem.
	 * I casi tipici di utilizzo di questo factory sono sostanzialmente legati alla gestione
	 * dei test (sorgente dati diretta!) e delle datasource per ireport. 
	 * Con ireport, infatti, esistono grosse difficoltà per la lettura del 
	 * file jbrick2config.xml standard a causa di un classloader che "litiga" con 
	 * le logiche normali utilizzate da commons-configuration.
	 * Siccome sia questo metodo che il getInstance() normale ragionano sul fatto di fare
	 * cache della configurazione e settano mXMLConfiguration, mBlnCache bisogna fare molta
	 * attenzione perchè i due metodi possono "pestarsi i piedi". In tutti i casi, come regola,
	 * questo metodo è da utilizzare sono nei casi "particolari" sopra menzionati: test, report.
	 * Proprio per questa ragione ignora la configurazione della cache e setta mBlnCache a true
	 * in modo da non poter essere sovrascritto dalle getInstance() a valle nello stack di esecuzione!
	 * 
	 * @param pStrConfigurationFilePath che è il path assoluto al file di configurazione
	 * @return istanza di ConfigManager.
	 */
	
	//Da usare solo per test
	@Deprecated
	public static JBrickConfigManager getInstanceFromAbsolutFilePath(String pStrConfigurationFilePath) {
		return new JBrickConfigManager(pStrConfigurationFilePath);
	}

	private JBrickConfigManager(String pStrConfigurationFilePath) {
        mXMLConfiguration = new XMLConfiguration();
        mXMLConfiguration.setFileName(pStrConfigurationFilePath);
        mXMLConfiguration.setExpressionEngine(new XPathExpressionEngine());
		try {
			mXMLConfiguration.load();
		} catch (ConfigurationException e) {
			mLog.error(e,"Error while reading configuration from file ",this.getStrConfigFileName());
			throw new JBrickException(e, "jBrickException.configManager.errorWhileReadingConfiguration", e.getMessage());
		}
		mLog.debug("ConfigManager initialized using file ", pStrConfigurationFilePath);
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private XMLConfiguration getXMLConfiguration(InputStream pInputStream) {

		XMLConfiguration lXMLConfiguration = null;

		lXMLConfiguration = new XMLConfiguration();
		lXMLConfiguration.setEncoding(ENCODING);

		try {
			lXMLConfiguration.load(pInputStream);
			lXMLConfiguration.setExpressionEngine(new XPathExpressionEngine());
		} catch (ConfigurationException e) {
			mLog.error(e,"Error while reading configuration from file ",this.getStrConfigFileName());
			throw new JBrickException(e, "jBrickException.configManager.errorWhileReadingConfiguration", this.getStrConfigFileName(), e.getMessage());
		}
		return lXMLConfiguration;
	}

	/** Metodo che restituisce il valore della proprietà passata come parametro, recuperandolo dal file di 
	 * configurazione. Il nome della proprietà deve rispettare l'XPath convention. In particolare:
	 * 
	 * - se la proprietà è un nodo occorre specificare il path completo del nodo
	 * - se la proprietà è un attributo occorre specificare il path completo del nodo cui fa riferimento 
	 * seguito da "@" e "nomeAttributo". Eg. getProperty("node/@attributo").
	 * @param pStrProperty la proprietà di cui reperire il valore
	 * @return il valore trovato.
	 */
	public String getProperty(String pStrProperty){
		mLog.debug("Start to retrieve " + pStrProperty + " property");
		String lStrProperty = mXMLConfiguration.getString(pStrProperty);
		mLog.debug("Returning ",lStrProperty);
		return lStrProperty;
	}
	
	public Object getPropertyObject(String pStrProperty){
		return mXMLConfiguration.getProperty(pStrProperty);
	}
	
	/** Metodo che restituisce il valore in forma di lista della proprietà passata come parametro, 
	 *  recuperandolo dal file di configurazione. Il separatore di default è la virgola. 
	 *  Il nome della proprietà deve rispettare l'XPath convention. In particolare:
	 * 
	 * - se la proprietà è un nodo occorre specificare il path completo del nodo
	 * - se la proprietà è un attributo occorre specificare il path completo del nodo cui fa riferimento 
	 * seguito da "@" e "nomeAttributo". Eg. getProperty("node/@attributo").
	 * @param pStrProperty la proprietà di cui reperire il valore
	 * @return il valore trovato.
	 */
	public List<String> getPropertyAsList(String pStrProperty){
		mLog.debug("Start to retrieve " + pStrProperty + " property");
		List<String> lListProperties = mXMLConfiguration.getList(pStrProperty);
		if (lListProperties!=null && lListProperties.size()>0) {
			for (String lStrProperty : lListProperties) {
				mLog.debug("Returning ",lStrProperty);
			}
		}
		
		return lListProperties;
	}
	
	
	
	
	
	/** Metodo che restituisce il valore della proprietà passata come parametro, recuperandolo dal file di 
	 * configurazione. Il nome della proprietà deve rispettare l'XPath convention. In particolare:
	 * 
	 * - se la proprietà è un nodo occorre specificare il path completo del nodo
	 * - se la proprietà è un attributo occorre specificare il path completo del nodo cui fa riferimento 
	 * seguito da "@" e "nomeAttributo". Eg. getProperty("node/@attributo").
	 * 
	 * Se non è presente alcuna proprietà con quel nome, viene lanciata una jbrickException.
	 * @param pStrProperty la proprietà di cui reperire il valore
	 * @return il valore trovato.
	 */
	public String getMandatoryProperty(String pStrProperty){
		mLog.debug("Start to retrieve " + pStrProperty + " mandatory property");
		String lString = getProperty(pStrProperty);
		
		if (lString == null){
			mLog.error("Property " + pStrProperty + " not found");
			throw new JBrickException("jBrickException.configManager.getProperty.PropertyNotFound", this.getStrConfigFileName(), pStrProperty);
		}
		mLog.debug("Returning ",lString);
		return lString;
	}
	
	/**Metodo che restituisce il numero di nodi figli di un elemento del file xml.
	 * @param pStrNodeParentPath l'XPath del nodo di cui si vuole conoscere il numero di figli
	 * @return il numero di nodi figli
	 */
	@SuppressWarnings("unchecked")
	public int getNodeChildNumber(String pStrNodeParentPath){
		List<HierarchicalConfiguration> lListNodeChild;
		lListNodeChild = mXMLConfiguration.configurationsAt(pStrNodeParentPath);
		return lListNodeChild.size();
	}
	
	/** 
	 * Metodo che restituisce, recuperandolo dal file di 
	 * configurazione, l'array del valore della proprietà passata in input 
	 * dei nodi che soddisfano la condizione passata in input. Quindi l'XPATH deve fare 
	 * riferimento AL NODO e non ALLA RADICE.
	 * 
	 * Ad esempio in una configurazione:</br>
	 * <config>
	 * 	<node name="nome1" type="typeA"/> 
	 *  <node name="nome2" type="typeB"/>
	 *  <node name="nome3" type="typeA"/>
	 *  <node name="nome4" type="typeB"/>
	 * </config>
	 * 
	 * la chiamata getPropertyList("node[@type='typeA']", "@name")
	 * restituisce un array di due stringhe: "nome1", "nome3". 
	 *  
	 * 
	 * Il nome della proprietà e la condizione devono rispettare l'XPath convention. In particolare:
	 * 
	 * - se la proprietà è un nodo occorre specificare il path completo del nodo
	 * - se la proprietà è un attributo occorre specificare il path completo del nodo cui fa riferimento 
	 * seguito da "@" e "nomeAttributo". Eg. getProperty("node/@attributo").
	 * </br>
	 * 
	 * @param pStrCondition condizione con cui selezionare le proprietà.
	 * @param pStrProperty la proprietà di cui reperire il valore.
	 * @return array dei valori delle proprietà trovate.
	 */
	@SuppressWarnings("unchecked")
	public String[] getPropertyList(String pStrCondition, String pStrProperty) {
		List<HierarchicalConfiguration> lListNodeChild;
		String[] lStrProperties;
		int lIntPropertiesFound = 0;
		     
		mLog.debug("Start getPropertyList(String,String)");
		mLog.debug("condition=",pStrCondition);
		mLog.debug("property=",pStrProperty);
		lStrProperties = null;
		lListNodeChild = mXMLConfiguration.configurationsAt(pStrCondition);
		if (lListNodeChild!=null && lListNodeChild.size()>0) {
			lIntPropertiesFound = lListNodeChild.size();
			lStrProperties = new String[lIntPropertiesFound];
			for (int i = 0; i < lStrProperties.length; i++) {
				lStrProperties[i] = lListNodeChild.get(i).getString(pStrProperty);
			}
		} 
		mLog.debug("Found ",String.valueOf(lIntPropertiesFound)," properties.");
		mLog.debug("End getPropertyList(String,String)");
		
		return lStrProperties;
	}
	
	/**Metodo che restituisce il nome del nodo figlio di un elemento del file xml.
	 * @param pStrNodeParentPath l'XPath del nodo di cui si vuole conoscere il nome del figlio
	 * @return il nome del nodo figlio
	 */	
	@SuppressWarnings("unchecked")
	public String getNodeChildName(String pStrNodeParentPath){
		mLog.debug("Start getNodeChildName");
		List<HierarchicalConfiguration> lListNodeChild = mXMLConfiguration.configurationsAt(pStrNodeParentPath+"/child::*");
		HierarchicalConfiguration lHierarchicalConfiguration = lListNodeChild.get(0);
		String lStrChildName = lHierarchicalConfiguration.getRootNode().getName();
		mLog.debug("Found "+lStrChildName+ " for parent "+ pStrNodeParentPath);
		return lStrChildName;
	}

	protected String getStrConfigFileName() {
		return mStrConfigFileName;
	}

	protected void setStrConfigFileName(String mStrConfigFileName) {
		this.mStrConfigFileName = mStrConfigFileName;
	}
}
