package omc.spop.service.impl;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import omc.spop.base.SessionManager;
import omc.spop.dao.ProjectSqlQtyChkRuleMngDao;
import omc.spop.model.ProjectSqlQtyChkRule;
import omc.spop.service.ProjectSqlQtyChkRuleMngService;
import omc.spop.utils.CryptoUtil;
import oracle.sql.CLOB;

/***********************************************************
 * 2018.08.23 임호경 최초작성
 **********************************************************/

@Service("projectSqlQtyChkRuleMngService")
public class ProjectSqlQtyChkRuleMngServiceImpl implements ProjectSqlQtyChkRuleMngService {
	private static final Logger logger = LoggerFactory.getLogger(ProjectSqlQtyChkRuleMngServiceImpl.class);
	private String key = "openmade";

	@Autowired
	private ProjectSqlQtyChkRuleMngDao projectSqlQtyChkRuleMngDao;

	@Value("#{defaultConfig['max_length_of_cell_contents']}")
	private int maxLengthOfCellContents;

	@Override
	public List<ProjectSqlQtyChkRule> getProjectSqlQtyChkRuleList(ProjectSqlQtyChkRule projectSqlQtyChkRule)
			throws Exception {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		logger.debug("auth_cd :" + auth_cd);

		List<ProjectSqlQtyChkRule> resultList = projectSqlQtyChkRuleMngDao
				.getProjectSqlQtyChkRuleList(projectSqlQtyChkRule);

		if (auth_cd.equals("ROLE_OPENPOPMANAGER")) {
			for (ProjectSqlQtyChkRule rule : resultList) {
				String qty_chk_sql = rule.getQty_chk_sql();
				try {
					qty_chk_sql = CryptoUtil.decryptAES128(qty_chk_sql, key);
					rule.setQty_chk_sql(qty_chk_sql);
				}catch(Exception e) {
					rule.setQty_chk_sql("복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.");
				}
			}
		}
		return resultList;
	}

	@Override
	public int countProjectSqlStdQtyChkSql(ProjectSqlQtyChkRule projectSqlQtyChkRule) throws Exception {
		return projectSqlQtyChkRuleMngDao.countProjectSqlStdQtyChkSql(projectSqlQtyChkRule);
	}

	@Override
	public int insertProjectSqlQtyChkRuleApply(ProjectSqlQtyChkRule projectSqlQtyChkRule) {
		int cnt = projectSqlQtyChkRuleMngDao.countProjectSqlStdQtyChkSql(projectSqlQtyChkRule);
		if (cnt <= 0) {
			return projectSqlQtyChkRuleMngDao.insertProjectSqlQtyChkRuleApply(projectSqlQtyChkRule);
		} else {
			return 0;
		}
	}

	@Override
	public int insertProjectSqlQtyChkRule(ProjectSqlQtyChkRule projectSqlQtyChkRule) throws Exception {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		logger.debug("auth_cd :" + auth_cd);

		String qty_chk_sql = projectSqlQtyChkRule.getQty_chk_sql();
		logger.debug("qty_chk_sql :" + qty_chk_sql);
		if (auth_cd.equals("ROLE_OPENPOPMANAGER")) {
			qty_chk_sql = CryptoUtil.encryptAES128(qty_chk_sql, key);
			projectSqlQtyChkRule.setQty_chk_sql(qty_chk_sql);
		}
		return projectSqlQtyChkRuleMngDao.insertProjectSqlQtyChkRule(projectSqlQtyChkRule);
	}

	@Override
	public int updateProjectSqlQtyChkRule(ProjectSqlQtyChkRule projectSqlQtyChkRule) throws Exception {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		logger.debug("auth_cd :" + auth_cd);

		String qty_chk_idt_cd = projectSqlQtyChkRule.getQty_chk_idt_cd();
		String project_id = projectSqlQtyChkRule.getProject_id();
		String qty_chk_sql = projectSqlQtyChkRule.getQty_chk_sql();
		logger.debug("qty_chk_idt_cd :" + qty_chk_idt_cd);
		logger.debug("project_id :" + project_id);
		logger.debug("qty_chk_sql :" + qty_chk_sql);
		if (auth_cd.equals("ROLE_OPENPOPMANAGER")) {
			qty_chk_sql = CryptoUtil.encryptAES128(qty_chk_sql, key);
			projectSqlQtyChkRule.setQty_chk_sql(qty_chk_sql);
		}
		return projectSqlQtyChkRuleMngDao.updateProjectSqlQtyChkRule(projectSqlQtyChkRule);
	}

	@Override
	public int deleteProjectSqlQtyChkRule(ProjectSqlQtyChkRule projectSqlQtyChkRule) throws Exception {
		return projectSqlQtyChkRuleMngDao.deleteProjectSqlQtyChkRule(projectSqlQtyChkRule);
	}

	@Override
	public List<LinkedHashMap<String, Object>> excelDownload(ProjectSqlQtyChkRule projectSqlQtyChkRule)
			throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, SQLException {
		String auth_cd = SessionManager.getLoginSession().getUsers().getAuth_cd();
		logger.debug("auth_cd :" + auth_cd);
		List<LinkedHashMap<String, Object>> dataList = projectSqlQtyChkRuleMngDao.excelDownload(projectSqlQtyChkRule);
		String qty_chk_sql;
		for (Map<String, Object> dataListRow : dataList) {
			CLOB clob = (oracle.sql.CLOB) dataListRow.get("QTY_CHK_SQL");
			if (auth_cd.equals("ROLE_OPENPOPMANAGER")) {
				
				try {
					qty_chk_sql = CryptoUtil.decryptAES128(clob.stringValue(), key);
				}catch(Exception e) {
					qty_chk_sql="복호화하는 과정에서 에러가 발생하였습니다. 해당 RULE을 수정해 주세요.";
				}				
			} else {
				qty_chk_sql = clob.stringValue();
				if (qty_chk_sql.length() > maxLengthOfCellContents) {
					qty_chk_sql = qty_chk_sql.substring(0, maxLengthOfCellContents);
				}
			}
			dataListRow.put("QTY_CHK_SQL", qty_chk_sql);
		}
		return dataList;
	}

}
