package omr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;



import org.apache.poi.ss.usermodel.Sheet;

/*import jxl.Workbook;
import jxl.write.*;*/
import omr.QuestionGroup.Orientation;
import omr.gui.results.ResultsTableModel;

/**
 * Saves answers and results as comma separated value (CSV) files. 
 */
public class CsvSerializer {
	
	private ResultsTableModel resultsTableModel;

	/*
	 * public void saveAnswers(Project project, File file) throws IOException {
	 * 
	 * SheetStructure structure = project.getSheetStructure();
	 * 
	 * PrintStream fout = null; try { fout = new PrintStream(new FileOutputStream
	 * (file));
	 * 
	 * // Write header line fout.print("image,studentId"); for (QuestionGroup group
	 * : structure.getQuestionGroups()) { // Skip the studentnumber if
	 * (group.getOrientation() != Orientation.VERTICAL && group.getOrientation() !=
	 * Orientation.HORIZONTAL) { continue; }
	 * 
	 * // Question numbers int questionsCount = group.getQuestionsCount(); int
	 * indexOffset = group.getIndexOffset(); for (int i = 0; i < questionsCount;
	 * i++) { fout.print(","); fout.print(indexOffset + i); } } fout.println();
	 * 
	 * // Iterate throuh sheets for (Sheet sheet : project.getSheetsContainer()) {
	 * // Image filename fout.print(sheet.getFileName()); fout.print(",");
	 * 
	 * // Studentnumber String studentId = sheet.getStudentId(); if (studentId ==
	 * null || studentId.length() < 1) { fout.print(sheet.getId()); } else {
	 * fout.print(studentId); }
	 * 
	 * // Print answers for (QuestionGroup group : structure.getQuestionGroups()) {
	 * // Skip the studentnumber if (group.getOrientation() != Orientation.VERTICAL
	 * && group.getOrientation() != Orientation.HORIZONTAL) { continue; }
	 * 
	 * int questionsCount = group.getQuestionsCount(); for (int i = 0; i <
	 * questionsCount; i++) { fout.print(","); String choices =
	 * sheet.getChoices(group, i); if (choices != null) { fout.print(choices); } } }
	 * 
	 * fout.println(); } } finally { if (fout != null) { fout.close(); } } }
	 */
    
    public void saveResults(Project project, File file) throws IOException {
		
		  SheetStructure structure = project.getSheetStructure(); 
		/*
		 * GradingScheme grading = project.getGradingScheme(); PrintStream fout = null;
		 * 
		 * Workbook wb = new HSSFWorkbook(); CreationHelper createhelper =
		 * wb.getCreationHelper(); Sheet sheet = (Sheet) wb.createSheet("results"); Row
		 * row = null; Cell cell = null;
		 * 
		 * for (int i=0;i<1;) { row = sheet.createRow(i); for (int
		 * j=0;j<dtm.getColumnCount();j++) {
		 * 
		 * cell = row.createCell(j); cell.setCellValue("S No."); } }
		 */
		 
        project.getSheetsContainer().getSheet(project.getSheetStructure().getReferenceSheet().getFileName());
        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("results");
		//sheet.setDefaultColumnWidth(30);
        sheet.autoSizeColumn(0);

		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		CellStyle style2= workbook.createCellStyle();
		Font font = workbook.createFont();
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font.setFontName("Calibri");
		font.setBold(false);
		font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		style.setFont(font);
		
		// create header row
		HSSFRow header = sheet.createRow(0);

		header.createCell(0).setCellValue("S No.");
		header.getCell(0).setCellStyle(style);

		header.createCell(1).setCellValue("File");
		header.getCell(1).setCellStyle(style);

		header.createCell(2).setCellValue("Roll No.");
		header.getCell(2).setCellStyle(style);

		header.createCell(3).setCellValue("Test ID");
		header.getCell(3).setCellStyle(style);

		header.createCell(4).setCellValue("SID");
		header.getCell(4).setCellStyle(style);
		
		for (QuestionGroup group : structure.getQuestionGroups()) {
        	// Skip the studentnumber
        	if (group.getOrientation() != Orientation.VERTICAL && group.getOrientation() != Orientation.HORIZONTAL) {
        		continue;
        	}
        	
            int questionsCount = group.getQuestionsCount();
            int indexOffset = group.getIndexOffset();
            for (int i = 0; i < questionsCount; i++) {
            	header.createCell(i+5).setCellValue("q"+(indexOffset+i));
        		header.getCell(i+5).setCellStyle(style);
               
            }
        }
		 int rowCount = 1;
		for (omr.Sheet sheet1 : project.getSheetsContainer()) {
            // Image filename
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(sheet1.getSno());
			aRow.createCell(1).setCellValue(sheet1.getFilePath());
			for (QuestionGroup group : project.getSheetStructure().getQuestionGroups()) {
	            if (group.getOrientation() != Orientation.VERTICAL && group.getOrientation() != Orientation.HORIZONTAL) {
	                continue;
	            }
	            for(int i=0;i<group.getQuestionsCount();i++) {
		            
		            	String res=sheet1.getChoices(group,i);
		            	if(res.length()>1) {
		            		
		            	}else {
		            		aRow.createCell(i+5).setCellValue(res);	
		            	//return grading.getScore(sheet, group, col); 
		            	}
		            
	            }
	        }
            		 
		FileOutputStream out = new FileOutputStream(file.getPath().concat(".xls"));
        workbook.write(out);
        out.close();
        workbook.close();
        
        
		/*
		 * try {
		 * 
		 * WritableWorkbook workbook1 = Workbook.createWorkbook(file); WritableSheet
		 * sheet1 = workbook1.createSheet("First Sheet", 0); TableModel model = new
		 * ResultsTableModel();
		 * 
		 * for (int i = 0; i < model.getColumnCount(); i++) { Label column = new
		 * Label(i, 0, model.getColumnName(i)); sheet1.addCell(column); } int j = 0; for
		 * (int i = 0; i < model.getRowCount(); i++) { for (j = 0; j <
		 * model.getColumnCount(); j++) { Label row = new Label(j, i + 1,
		 * model.getValueAt(i, j).toString()); sheet1.addCell(row); } }
		 * workbook1.write(); workbook1.close(); } catch (Exception ex) {
		 * ex.printStackTrace(); }
		 */

        
		/*
		 * try { fout = new PrintStream(new FileOutputStream (file));
		 * 
		 * // Write header line //fout.print("image,studentId");
		 * fout.print("S.No,File,Roll No,Test ID,SID"); for (QuestionGroup group :
		 * structure.getQuestionGroups()) { // Skip the studentnumber if
		 * (group.getOrientation() != Orientation.VERTICAL && group.getOrientation() !=
		 * Orientation.HORIZONTAL) { continue; }
		 * 
		 * int questionsCount = group.getQuestionsCount(); int indexOffset =
		 * group.getIndexOffset(); for (int i = 0; i < questionsCount; i++) {
		 * fout.print(","); fout.print(indexOffset + i); } } fout.print(",total");
		 * fout.println();
		 * 
		 * for (Sheet sheet : project.getSheetsContainer()) { // Image filename
		 * fout.print(sheet.getSno()); fout.print(","); fout.print(sheet.getFilePath());
		 * fout.print(",");
		 * 
		 * // Student id String studentId = sheet.getStudentId(); if (studentId == null
		 * || studentId.length() < 1) { fout.print(sheet.getId()); } else {
		 * fout.print(studentId); }
		 * 
		 * String studentId2 = sheet.getStudentId(); if (studentId == null ||
		 * studentId.length() < 1) { fout.print(sheet.getId()); } else {
		 * fout.print(studentId); } String studentId3 = sheet.getStudentId(); if
		 * (studentId == null || studentId.length() < 1) { fout.print(sheet.getId()); }
		 * else { fout.print(studentId); } // Print points for (QuestionGroup group :
		 * structure.getQuestionGroups()) { // Skip the studentnumber if
		 * (group.getOrientation() != Orientation.VERTICAL && group.getOrientation() !=
		 * Orientation.HORIZONTAL) { continue; }
		 * 
		 * int questionsCount = group.getQuestionsCount(); for (int i = 0; i <
		 * questionsCount; i++) { //double score = grading.getScore(sheet, group, i);
		 * String res=sheet.getChoices(group, i); fout.print(","); fout.print(res); } }
		 * 
		 * // Total score in the last column double totalScore = grading.getScore(sheet,
		 * project.getSheetStructure()); fout.print(","); fout.print(totalScore);
		 * 
		 * fout.println(); } } finally { if (fout != null) { fout.close(); } }
		 */
    
    }}
}
