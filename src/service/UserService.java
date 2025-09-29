package service;

import java.util.Scanner;
import model.LoggedInUser;
import java.util.Scanner;

public interface UserService {
    LoggedInUser login(Scanner scanner);
    void register(Scanner scanner);
}
