package com.airtelbpo.bpoutils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.airtelbpo.pojo.AirtelNumberData;
import java.sql.Connection;

public class CsvFileReader {
	
		//Delimiter used in CSV file
		private static Connection jdbcConnection;
		private static final String COMMA_DELIMITER = ",";
		//Student attributes index
		private static final int NUMBER_ID = 0;
		private static final int NUMBER_VALUE = 1;
		
		public static void readCsvFile(String fileName) {
			BufferedReader fileReader = null;
	     
	        try {
	        	
	        	//Create a new list of student to be filled by CSV file data 
	        	ArrayList<AirtelNumberData> numberDumpList = new ArrayList();
	        	
	            String line = "";
	            
	            //Create the file reader
	            fileReader = new BufferedReader(new FileReader(fileName));
	            
	            //Read the CSV file header to skip it
	            fileReader.readLine();
	            
	            //Read the file line by line starting from the second line
	            while ((line = fileReader.readLine()) != null) {
	                //Get all tokens available in line
	                String[] tokens = line.split(COMMA_DELIMITER);
	                if (tokens.length > 0) {
	                	//Create a new student object and fill his  data
						AirtelNumberData numberRecord = new AirtelNumberData(tokens[NUMBER_ID], tokens[NUMBER_VALUE]);
						numberDumpList.add(numberRecord);
					}
	            }
	            
	            //Print the new number list
	            for (AirtelNumberData numberDta : numberDumpList) {
					System.out.println(numberDta.getId()+"_"+numberDta.getNumber());
				}
	            
	            if(BpoUtils.importNumberDump(numberDumpList) != 0) {
	            	System.out.println("Import Dump Successful");
	            	//BpoUtils.setNumberDumpToUsers();
	            }
	        } 
	        catch (Exception e) {
	        	System.out.println("Error in CsvFileReader !!!");
	            e.printStackTrace();
	        } finally {
	            try {
	                fileReader.close();
	            } catch (IOException e) {
	            	System.out.println("Error while closing fileReader !!!");
	                e.printStackTrace();
	            }
	        }

		}
}