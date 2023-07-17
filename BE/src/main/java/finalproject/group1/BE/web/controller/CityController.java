package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.services.CityService;
import finalproject.group1.BE.web.dto.response.ResponseDTO;
import finalproject.group1.BE.web.dto.response.city.CityListResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cities")
@AllArgsConstructor
public class CityController {
    private CityService cityService;

    @GetMapping("/search")
    public ResponseEntity getCitiesList() {
        List<CityListResponse> response = cityService.getAllCities();
        return ResponseEntity.ok().body(ResponseDTO.success(response));
    }
}
