package it.cs.unicam.app_valorizzazione_territorio.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MessageIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MessageOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.handlers.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {

    private final MessageHandler messageHandler;

    @Autowired
    public MessageController(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @PutMapping("insert")
    public ResponseEntity<Long> insertMunicipalityRequestMessage(@RequestBody MessageIF messageIF) {
        try {
            long id = messageHandler.insertMunicipalityRequestMessage(messageIF);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @JsonView(View.Synthesized.class)
    @GetMapping("view")
    public ResponseEntity<List<MessageOF>> viewMessages() {
        try {
            List<MessageOF> messages = messageHandler.viewMessages();
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @GetMapping("view/{id}")
    public ResponseEntity<MessageOF> viewMessage(@PathVariable long id) {
        try {
            MessageOF message = messageHandler.viewMessage(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}