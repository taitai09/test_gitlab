package omc.spop.security;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextListener;

import omc.spop.base.Role;
import omc.spop.dao.CommonDao;
import omc.spop.model.Login;
import omc.spop.model.Users;

/***********************************************************
 * 2017.08.10 이원식 최초작성
 **********************************************************/

@Transactional
public class LoginDetailsService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(LoginDetailsService.class);

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private HttpServletRequest request;

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException, DataAccessException {

		Login login = new Login();
		Users paramUsers = new Users();

		String user_id = request.getParameter("user_id");
		String default_wrkjob_cd = request.getParameter("default_wrkjob_cd");
		String default_auth_grp_id = request.getParameter("default_auth_grp_id");
		String default_auth_grp_cd = request.getParameter("default_auth_grp_cd");

		paramUsers.setUser_id(user_id);
		paramUsers.setAuth_cd(default_auth_grp_cd);
		paramUsers.setWrkjob_cd(default_wrkjob_cd);
		commonDao = sqlSession.getMapper(CommonDao.class);
		Users result = commonDao.login(paramUsers);
//		System.out.println("#######인터셉터 로그인 핸들러1#######"+paramUsers.toString());

		// 사용자 정보 체크
		if (login != null) {
			login.setUsername(result.getUser_id());
			login.setPassword(result.getPassword());
			login.setUsers(result);

			/* UUID 추가 */
			String uuid = UUID.randomUUID().toString().replace("-", "");
			login.setUuid(uuid);

			Role role = new Role();
			role.setName(result.getAuth_cd());

			List<Role> roles = new ArrayList<Role>();
			roles.add(role);
			login.setAuthorities(roles);
//			System.out.println("#######인터셉터 로그인 핸들러1(Authorities)#######"+login.getAuthorities().toString());
//			System.out.println("#######인터셉터 로그인 핸들러1(roles)#######"+roles.toString());
//			System.out.println("#######인터셉터 로그인 핸들러1(Authorities.size)#######"+roles.size());
		}
		return login;
	}
}