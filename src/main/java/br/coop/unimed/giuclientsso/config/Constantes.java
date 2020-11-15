package br.coop.unimed.giuclientsso.config;

public class Constantes {
    public static final String _LOGIN_PATH = "/login";
    public static final String _API_PREFIX_PATH = "/api/";


    public static final String _AUTH_ROUTE = "/auth";

    public final static String _X_COOKIE_NAME = "X-CSRF-TOKEN";

    public final static String _AUTHORIZATION_HEADER = "Authorization";
    public final static String _AUTHORIZATION_HEADER_TYPE = "Bearer ";
    public static final String _AUTHORIZATION_HEADER_NEW = "X-AUTHORIZATION-NEW";
    public static final String _X_UNIMED_HEADER = "X-UNIMED-APP";

    public final static Long _REFRESH_TOLERANCE_IN_MINUTES = 60L;

    private Constantes() {
    }
}
