package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.entities.City;
import finalproject.group1.BE.domain.entities.District;
import finalproject.group1.BE.domain.repository.DistrictRepository;
import finalproject.group1.BE.web.dto.request.district.DistrictRequest;
import finalproject.group1.BE.web.dto.response.city.CityListResponse;
import finalproject.group1.BE.web.dto.response.district.DistrictListResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DistrictService {
    private DistrictRepository districtRepository;
    private ModelMapper modelMapper;

    public List<DistrictListResponse> getDistrictByCityId(DistrictRequest districtRequest) {
        List<District> districts = districtRepository.findByCityId(districtRequest.getCityId());

        List<DistrictListResponse> districtListDTO = districts.stream()
                .map(district -> {
                    DistrictListResponse response = modelMapper.map(district, DistrictListResponse.class);
                    response.setId(district.getId());
                    response.setName(district.getName());
                    return response;
                }).collect(Collectors.toList());
        return districtListDTO;
    }
}
