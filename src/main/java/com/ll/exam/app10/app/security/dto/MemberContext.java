package com.ll.exam.app10.app.security.dto;

import com.ll.exam.app10.app.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class MemberContext extends User implements OAuth2User {
    private final Long id;
    private final String email;
    @Setter
    private LocalDateTime modifyDate;
    private final String profileImgUrl;

    private Map<String, Object> attributes;
    private String userNameAttributeName;

    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.id = member.getId();
        this.email = member.getEmail();
        this.modifyDate = member.getModifyDate();
        this.profileImgUrl = member.getProfileImgUrl();
    }

    public MemberContext(Member member, List<GrantedAuthority> authorities, Map<String, Object> attributes, String userNameAttributeName) {
        this(member, authorities);
        this.attributes = attributes;
        this.userNameAttributeName = userNameAttributeName;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return super.getAuthorities().stream().collect(Collectors.toSet());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.getAttribute(this.userNameAttributeName).toString();
    }

    public String getProfileImgRedirectUrl() {
        return "/member/profile/img/" + getId() + "?cacheKey=" + getModifyDate().toString();
    }

    public boolean memberIs(Member member) {
        return id.equals(member.getId());
    }

    public boolean memberIsNot(Member member) {
        return memberIs(member) == false;
    }
}
