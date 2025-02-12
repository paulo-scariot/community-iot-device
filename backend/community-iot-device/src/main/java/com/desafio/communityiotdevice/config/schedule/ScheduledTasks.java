package com.desafio.communityiotdevice.config.schedule;

import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import com.desafio.communityiotdevice.modules.measurement.model.Measurement;
import com.desafio.communityiotdevice.modules.measurement.service.MeasurementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    @Value("${app-config.secrets.telnet-port}")
    private Integer port;

    private final DeviceService deviceService;
    private final MeasurementService measurementService;

    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void reportCurrentTime() {
        List<Device> allDevices = deviceService.getAllDevicesActive();
        for (Device device : allDevices) {
            String url = device.getUrl();
            List<Command> commands = device.getCommands();
            port = (port != null) ? port : 3000;
            try (Socket socket = new Socket(url, port);
                 InputStream input = socket.getInputStream();
                 OutputStream output = socket.getOutputStream()) {

                for (Command command : commands) {

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
                    double measurementDouble = Double.parseDouble(response.toString());
                    log.info("Telnet Server-> host {}, port {}, Mesarument Received: {}", url, port, measurementDouble);
                    measurement.setResult(Double.parseDouble(response.toString()));
                    measurement.setDevice(device);
                    measurement.setCommand(command);
                    measurementService.save(measurement);
                }

            } catch (ConnectException e) {
                System.err.println("Failed to connect to Telnet server: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }
}