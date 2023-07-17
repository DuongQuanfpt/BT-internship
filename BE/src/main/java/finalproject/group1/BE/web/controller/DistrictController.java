package finalproject.group1.BE.web.controller;

import finalproject.group1.BE.domain.services.DistrictService;
import finalproject.group1.BE.web.dto.request.district.DistrictRequest;
import finalproject.group1.BE.web.dto.response.ResponseDTO;
import finalproject.group1.BE.web.dto.response.district.DistrictListResponse;
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
    public ResponseEntity getCitiesList(@RequestBody @Valid DistrictRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map( fieldError -> fieldError.getField() + " " +fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        List<DistrictListResponse> response = districtService.getDistrictByCityId(request);
        return ResponseEntity.ok().body(ResponseDTO.success(response));
    }
}
