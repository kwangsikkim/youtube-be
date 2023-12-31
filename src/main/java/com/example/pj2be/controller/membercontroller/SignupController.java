package com.example.pj2be.controller.membercontroller;

import com.example.pj2be.domain.member.MemberDTO;
import com.example.pj2be.service.memberservice.MemberLoginService;
import com.example.pj2be.service.memberservice.MemberService;
import com.example.pj2be.service.memberservice.MemberSignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signup")
public class SignupController {
        private final MemberSignupService service;

        // 회원 가입
        @PostMapping
        public ResponseEntity signup(@Valid @RequestBody MemberDTO memberDTO,
                                     BindingResult bindingResult) {

            memberDTO.setPhone_number("010-0000-0000");

            if (bindingResult.hasErrors()) {
                System.out.println(" bindingerror! ");
                return ResponseEntity.badRequest().build();
            }
            try {
                System.out.println("요기 실행 ");
                memberDTO.setRole_id(2);
                if (service.signup(memberDTO)) {
                    System.out.println("가입 성공!!");
                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.internalServerError().build();
        }

        // 중복 체크 시작
        // 아이디
        @GetMapping(value = "/check", params = "member_id")
        public ResponseEntity<?> checkMemberId(@RequestParam Optional<String> member_id) {
            // null 여부
            if (member_id.isPresent()) {
                // 중복되는 경우
                if (service.getMemberId(member_id.get())) {
                    // 409 : 리소스 충돌을 나타내는 상태코드
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다");
                } else {
                    System.out.println("service.getMemberId(member_id.get()) = " + service.getMemberId(member_id.get()));
                    return ResponseEntity.ok().body("사용 가능한 아이디입니다");
                }
            } else {
                return ResponseEntity.badRequest().body("잘못된 요청입니다");
            }
        }

        // 닉네임
        @GetMapping(value = "/check", params = "nickname")
        public ResponseEntity<?> checkNickName(@RequestParam Optional<String> nickname) {
            // null 여부
            if (nickname.isPresent()) {
                // 중복되는 경우
                if (service.getNickname(nickname.get())) {
                    // 409 : 리소스 충돌을 나타내는 상태코드
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 별명입니다");
                } else {
                    return ResponseEntity.ok().body("사용 가능한 별명입니다");
                }
            } else {
                return ResponseEntity.badRequest().body("잘못된 요청입니다");
            }
        }

        // 이메일
        @GetMapping(value = "/check", params = "email")
        public ResponseEntity<?> checkEmail(@RequestParam Optional<String> email) {
            // null 여부
            if (email.isPresent()) {
                // 중복되는 경우
                if (service.getEmail(email.get())) {
                    // 409 : 리소스 충돌을 나타내는 상태코드
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일입니다");
                } else {
                    return ResponseEntity.ok().body("등록 가능한 이메일입니다");
                }
            } else {
                return ResponseEntity.badRequest().body("잘못된 요청입니다");
            }
        }
    }
