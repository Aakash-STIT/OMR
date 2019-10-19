package omr.gui.results;

import java.util.AbstractList;

import javax.swing.table.AbstractTableModel;

import omr.GradingScheme;
import omr.Project;
import omr.QuestionGroup;
import omr.Sheet;
import omr.SheetStructure;
import omr.QuestionGroup.Orientation;

public class ResultsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    private Project project;
    static int num=1;
    public ResultsTableModel() {
    }
    
    public void setProject(Project project) {
        this.project = project;
        fireTableStructureChanged();
    }
    
    public int getColumnCount() {
        if (project == null) {
            return 3;
        } else {
            // TODO: cache this.
            
            int questionsCount = 0;
            for (QuestionGroup group : project.getSheetStructure().getQuestionGroups()) {
            	if (group.getOrientation() != Orientation.VERTICAL && group.getOrientation() != Orientation.HORIZONTAL) {
                    continue;
                }
            	
                questionsCount += group.getQuestionsCount();
            }
            
            // Filename, Student id, questions, total score
            return questionsCount + 6;
        }
        
    }
    
    public String getColumnName(int col) {
        if (col == 0) {
            return "S.No";
        } else if (col == 1) {
            return "File";
        } else if(col==2) {
        	return "Roll No";
        }else if(col==3) {
        	return "Test ID";
        }else if(col==4) {
        	return "Sid";
        }
        
        if (project == null) {
            return "Total points";
        }
        
        col -= 5;
        for (QuestionGroup group : project.getSheetStructure().getQuestionGroups()) {
            if (group.getOrientation() != Orientation.VERTICAL && group.getOrientation() != Orientation.HORIZONTAL) {
                continue;
            }
            if(group.getQuestionsCount()!=1) {
            if (col < group.getQuestionsCount()) {
                String res=Integer.toString(group.getQuestionNumber(col));
                return "q"+res;
            } else {
                col -= group.getQuestionsCount();
            }
            }
        }
        
        return "Total points";
    }
    
    public int getRowCount() {
        if (project == null) {
            return 0;
        }
        
        return project.getAnswerSheets().size();
    }
    
    public Object getValueAt(int row, int col) {
        if (project == null) {
            return null;
        }
        
        Sheet sheet = project.getAnswerSheets().get(row);
        GradingScheme grading = project.getGradingScheme();
        
        // Studentnumber in the left-most column
        if (col == 0) {
            return sheet.getSno();            
        } else if (col == 1) {
        	return sheet.getFilePath();
        }else if (col == 2) {
        	for (QuestionGroup group : project.getSheetStructure().getQuestionGroups()) {
  			  //AbstractList<QuestionGroup> s= project.getSheetStructure().getQuestionGroups(); 
  			  int l=group.getQuestionsCount(); 
  			  if(l==1) { 
  				  for(int i=0;i<l;) {
  					  String res=sheet.getChoices(group, 0);
  					  return res; }
  				  }
  			  }
        	return sheet.getStudentId();
        }else if (col == 3) {
            return sheet.getStudentId();
        }else if (col == 4) {
			
			  for (QuestionGroup group : project.getSheetStructure().getQuestionGroups()) {
			  //AbstractList<QuestionGroup> s= project.getSheetStructure().getQuestionGroups(); 
			  int l=group.getQuestionsCount(); 
			  if(l<8) { 
				  for(int i=0;i<l;) {
					  String res=sheet.getChoices(group, 0);
					  return res; }
				  }
			  }
			 
        	return sheet.getStudentId();
        }

        // Points
        col -= 5;
        for (QuestionGroup group : project.getSheetStructure().getQuestionGroups()) {
            if (group.getOrientation() != Orientation.VERTICAL && group.getOrientation() != Orientation.HORIZONTAL) {
                continue;
            }
            if(group.getQuestionsCount()!=1) {
            if (col < group.getQuestionsCount()) {
            	String res=sheet.getChoices(group,col);
            	if(res.length()>1) {
            		return null;
            	}
            	return res;
                //return grading.getScore(sheet, group, col); 
            } else {
                col -= group.getQuestionsCount();
            }
            }
        }
            
        // Total score in the last column
        return grading.getScore(sheet, project.getSheetStructure());
    }
    
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    public void setValueAt(Object value, int row, int col) {
        return;
    }
    
    public void refreshStructure() {
        fireTableStructureChanged();
    }

}
