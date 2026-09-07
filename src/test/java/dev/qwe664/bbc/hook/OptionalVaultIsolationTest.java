package dev.qwe664.bbc.hook;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionalVaultIsolationTest {

    private static final String VAULT_PACKAGE = "net/milkbowl/vault";

    @Test
    void alwaysLoadedClassesDoNotLinkToVaultApi() throws IOException {
        for (String className : new String[]{
                "dev.qwe664.bbc.BentoBoxBedrockCompanion",
                "dev.qwe664.bbc.form.IslandMenuForm",
                "dev.qwe664.bbc.form.WalletBankForm",
                "dev.qwe664.bbc.placeholder.BBCExpansion",
                "dev.qwe664.bbc.hook.EconomyHook",
                "dev.qwe664.bbc.hook.EconomyTransactionResult",
                "dev.qwe664.bbc.hook.UnavailableEconomyHook"
        }) {
            assertFalse(classBytes(className).contains(VAULT_PACKAGE),
                    className + " must remain loadable without Vault");
        }
    }

    @Test
    void vaultApiReferencesStayInsideDeferredHook() throws IOException {
        assertTrue(classBytes("dev.qwe664.bbc.hook.VaultHook").contains(VAULT_PACKAGE));
    }

    private String classBytes(String className) throws IOException {
        String resourceName = "/" + className.replace('.', '/') + ".class";
        try (InputStream input = getClass().getResourceAsStream(resourceName)) {
            assertNotNull(input, resourceName);
            return new String(input.readAllBytes(), StandardCharsets.ISO_8859_1);
        }
    }
}
