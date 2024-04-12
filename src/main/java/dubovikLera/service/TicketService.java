package dubovikLera.service;

import dubovikLera.dao.TicketDao;
import dubovikLera.dto.TicketDto;

import java.util.List;
import java.util.stream.Collectors;

public class TicketService {
    private final static TicketService INSTANCE = new TicketService();
    private TicketService(){

    }
    private final TicketDao ticketDao = TicketDao.getINSTANCE();
    public List<TicketDto> findAllByFlightId(Long flightId){
        return ticketDao.findAllByFlightId(flightId).stream().map(
                ticket -> new TicketDto(ticket.getId(), ticket.getFlight().getId(),
                        ticket.getSeatNo())
        ).collect(Collectors.toList());
    }

    public static TicketService getINSTANCE() {
        return INSTANCE;
    }
}
