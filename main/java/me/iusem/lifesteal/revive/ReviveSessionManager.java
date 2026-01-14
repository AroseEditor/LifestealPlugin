package me.iusem.lifesteal.revive;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReviveSessionManager {

    private static final Map<UUID, String> SESSIONS = new HashMap<>();

    public static void start(UUID reviver, String targetName) {
        SESSIONS.put(reviver, targetName);
    }

    public static String getTarget(UUID reviver) {
        return SESSIONS.get(reviver);
    }

    public static boolean hasSession(UUID reviver) {
        return SESSIONS.containsKey(reviver);
    }

    public static void end(UUID reviver) {
        SESSIONS.remove(reviver);
    }
}
