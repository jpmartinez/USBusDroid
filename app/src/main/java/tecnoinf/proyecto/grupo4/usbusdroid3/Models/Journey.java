package tecnoinf.proyecto.grupo4.usbusdroid3.Models;

import java.util.Date;

/**
 * Created by Kavesa on 31/05/16.
 */
public class Journey {
    private Long id;
    private Service service;
    private Date date;
    private Bus bus;
    private String thirdPartyBus;
    private HumanResource driver;
    private Integer busNumber;
    private Integer seats;
    private Integer standingPassengers;
    private String trunkWeight;
    private JourneyStatus status;
}
