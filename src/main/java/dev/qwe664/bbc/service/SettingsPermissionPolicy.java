package dev.qwe664.bbc.service;

final class SettingsPermissionPolicy {
    private SettingsPermissionPolicy() {}
    static boolean permits(boolean admin, boolean rankAllowed, boolean flagAllowed) {
        return admin || (rankAllowed && flagAllowed);
    }
}
