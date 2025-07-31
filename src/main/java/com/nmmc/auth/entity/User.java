package com.nmmc.auth.entity;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nmmc.hrms.Audit.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_master", schema = "common")
public class User extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    // @GeneratedValue(generator = "UUID")
    // @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
    // parameters = {
    // @Parameter(name = "uuid_gen_strategy_class", value =
    // "org.hibernate.id.uuid.CustomVersionOneStrategy")
    // })
    @Column(name = "\"id\"", precision = 19)
    // @GeneratedValue(strategy = GenerationType.AUTO, generator =
    // "common.user_master_seq_gen")
    // @SequenceGenerator(name = "common.user_master_seq_gen", sequenceName =
    // "common.user_master_seq", allocationSize = 1)
    private UUID id;

    // @Column(name = "sequence_no")
    // private Long sequenceNo;

    @Column(name = "user_first_name")
    private String userFirstName;

    @Column(name = "user_first_name_mr")
    private String userFirstNameMr;

    @Column(name = "user_middle_name")
    private String userMiddleName;

    @Column(name = "user_middle_name_mr")
    private String userMiddleNameMr;

    @Column(name = "user_last_name")
    private String userLastName;

    @Column(name = "user_last_name_mr")
    private String userLastNameMr;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_Mobile")
    private String userMobile;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "raw_password")
    private String rawPassword;

    @Column(name = "ugp_prev_id")
    private Long ugpPrevId;

    @Column(name = "title_id")
    private UUID titleId;

    @Column(name = "dpt_id")
    private UUID dptId;

    @Column(name = "dpt_name")
    private String dptName;

    @Column(name = "subdpt_id")
    private UUID subDptId;

    @Column(name = "designation_id")
    private UUID designationId;

    @Column(name = "is_cfc_user")
    private Boolean isCfcUser;

    @Column(name = "is_dpt_user")
    private Boolean isDptUser;

    @Column(name = "mobile_otp")
    private Long mobileOtp;

    @Column(name = "email_otp")
    private Long emailOtp;

    @Column(name = "gender")
    private String gender;

    // @Column(name = "thumb_fingure_print")
    // private String thumbFingurePrint;

    // @Column(name = "photo")
    // private String photo;

    @Column(name = "DOB")
    private LocalDate dob;

    // @Column(name = "adhaar_number")
    // private String adhaarNumber;

    @Column(name = "adress")
    private String adress;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "pin_code")
    private Long pinCode;

    @Column(name = "zone_id")
    private UUID zoneId;

    @Column(name = "ward_id")
    private UUID wardId;

    @Column(name = "documents")
    private String documents;

    @Column(name = "location_id")
    private UUID locationId;
    
    @Column(name = "ddo_id")
    private UUID ddoId;

}
