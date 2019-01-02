package com.sven.security;

import com.sven.security.dao.DataSourceDao;
import com.sven.security.dao.UserDao;
import com.sven.security.dao.UserRoleDao;
import com.sven.security.vo.DataSource;
import com.sven.security.vo.User;
import com.sven.security.vo.UserRole;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecurityApplication.class)
public class SecurityApplicationTests {

    private static Log log = LogFactory.getLog(SecurityApplicationTests.class);


    @Test
    public void testDaos(){
        testUserRoleDao();
        testUserDao();
        testDataSourceDao();
    }


    @Autowired
    private DataSourceDao dataSourceDao;

    @Test
    @Transactional
    public void testDataSourceDao(){
        int tag = 0;
        DataSource dataSourceOri = new DataSource();
        dataSourceOri.setDataSourceId("dataSourceId");
        dataSourceOri.setType("type");
        dataSourceOri.setUrl("url");
        dataSourceOri.setUserName("userName");
        dataSourceOri.setPassword("password");

        DataSource dataSource = dataSourceDao.findByKey(dataSourceOri.getDataSourceId());
        if(dataSource == null){
            dataSource = new DataSource();
            dataSource.setDataSourceId(dataSourceOri.getDataSourceId());
            tag ++;
            swapDataSources(dataSource, dataSourceOri, tag);
            dataSourceDao.insert(dataSource);
        }
        List<DataSource> dataSourceList = dataSourceDao.findListByKey(dataSourceOri.getDataSourceId());
        Assert.assertEquals(dataSourceList.size(), 1);
        assertDataSourceEqual(dataSourceDao.findByKey(dataSourceOri.getDataSourceId()), dataSourceOri);

        tag ++;
        swapDataSources(dataSource, dataSourceOri, tag);
        dataSourceDao.update(dataSource);
        assertDataSourceEqual(dataSourceDao.findByKey(dataSourceOri.getDataSourceId()), dataSourceOri);

        tag++;
        swapDataSources(dataSource, dataSourceOri, tag);
        dataSourceDao.updateFields(dataSource, new String[]{ "type", "url", "user_name", "password"});
        assertDataSourceEqual(dataSourceDao.findByKey(dataSourceOri.getDataSourceId()), dataSourceOri);

        tag++;
        swapDataSources(dataSource, dataSourceOri, tag);
        dataSourceDao.save(dataSource);
        assertDataSourceEqual(dataSourceDao.findByKey(dataSourceOri.getDataSourceId()), dataSourceOri);

        dataSourceDao.deleteByKey(dataSourceOri.getDataSourceId());
        Assert.assertSame(null, dataSourceDao.findByKey(dataSourceOri.getDataSourceId()));
        dataSourceList = dataSourceDao.findListByKey(dataSourceOri.getDataSourceId());
        Assert.assertEquals(dataSourceList.size(), 0);
    }

    private void swapDataSources(DataSource dataSource, DataSource dataSourceOri, int tag) {
        dataSourceOri.setType(dataSourceOri.getType() + tag);
        dataSourceOri.setUrl(dataSourceOri.getUrl() + tag);
        dataSourceOri.setUserName(dataSourceOri.getUserName() + tag);
        dataSourceOri.setPassword(dataSourceOri.getPassword() + tag);

        dataSource.setType(dataSourceOri.getType());
        dataSource.setUrl(dataSourceOri.getUrl());
        dataSource.setUserName(dataSourceOri.getUserName());
        dataSource.setPassword(dataSourceOri.getPassword());

    }

    private void assertDataSourceEqual(DataSource dataSource, DataSource dataSourceOri) {
        Assert.assertEquals(dataSource.getDataSourceId(), dataSourceOri.getDataSourceId());
        Assert.assertEquals(dataSource.getType(), dataSourceOri.getType());
        Assert.assertEquals(dataSource.getUrl(), dataSourceOri.getUrl());
        Assert.assertEquals(dataSource.getUserName(), dataSourceOri.getUserName());
        Assert.assertEquals(dataSource.getPassword(), dataSourceOri.getPassword());

    }

    @Autowired
    private UserRoleDao userRoleDao;

    @Test
    @Transactional
    public void testUserRoleDao(){
        int tag = 0;

        UserRole userRoleOri = new UserRole();

        userRoleOri.setUserRoleId("userRoleId");
        userRoleOri.setRoleDesc("roleDesc");

        UserRole userRole = userRoleDao.findByKey(userRoleOri.getUserRoleId());
        if(userRole == null){
            userRole = new UserRole();
            userRole.setUserRoleId(userRoleOri.getUserRoleId());
            tag ++;
            swapObjects(userRole, userRoleOri, tag);
            userRoleDao.insert(userRole);
        }
        List<UserRole> userRoleList = userRoleDao.findListByKey(userRoleOri.getUserRoleId());
        Assert.assertEquals(userRoleList.size(), 1);
        assertObjecs(userRoleDao.findByKey(userRoleOri.getUserRoleId()), userRoleOri);

        tag ++;
        swapObjects(userRole, userRoleOri, tag);
        userRoleDao.update(userRole);
        assertObjecs(userRoleDao.findByKey(userRoleOri.getUserRoleId()), userRoleOri);

        tag++;
        swapObjects(userRole, userRoleOri, tag);
        userRoleDao.updateFields(userRole, new String[]{"user_role_id","role_desc"});
        assertObjecs(userRoleDao.findByKey(userRoleOri.getUserRoleId()), userRoleOri);

        tag++;
        swapObjects(userRole, userRoleOri, tag);
        userRoleDao.save(userRole);
        assertObjecs(userRoleDao.findByKey(userRoleOri.getUserRoleId()), userRoleOri);

        userRoleDao.deleteByKey(userRoleOri.getUserRoleId());
        Assert.assertSame(null, userRoleDao.findByKey(userRoleOri.getUserRoleId()));
        userRoleList = userRoleDao.findListByKey(userRoleOri.getUserRoleId());
        Assert.assertEquals(userRoleList.size(), 0);
    }

    private void swapObjects(UserRole userRole, UserRole userRoleOri, int tag) {
        userRoleOri.setRoleDesc(userRoleOri.getRoleDesc() + tag);

        userRole.setRoleDesc(userRoleOri.getRoleDesc());
    }

    private void assertObjecs(UserRole userRole, UserRole userRoleOri) {
        Assert.assertEquals(userRole.getUserRoleId(), userRoleOri.getUserRoleId());
        Assert.assertEquals(userRole.getRoleDesc(), userRoleOri.getRoleDesc());
    }

    @Autowired
    private UserDao userDao;

    @Test
    @Transactional
    public void testUserDao(){
        int tag = 0;
        User userOri = new User();
        userOri.setUserId("userId");
        userOri.setPassword("password");
        userOri.setUserAlias("userAlias");
        userOri.setRoles("roles");
        userOri.setActive(0);

        tag ++;
        User user = userDao.findByKey(userOri.getUserId());
        if(user == null){
            user = new User();
            user.setUserId(userOri.getUserId());

            swapUsers(user, userOri, tag);
            userDao.insert(user);
        }
        List<User> userList = userDao.findListByKey(userOri.getUserId());
        Assert.assertEquals(userList.size(), 1);
        assertUserEqual(userDao.findByKey(userOri.getUserId()), userOri);

        tag ++;
        swapUsers(user, userOri, tag);
        userDao.update(user);
        assertUserEqual(userDao.findByKey(userOri.getUserId()), userOri);

        tag++;
        swapUsers(user, userOri, tag);

        userDao.updateFields(user, new String[]{"password", "user_alias", "roles", "active"});
        assertUserEqual(userDao.findByKey(userOri.getUserId()), userOri);

        tag++;
        swapUsers(user, userOri, tag);
        userDao.save(user);
        assertUserEqual(userDao.findByKey(userOri.getUserId()), userOri);

        userDao.deleteByKey(userOri.getUserId());
        Assert.assertSame(null, userDao.findByKey(userOri.getUserId()));
        userList = userDao.findListByKey(userOri.getUserId());
        Assert.assertEquals(userList.size(), 0);
    }

    private void swapUsers(User user, User userOri, int tag) {
        userOri.setPassword(userOri.getPassword() + tag);
        userOri.setUserAlias(userOri.getUserAlias() + tag);
        userOri.setRoles(userOri.getRoles() + tag);
        userOri.setActive(userOri.getActive() + tag);

        user.setPassword(userOri.getPassword());
        user.setUserAlias(userOri.getUserAlias());
        user.setRoles(userOri.getRoles());
        user.setActive(userOri.getActive());

    }

    private void assertUserEqual(User user, User userOri) {
        Assert.assertEquals(user.getUserId(), userOri.getUserId());
        Assert.assertEquals(user.getPassword(), userOri.getPassword());
        Assert.assertEquals(user.getUserAlias(), userOri.getUserAlias());
        Assert.assertEquals(user.getRoles(), userOri.getRoles());
        Assert.assertEquals(user.getActive(), userOri.getActive());
    }

    @Test
    public void genrateTestUsers(){
//       String[] users = new String[];

    }
}
