package omc.spop.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import omc.spop.model.SelftunPlanTable;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
public class TreeTest {
    public static void main(String[] args) {
    	try{
    		List<SelftunPlanTable> list = new ArrayList<SelftunPlanTable>();
    		List<String> data = fileLineRead("D:/tree2.txt");
			
			for(int i = 0 ; i < data.size() ; i++){
				String[] tempArry = data.get(i).split("\\|");
				
				SelftunPlanTable temp = new SelftunPlanTable();

				temp.setId(tempArry[0].trim());
				temp.setParent_id(tempArry[1].trim());
				temp.setText(tempArry[2].trim());
				temp.setQblock_name(tempArry[3].trim());
				temp.setPartition_start(tempArry[4].trim());
				temp.setDistribution(tempArry[5].trim());
				temp.setAccess_predicates(tempArry[6].trim());
				temp.setFilter_predicates(tempArry[7].trim());
				
				list.add(temp);
			}

			List<SelftunPlanTable> treeNodes = buildtree(list,"-1");
            JSONArray jsonArray = JSONArray.fromObject(treeNodes);
            
            String json = jsonArray.toString();
			System.out.println("json -> " + json);
			
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
	
	
	private static List<SelftunPlanTable> buildtree(List<SelftunPlanTable> list, String parent_id){
		List<SelftunPlanTable> targetList = new ArrayList<SelftunPlanTable>();
		
		for(SelftunPlanTable treeNode : list){
			SelftunPlanTable selftunPlanTable = new SelftunPlanTable();
			SelftunPlanTable attributes = new SelftunPlanTable();
			
			selftunPlanTable.setId(treeNode.getId());
			selftunPlanTable.setParent_id(treeNode.getParent_id());
			selftunPlanTable.setText(treeNode.getText());

			attributes.setQblock_name(treeNode.getQblock_name());
			attributes.setPartition_start(treeNode.getPartition_start());
			attributes.setDistribution(treeNode.getDistribution());
			attributes.setAccess_predicates(treeNode.getAccess_predicates());
			attributes.setFilter_predicates(treeNode.getFilter_predicates());
			
			selftunPlanTable.setAttributes(attributes);

			if(parent_id.equals(treeNode.getParent_id())){
				selftunPlanTable.setChildren(buildtree(list, selftunPlanTable.getId()));
				targetList.add(selftunPlanTable);
			}
		}
		
		return targetList;
	}
}
