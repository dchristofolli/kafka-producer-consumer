package br.com.dchristofolli.kafka;

import br.com.dchristofolli.kafka.exception.ApiException;
import br.com.dchristofolli.kafka.repository.UserEntity;
import br.com.dchristofolli.kafka.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class UserService {
    private UserRepository repository;

    public UserEntity create(UserEntity user) {
        return repository.save(user);
    }

    public List<UserEntity> findAllUsers() {
        return repository.findAll();
    }

    public boolean repositoryIsEmpty() {
        return repository.findAll().isEmpty();
    }

    public void delete(String id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new ApiException("Not found", HttpStatus.NOT_FOUND)));
    }

    public UserEntity findByIdOrCpfOrEmailOrName(String id, String cpf, String email, String name) {
        return repository.findByIdOrCpfOrEmailOrName(id, cpf, email, name)
                .orElseThrow(() -> new ApiException("Bad request", HttpStatus.BAD_REQUEST));
    }

    public boolean userDoesNotExistsByName(String name) {
        return !repository.existsByName(name.toLowerCase());
    }

    public UserEntity updateUsername(String oldName, String newName) {
        UserEntity entity = repository.findByName(oldName)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        entity.setName(newName);
        return repository.save(entity);
    }

    public boolean newNameIsEqualsOldName(String oldName, String newName) {
        return oldName.equals(newName);
    }

    public boolean userArgumentsIsEmpty(String id, String cpf, String email, String name) {
        return ObjectUtils.isEmpty(id) && ObjectUtils.isEmpty(cpf) &&
                ObjectUtils.isEmpty(email) && ObjectUtils.isEmpty(name);
    }
}
