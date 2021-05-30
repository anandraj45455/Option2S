package com.option2s.zerodha.model;

import com.option2s.common.model.AuditModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken extends AuditModel {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "refresh_token_sequence")
    @SequenceGenerator(name = "refresh_token_sequence", sequenceName = "refresh_token_sequence", allocationSize = 1)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String userId;

    private String refreshToken;

}
