package omc.spop.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImsiTest {
	public static void main(String[] args) {
		try{
    		List<String> data = fileLineRead("D:/dbio sqls.txt");
    		
    		System.out.println("data.size() => " + data.size()); 
			int rowCnt = 0;
			for(int i = 0 ; i < data.size() ; i++){
				System.out.println("데이터 : " + data.get(i) + "["+data.get(i).indexOf(";")+"]");
				
				if(data.get(i).indexOf(";") > -1){
					rowCnt++;
				}
			}
			
			System.out.println("rowCnt => " + rowCnt);
    	}catch (Exception e){
            e.printStackTrace();
    	}		
    } 
	
	public static List<String> fileLineRead(String name) throws IOException{
		List<String> retStr = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(name));
		String s;
		while ((s = in.readLine()) != null) {
			retStr.add(s);
		}
		in.close();
		return retStr;  
	}	
}
