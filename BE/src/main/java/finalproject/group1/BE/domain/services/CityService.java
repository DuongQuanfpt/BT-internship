package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.entities.City;
import finalproject.group1.BE.domain.repository.CityRepository;
import finalproject.group1.BE.web.dto.response.city.CityListResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.swing.event.ListDataEvent;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CityService {
    private CityRepository cityRepository;
    private ModelMapper modelMapper;

    public List<CityListResponse> getAllCities() {
        List<City> cities = cityRepository.findAll();

        List<CityListResponse> cityListDTO = cities.stream()
                .map(city -> {
                    CityListResponse response = modelMapper.map(city, CityListResponse.class);
                    response.setId(city.getId());
                    response.setName(city.getName());
                    return response;
                }).collect(Collectors.toList());
        return cityListDTO;
    }
}
