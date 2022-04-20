package com.tmt.tmdt.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;



public class CustomUserDetail extends User {
    private String ImageLink;

    public CustomUserDetail(String username,
                            String password,
                            Collection<? extends GrantedAuthority> authorities,
                            String imageLink) {
        super(username, password, authorities);
        ImageLink = imageLink;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }
}
