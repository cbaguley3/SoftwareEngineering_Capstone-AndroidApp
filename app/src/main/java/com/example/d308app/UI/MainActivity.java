package com.example.d308app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308app.R;
import com.example.d308app.dao.UserDAO;
import com.example.d308app.database.Repository;
import com.example.d308app.database.UserDatabase;
import com.example.d308app.entities.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public static int numAlert;
    private Repository repository;

    // Registration code
    EditText userName, password, name, editUsername, editPassword;
    Button register;

    public FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Login code
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            String usernameText = editUsername.getText().toString();
            String passwordText = editPassword.getText().toString();
            if (usernameText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Please fill out both username and password fields!", Toast.LENGTH_SHORT).show();
            }else {
                // Perform SQL Database Query
                UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                UserDAO userDAO = userDatabase.userDao();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = userDAO.login(usernameText, passwordText);
                        if (user == null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Intent intent = new Intent(MainActivity.this, VacationList.class);
                            intent.putExtra("test", "information sent");
                            startActivity(intent);
                        }
                    }
                }).start();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Registration code
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating user
                User user = new User();
                user.setUserName(userName.getText().toString());
                user.setPassword(password.getText().toString());
                user.setName(name.getText().toString());
                if (validateInput(user)) {
                // Perform insert to database
                    UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                    UserDAO userDAO = userDatabase.userDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Register User
                            userDAO.registerUser(user);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Registration Complete. Please login below.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(MainActivity.this, "All fields must be filled out!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Registration code ^^^^^
        repository = new Repository(getApplication());
    }
        // Registration Validation
    private Boolean validateInput(User user) {
        if (user.getUserName().isEmpty() ||
            user.getPassword().isEmpty() ||
            user.getName().isEmpty()) {
            return false;
        }
        return true;
    }
}