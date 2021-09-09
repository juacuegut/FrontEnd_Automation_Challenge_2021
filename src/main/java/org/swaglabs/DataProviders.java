package org.swaglabs;

import org.testng.annotations.DataProvider;
import java.util.*;

public class DataProviders {

    @DataProvider(name = "users")
    public Iterator<Object[]> users () {
                List<HashMap<String, HashMap<String,String>>> usersList = new ArrayList<>();
                HashMap<String,String> data = new HashMap<>();
                HashMap<String, HashMap<String,String>> users = new HashMap<>();

                data.put("user", "standard_user");
                data.put("pass", "secret_sauce");
                data.put("Name","Miguel");
                data.put("LastName","Perez");
                data.put("postalCode", "41015");
                users.put("validUser", data);

                data = new HashMap<>();
                data.put("user", "invalidUser");
                data.put("pass", "invalidPass");
                users.put("invalidUser", data);

                usersList.add(users);

                Collection<Object[]> dp = new ArrayList<>();
                dp.add(new Object[]{users});

                return dp.iterator();
    }
}
