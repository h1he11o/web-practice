package com.example.jbnu.osj.demo.api.v1;

import com.example.jbnu.osj.demo.api.dto.ApiResponse;
import com.example.jbnu.osj.demo.api.dto.ItemDto;
import com.example.jbnu.osj.demo.api.request.ItemCreateRequest;
import com.example.jbnu.osj.demo.api.request.ItemUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {
    private final Map<Long, ItemDto> store = new HashMap<>();
    private long sequence = 1L;

    private ResponseEntity<ApiResponse<?>> ok(int code, Object data) {
        return ResponseEntity.status(code).body(ApiResponse.success(data));
    }

    private ResponseEntity<ApiResponse<?>> fail(int code, String message) {
        return ResponseEntity.status(code).body(ApiResponse.error(message));
    }

    private boolean blank(String s) {
        return s == null || s.isBlank();
    }

    private boolean isEmptyButSent(String s) {
        return s != null && s.isBlank();
    }

    private String validate(ItemCreateRequest r) {
        if (blank(r.getSender())) return "sender(보내는 사람)는 비어 있을 수 없습니다.";
        if (blank(r.getItem())) return "item(품목)은 비어 있을 수 없습니다.";
        if (blank(r.getOrigin())) return "origin(원산지)은 비어 있을 수 없습니다.";
        if (blank(r.getReceiver())) return "receiver(받는 사람)는 비어 있을 수 없습니다.";
        if (r.getQuantity() == null || r.getQuantity() < 1) return "quantity(수량)는 1 이상이어야 합니다.";
        return null;
    }

    private ItemDto toItem(ItemCreateRequest r) {
        ItemDto dto = new ItemDto();
        dto.setId(sequence++);
        dto.setSender(r.getSender());
        dto.setItem(r.getItem());
        dto.setOrigin(r.getOrigin());
        dto.setReceiver(r.getReceiver());
        dto.setQuantity(r.getQuantity());
        store.put(dto.getId(), dto);
        return dto;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getItems(@RequestParam(required = false) String origin) {
        List<ItemDto> result = new ArrayList<>();
        for (ItemDto dto : store.values()) {
            if (origin == null || origin.equals(dto.getOrigin())) {
                result.add(dto);
            }
        }
        return ok(200, result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getItem(@PathVariable Long id) {
        ItemDto dto = store.get(id);
        if (dto == null) {
            return fail(404, "상품을 찾을 수 없습니다. id=" + id);
        }
        return ok(200, dto);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createItem(@RequestBody ItemCreateRequest request) {
        String error = validate(request);
        if (error != null) {
            return fail(400, error);
        }
        return ok(201, toItem(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<?>> createItems(@RequestBody List<ItemCreateRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return fail(400, "생성할 목록이 비어 있습니다.");
        }
        for (ItemCreateRequest r : requests) {
            String error = validate(r);
            if (error != null) {
                return fail(400, error);
            }
        }
        List<ItemDto> created = new ArrayList<>();
        for (ItemCreateRequest r : requests) {
            created.add(toItem(r));
        }
        return ok(201, created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateItem(
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest request
    ) {
        ItemDto dto = store.get(id);
        if (dto == null) {
            return fail(404, "상품을 찾을 수 없습니다. id=" + id);
        }
        if (request.getQuantity() != null && request.getQuantity() < 1) {
            return fail(400, "quantity(수량)는 1 이상이어야 합니다.");
        }

        if (isEmptyButSent(request.getSender())) return fail(400, "sender(보내는 사람)는 비어 있을 수 없습니다.");
        if (isEmptyButSent(request.getItem())) return fail(400, "item(품목)은 비어 있을 수 없습니다.");
        if (isEmptyButSent(request.getOrigin())) return fail(400, "origin(원산지)은 비어 있을 수 없습니다.");
        if (isEmptyButSent(request.getReceiver())) return fail(400, "receiver(받는 사람)는 비어 있을 수 없습니다.");

        if (request.getSender() != null) dto.setSender(request.getSender());
        if (request.getItem() != null) dto.setItem(request.getItem());
        if (request.getOrigin() != null) dto.setOrigin(request.getOrigin());
        if (request.getReceiver() != null) dto.setReceiver(request.getReceiver());
        if (request.getQuantity() != null) dto.setQuantity(request.getQuantity());
        return ok(200, dto);
    }

    @PutMapping("/{id}/receiver")
    public ResponseEntity<ApiResponse<?>> updateReceiver(
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest request
    ) {
        ItemDto dto = store.get(id);
        if (dto == null) {
            return fail(404, "상품을 찾을 수 없습니다. id=" + id);
        }
        if (blank(request.getReceiver())) {
            return fail(400, "receiver(받는 사람)는 비어 있을 수 없습니다.");
        }
        dto.setReceiver(request.getReceiver());
        return ok(200, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteItem(@PathVariable Long id) {
        ItemDto removed = store.remove(id);
        if (removed == null) {
            return fail(404, "상품을 찾을 수 없습니다. id=" + id);
        }
        return ok(200, removed);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deleteAllItems() {
        int count = store.size();
        store.clear();
        Map<String, Integer> result = new HashMap<>();
        result.put("deletedCount", count);
        return ok(200, result);
    }

    @GetMapping("/error/500")
    public ResponseEntity<ApiResponse<?>> serverError() {
        return fail(500, "서버 내부 오류가 발생했습니다.");
    }

    @GetMapping("/error/503")
    public ResponseEntity<ApiResponse<?>> serviceUnavailable() {
        return fail(503, "서비스를 일시적으로 사용할 수 없습니다.");
    }
}