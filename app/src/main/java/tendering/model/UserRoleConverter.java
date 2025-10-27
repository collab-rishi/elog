package tendering.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

	@Override
	public String convertToDatabaseColumn(UserRole role) {
		if (role == null) {
			return null;
		}
		return role.getRoleName(); // returns "3PL" or "LSP"
	}

	@Override
	public UserRole convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		return UserRole.fromValue(dbData); // maps "3PL" back to THREE_PL
	}
}