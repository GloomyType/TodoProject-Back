package com.example.todoProject.service;

import java.util.regex.Pattern;

import org.springframework.security.core.AuthenticationException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.todoProject.jwt.TokenProvider;
import com.example.todoProject.model.dto.MemberRequestDto;
import com.example.todoProject.model.dto.MemberResponseDto;
import com.example.todoProject.model.dto.TokenDto;
import com.example.todoProject.model.entity.Member;
import com.example.todoProject.repository.MemberRepository;
import com.example.todoProject.repository.TodoRepository;

import java.util.regex.Matcher;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
	
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
	private final TodoRepository todoRepository;
	
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    //회원가입
    public MemberResponseDto signup(MemberRequestDto requestDto) {   	
        Matcher matcher = pattern.matcher(requestDto.getEmail());
        if (!matcher.matches()) {
            throw new RuntimeException("올바른 이메일 형식이 아닙니다.");
        }
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }
        Member member = requestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }
    
    //로그인
    public TokenDto login(MemberRequestDto requestDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
            Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
            if (authentication.isAuthenticated()) {
                logger.info("로그인 성공: {}", requestDto.getEmail());
                return tokenProvider.generateTokenDto(authentication);
            } else {
                logger.error("로그인 실패: {}", requestDto.getEmail());
                throw new RuntimeException("로그인 실패");
            }
        } catch (AuthenticationException ex) {
            logger.error("인증 오류: {}", ex.getMessage());
            throw new RuntimeException("인증 오류: " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("로그인 오류: {}", ex.getMessage());
            throw new RuntimeException("로그인 오류: " + ex.getMessage());
        }
    }
    
    //회원탈퇴
    public void deleteMember(String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userId = authentication.getName(); 
            Member member = memberRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (passwordEncoder.matches(password, member.getPassword())) {
                // Todo를 회원 ID를 기반으로 삭제
                todoRepository.deleteByMemberId(member.getId());
                memberRepository.delete(member);
                logger.info("User deleted: {}", userId);
            } else {
                throw new RuntimeException("Incorrect password");
            }
        } else {
            throw new RuntimeException("User not authenticated");
        }
    }    
}