package com.option2s.zerodha.model;

import com.option2s.common.model.AuditModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "zerodha_kite_tokens")
@Getter
@Setter
@NoArgsConstructor
public class ZerodhaKiteToken extends AuditModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "zerodha_kite_token_sequence")
    @SequenceGenerator(name = "zerodha_kite_token_sequence", sequenceName = "zerodha_kite_token_sequence", allocationSize = 1)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String userId;

    private String refreshTokenValue;

    @Column(columnDefinition = "TEXT")
    private String accessTokenValue;

    @Column(columnDefinition = "TEXT")
    private String publicTokenValue;

}
