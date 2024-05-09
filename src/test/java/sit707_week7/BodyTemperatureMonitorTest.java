package sit707_week7;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

public class BodyTemperatureMonitorTest {
	
	private TemperatureSensor temperatureSensor;
    private CloudService cloudService;
    private NotificationSender notificationSender;
    private BodyTemperatureMonitor monitor;

    @Before
    public void setUp() {
        // Initialize mocks
        temperatureSensor = Mockito.mock(TemperatureSensor.class);
        cloudService = Mockito.mock(CloudService.class);
        notificationSender = Mockito.mock(NotificationSender.class);

        // Create the BodyTemperatureMonitor with mocked dependencies
        monitor = new BodyTemperatureMonitor(temperatureSensor, cloudService, notificationSender);
    }

		@Test
	    public void testStudentIdentity() {
	        String studentId = "221508095";
	        Assert.assertNotNull("Student ID is null", studentId);
	    }
	
	    @Test
	    public void testStudentName() {
	        String studentName = "Tao Pan";
	        Assert.assertNotNull("Student name is null", studentName);
	    }
	
	  @Test
	    public void testReadTemperatureNegative() {
	        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(-1.0);
	        double temperature = monitor.readTemperature();
	        Assert.assertEquals(-1.0, temperature, 0.001);
	    }

	    @Test
	    public void testReadTemperatureZero() {
	        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(0.0);
	        double temperature = monitor.readTemperature();
	        Assert.assertEquals(0.0, temperature, 0.001);
	    }

	    @Test
	    public void testReadTemperatureNormal() {
	        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(36.5);
	        double temperature = monitor.readTemperature();
	        Assert.assertEquals(36.5, temperature, 0.001);
	    }

	    @Test
	    public void testReadTemperatureAbnormallyHigh() {
	        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(39.5);
	        double temperature = monitor.readTemperature();
	        Assert.assertEquals(39.5, temperature, 0.001);
	    }

	


    @Test
    public void testReadTemperature() {
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(37.0);
        Assert.assertEquals(37.0, monitor.readTemperature(), 0.001);
    }

    @Test
    public void testReportTemperatureReadingToCloud() {
        TemperatureReading reading = new TemperatureReading();
        monitor.reportTemperatureReadingToCloud(reading);
        Mockito.verify(cloudService).sendTemperatureToCloud(reading);
    }

    @Test
    public void testInquireBodyStatusNormal() {
        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("NORMAL");
        monitor.inquireBodyStatus();
        Mockito.verify(notificationSender).sendEmailNotification(Mockito.any(Customer.class), Mockito.eq("Thumbs Up!"));
    }

    @Test
    public void testInquireBodyStatusAbnormal() {
        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("ABNORMAL");
        monitor.inquireBodyStatus();
        Mockito.verify(notificationSender).sendEmailNotification(Mockito.any(FamilyDoctor.class), Mockito.eq("Emergency!"));
    }

    @Test
    public void testInquireBodyStatusNormalNotification() {
        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("NORMAL");
        monitor.inquireBodyStatus();
        Mockito.verify(notificationSender).sendEmailNotification(Mockito.any(Customer.class), Mockito.eq("Thumbs Up!"));
    }
    
    @Test
    public void testInquireBodyStatusAbnormalNotification() {
               Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("ABNORMAL");
        monitor.inquireBodyStatus();
        Mockito.verify(notificationSender).sendEmailNotification(Mockito.any(FamilyDoctor.class), Mockito.eq("Emergency!"));
    }

}
