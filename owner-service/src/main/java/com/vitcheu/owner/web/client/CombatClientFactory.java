package com.vitcheu.owner.web.client;

import java.net.URI;


public class CombatClientFactory {

    public static CombatClient  createClient(String url, long userId) throws Exception {
        CombatClient client = new CombatClient(new URI(url), userId);
        return client;
    }
}