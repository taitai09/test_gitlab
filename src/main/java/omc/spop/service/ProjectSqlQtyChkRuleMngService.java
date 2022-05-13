package omc.spop.service;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import omc.spop.model.ProjectSqlQtyChkRule;

public interface ProjectSqlQtyChkRuleMngService {
	public List<ProjectSqlQtyChkRule> getProjectSqlQtyChkRuleList(ProjectSqlQtyChkRule projectSqlQtyChkRule)
			throws Exception;

	public int countProjectSqlStdQtyChkSql(ProjectSqlQtyChkRule projectSqlQtyChkRule) throws Exception;

	public int insertProjectSqlQtyChkRuleApply(ProjectSqlQtyChkRule projectSqlQtyChkRule);

	public int insertProjectSqlQtyChkRule(ProjectSqlQtyChkRule projectSqlQtyChkRule) throws Exception;

	public int updateProjectSqlQtyChkRule(ProjectSqlQtyChkRule projectSqlQtyChkRule) throws Exception;

	public int deleteProjectSqlQtyChkRule(ProjectSqlQtyChkRule projectSqlQtyChkRule) throws Exception;

	public List<LinkedHashMap<String, Object>> excelDownload(ProjectSqlQtyChkRule projectSqlQtyChkRule)
			throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, SQLException;

}
