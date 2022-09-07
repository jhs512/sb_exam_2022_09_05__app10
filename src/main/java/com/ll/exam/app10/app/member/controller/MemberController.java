package com.ll.exam.app10.app.member.controller;

import com.ll.exam.app10.app.member.entity.Member;
import com.ll.exam.app10.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }

    @PostMapping("/join")
    public String join(HttpServletRequest req, String username, String password, String email, MultipartFile profileImg) throws ServletException {
        Member oldMember = memberService.getMemberByUsername(username);

        if (oldMember != null) {
            return "redirect:/?errorMsg=Already done.";
        }

        String passwordClearText = password;

        password = passwordEncoder.encode(password);

        Member member = memberService.join(username, password, email, profileImg);

        req.login(member.getUsername(), passwordClearText);

        return "redirect:/member/profile";
    }

    @GetMapping("/profile")
    public String showProfile(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/?errorMsg=Need to login!";
        }

        Member loginedMember = memberService.getMemberByUsername(principal.getName());

        model.addAttribute("loginedMember", loginedMember);

        return "member/profile";
    }
}