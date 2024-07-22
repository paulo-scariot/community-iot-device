package com.desafio.communityiotdevice.config.schedule;

import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import com.desafio.communityiotdevice.modules.measurement.model.Measurement;
import com.desafio.communityiotdevice.modules.measurement.service.MeasurementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private static final int PORT = 3000;

    private final DeviceService deviceService;
    private final MeasurementService measurementService;

    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void reportCurrentTime() {
        List<Device> allDevices = deviceService.getAllDevicesActive();
        for (Device device : allDevices) {
            String url = device.getUrl();
            List<Command> commands = device.getCommands();

            try (Socket socket = new Socket(url, PORT);
                 InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream()) {

                for (Command command : commands) {

//                    List<String> parameters = command.getParameters().stream().map(Parameter::getName).toList();
//                    String fullCommand = command.getCommand() + " " + String.join(" ", parameters) + "\n";

                    output.write(command.getCommand().getBytes());
                    output.flush();

                    StringBuilder response = new StringBuilder();
                    int character;
                    while ((character = input.read()) != -1) {
                        response.append((char) character);
                        if (response.toString().endsWith("\n")) {
                            break;
                        }
                    }
                    Measurement measurement = new Measurement();
                    measurement.setResult(Double.parseDouble(response.toString()));
                    measurement.setDevice(device);
                    measurement.setCommand(command);
                    measurementService.save(measurement);
                }

            } catch (Exception e) {
                log.info("Error occured while send the commands", e);
            }
        }
    }
}