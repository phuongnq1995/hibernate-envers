package org.phuongnq.hibernate_envers.rest;

import lombok.RequiredArgsConstructor;
import org.phuongnq.hibernate_envers.service.HistoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/history", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class HistoryResource {

  private final HistoryService historyService;

  @GetMapping
  public ResponseEntity<Void> history(@RequestParam String module) {
    historyService.getHistory(module);
    return ResponseEntity.ok().build();
  }
}
