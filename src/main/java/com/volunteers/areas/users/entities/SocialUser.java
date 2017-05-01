package com.volunteers.areas.users.entities;

import com.volunteers.enumerators.Provider;

import javax.persistence.*;

@Entity
@Table(name= "social_users")
@DiscriminatorValue(value = "soc")
public class SocialUser extends Volunteer{

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(length=10000)
    private String image;

    public SocialUser() {
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
