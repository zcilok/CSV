/*********
 * 
 * Chunyang Zhan
 * 
 * As requirements, The schema of the file would be 
 * name, city, address, age, city, zipCode
 * 
 * Since there is a repeated column(city), to make each CSVRecord meaningful
 * assume that there will be same values in two city columns
 * 
 * This class is used to edit/store one CSVRecord
 * 
 *********/
package csv;

import java.util.ArrayList;
import java.util.List;

public class CSVRecord{
	private List<String> values;	//store one line record
	
	public CSVRecord(){
		values = new ArrayList<String>();
	}
	
	/**
	 * add a value into record
	 * @param s : value to be added
	 */
	public void addValue(String s){
		values.add(s);
	}
	
	/**
	 * print a CSVRecord
	 */
	public void print(){
		for(String s:values){
			System.out.print(s+",");
		}
		System.out.print("\n");
	}
	
	/**
	 * returns the value at index "i" in the record
	 * if the given index is out of bound, an exception raised
	 * @param i: index number
	 * @return value in the index
	 */
	public String get(int i){
		String value="";
		try{
			value = values.get(i);
		}catch(Exception e){
			System.out.println("index is out of bound");
			e.getMessage();
		}
		return value;
	}
	
	/**
	 * returns value for the column named "columnOne"
	 * @param columnOne
	 * @return value in that column
	 */
	public String get(String columnOne){
		for(int i=0;i<CSVDataWarehouse.index.size();i++){
			//System.out.println(CSVDataWarehouse.index.get(i).charAt(0));
			if(columnOne.equals(CSVDataWarehouse.index.get(i))){
				return values.get(i);
			}
		}
		System.out.println("no such column");
		return "";
	}
	
	/**
	 * returns the size of CSVRecord
	 * @return
	 */
	public int size(){
		return values.size();
	}
	
}
