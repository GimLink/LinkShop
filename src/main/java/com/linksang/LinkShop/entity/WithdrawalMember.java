package com.linksang.LinkShop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "withdrawal_member")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class WithdrawalMember extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String userId;
}
