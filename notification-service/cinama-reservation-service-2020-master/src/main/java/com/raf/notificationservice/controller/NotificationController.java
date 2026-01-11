package com.raf.notificationservice.controller;

import com.raf.notificationservice.dto.NotificationDto;
import com.raf.notificationservice.service.NotificationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ApiOperation(value = "Get notifications")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query")})
    @GetMapping
    public ResponseEntity<Page<NotificationDto>> findAll(@RequestParam(required = false) Long userId,
                                                         @ApiIgnore Pageable pageable) {
        if (userId != null) {
            return new ResponseEntity<>(notificationService.findByUserId(userId, pageable), HttpStatus.OK);
        }
        return new ResponseEntity<>(notificationService.findAll(pageable), HttpStatus.OK);
    }
}