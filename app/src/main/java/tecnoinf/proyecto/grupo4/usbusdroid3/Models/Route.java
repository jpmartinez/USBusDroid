package tecnoinf.proyecto.grupo4.usbusdroid3.Models;

/**
 * Created by Kavesa on 31/05/16.
 */
public class Route {
    private Long id;
    private String name;
    private String origin;
    private BusStop destination;
    private RouteStop[] busStops;
    private Boolean active;
    private Boolean hasCombination;
    private Double pricePerKm;
}
