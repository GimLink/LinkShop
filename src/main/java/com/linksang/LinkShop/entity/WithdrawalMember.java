package com.linksang.LinkShop.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "withdrawal_member")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class WithdrawalMember extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String userId;
}
