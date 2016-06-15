package tecnoinf.proyecto.grupo4.usbusdroid3.Models;

import java.util.Date;

/**
 * Created by Kavesa on 15/06/16.
 */
public class User {

    private long tenantId;
    private Date creationDate;
    private Date lastChange;
    private long version;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String password;
    private byte[] passwordHash;
    private byte[] salt;
    private Gender gender;
    private Date startDate;
    private Date lastActive;
    private Boolean active;
}
