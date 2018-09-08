package cn.smbms.dao.user;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import cn.smbms.dao.BaseDao;
import cn.smbms.pojo.User;

public class UserDaoTest {
	private Logger logger = Logger.getLogger(UserDaoTest.class);
	private UserDao userDao;
	
	@Before
	public void setUp()throws Exception{
		userDao = new UserDaoImpl();
	}
	@Test
	public void testAdd() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLoginUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUserList() {
		logger.info("testGetUserList");
		Connection connection = null;
		List<User> userList = null;
		String userName = null;
		int userRole = 0;
		int currentPageNo = 1;
		int pageSize = 5;
	
		try {
			connection = BaseDao.getConnection();
			userList = userDao.getUserList(connection, userName, userRole, currentPageNo, pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		for(User user:userList){
			logger.info("testGetUserList--> id: " + user.getId() + ", userCode: " + user.getUserCode() + ", userName: " + user.getUserName());
		}
		
	}

	@Test
	public void testGetUserCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteUserById() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUserById() {
		fail("Not yet implemented");
	}

	@Test
	public void testModify() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdatePwd() {
		fail("Not yet implemented");
	}

}
