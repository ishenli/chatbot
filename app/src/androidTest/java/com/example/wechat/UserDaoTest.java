package com.example.wechat;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wechat.repository.dao.UserDao;
import com.example.wechat.repository.database.AppDatabase;
import com.example.wechat.repository.entity.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;


// UserDaoTest.java
@RunWith(AndroidJUnit4.class)
public class UserDaoTest {

    private UserDao userDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
//        db = Room.databaseBuilder(context, AppDatabase.class, "wechat_test.db").build();
        userDao = db.userDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertAndGetUser() throws Exception {
        User user = new User();
        user.setNickname("John Doe");
        userDao.insert(user);
        List<User> users = userDao.getAllUsers();
        assertEquals(users.get(0).getNickname(), "John Doe");
    }

    @Test
    public void getAllUsers() throws Exception {
        User user1 = new User();
        user1.setNickname("John Doe");
        userDao.insert(user1);

        User user2 = new User();
        user2.setNickname("Jane Doe");
        userDao.insert(user2);

        List<User> users = userDao.getAllUsers();
        assertEquals(users.size(), 2);
    }

//    @Test
//    public void deleteAllUsers() throws Exception {
//        User user1 = new User();
//        user1.setName("John Doe");
//        user1.setEmail("john.doe@example.com");
//        userDao.insert(user1);
//
//        User user2 = new User();
//        user2.setName("Jane Doe");
//        user2.setEmail("jane.doe@example.com");
//        userDao.insert(user2);
//
//        userDao.deleteAll();
//        List<User> users = userDao.getAllUsers();
//        assertTrue(users.isEmpty());
//    }
}