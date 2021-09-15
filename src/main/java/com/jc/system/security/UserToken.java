package com.jc.system.security;

import com.jc.system.security.domain.User;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class UserToken extends UsernamePasswordToken {

    private boolean thirdly;

    private User loginUser;

    public UserToken() {
    }

    /**
     * Constructs a new UsernamePasswordToken encapsulating the username and password submitted
     * during an authentication attempt, with a <tt>null</tt> {@link #getHost() host} and a
     * <tt>rememberMe</tt> default of <tt>false</tt>.
     *
     * @param username the username submitted for authentication
     * @param password the password character array submitted for authentication
     */
    public UserToken(final String username, final char[] password) {
        this(username, password, false, null);
    }

    /**
     * Constructs a new UsernamePasswordToken encapsulating the username and password submitted
     * during an authentication attempt, with a <tt>null</tt> {@link #getHost() host} and
     * a <tt>rememberMe</tt> default of <tt>false</tt>
     * <p/>
     * <p>This is a convience constructor and maintains the password internally via a character
     * array, i.e. <tt>password.toCharArray();</tt>.  Note that storing a password as a String
     * in your code could have possible security implications as noted in the class JavaDoc.</p>
     *
     * @param username the username submitted for authentication
     * @param password the password string submitted for authentication
     */
    public UserToken(final String username, final String password) {
        this(username, password != null ? password.toCharArray() : null, false, null);
    }

    /**
     * Constructs a new UsernamePasswordToken encapsulating the username and password submitted, the
     * inetAddress from where the attempt is occurring, and a default <tt>rememberMe</tt> value of <tt>false</tt>
     *
     * @param username the username submitted for authentication
     * @param password the password string submitted for authentication
     * @param host     the host name or IP string from where the attempt is occuring
     * @since 0.2
     */
    public UserToken(final String username, final char[] password, final String host) {
        this(username, password, false, host);
    }

    /**
     * Constructs a new UsernamePasswordToken encapsulating the username and password submitted, the
     * inetAddress from where the attempt is occurring, and a default <tt>rememberMe</tt> value of <tt>false</tt>
     * <p/>
     * <p>This is a convience constructor and maintains the password internally via a character
     * array, i.e. <tt>password.toCharArray();</tt>.  Note that storing a password as a String
     * in your code could have possible security implications as noted in the class JavaDoc.</p>
     *
     * @param username the username submitted for authentication
     * @param password the password string submitted for authentication
     * @param host     the host name or IP string from where the attempt is occuring
     * @since 1.0
     */
    public UserToken(final String username, final String password, final String host) {
        this(username, password != null ? password.toCharArray() : null, false, host);
    }

    /**
     * Constructs a new UsernamePasswordToken encapsulating the username and password submitted, as well as if the user
     * wishes their identity to be remembered across sessions.
     *
     * @param username   the username submitted for authentication
     * @param password   the password string submitted for authentication
     * @param rememberMe if the user wishes their identity to be remembered across sessions
     * @since 0.9
     */
    public UserToken(final String username, final char[] password, final boolean rememberMe) {
        this(username, password, rememberMe, null);
    }

    /**
     * Constructs a new UsernamePasswordToken encapsulating the username and password submitted, as well as if the user
     * wishes their identity to be remembered across sessions.
     * <p/>
     * <p>This is a convience constructor and maintains the password internally via a character
     * array, i.e. <tt>password.toCharArray();</tt>.  Note that storing a password as a String
     * in your code could have possible security implications as noted in the class JavaDoc.</p>
     *
     * @param username   the username submitted for authentication
     * @param password   the password string submitted for authentication
     * @param rememberMe if the user wishes their identity to be remembered across sessions
     * @since 0.9
     */
    public UserToken(final String username, final String password, final boolean rememberMe) {
        this(username, password != null ? password.toCharArray() : null, rememberMe, null);
    }

    /**
     * Constructs a new UsernamePasswordToken encapsulating the username and password submitted, if the user
     * wishes their identity to be remembered across sessions, and the inetAddress from where the attempt is ocurring.
     *
     * @param username   the username submitted for authentication
     * @param password   the password character array submitted for authentication
     * @param rememberMe if the user wishes their identity to be remembered across sessions
     * @param host       the host name or IP string from where the attempt is occuring
     * @since 1.0
     */
    public UserToken(final String username, final char[] password,
                     final boolean rememberMe, final String host) {

        super(username, password, rememberMe, host);
    }


    /**
     * Constructs a new UsernamePasswordToken encapsulating the username and password submitted, if the user
     * wishes their identity to be remembered across sessions, and the inetAddress from where the attempt is ocurring.
     * <p/>
     * <p>This is a convience constructor and maintains the password internally via a character
     * array, i.e. <tt>password.toCharArray();</tt>.  Note that storing a password as a String
     * in your code could have possible security implications as noted in the class JavaDoc.</p>
     *
     * @param username   the username submitted for authentication
     * @param password   the password string submitted for authentication
     * @param rememberMe if the user wishes their identity to be remembered across sessions
     * @param host       the host name or IP string from where the attempt is occuring
     * @since 1.0
     */
    public UserToken(final String username, final String password,
                     final boolean rememberMe, final String host) {
        this(username, password != null ? password.toCharArray() : null, rememberMe, host);
    }

    public boolean isThirdly() {
        return thirdly;
    }

    public void setThirdly(boolean thirdly) {
        this.thirdly = thirdly;
    }

    public User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }
}
