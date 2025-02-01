package dev.bti.starters.common;

import java.util.Set;
import java.util.regex.Pattern;

public class RouteMatcher {

    private final Set<String> routes;

    public RouteMatcher(Set<String> routes) {
        this.routes = routes;
    }

    public boolean isMatch(String input) {
        for (String route : routes) {
            String regex = route.replaceAll("\\{[^/]+}", "[^/]+");
            regex += "(\\?(\\w+=\\w+(&\\w+=\\w+)*)?)?$";
            regex = "^" + regex;

            if (Pattern.matches(regex, input)) {
                return true;
            }
        }
        return false;
    }
}

