package assessment7;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class framework {
 public static String vSearch;
 public static int xlRows,xlCols;
 public static String xData[][];
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		
		framework f=new framework();
		f.web();
	
	}
	
	public void web() throws Exception {
		//Reading excel file
		xlRead("C:\\Users\\gkulkarni29\\Documents\\YahooDDF.xls");
		
		for(int i=1;i<xlRows;i++) {
			//Reading the data where flag is Y
			if(xData[i][1].equals("Y")) {
			System.setProperty("webdriver.chrome.driver","C:\\jars\\chromedriver.exe");
			WebDriver driver=new ChromeDriver();
		String title=null;
		vSearch=xData[i][0];
		driver.get("https://in.yahoo.com/?p=us");
		driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div/div/div/div[2]/div/div/form/table/tbody/tr/td[1]/input[1]")).sendKeys(vSearch);
		driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div/div/div/div[2]/div/div/form/table/tbody/tr/td[2]/button")).click();
		title=driver.getTitle();
		System.out.println(title);
		
		xData[i][2]=title;
		//writing the title into the result Excel sheet
		xlwrite("C:\\Users\\gkulkarni29\\Documents\\YahooDDFResults.xls", xData);
		driver.close();
		}
		
		}
	}
	//Excel read function
	public static void xlRead(String sPath) throws Exception
	{
		File myFile=new File(sPath);
		FileInputStream myStream=new FileInputStream(myFile);
		HSSFWorkbook myworkbook=new HSSFWorkbook(myStream);
		HSSFSheet mySheet=myworkbook.getSheetAt(0);
		xlRows=mySheet.getLastRowNum()+1;
		xlCols=mySheet.getRow(0).getLastCellNum();
		xData=new String[xlRows][xlCols];
		for(int i=0;i<xlRows;i++)
		{
			HSSFRow row=mySheet.getRow(i);
			for(short j=0;j<xlCols;j++)
			{
				HSSFCell cell=row.getCell(j);
				String value=cellToString(cell);
				xData[i][j]=value;
				System.out.print("-"+xData[i][j]);
			}
			System.out.println();
		}
	}
	
	
		public static String cellToString(HSSFCell cell)
		{
			int type=cell.getCellType();
			Object result;
			switch(type)
			{
			case HSSFCell.CELL_TYPE_NUMERIC:
			result=cell.getNumericCellValue();
			break;
			case HSSFCell.CELL_TYPE_STRING:
			result=cell.getStringCellValue();
			break;
			case HSSFCell.CELL_TYPE_FORMULA:
			throw new RuntimeException("We cannot evaluate formula");
			case HSSFCell.CELL_TYPE_BLANK:
			result="-";
			case HSSFCell.CELL_TYPE_BOOLEAN:
			result=cell.getBooleanCellValue();
			case HSSFCell.CELL_TYPE_ERROR:
			result="This cell has some error";
			default:
			throw new RuntimeException("We do not support this cell type");
			}
			return result.toString();
			
		}
		
		//Excel write function
		public static void xlwrite(String xlpath1,String[][] xData) throws Exception
		{
			System.out.println("Inside XL Write");
			File myFile1=new File(xlpath1);
			FileOutputStream fout=new FileOutputStream(myFile1);
			HSSFWorkbook wb=new HSSFWorkbook();
			HSSFSheet mySheet1=wb.createSheet("TestResults");
			for(int i=0;i<xlRows;i++)
			{
				HSSFRow row1=mySheet1.createRow(i);
				for(short j=0;j<xlCols;j++)
				{
					HSSFCell cell1=row1.createCell(j);
					cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell1.setCellValue(xData[i][j]);
				}
			}
			wb.write(fout);
			fout.flush();
			fout.close();
		}


}
