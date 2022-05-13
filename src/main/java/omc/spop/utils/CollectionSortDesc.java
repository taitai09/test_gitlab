package omc.spop.utils;

import java.util.Comparator;


import omc.spop.model.OdsHistSqlstat;

public class CollectionSortDesc implements Comparator<OdsHistSqlstat> {

	@Override
	public int compare(OdsHistSqlstat o1, OdsHistSqlstat o2) {  //내림차순
		return (Double.valueOf(o2.getElapsed_time()))
				.compareTo(Double.valueOf(o1.getElapsed_time()));
	}

}
class CollectionSortAsc implements Comparator<OdsHistSqlstat> {
	
	@Override
	public int compare(OdsHistSqlstat o1, OdsHistSqlstat o2) { // 오름차순
		return (Double.valueOf(o2.getElapsed_time()))
				.compareTo(Double.valueOf(o1.getElapsed_time()));
	}
	
}




