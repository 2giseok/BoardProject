package me.leegiseok.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {

@NotBlank    private  String username;
 @NotBlank   private  String password;
  @NotBlank  private  String nickname;

}
