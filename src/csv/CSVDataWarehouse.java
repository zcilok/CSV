/*********
 * 
 * Chunyang Zhan
 * 
 * As requirements, The schema of the file would be 
 * name, city, address, age, city, zipCode
 * 
 * Assumptions:
 * 1. Since there is a repeated column(city), to make each CSVRecord meaningful
 * assume that there will be same values in two city columns
 * 2. I assume there is no records that has number of values greater than the number of schemas
 * 
 * To initialize, change the input file path in init()  
 * --I believe I can use args[],but just maintain prototype init() as given in the pdf
 * 
 * To run, it need a value from command line. args[0] is the output file path for writeToProtoBuf() function
 * 
 * 
 *********/
package csv;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import csv.csvproto.CSVData;
import csv.csvproto.CSVDataHouse;



public class CSVDataWarehouse {
	
	public static List<String> index;	//index map for index of schema
	
	/**
	 * Takes CSV file as input.
	 * Parse the input file and return a List of CSVRecords
	 */
	public static List<CSVRecord> init(){
		
		System.out.println("start initialize\n");
		index = new ArrayList<String>();
		List<CSVRecord> recordList = new LinkedList<CSVRecord>();
		
		try {    
            BufferedReader reader = new BufferedReader(new FileReader("sample.csv"));//chance to your csv file location   
            //dealing with schema
            String line = reader.readLine();//read first line to get schemas
            System.out.println("Schemas are:");//test
            String schemas[] = line.split(",");
            for(int i=0;i<schemas.length;i++){
            	index.add(schemas[i]);
            }
            System.out.println(index);

            //read CSVRecord line by line
            while((line=reader.readLine())!=null){    
            	String item[] = line.split(",");   
            	CSVRecord record = new CSVRecord();
            	for(String s:item){
            		record.addValue(s);
            	}
//            	record.print();		//show curent record for test
            	recordList.add(record);  
            	
            }
            reader.close();
		} catch (Exception e) {    
			e.printStackTrace();    
		}
		
		
		return recordList;
	}
	
	/**
	 * Returns a list of CSVRecords in Json format but sorted based on the age and filtered 
	 * based on input zip code; limit to 12;
	 * @param zipCode
	 * @param CSVList
	 * @return an array of CSVRecords in JSON format
	 */
	public static String[] getYoungestDozenFromAZipCode (String zipCode, List<CSVRecord> CSVList){
		//select 12 youngest records with the given zipcode
		PriorityQueue<CSVRecord> maxHeap = new PriorityQueue<CSVRecord>(12,(a,b)->Integer.valueOf(b.get(3))-Integer.valueOf(a.get(3)));
		for(CSVRecord r:CSVList){
			if(r.get("zipCode").equals(zipCode)){
				if(maxHeap.size()==12){
					//System.out.println("heap is full");
					//maxHeap.peek().print();
					//System.out.println();
					if(Integer.valueOf(maxHeap.peek().get("age"))>Integer.valueOf(r.get("age"))){
						maxHeap.poll();
						maxHeap.offer(r);
					}
				}else{
					maxHeap.offer(r);
				}
			}
		}
		
		//construct JSON format
		String[] resList = new String[12];
		int k = 11;
		//maxHeap.peek().print();
		while(!maxHeap.isEmpty()){
			CSVRecord cur = maxHeap.poll();
			String s = new String();
			s = s+'{';
			for(int i=0;i<cur.size();i++){
				s = s+'"';
				s = s+index.get(i);
				s = s+'"';
				s = s+':';
				s = s+'"';
				s = s+cur.get(i);
				s = s+'"';
				if(i<cur.size()-1){
					s = s+",";
				}
			}
			s = s+'}';
			//System.out.println(s);
			resList[k] = s;
			k--;
		}

		return resList;
	}
	
	/**
	 * Write the list of CSVRecords to the target file
	 * @param location args[0]
	 * @param CSVList a list of csvrecords
	 */
	public static void writeToProtoBuf(String location, List<CSVRecord> CSVList){
		CSVDataHouse.Builder dataHouse = CSVDataHouse.newBuilder();
		
		for(CSVRecord r:CSVList){
			CSVData.Builder record = CSVData.newBuilder();
			record.setName(r.get(0));
			record.setCity(r.get(1));
			record.setAddress(r.get(2));
			record.setAge(Integer.valueOf(r.get(3)));
			record.setCity2(r.get(4));
			record.setZipcode(r.get(5));
		
			dataHouse.addCsvdata(record);
		}
			
		try{
			FileOutputStream output = new FileOutputStream(location);
			dataHouse.build().writeTo(output);
			output.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Main function: implement several tests here
	 * @param args args[0] is the output file used in writeToProtoBuf
	 */
	public static void main(String[] args){
		
		//String fileName = args[0];
		List<CSVRecord> res= init();
		//show all records line by line
		System.out.println("Records are:");
		for(CSVRecord r:res){
			r.print();
		}
		System.out.println();
		
		

		//test getYoungestDozenFromAZipCod()
		String[] youngestDozen = new String[12];
		youngestDozen = getYoungestDozenFromAZipCode("13210",res);
		
		System.out.println("print youngest dozen from a zipcode");
		for(String s:youngestDozen){
			System.out.println(s);
		}
		
		
		System.out.println("write to protocol buffer");
		writeToProtoBuf(args[0],res);
		
		/*
		//test get(i)
		CSVRecord rec1 = res.get(0);
		System.out.println(rec1.get(-1));
		System.out.println(rec1.get(0));
		*/
		
		
		//test get(columnOne)
//		CSVRecord rec2 = res.get(0);
//		System.out.println(rec2.get("name"));
//		System.out.println(rec2.get("city"));
//		System.out.println(rec2.get("rua"));
		
		
		/*
		//test size()
		CSVRecord rec3 = res.get(0);
		System.out.println(rec3.size());
		*/
		
		System.out.println("test done, tear down");

	}
}
