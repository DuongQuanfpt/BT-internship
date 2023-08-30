package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.services.DistrictService;
import finalproject.group1.BE.web.dto.request.district.DistrictRequest;
import finalproject.group1.BE.web.dto.response.ResponseDTO;
import finalproject.group1.BE.web.dto.response.ResponseDataDTO;
import finalproject.group1.BE.web.dto.response.district.DistrictListResponse;
import finalproject.group1.BE.web.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/districts")
@AllArgsConstructor
public class DistrictController {
    private DistrictService districtService;

    @GetMapping("/search")
    public ResponseEntity<ResponseDataDTO<List<DistrictListResponse>>> getCitiesList(@RequestBody @Valid DistrictRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
           throw new ValidationException(bindingResult);
        }
        List<DistrictListResponse> response = districtService.getDistrictByCityId(request);
        return ResponseEntity.ok().body(ResponseDataDTO.success(response));
    }
}
