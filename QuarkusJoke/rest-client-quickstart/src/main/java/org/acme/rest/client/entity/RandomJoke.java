package org.acme.rest.client.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.acme.rest.client.dto.JokeDTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RandomJoke {

    @Id
    @Column(name = "Id")
    private Long id;
    @Column(name = "Setup")
    private String setup;
    @Column(name = "Punch_Line")
    private String punchline;
    @Column(name = "Type")
    private String type;

    public RandomJoke(JokeDTO jokeDto) {
        this.id = jokeDto.getId();
        this.setup = jokeDto.getSetup();
        this.punchline = jokeDto.getPunchline();
        this.type = jokeDto.getType();
    }
}