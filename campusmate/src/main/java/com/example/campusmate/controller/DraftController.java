package com.example.campusmate.controller;

import com.example.campusmate.entity.Draft;
import com.example.campusmate.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.campusmate.dto.ApiResponse;

@RestController
@RequestMapping("/api/drafts")
@CrossOrigin(origins = "*")
public class DraftController {

    @Autowired
    private DraftService draftService;

    @GetMapping
    public ApiResponse<List<Draft>> listDrafts(@RequestParam Long userId, @RequestParam(defaultValue = "1") int page) {
        List<Draft> list = draftService.listDrafts(userId, page);
        return ApiResponse.success(list);
    }

    @PostMapping("/{draftId}/publish")
    public ApiResponse<Boolean> publishDraft(@PathVariable Long draftId) {
        boolean result = draftService.publishDraft(draftId);
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{draftId}")
    public ApiResponse<Boolean> deleteDraft(@PathVariable Long draftId) {
        boolean result = draftService.deleteDraft(draftId);
        return ApiResponse.success(result);
    }
}