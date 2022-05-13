package omc.spop.service;

import java.util.LinkedHashMap;
import java.util.List;

import omc.spop.model.Result;
import omc.spop.model.SQLEditor;

/***********************************************************
 * 2019.12.05 명성태 OPENPOP V2 최초작업
 **********************************************************/

public interface SQLEditorService {
	List<LinkedHashMap<String, Object>> retrieve(SQLEditor sqlEditor) throws Exception;
	
	List<LinkedHashMap<String, Object>> executeQuery(SQLEditor sqlEditor) throws Exception;
	
	Result executeUpdate(SQLEditor sqlEditor) throws Exception;
	
	Result execute(SQLEditor sqlEditor) throws Exception;
	
	List<LinkedHashMap<String, Object>> excelDownRetrieve(SQLEditor sqlEditor) throws Exception;
	
	Result disConn(SQLEditor sqlEditor);
}
