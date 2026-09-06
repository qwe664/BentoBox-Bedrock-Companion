package dev.qwe664.bbc.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class SettingsPermissionPolicyTest {
    @Test void memberWithFlagPermissionCannotBypassOwnerThreshold() {
        assertFalse(SettingsPermissionPolicy.permits(false, false, true));
    }
    @Test void authorizedRankStillNeedsFlagPermission() {
        assertFalse(SettingsPermissionPolicy.permits(false, true, false));
        assertFalse(SettingsPermissionPolicy.permits(false, false, false));
    }
    @Test void delegatedMemberCanEdit() {
        assertTrue(SettingsPermissionPolicy.permits(false, true, true));
    }
    @Test void revokedRankOrPermissionDeniesSubsequentSubmission() {
        assertTrue(SettingsPermissionPolicy.permits(false, true, true));
        assertFalse(SettingsPermissionPolicy.permits(false, false, true));
        assertFalse(SettingsPermissionPolicy.permits(false, true, false));
    }
    @Test void nativeAdminExceptionIsPreserved() {
        for (boolean rank : new boolean[]{false, true})
            for (boolean flag : new boolean[]{false, true})
                assertTrue(SettingsPermissionPolicy.permits(true, rank, flag));
    }
}
