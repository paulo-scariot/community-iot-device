package com.desafio.communityiotdevice.modules.device.service;


import com.desafio.communityiotdevice.config.exception.CustomHttpException;
import com.desafio.communityiotdevice.config.messages.SuccessResponse;
import com.desafio.communityiotdevice.modules.device.dto.DeviceRequest;
import com.desafio.communityiotdevice.modules.device.dto.DeviceResponse;
import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.device.repository.DeviceRepository;
import com.desafio.communityiotdevice.modules.user.model.RoleEnum;
import com.desafio.communityiotdevice.modules.user.model.User;
import com.desafio.communityiotdevice.modules.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
                .orElseThrow(() -> new CustomHttpException(HttpStatus.NOT_FOUND, "Device with id " + id + " not found"));
    }

    public DeviceResponse save(DeviceRequest request) {
        validateDeviceData(request);
        User user = userService.findUserByContext();
        Device device = Device.of(request, user);
        Device save = deviceRepository.save(device);
        return DeviceResponse.of(save);
    }

    public DeviceResponse update(DeviceRequest request,
                                    Integer id) {
        validateDeviceData(request);
        validateId(id);
        Device device = findById(id);
        User user = userService.findUserByContext();
        User userOwner = device.getUser();
        if (!user.equals(userOwner) && !user.getRole().equals(RoleEnum.ADMIN)){
            throw new CustomHttpException(HttpStatus.FORBIDDEN, "User is not owner of device");
        }
        device = Device.of(request, user);
        device.setId(id);
        Device save = deviceRepository.save(device);
        return DeviceResponse.of(save);
    }

    public SuccessResponse delete(Integer id) {
        validateId(id);
        Device device = findById(id);
        User user = userService.findUserByContext();
        User userOwner = device.getUser();
        if (!user.equals(userOwner) && !user.getRole().equals(RoleEnum.ADMIN)){
            throw new CustomHttpException(HttpStatus.FORBIDDEN, "User is not owner of device");
        }
        deviceRepository.deleteById(id);
        return SuccessResponse.create("Device with id " + id + " has been deleted");
    }

    private void validateDeviceData(DeviceRequest request) {
        if (isEmpty(request.getIdentifier())){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "The identifier cannot be empty");
        }
        if (isEmpty(request.getDescription())){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "The description cannot be empty");
        }
        if (isEmpty(request.getManufacturer())){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "The manufacturer cannot be empty");
        }
        if (isEmpty(request.getUrl())){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "The url cannot be empty");
        }
    }

    private void validateId(Integer id) {
        if (isEmpty(id)){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "The parameter id cannot be empty");
        }
    }

    public Boolean existsByCommandId(Integer id) {
        return deviceRepository.existsByCommandsId(id);
    }
}
