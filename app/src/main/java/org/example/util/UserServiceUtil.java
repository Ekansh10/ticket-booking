package org.example.util;


import org.mindrot.jbcrypt.BCrypt;
import java.util.*;

public class UserServiceUtil {
    private static int currentId = 0;
    private static String currentUid = "U" + currentId;
    public static String hashPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    public static boolean checkPassword(String plainPassword, String hashedPassword){
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    public static String getUid(){
        currentId += 1;
        return currentUid;
    }

}
