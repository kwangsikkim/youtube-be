package com.example.pj2be.service.memberservice;

import com.example.pj2be.config.jwt.JwtTokenProvider;
import com.example.pj2be.domain.member.MemberDTO;
import com.example.pj2be.mapper.membermapper.MemberMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final MemberMapper memberMapper;
    @Value("${image.file.prefix}")
    private String urlPrefix;
    private final JwtTokenProvider jwtTokenProvider;

    /*멤버의 프로필 정보 가져오기*/
    public MemberDTO getLoginInfo(String member_id) {
        MemberDTO memberDTO = memberMapper.findLoginInfoByMemberId(member_id);
        try{
            String decodedFileName = memberDTO.getImage_name();
            String encodedFileName = URLEncoder.encode(decodedFileName, StandardCharsets.UTF_8.toString());
            String url = urlPrefix + "member-profiles/" + member_id+"/" + encodedFileName;
            memberDTO.setUrl(url);
        }catch (Exception e){

        }
        return memberDTO;
    }
    public void expireJwtCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public boolean jwtRefreshTokenValidate(String jwtRefresh){
    return false;
    }
}
