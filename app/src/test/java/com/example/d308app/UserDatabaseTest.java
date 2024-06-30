package com.example.d308app;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.runner.AndroidJUnit4;


import com.example.d308app.dao.UserDAO;
import com.example.d308app.database.UserDatabase;
import com.example.d308app.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@RunWith(RobolectricTestRunner.class)
public class UserDatabaseTest {

    private UserDatabase userDatabase;
    private UserDAO userDAO;

    @Test
    @Config(manifest=Config.NONE)
    @Before
    public void setUp() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));

        System.setOut(originalOut);
        String logOutput = bos.toString();



        Context context = ApplicationProvider.getApplicationContext();
        userDatabase = Room.inMemoryDatabaseBuilder(context, UserDatabase.class)
                // Add logging for database creation
                .allowMainThreadQueries() // This is for testing only, don't use in production
                .build();

        userDAO = userDatabase.userDao();

    }


    @After
    public void tearDown() {
        userDatabase.close();
    }

    @Test
    @Config(manifest=Config.NONE)
    public void insertUser() {
        User user = new User(1, "testuser", "password");
        userDAO.registerUser(user);
        User retrievedUser = userDAO.getUserById(1);
        assertNotNull(retrievedUser);
        assertEquals(user.getUserName(), retrievedUser.getUserName());
    }
    @Test
    @Config(manifest=Config.NONE)
    public void updateUser() {
        User user = new User(2, "testuser", "password");
        userDAO.registerUser(user);

        user.setUserName("updatedUser");
        userDAO.update(user);

        User updatedUser = userDAO.getUserById(2);
        assertNotNull(updatedUser);
        assertEquals("updatedUser", updatedUser.getUserName());
    }

}
