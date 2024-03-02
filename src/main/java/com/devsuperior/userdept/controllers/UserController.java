package com.devsuperior.userdept.controllers;

import com.devsuperior.userdept.entities.User;
import com.devsuperior.userdept.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    //buscar tudo
    @GetMapping
    public List<User> findAll() {
        List<User> result = repository.findAll();
        return result;
    }

    //    //buscar pelo id
//    @GetMapping(value = "/{id}")
//    public User findById(@PathVariable Long id) {
//        User result = repository.findById(id).get();
//        return result;
//    }
    //buscar pelo id
    @GetMapping(value = "/{id}")
    public User findById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    //buscar pelo nome
    @GetMapping(value = "/buscaNome")
    public List<User> findByName(@RequestParam String name) {
        List<User> users = repository.findByName(name);
        if(users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado com o nome: " + name);
        }
        return users;
    }
//    //buscar pelo nome
//    @GetMapping(value = "/buscaNome")
//    public List<User> findByName(@RequestParam String name) {
//        List<User> result = repository.findByName(name);
//        return result;
//    }


//    @DeleteMapping(value = "/delete/{id}")
//    public ResponseEntity<?> deleteById(@PathVariable Long id) {
//        if (repository.exists(id)) {
//            repository.deleteById(id);
//            return ResponseEntity.ok("\nUsuário deletado com sucesso!\n");
//        }else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    //deletar
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.ok("\nUsuário deletado com sucesso!\n");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //inserir
    @PostMapping
    public User insert(@RequestBody User user) {
        User result = repository.save(user);
        return result;
    }

    //alterar

    @PutMapping("/alterar/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userOptional = repository.findById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            user.setId(id);//Garantir que o ID do usuário é o mesmo do caminho
            User updatedUser = repository.save(user);
            return ResponseEntity.ok(updatedUser);
        }
    }

}
