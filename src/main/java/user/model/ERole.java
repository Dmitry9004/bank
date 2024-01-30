package user.model;

public enum ERole {
	ADMIN("ADMIN"),
	USER("USER");
	
	String role;
	ERole(String role) { this.role = role;}
	public String getRoleOfString() { return role; }
}
