package com.desafio.communityiotdevice.modules.user.controller;

import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.user.dto.UserRequest;
import com.desafio.communityiotdevice.modules.user.dto.UserResponse;
import com.desafio.communityiotdevice.modules.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Page<UserResponse> list(@RequestParam(required = false) String filter,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size){
        return userService.getUsers(filter, page, size);
    }

    @GetMapping("{id}")
    public UserResponse get(@PathVariable Integer id) {
        return UserResponse.of(userService.findById(id));
    }

    @PostMapping
    public UserResponse save(@RequestBody UserRequest request) {
        return userService.save(request);
    }

    @PutMapping("{id}")
    public UserResponse update(@RequestBody UserRequest request,
                                  @PathVariable Integer id) {
        return userService.update(request, id);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id) {
        return userService.delete(id);
    }
}
