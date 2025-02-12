package com.desafio.communityiotdevice.config.schedule;

import com.desafio.communityiotdevice.modules.command.model.Command;
import com.desafio.communityiotdevice.modules.device.model.Device;
import com.desafio.communityiotdevice.modules.device.service.DeviceService;
import com.desafio.communityiotdevice.modules.measurement.model.Measurement;
import com.desafio.communityiotdevice.modules.measurement.service.MeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

class ScheduledTasksTest {

    @Mock
    private DeviceService deviceService;

    @Mock
    private MeasurementService measurementService;

    @Mock
    private Device device;

    @Mock
    private Command command;

    @Mock
    private OutputStream outputStream;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Manually initialize mocks
    }

    @Test
    void testReportCurrentTime_WhenSendCommand_ThenReceiveResponseFromTelnetServer() throws Exception {
        given(deviceService.getAllDevicesActive()).willReturn(List.of(device));
        willDoNothing().given(measurementService).save(any(Measurement.class));
        given(device.getUrl()).willReturn("localhost");
        given(device.getCommands()).willReturn(List.of(command));
        given(command.getCommand()).willReturn("someCommand");
        byte[] simulatedResponse = "123.45\n".getBytes();
        ByteArrayInputStream mockInputStream = new ByteArrayInputStream(simulatedResponse);

        try (MockedConstruction<Socket> mockedSocket = Mockito.mockConstruction(Socket.class,
                (mock, context) -> {
                    given(mock.getInputStream()).willReturn(mockInputStream);
                    given(mock.getOutputStream()).willReturn(outputStream);
                })) {

            ScheduledTasks scheduledTasks1 = new ScheduledTasks(deviceService, measurementService);
            scheduledTasks1.reportCurrentTime();

            verify(deviceService).getAllDevicesActive();
            verify(measurementService).save(any(Measurement.class));
            verify(outputStream).write(any(byte[].class));
            verify(outputStream).flush();
        }
    }
}