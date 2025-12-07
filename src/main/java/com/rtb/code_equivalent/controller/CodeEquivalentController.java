package com.rtb.code_equivalent.controller;

import com.rtb.code_equivalent.dto.CodeEquivalentDTO;
import com.rtb.code_equivalent.dto.CodeEquivalentResponseDTO;
import com.rtb.code_equivalent.service.CodeEquivalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code-equivalent")
public class CodeEquivalentController {

    private final CodeEquivalentService codeEquivalentService;

    @PostMapping("/report")
    public ResponseEntity<String> generateReport(@RequestBody CodeEquivalentDTO codeEquivalentDTO) {

        ResponseEntity<String> response;

        try {
            response = ResponseEntity.ok(codeEquivalentService.generateReport(codeEquivalentDTO));
        } catch (IllegalArgumentException e) {
            response = ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            response = ResponseEntity.internalServerError().body(e.getMessage());
        }

        return response;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CodeEquivalentResponseDTO>> getAllReports() {

        return ResponseEntity.ok(codeEquivalentService.getAllSavedReports());
    }

    @GetMapping("/report/{id}")
    public ResponseEntity<HashMap<String, String>> getReportById(@PathVariable Long id) {

        HashMap<String, String> map = new HashMap<>();

        ResponseEntity<HashMap<String, String>> response;

        try {

            map.put("response", codeEquivalentService.getReportById(id));

            response = ResponseEntity.ok(map);
        } catch (IllegalArgumentException e) {
            map.put("response", e.getMessage());
            response = ResponseEntity.badRequest().body(map);
        } catch (Exception e) {
            map.put("response", e.getMessage());
            response = ResponseEntity.internalServerError().body(map);
        }

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HashMap<String, String>> deleteReportById(@PathVariable Long id) {

        codeEquivalentService.deleteById(id);

        HashMap<String, String> response = new HashMap<>();
        response.put("message", "report deleted");

        return ResponseEntity.ok(response);
    }

}
