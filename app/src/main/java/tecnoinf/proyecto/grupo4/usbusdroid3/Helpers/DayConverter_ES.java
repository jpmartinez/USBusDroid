package tecnoinf.proyecto.grupo4.usbusdroid3.Helpers;

import tecnoinf.proyecto.grupo4.usbusdroid3.Models.DayOfWeek;

/**
 * Created by Kavesa on 02/06/16.
 */
public class DayConverter_ES {

    public static String convertES(DayOfWeek day){
        switch (day){
            case MONDAY:    return "LUNES";
            case TUESDAY:   return "MARTES";
            case WEDNESDAY: return "MIÉRCOLES";
            case FRIDAY:    return "VIERNES";
            case THURSDAY:  return "JUEVES";
            case SATURDAY:  return "SÁBADO";
            case SUNDAY:    return "DOMINGO";
            default:        return null;
        }
    }
}
