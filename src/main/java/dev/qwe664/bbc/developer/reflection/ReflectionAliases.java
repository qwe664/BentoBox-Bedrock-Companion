package dev.qwe664.bbc.developer.reflection;

public final class ReflectionAliases {

    private ReflectionAliases() {
    }

    public static String resolve(String target) {

        return switch (target.toLowerCase()) {

            case "flag" ->
                    "world.bentobox.bentobox.api.flags.Flag";

            case "user" ->
                    "world.bentobox.bentobox.api.user.User";

            default -> target;
        };
    }
}
