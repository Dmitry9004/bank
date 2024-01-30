package user.dto;

import java.util.List;

import account.model.Account;
import card.model.Card;
import lombok.Getter;
import lombok.Setter;
import user.model.User;
@Getter
@Setter
public class UserDTO {
	private String phoneNumber;
	private String name;
}
