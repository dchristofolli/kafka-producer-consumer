package br.com.dchristofolli.kafka.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Users")
public class UserEntity {
    @Id
    private String id;

    private String name;

    private String cpf;

    private String email;

    private String password;
}
