package application.exp.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import application.student.Student;


public class ImportService {
	
	
	
	public List<Student>  getStudentListFromExcel(File file) throws FileNotFoundException, IOException{
		List<Student> list = new ArrayList<Student>();
		try (InputStream is = new FileInputStream(file); ReadableWorkbook wb = new ReadableWorkbook(is)) {
		    Sheet sheet = wb.getFirstSheet();
		    List<Row> rows = sheet.read();
		    int rSize = rows.size();
		    for (int i = 1; i < rSize; i++) {
		    	Student s = new Student(rows.get(i).getCellText(2).trim(), rows.get(i).getCellText(3).trim(), rows.get(i).getCellText(4).trim().substring(0,1), rows.get(i).getCellText(1).trim(), rows.get(i).getCellText(6).trim(), rows.get(i).getCellText(5).trim());
		    	list.add(s);
			}
		}
		return list;
	}
	

	

}
