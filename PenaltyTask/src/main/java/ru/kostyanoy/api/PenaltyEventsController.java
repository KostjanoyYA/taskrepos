package ru.kostyanoy.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kostyanoy.api.dto.PenaltyEventDto;
import ru.kostyanoy.service.penaltyevents.PenaltyEventService;

import java.util.List;

@RestController
@RequestMapping(path = Paths.PENALTYEVENTS, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PenaltyEventsController {

    private final PenaltyEventService penaltyEventService;

    @GetMapping
    public List<PenaltyEventDto> get(
            @RequestParam(name = Parameters.FIRST_NAME, required = false) String firstName,
            @RequestParam(name = Parameters.MIDDLE_NAME, required = false) String middleName,
            @RequestParam(name = Parameters.LAST_NAME, required = false) String lastName,
            @RequestParam(name = Parameters.FULL_STATE_NUMBER, required = false) String fullStateNumber
    ) {
        return penaltyEventService.get(firstName, middleName, lastName, fullStateNumber);
    }
}
