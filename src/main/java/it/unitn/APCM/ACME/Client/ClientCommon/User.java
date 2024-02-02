package it.unitn.APCM.ACME.Client.ClientCommon;

/**
 * The type User.
 */
public class User {
    private String email; // Email of the user
    private boolean authenticated; // Status of the user to know if he is authenticated or not
    private String jwt; // JWT token assigned to the user

	/**
	 * Instantiates a new User.
	 */
	public User() {
        this.email = "";
        this.authenticated = false;
        this.jwt = "";
    }

	/**
	 * Gets email.
	 *
	 * @return the email
	 */
	public String getEmail() { return email; }

	/**
	 * Is authenticated.
	 *
	 * @return the boolean
	 */
	public boolean isAuthenticated() { return authenticated; }

	/**
	 * Gets jwt.
	 *
	 * @return the jwt string
	 */
	public String getJwt() { return jwt; }

	/**
	 * Sets email.
	 *
	 * @param email the email
	 */
	public void setEmail(String email) { this.email = email; }

	/**
	 * Sets authenticated.
	 *
	 * @param authenticated the authenticated
	 */
	public void setAuthenticated(boolean authenticated) { this.authenticated = authenticated; }

	/**
	 * Sets jwt.
	 *
	 * @param jwt the jwt
	 */
	public void setJwt(String jwt) { this.jwt = jwt; }
}
