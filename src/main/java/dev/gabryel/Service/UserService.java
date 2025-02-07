package dev.gabryel.Service;

import dev.gabryel.Entity.User;
import dev.gabryel.Repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public User getUserByUsername(String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new ValidationException("Usuario não encontrado!");
        }

        return user;
    }

    public void validateUser(User user) {
        if (userRepository.findByUsername(user.username) != null) {
            throw new ValidationException("Username já está em uso!");
        }

        if (userRepository.findByCPF(user.cpf) != null) {
            throw new ValidationException("CPF ja cadastrado!");
        }

        if (!isValidCPF(user.cpf)) {
            throw new ValidationException("CPF inválido!");
        }

        if (!isValidPhone(user.phone)) {
            throw new ValidationException("Número de telefone inválido!");
        }
    }

    private boolean isValidCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0, remainder;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        remainder = (sum * 10) % 11;
        if (remainder == 10) remainder = 0;
        if (remainder != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        remainder = (sum * 10) % 11;
        if (remainder == 10) remainder = 0;
        return remainder == Character.getNumericValue(cpf.charAt(10));
    }

    private boolean isValidPhone(String phone) {
        phone = phone.replaceAll("[^0-9]", "");

        return phone.length() == 10 || phone.length() == 11;
    }
}
