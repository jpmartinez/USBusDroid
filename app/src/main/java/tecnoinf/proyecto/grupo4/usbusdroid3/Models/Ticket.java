package tecnoinf.proyecto.grupo4.usbusdroid3.Models;

import java.util.Date;

/**
 * Created by Kavesa on 15/06/16.
 */
public class Ticket {
    private Long id;
    private Date emissionDate;
    private Boolean hasCombination;
    private Service combination;
    private Double amount;
    private User passenger;
    private HumanResource seller;
    private TicketStatus status;

}
