package com.ll.exam.app10.app.member.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
public class MemberContext extends User {
    private final Long id;
    private final String imgPath;
    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.id = member.getId();
        this.imgPath = member.getProfileImgUrl();
    }
}
