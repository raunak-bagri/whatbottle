package com.whatbottle.util;

import com.lithium.mineraloil.api.lia.LIAAPIConnection;
import com.lithium.mineraloil.api.lia.api.models.User;
import com.lithium.mineraloil.api.rest.APIVersion;
import com.lithium.mineraloil.api.rest.models.APIUser;
import org.apache.http.HttpHost;

public class LiaApiConnector {

    public static LIAAPIConnection getLIAAPIConnectionV1(User user, String url, int port, String community) {
        APIUser apiUser = new APIUser();
        apiUser.setEmail(user.getEmail());
        apiUser.setPassword(user.getPassword());
        apiUser.setUsername(user.getUsername());
        apiUser.setName(user.getFirstname());
        return LIAAPIConnection.builder()
                .user(apiUser)
                .httpHost(new HttpHost(url, port, "http"))
                .version(APIVersion.VC)
                .community(community)
                .build();
    }


    public static User getDefaultUser() {
        User defaultAdmin = User.builder()
                .username("admin")
                .email("admin@lithium.com")
                .password("arfarf")
                .firstname("admin")
                .build();

        return defaultAdmin;
    }


}
