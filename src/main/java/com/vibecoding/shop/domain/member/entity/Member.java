package com.vibecoding.shop.domain.member.entity;

import com.vibecoding.shop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity @Table(name = "members")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) private String email;
    @Column(nullable = false)                private String password;
    @Column(nullable = false)                private String name;
    @Column                                  private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Column(nullable = false)
    private int point = 0;

    // 배송지 주소 (값 객체 임베드)
    @Column private String city;
    @Column private String street;
    @Column private String zipCode;

    public static Member create(String email, String encodedPassword, String name) {
        Member m = new Member();
        m.email    = email;
        m.password = encodedPassword;
        m.name     = name;
        m.role     = MemberRole.USER;
        return m;
    }

    public void earnPoint(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("포인트는 양수여야 합니다.");
        this.point += amount;
    }
    public void updateAddress(String city, String street, String zipCode) {
        this.city = city; this.street = street; this.zipCode = zipCode;
    }

    public enum MemberRole { USER, ADMIN }
}
