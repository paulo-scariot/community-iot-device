package com.desafio.communityiotdevice.modules.device.service;

import com.desafio.communityiotdevice.config.exception.ValidationException;
import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.device.dto.DeviceRequest;
import com.desafio.communityiotdevice.modules.device.dto.DeviceResponse;
import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.device.repository.DeviceRepository;
import com.desafio.communityiotdevice.modules.user.model.User;
import com.desafio.communityiotdevice.modules.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor(onConstructor = @__(@Lazy))
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Lazy
    private final UserService userService;

    public Page<DeviceResponse> getDevices(String filter,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Device> devicePage;
        if (filter != null && !isEmpty(filter)) {
            devicePage = deviceRepository.findByIdentifierContainingIgnoreCase(filter, pageable);
        } else {
            devicePage = deviceRepository.findAll(pageable);
        }
        return devicePage.map(DeviceResponse::of);
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device findById(Integer id) {
        return deviceRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Device with id " + id + " not found"));
    }

    public DeviceResponse save(DeviceRequest request) {
        validateDeviceData(request);
        User user = findUserByContext();
        Device device = Device.of(request, user);
        Device save = deviceRepository.save(device);
        return DeviceResponse.of(save);
    }

    public DeviceResponse update(DeviceRequest request,
                                    Integer id) {
        validateDeviceData(request);
        validateId(id);
        Device device = findById(id);
        User user = findUserByContext();
        User userOwner = device.getUser();
        if (!user.equals(userOwner)){
            throw new ValidationException("User is not owner of device");
        }
        device = Device.of(request, user);
        device.setId(id);
        Device save = deviceRepository.save(device);
        return DeviceResponse.of(save);
    }

    public SuccessResponse delete(Integer id) {
        validateId(id);
        Device device = findById(id);
        User user = findUserByContext();
        User userOwner = device.getUser();
        if (!user.equals(userOwner)){
            throw new ValidationException("User is not owner of device");
        }
        deviceRepository.deleteById(id);
        return SuccessResponse.create("Device with id " + id + " has been deleted");
    }

    private User findUserByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
                return userService.findByUsername(username);
            }
        }
        throw new ValidationException("Username not found");
    }

    private void validateDeviceData(DeviceRequest request) {
        if (isEmpty(request.getIdentifier())){
            throw new ValidationException("The identifier cannot be empty");
        }
        if (isEmpty(request.getDescription())){
            throw new ValidationException("The description cannot be empty");
        }
        if (isEmpty(request.getManufacturer())){
            throw new ValidationException("The manufacturer cannot be empty");
        }
        if (isEmpty(request.getUrl())){
            throw new ValidationException("The url cannot be empty");
        }
    }

    private void validateId(Integer id) {
        if (isEmpty(id)){
            throw new ValidationException("The parameter id cannot be empty");
        }
    }
}
