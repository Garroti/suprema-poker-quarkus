package dev.gabryel.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    public Long userId;
    public String username;

    public UserDTO(){}

    public UserDTO(Long userId) {
        this.userId = userId;
    }
}
