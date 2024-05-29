package com.desafio.communityiotdevice.config.schedule;

import com.desafio.communityiotdevice.modules.commanddescription.model.CommandDescription;
import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import com.desafio.communityiotdevice.modules.parameter.model.Parameter;
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

    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void reportCurrentTime() {
        List<Device> allDevices = deviceService.getAllDevices();
        for (Device device : allDevices) {
            String url = device.getUrl();
            List<CommandDescription> commandDescriptions = device.getCommandDescriptions();

            try (Socket socket = new Socket(url, PORT);
                 InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream()) {

                for (CommandDescription commandDescription : commandDescriptions) {

                    List<String> parameters = commandDescription.getCommand().getParameters().stream().map(Parameter::getName).toList();
                    String fullCommand = commandDescription.getCommand().getCommand() + " " + String.join(" ", parameters) + "\n";

                    output.write(fullCommand.getBytes());
                    output.flush();

                    StringBuilder response = new StringBuilder();
                    int character;
                    while ((character = input.read()) != -1) {
                        response.append((char) character);
                        if (response.toString().endsWith("\n")) {
                            break;
                        }
                    }
                    commandDescription.setResult(response.toString().split("\n")[0]);
                }

            } catch (Exception e) {
                log.info("Error occured while send the commands", e);
            }
        }
    }
}