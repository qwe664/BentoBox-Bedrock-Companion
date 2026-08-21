package dev.qwe664.bbc.developer.reflection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectionAliasesTest {

    @Test
    void flagAliasResolvesToFlagClass() {
        assertEquals(
                "world.bentobox.bentobox.api.flags.Flag",
                ReflectionAliases.resolve("flag")
        );
    }

    @Test
    void userAliasResolvesToUserClass() {
        assertEquals(
                "world.bentobox.bentobox.api.user.User",
                ReflectionAliases.resolve("user")
        );
    }

    @Test
    void aliasLookupIsCaseInsensitive() {
        assertEquals(
                "world.bentobox.bentobox.api.flags.Flag",
                ReflectionAliases.resolve("FLAG")
        );
    }

    @Test
    void unknownTargetIsReturnedUnchanged() {
        assertEquals(
                "world.bentobox.bentobox.BentoBox",
                ReflectionAliases.resolve("world.bentobox.bentobox.BentoBox")
        );
    }
}
