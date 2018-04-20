/**
 * 
 */
package it.makeit.jbrick.print;

import it.makeit.jbrick.Constants;
import it.makeit.jbrick.JBrickException;
import it.makeit.jbrick.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * @author frametta
 * 
 *         Classe di utility per la stampa di report.
 */

public class PrintUtil {

	// chiave della parameters map in cui passiamo ai report il path
	// assoluto di esecuzione (noto solo alle servlet grazie al context)
	// e che risulta necessario per la brutta gestione di jasper delle
	// location dei subreport, delle immagini, ...
	public static final String C_BASEPATH = "basepath";

	private static Log mLog = Log.getInstance(PrintUtil.class);

	/**
	 * Questo metodo genera l'output di un report in base al formato richiesto.
	 * Per generare il report necessita di un file compilato .jasper o di un
	 * file jrxml che provvede lui a compilare. Qualora non fosse presente
	 * nessuno dei due file viene lanciata una eccezione. Il report compilato
	 * .jasper viene riempito con l'array di Bean ricevuto in ingresso. Vengono
	 * inoltre aggiunti i parametri presenti nella parametersMap e viene settato
	 * anche il relativo resource Bundle sulla base della Locale di sessione. In
	 * base al formato viene generato un foglio excel o un file pdf.
	 * 
	 * @param pStrReportFile
	 *            NOT null path completo del file senza suffisso .jasper o
	 *            .jrxml
	 * @param pBeanArray
	 *            NOT null l'array di Bean che rappresenta la sorgente dati
	 * 
	 * @param pParametersMap
	 *            NOT null i parametri da aggiungere al report
	 * @param pLocale
	 *            NOT null la Locale attualmente a sessione
	 * @param pOutputStream
	 *            NOT null l'output su cui scrivere il report
	 * @param pPrintFormat
	 *            NOT null il formato del report da generare.
	 */
	public static void printBeanArray(String pStrReportFile,
			Object[] pBeanArray, Map<String, Object> pParametersMap,
			Locale pLocale, OutputStream pOutputStream, PrintFormat pPrintFormat) {

		mLog.debug("Start printBeanArray with report ", pStrReportFile);

		// Non posso essere chiamato con un null!
		if (pStrReportFile == null || pParametersMap == null || pLocale == null
				|| pOutputStream == null || pPrintFormat == null) {
			throw new IllegalArgumentException("Null argument not allowed!!");
		}

		JasperPrint lJasperPrint = null;

		File lReportFile = new File(pStrReportFile + "."
				+ pPrintFormat.toString().toLowerCase() + ".jasper");
		File lJrxmlFile = new File(pStrReportFile + "."
				+ pPrintFormat.toString().toLowerCase() + ".jrxml");
		boolean lReportFileExists = lReportFile.exists();
		boolean lJrxmlFileExists = lJrxmlFile.exists();

		if (!lReportFileExists
				|| (lReportFile.lastModified() < lJrxmlFile.lastModified())) {

			mLog.debug("File .jasper not found or obsolete. Trying to compile "
					+ lJrxmlFile.getName());

			if (lJrxmlFileExists) {

				try {
					JasperCompileManager.compileReportToFile(
							lJrxmlFile.getPath(), lReportFile.getPath());
					mLog.debug("File .jasper compiled");

				} catch (Exception e) {
					mLog.error(e, "Not able to compile .jasper");
					throw new JBrickException(e, e.getMessage());
				}

			} else {
				mLog.error("File jrxml not found");
				throw new JBrickException(JBrickException.IO_EXCEPTION);
			}
		}

		mLog.debug(lReportFile.getPath());
		JasperReport lJasperReport;
		try {
			lJasperReport = (JasperReport) JRLoader.loadObject(lReportFile);

		} catch (JRException e) {
			mLog.error(e, "Not able to load .jasper");
			throw new JBrickException(e, e.getMessage());
		}
		mLog.debug("Loaded the .jasper");

		// Se il BeanArray è vuoto o null, uso la EmptyDataSource di Jasper
		JRDataSource lJRDataSource;
		if (pBeanArray == null || pBeanArray.length == 0) {
			lJRDataSource = new JREmptyDataSource();
		} else {
			lJRDataSource = new JRBeanArrayDataSource(pBeanArray);
		}
		mLog.debug("Created Bean Array Data Source");
		// Sistemiamo i problemi di locale
		pParametersMap.put(JRParameter.REPORT_LOCALE, pLocale);
		ResourceBundle lResourceBundle = ResourceBundle.getBundle(
				Constants.RESOURCEBUNDLE, pLocale);
		pParametersMap.put(JRParameter.REPORT_RESOURCE_BUNDLE, lResourceBundle);
		mLog.debug("Added ResourceBundle");

		try {
			lJasperPrint = JasperFillManager.fillReport(lJasperReport,
					pParametersMap, lJRDataSource);
		} catch (JRException e) {
			mLog.error(e, "Not able to fill the report");
			throw new JBrickException(e, e.getMessage());
		}
		mLog.debug("Report filled");

		printBeanArray(lJasperPrint, pOutputStream, pPrintFormat);

		mLog.debug("End of export Report");

	}

	public static void printBeanArray(JasperPrint pJasperPrint,
			OutputStream pOutputStream, PrintFormat pPrintFormat) {
		mLog.debug("Begin printBeanArray(JasperPrint pJasperPrint,..)");
		if (pPrintFormat.equals(PrintFormat.PDF)) {

			try {
				JasperExportManager.exportReportToPdfStream(pJasperPrint,
						pOutputStream);
			} catch (JRException e) {
				mLog.error(e, "Not able to print the report");
				throw new JBrickException(e, e.getMessage());
			}

		}
		// if (pPrintFormat.equals(PrintFormat.XLS)) {
		//
		// JExcelApiExporter exporterXLS = new JExcelApiExporter();
		// exporterXLS.setParameter(JExcelApiExporterParameter.JASPER_PRINT,
		// pJasperPrint);
		// exporterXLS.setParameter(JExcelApiExporterParameter.OUTPUT_STREAM,
		// pOutputStream);
		// exporterXLS.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE,
		// true);
		// exporterXLS.setParameter(JExcelApiExporterParameter.IS_IGNORE_CELL_BORDER,
		// true);
		// exporterXLS.setParameter(JExcelApiExporterParameter.IS_IGNORE_GRAPHICS,
		// true);
		// exporterXLS.setParameter(JExcelApiExporterParameter.IS_COLLAPSE_ROW_SPAN,
		// true);
		// exporterXLS.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
		// true);
		// exporterXLS.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
		// true);
		// exporterXLS.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND,
		// false);
		// mLog.debug("Parameter setted for exporterXLS");
		// try {
		// exporterXLS.exportReport();
		//
		// } catch (JRException e) {
		// mLog.error(e, "Not able to fill Xls File");
		// throw new jBrickException(e, e.getMessage());
		// }
		// }
		if (pPrintFormat.equals(PrintFormat.HTML)) {
			JRHtmlExporter exporterHTML = new JRHtmlExporter();
			exporterHTML.setParameter(JRHtmlExporterParameter.JASPER_PRINT,
					pJasperPrint);
			exporterHTML.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM,
					pOutputStream);
			exporterHTML.setParameter(
					JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);
			mLog.debug("Parameter setted for exporterHTML");
			try {
				exporterHTML.exportReport();

			} catch (JRException e) {
				mLog.error(e, "Not able to fill html File");
				throw new JBrickException(e, e.getMessage());
			}
		}
		if (pPrintFormat.equals(PrintFormat.RTF)) {
			JRRtfExporter exporterRTF = new JRRtfExporter();
			exporterRTF.setParameter(JRHtmlExporterParameter.JASPER_PRINT,
					pJasperPrint);
			exporterRTF.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM,
					pOutputStream);
			mLog.debug("Parameter setted for exporterRTF");
			try {
				exporterRTF.exportReport();
			} catch (JRException e) {
				mLog.error(e, "Not able to fill html File");
				throw new JBrickException(e, e.getMessage());
			}

		}

		mLog.debug("End printBeanArray(JasperPrint pJasperPrint,..)");
	}

	/**
	 * Questo metodo genera l'output di un report in base al formato richiesto.
	 * Per generare il report necessita di un file compilato .jasper presente
	 * nel classpath e delle nome del pkg contenente le immagini
	 * 
	 * @param pStrReportFile
	 *            NOT null path completo del file senza suffisso .jasper o
	 *            .jrxml
	 * @param pBeanArray
	 *            NOT null l'array di Bean che rappresenta la sorgente dati
	 * @param pParametersMap
	 *            NOT null i parametri da aggiungere al report
	 * @param pLocale
	 *            NOT null la Locale attualmente a sessione
	 * @param pOutputStream
	 *            NOT null l'output su cui scrivere il report
	 * @param pPrintFormat
	 *            NOT null il formato del report da generare.
	 */
	public static void printBeanArrayFromClasspath(String pStrReportFilePath,
			Object[] pBeanArray, Map<String, Object> pParametersMap,
			Locale pLocale, OutputStream pOutputStream, PrintFormat pPrintFormat) {

		mLog.debug("Start printBeanArray with report ", pStrReportFilePath);

		// Non posso essere chiamato con un null!

		if (pStrReportFilePath == null || pParametersMap == null
				|| pLocale == null || pOutputStream == null
				|| pPrintFormat == null) {
			throw new IllegalArgumentException("Null argument not allowed!!");
		}

		JasperPrint lJasperPrint = null;

		JasperReport lJasperReport;
		try {
			lJasperReport = (JasperReport) JRLoader.loadObject(PrintUtil.class
					.getResourceAsStream(pStrReportFilePath));

		} catch (JRException e) {
			mLog.error(e, "Not able to load .jasper");
			throw new JBrickException(e, e.getMessage());
		}
		mLog.debug("Loaded the .jasper");

		// Se il BeanArray è vuoto o null, uso la EmptyDataSource di Jasper
		JRDataSource lJRDataSource;
		if (pBeanArray == null || pBeanArray.length == 0) {
			lJRDataSource = new JREmptyDataSource();
		} else {
			lJRDataSource = new JRBeanArrayDataSource(pBeanArray);
		}
		mLog.debug("Created Bean Array Data Source");
		// Sistemiamo i problemi di locale
		pParametersMap.put(JRParameter.REPORT_LOCALE, pLocale);
		ResourceBundle lResourceBundle = ResourceBundle.getBundle(
				Constants.RESOURCEBUNDLE, pLocale);
		pParametersMap.put(JRParameter.REPORT_RESOURCE_BUNDLE, lResourceBundle);
		mLog.debug("Added ResourceBundle");

		try {
			lJasperPrint = JasperFillManager.fillReport(lJasperReport,
					pParametersMap, lJRDataSource);
		} catch (JRException e) {
			mLog.error(e, "Not able to fill the report");
			throw new JBrickException(e, e.getMessage());
		}
		mLog.debug("Report filled");

		printBeanArray(lJasperPrint, pOutputStream, pPrintFormat);

		mLog.debug("End of export Report");

	}

	public static void printXLSBeanArray(String pStrReportFile,
			ArrayList<String> pArrayStrHeader, ArrayList<Map> pArrayList,
			Locale pLocale, OutputStream pOutputStream, String pDataFormat) {

		mLog.debug("Start printXLSBeanArray with report ", pStrReportFile);

		HSSFWorkbook lWorkbook = new HSSFWorkbook();
		HSSFSheet lSheet = lWorkbook.createSheet();

		// definisco lo stile delle celle
		HSSFFont lFont = lWorkbook.createFont();
		lFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle lStyle = lWorkbook.createCellStyle();
		lStyle.setFont(lFont);
		lStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		
		//	definisco l'header della tabella di report
		HSSFRow lRowHeader = lSheet.createRow(0);
		for(int x = 0; x < pArrayStrHeader.size(); x++) {
			HSSFCell lCellHeader = lRowHeader.createCell(x);
			lCellHeader.setCellValue(pArrayStrHeader.get(x));
			lCellHeader.setCellStyle(lStyle);
		}
		
		//	riempio i campi della tabella di report
		for (int i = 0; i < pArrayList.size(); i++) {
			HSSFRow lRow = lSheet.createRow(i+1);
			Map lMap = pArrayList.get(i);
			Iterator lIterator = lMap.keySet().iterator();
			Integer lIntIndexIterator = 0;
			while (lIterator.hasNext()) {
				Object lObjKey = lIterator.next();
				HSSFCell lCell = lRow.createCell(lIntIndexIterator);
				if (lMap.get(lObjKey) instanceof Date){
				HSSFCellStyle lCellStyle = lWorkbook.createCellStyle();
				lCellStyle.setDataFormat(lWorkbook.createDataFormat().getFormat(pDataFormat));
				lCell.setCellValue((Date) lMap.get(lObjKey));
				lCell.setCellStyle(lCellStyle);
				}
				else if (lMap.get(lObjKey) instanceof Boolean)
					lCell.setCellValue((Boolean) lMap.get(lObjKey));
				else if (lMap.get(lObjKey) instanceof String)
					lCell.setCellValue((String) lMap.get(lObjKey));
				else if (lMap.get(lObjKey) instanceof Double)
					lCell.setCellValue((Double) lMap.get(lObjKey));
				else if (lMap.get(lObjKey) instanceof Integer)
					lCell.setCellValue((Integer) lMap.get(lObjKey));

				lIntIndexIterator++;
			}

		}

		//	Faccio in modo che le celle si adattino alla dimensione corretta
		for (int i = 0; i < lSheet.getRow(0).getPhysicalNumberOfCells(); i++) {
			lSheet.autoSizeColumn(i);
		}

		try {
			lWorkbook.write(pOutputStream);
			pOutputStream.close();
		} catch (FileNotFoundException e) {
			mLog.error(e, "Not able to fill the report");
			throw new JBrickException(e, e.getMessage());
		} catch (IOException e) {
			mLog.error(e, "Not able to fill the report");
			throw new JBrickException(e, e.getMessage());
		}

		mLog.debug("Report filled");

		mLog.debug("End of export Report");
	}
}
