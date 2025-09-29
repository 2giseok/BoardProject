package me.leegiseok.project.controller;

import jakarta.validation.Valid;
import me.leegiseok.project.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.*;

//Code Cover를 위한 class
@RestController
@RequestMapping("/test/errors")
public class ErrorController {


    //401
    @GetMapping("/unauth")
    void unautn() {
        throw  new UnauthorizedException("login required");
    }
    //403
    @GetMapping("/forbidden")
    void forbidden() {
        throw  new SecurityException("no permission");
    }
    //500
    @GetMapping("/boom")
    void boon() {
        throw  new RuntimeException("boom");
    }
    //404
    @GetMapping("/notfound")
    void notfound() {
        throw new IllegalArgumentException("not fount");
    }

    //400 바디 검증 실패
    @PostMapping("/validate")
    void validate( @RequestBody @Valid SampleDto dto) {
    }
    //400 - 파라미터 제약 위반
    @GetMapping("/violate")
    void vidlate(@RequestParam @jakarta.validation.constraints.Min(1)int id) {

    }
    //400 타입 미스매치

    @GetMapping("/type{id}")
    void type(@PathVariable Long id) {

    }

}
 record  SampleDto(
        @jakarta.validation.constraints.NotBlank String name
) {

}
