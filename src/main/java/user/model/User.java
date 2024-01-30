package user.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import account.model.Account;
import card.model.Card;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String phoneNumber;
	
	@Enumerated(EnumType.STRING)
//	@JoinTable(
//			name = "users_roles",
//			joinColumns = @JoinColumn(
//					name = "user_id", referencedColumnName = "id"),
//			inverseJoinColumns = @JoinColumn(
//					name = "role_id", referencedColumnName = "id"))
	private ERole role;
}
