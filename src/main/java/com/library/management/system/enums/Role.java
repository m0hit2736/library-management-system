package com.library.management.system.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

	ADMIN("ADMIN"), LIBRARIAN("LIBRARIAN"), USER("USER");

	private String value;

	public static List<String> getValues(List<Role> types) {
		return types.stream().map(Role::getValue).collect(Collectors.toList());
	}

	public static Role getByValue(String value) {
		return Arrays.stream(Role.values()).filter(enumValue -> enumValue.getValue().equalsIgnoreCase(value))
				.findFirst().orElse(null);
	}

	public static List<String> getAllKeys() {
		return Arrays.stream(Role.values()).map(Enum::name).collect(Collectors.toList());
	}
}
