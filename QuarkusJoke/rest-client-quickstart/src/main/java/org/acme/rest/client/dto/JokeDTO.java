package org.acme.rest.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class JokeDTO {

    public Long id;         // Unique ID for the joke
    public String type;       // Type of the joke (e.g., "general", "programming", etc.)
    public String setup;      // The setup or question part of the joke
    public String punchline;  // The punchline of the joke

}
